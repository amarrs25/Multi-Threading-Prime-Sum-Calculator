import java.util.Properties;
//Aaron Marrs
//CPSC 245
//Dr. Bonakdarian
//This program is intended to sum up the prime numbers in a range using parallel code
//Prime.java
//0 - No comm line args
//1 - Invalid amount of comm line args
//2 - End values must be more than start values
//3 - Integers were unable to parse (comm line args are words not numbers)
//4 - Thread was interupted

class Prime
{
  //-----------------------------------------------------------
  public static void main(String[] args)
  {
	 id();

	//Initializing array
	//Must subtract 2 because of java and filename
	int[] startVals = new int[(int) (args.length)/ 2];
	int[] endVals = new int[(int) (args.length)/ 2];

	//Checking if there are values to add
	if(args.length == 0) {
		System.err.println("**	ERROR: Too few command line args (0)");
		System.exit(0);
	}
	//Adding values to array
	if(args.length % 2 == 0) {
		try {
			int count = 0;
			for(int i = 0; i < args.length; i+=2) {
				startVals[count] = Integer.parseInt(args[i]);
				count++;
			}
			//Reseting count value
			count = 0;

			for(int i = 1; i < args.length; i+=2) {
				endVals[count] = Integer.parseInt(args[i]); //-1 because end
				count++;
			}
		} catch(Exception e) {
			System.err.println("**	ERROR: Int expected (3)");
			System.exit(3);
			}
	}
	else {
		System.err.println("**	ERROR: Invalid amount of comm line args,"
				+ " for every start value there must be an end value (1)");
		System.exit(1);
	}

	//Checking that the start values are lower than the end values and that there are no negatives
	for(int i = 0; i < startVals.length; i++) {
		if(endVals[i] < startVals[i]) {
			System.err.println("** ERROR: Start values must be lower than end values (2)");
			System.exit(2);
		}
	}

	//Initializing worker and thread arrays
	Worker[] workers = new Worker[(int) (args.length)/ 2];
	Thread[] threads = new Thread[(int) (args.length)/ 2];

    	//Initializing workers and threads
    	for(int i = 0; i < startVals.length; i++) {
    		workers[i] = new Worker(startVals[i],endVals[i]);
    		threads[i] = new Thread(workers[i]);
    	}

    	//Printing that threads are starting
    	System.out.printf("Creating %d thread(s).\n", threads.length);

	for(int i = 0; i < threads.length; i++) {
		long threadId = threads[i].getId();
		threads[i].start();
		System.out.printf("Thread [%03d] started.\n", threadId);
	}

    	//joining threads
    	for(int i = 0; i < threads.length; i++) {

    		try
		{
			threads[i].join();
		}
		catch (InterruptedException e) {
			System.err.println("Thread was interupted (4)");
			System.exit(4);
		}
    	}

	double total = 0;
	for(int i = 0; i < threads.length; i++) {
		System.out.printf("Sum of all primes [%,8d - %,9d) is %,13.0f\n", startVals[i],
				endVals[i], workers[i].getTotal());
		total += workers[i].getTotal();
	}

	System.out.printf("\nThe grand sum of all primes calculations is %,13.0f\n", total);

    id();
  }

  // -----------------------------------------------
  // print id and system info
  // -----------------------------------------------
  private static void id()
  {
    final String YOUR_NAME = "Aaron Marrs";

    int cores = Runtime.getRuntime().availableProcessors();
    String osName = System.getProperty("os.name");
    String osVer = System.getProperty("os.version");
    String osArch = System.getProperty("os.arch");
    String javaVer = System.getProperty("java.version");
    String javaName = System.getProperty("java.vm.name");

    System.out.printf("\n%14s | %s (%s) | Cores: %d\n", osArch, osName, osVer, cores);
    System.out.printf("%14s | %s\n", javaVer, javaName);
    System.out.printf("%14s | %s\n\n", "PrimeThreads", YOUR_NAME);
  }
}

/*
the class that becomes a thread, can be named anything, must have
"implements Runnable" which requires the public void run() method
*/
class Worker implements Runnable
{
 private int val1;
 private int val2;
 private long threadId;
 private double total;

 // constructor
 Worker(int val1, int val2)
 {
   this.val1 = val1;
   this.val2 = val2;
 }

 // required method
 public void run()
 {
   threadId = Thread.currentThread().getId();
   total = computeTotal(val1, val2);
 }

//-----------------------------------------------------------
 // sum up primes within range [start, finish)
 //-----------------------------------------------------------
 private static double computeTotal(int start, int finish)
 {
   double total = 0;

   for(int i = start; i < finish; i++)
     if (Slow_isPrime(i))
	{
	  total += i;
	}

   return total;
 }

 //------------------------------------------------
 // determines if a number is a prime number
 // you must use this code, and can't modify it.
 // -----------------------------------------------
 private static boolean Slow_isPrime(int val)
 {
   int i = 0;

   if (val <= 1)
     return false;

   if (val == 2)
     return true;

   if ( (val %2) == 0)
     return false;

   for(i = 3; i < val; i++)
     if ( (val % i) == 0)
   	  return false;

   return true;
 }

 public long getThreadId() {
	 return threadId;
 }

 public double getTotal() {
	 return total;
 }
}
