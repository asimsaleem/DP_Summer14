public class NumberIncrementer {

	public static void main(String... args) {
		NumberDisplay numberDisplay = new NumberDisplay();
		
		// Initialize Thread 1 with FALSE to indicate that it is ODD number thread
		Thread t1 = new Thread(new Incrementer(numberDisplay, false));

		// Initialize Thread 2 with TRUE to indicate that it is EVEN number thread
		Thread t2 = new Thread(new Incrementer(numberDisplay, true));

		// Start both the Threads one after the other. Odd first and then the Even, for obvious reasons
		t1.start();
		t2.start();
	}
}

/**
 * 
 * Description: 
 * 1. Two threads will get started
 * 2. Odd Number thread will lock and print the odd number
 * 3. Even Number thread will then kick in and print the even number
 * 4. The individual threads will notify the other once they are done printing
 * 5. The loop will execute for a max of 50 times for the individual threads for a total of 100
 *
 */
class Incrementer implements Runnable {

	private int max = 50; //50 is the maximum number each thread can print numbers for
	private NumberDisplay numberDisplay;
	private boolean isEvenNumber;

	//Initialize the constructor with the necessary values
	Incrementer(NumberDisplay numberDisplay, boolean isEvenNumber) {
		//Set the values needed into the Constructor
		this.numberDisplay = numberDisplay;
		this.isEvenNumber = isEvenNumber;
	}

	@Override
	public void run() {

		//Initialize the Starting Number to 0
		int number = 0;
		
		//Check if the boolean for Even is TRUE or FALSE
		if (isEvenNumber) {
			//If True, set the Number to 2 because that would be the first Even Number
			number = 2;
		} else {
			//If False, set the Number to 1 because that would be the first Odd Number
			number = 1;
		}

		//Loop through the counter for a Maximum of 50 times for each thread
		for (int i = 0; i < max; i++) {

			//Check the Flag that was set in the Constructor for Even or Odd
			if (isEvenNumber) {
				//If EVEN, we have to display Even Number by making a Call to the corresponding method inside NumberDisplay
				numberDisplay.displayEvenNums(number);
			} else {
				//If ODD, we have to display Odd Number by making a call to the corresponding method inside NumberDisplay
				numberDisplay.displayOddNums(number);
			}
			
			// Increment the Number by 2 every time, as long as the 
			// Number<50
			number += 2;
		}
	}
}

class NumberDisplay {

	boolean isEvenNum = true;
	
	synchronized void displayEvenNums(int number) {
		
		for(;;){
			if(isEvenNum){
				try {
					//Wait till the Odd thread has called notify()
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				//Break from the Daemon Loop once the isEvenNum flag changes to FALSE
				break;
			}
		}

		//Print the Even Number Here
		System.out.println("E:" + number);
		isEvenNum = true;
		
		//Notify the Odd Thread
		notify();
	}

	synchronized void displayOddNums(int number) {
		
		for(;;){
			if(!isEvenNum){
				try {
					//Wait till the Even thread has called notify()
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				//Break from the Daemon Loop once the isEvenNum flag changes to TRUE
				break;
			}
		}
		
		System.out.println("O:" + number);
		isEvenNum = false;
		
		//Notify the Even Thread
		notify();
	}
}