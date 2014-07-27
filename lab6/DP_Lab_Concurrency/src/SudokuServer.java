import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SudokuServer {
	
	static int PORT = -1;
	// no matter how many concurrent requests you get,
	// do not have more than three solvers running concurrently
	final static int MAXPARALLELTHREADS = 3;

	//START
	private SudokuServer(){
	}

	//Lazy initialize the Sudoku Cache
	private static class SudokuCacheLazyHolder{
		private static final Map<String, String> cachedSudokuMap = new HashMap<String, String>();
	}
	
	public static Map<String, String> getCache(){
		return SudokuCacheLazyHolder.cachedSudokuMap;
	}
	// END

	// this method exists for testing purposes, since
	// we want to clear out the singleton cache for
	// subsequent junit tests.
	static void resetcache() {
		// TODOBEGIN(DP)
		//START
		SudokuServer.getCache().clear(); //Clean up the Cache
		//END
		// TODOEND(DP)
	}

	public static void start(int portNumber) throws IOException {
		PORT = portNumber;
		Runnable serverThread = new ThreadedSudokuServer();
		Thread t = new Thread(serverThread);
		t.start();
	}
}

// TODOBEGIN(DP)
class ThreadedSudokuServer implements Runnable {

	private final ServerSocket serverSocket;
	private final ExecutorService executorServicePool;
	private Map<String, String> cachedSudokuMap;

	public ThreadedSudokuServer() throws IOException {
		//Initialize the Server Socket with the Port Number specified
		serverSocket = new ServerSocket(SudokuServer.PORT);
		
		//Initialize a ThreadPool whose size is specified by the poolSize attribute
		//Executor framework will take care of controlling the threadpool size to the specified value
		executorServicePool = Executors.newFixedThreadPool(SudokuServer.MAXPARALLELTHREADS);
		
		//Use the Cache that was Lazy Initialized 
		cachedSudokuMap = SudokuServer.getCache();
	}

	public void run() {

		/**** START *****/
		try {
			//Execute the Thread Pool
			while(true) { 
				//Accept connections whenever a Client tries to connect 
				//ExecutorService will ensure that the Thread Pool Size is maintained
				executorServicePool.execute(new SudokuHandler(serverSocket.accept(), cachedSudokuMap));
			}
		} catch (IOException e) {
			executorServicePool.shutdown();
		}
		/***** END ******/
	}
}

/**** START *****/
class SudokuHandler implements Runnable {

	private final Socket socket;
	private Map<String, String> cachedSudokuMap;
	
	//Create a ReentrantReadWriteLock to control access to the Cached HashMap containing the solved Sudoku problems
	static ReentrantReadWriteLock reentrantRWLock = new ReentrantReadWriteLock();

	SudokuHandler(Socket socket, Map<String, String> cachedSudokuMap) {
		this.socket = socket;
		this.cachedSudokuMap = cachedSudokuMap;
	}

	public void run() {

		// Read the Data From the Client
		String sudokuSolution = null;
		
		try {
		
			//Read the InputStream of data sent by the Client
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			//Check the Number of Bytes available
			int count = dis.available();
			//Read in the exact number of Bytes that were available in the previous check in to the Byte Array
			byte[] bs = new byte[count];
			dis.read(bs);

			//Conver the ByteArray read from the InputStream into a String
			String sudokuProblem = new String(bs);

			//Check if the SudokuProblem already has a Solution in the CachedHashMap
			if (cachedSudokuMap != null && cachedSudokuMap.containsKey(sudokuProblem)) {
				//Establish a Reentrant Readlock before reading the value from the CachedSudokuMap
				reentrantRWLock.readLock().lock();
				//Get the cached value from the CachedSudokuMap
				sudokuSolution = cachedSudokuMap.get(sudokuProblem);
				//Unlock the Reentrant Readlock after reading the value from the CachedSudokuMap
				reentrantRWLock.readLock().unlock();
			} else {
				//Indicates that no match was found for the SudokuProblem in the Cache
				//Solve the Sudoku Problem
				sudokuSolution = SudokuSolver.solve(sudokuProblem);
				//Establish a Reentrant WriteLock on the CachedSudokuMap to save the Sudoku solution
				//This would help prevent other sudoku threads from trying to write at the same time
				reentrantRWLock.writeLock().lock();
				//Put the Sudoku solution into the Map
				cachedSudokuMap.put(sudokuProblem, sudokuSolution);
				//Unlock the CachedSudokuMap after populating the map with the Sudoku Solution
				reentrantRWLock.writeLock().unlock();
			}

			// Pass the Solution to the Client for further processing
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			//Write the Sudoku Solution as Bytes into the Output stream and then close it
			dos.writeBytes(sudokuSolution);
			dos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
/**** END *****/
// TODOEND(DP)