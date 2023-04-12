
/**
 * Runs all threads
 */
public class BridgeRunner {

	public static void main(String[] args) {

		// TODO - check command line inputs
		//<bridge limit> <num cars>
		//     0             1

		if (args.length > 2 || args.length < 2 || Integer.parseInt(args[0]) <= 0 || Integer.parseInt(args[1]) <= 0){
			System.out.println("Use: java BridgeRunner <bridge limit> <num cars> and/or input should be greater than 0.");
		}
		else{
			int bridgeLim = Integer.parseInt(args[0]);
			int numCars = Integer.parseInt(args[1]);
	
			//System.out.println("Bridge limit " + bridgeLim);
		  	//System.out.println("Num cars = " + numCars);
		
			// TODO - instantiate the bridge 
			OneLaneBridge br = new OneLaneBridge(bridgeLim);
		
			// TODO - allocate space for threads
			Thread[] threads = new Thread[numCars];

			// TODO - start then join the threads
			for (int i = 0; i < numCars; i++) {
				threads[i] = new Thread(new Car(i, br));
				threads[i].start();
			}

			//join the threads
			for (int i = 0; i < numCars; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("\nAll cars have crossed!!");
		}
	}
}