import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class CSPSolution {
	// agent
	static Semaphore agentSem = new Semaphore(1);
	static Semaphore tobacco = new Semaphore(0);
	static Semaphore paper = new Semaphore(0);
	static Semaphore matches = new Semaphore(0);

	/**** START ****/
	// Add three new semaphores for Tobacco, Paper and Match
	//Pushers use tobaccoSem to signal the smoker with tobacco
	static Semaphore tobaccoSem = new Semaphore(0);
	//Pushers use paperSem to signal the smoker with paper
	static Semaphore paperSem = new Semaphore(0);
	//Pushers use matchSem to signal the smoker with match
	static Semaphore matchSem = new Semaphore(0);

	//Add three new boolean variables to indicate whether Tobacco, Paper or Match ingredient is on the table
	static boolean isTobacco = false;
	static boolean isPaper = false;
	static boolean isMatch = false;
	/**** END ****/
	
	static class agentA implements Runnable {
		public void run() {
			while (true) {
				try {
					agentSem.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("agentA about to release tobacco and paper");
				tobacco.release();
				paper.release();
			}
		}
	}

	static class agentB implements Runnable {
		public void run() {
			while (true) {
				try {
					agentSem.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("agentB about to release tobacco and matches");
				tobacco.release();
				matches.release();
			}
		}
	}

	static class agentC implements Runnable {
		public void run() {
			while (true) {
				try {
					agentSem.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("agentC about to release paper and matches");
				paper.release();
				matches.release();
			}
		}
	}

	static class smokerMatches implements Runnable {
		public void run() {
			while (true) {
				try {
					//Acquire the Match Semaphore and release the Agent Semaphore
					matchSem.acquire();
					agentSem.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("smoker with matches ready");
			}
		}
	}

	static class smokerTobacco implements Runnable {
		public void run() {
			while (true) {
				try {
					//Acquire the Tobacco Semaphore and release the Agent Semaphore
					tobaccoSem.acquire();
					agentSem.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("smoker with tobacco ready");
			}
		}
	}

	static class smokerPaper implements Runnable {
		public void run() {
			while (true) {
				try {
					//Acquire the Paper Semaphore and release the Agent Semaphore
					paperSem.acquire();
					agentSem.release();
				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println("smoker with paper ready");
			}
		}
	}

	
	/******** Start ********/ 
	//Add the Pushers Here

	//This pusher wakes up any time there is tobacco on the table. If it finds isPaper true, it knows that Pusher B has already run, so it can signal the smoker with matches. 
	//Similarly, if it finds a match on the table, it can signal the smoker with paper.
	//If Pusher A runs first, then it will find both isPaper and isMatch false. It cannot signal any of the smokers, so it sets isTobacco.
	static class pusherA implements Runnable{
		public void run() {
			while (true) {
				try {
					tobacco.acquire();
					System.out.println("pusherA for tobacco is active");
					if (isPaper) {
						isPaper = false;
						matchSem.release();
					} else if (isMatch) {
						isMatch = false;
						paperSem.release();
					} else {
						isTobacco = true;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	static class pusherB implements Runnable{
		public void run() {
			while (true) {
				try {
					paper.acquire();
					System.out.println("pusherB for Paper is active");
					if (isTobacco) {
						isTobacco = false;
						matchSem.release();
					} else if (isMatch) {
						isMatch = false;
						tobaccoSem.release();
					} else {
						isPaper = true;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	static class pusherC implements Runnable{
		public void run() {
			while (true) {
				try {
					matches.acquire();
					System.out.println("pusherC for Match is active");
					if (isPaper) {
						isPaper = false;
						tobaccoSem.release();
					} else if (isTobacco) {
						isTobacco = false;
						paperSem.release();
					} else {
						isMatch = true;
					}					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/******* End ******/
	
	
	public static void main(String[] args) {
		List<Thread> tList = new ArrayList<Thread>();
		tList.add(new Thread(new CSPSolution.agentA()));
		tList.add(new Thread(new CSPSolution.agentB()));
		tList.add(new Thread(new CSPSolution.agentC()));
		
		/**** START ****/
		//Add the newly added Pusher classes to the list so that they are started too
		tList.add(new Thread(new CSPSolution.pusherA()));
		tList.add(new Thread(new CSPSolution.pusherB()));
		tList.add(new Thread(new CSPSolution.pusherC()));
		/**** END *****/
		
		tList.add(new Thread(new CSPSolution.smokerMatches()));
		tList.add(new Thread(new CSPSolution.smokerTobacco()));
		tList.add(new Thread(new CSPSolution.smokerPaper()));
		for (Thread t : tList) {
			t.start();
		}
		try {
			for (Thread t : tList) {
				t.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
