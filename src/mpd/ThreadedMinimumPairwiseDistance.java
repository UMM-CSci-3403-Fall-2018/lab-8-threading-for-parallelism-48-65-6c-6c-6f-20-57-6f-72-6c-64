package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {
    //We need the result to be a max value so that we can have a result of
    //any value.
    private long result = Integer.MAX_VALUE;
	//Synchronized method for updating the overall global result. This method is called once by each thread after they have finished cycling through their data.
    synchronized private void updateGlobalResult(long newResult) {
        if(newResult < result) {
            result = newResult;
        }
        return;
    }

    @Override
    public long minimumPairwiseDistance(int[] values){
	//n in the formula is equal to the maximum length of the values.
        int n = values.length;

	//Here we initialize the threads for each section that we want to get
	//values of.
        Thread threadLL = new Thread(new PairwiseThreadLL(values));
        Thread threadBR = new Thread(new PairwiseThreadBR(values));
        Thread threadTR = new Thread(new PairwiseThreadTR(values));
        Thread threadC = new Thread(new PairwiseThreadC(values));
	
	//Here we start each thread section.
        threadLL.start();
        threadBR.start();
        threadTR.start();
        threadC.start();
	
	//Here we wait for the threads to join.
        try {
            threadLL.join();
            threadBR.join();
            threadTR.join();
            threadC.join();
        } catch (InterruptedException e) {
            System.err.print(e);
        }


        return result;

    }
	
    //This is a constructor class for each thread that implements runnable. It saves the array of values in the constructor.
    class PairwiseThreadLL implements Runnable {
        private int[] values;
        private int n;

        public PairwiseThreadLL(int[] values) {
            this.values = values;
            this.n = values.length;
        }
	
	//We implement the algorithm of how the Lower left section of the data.
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int i = 1; i < n/2; ++i) {
                for (int j = 0; j < i; ++j) {
                    // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                    long diff = Math.abs(values[i] - values[j]);
                    if(diff<localResult) {
                        localResult = diff;
                    }
                }
            }
            updateGlobalResult(localResult);
        }
    }

    class PairwiseThreadBR implements Runnable {
        private int[] values;
        private int n;

        public PairwiseThreadBR(int[] values) {
            this.values = values;
            this.n = values.length;
        }
	//We implement the algorithm of how the Bottom Right section of the data.
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int i = n/2 + 1; i < n; ++i) {
                for (int j = 0; j + n/2 < i; ++j) {
                    // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                    long diff = Math.abs(values[i] - values[j]);
                    if(diff<localResult) {
                        localResult = diff;
                    }
                }
            }
            updateGlobalResult(localResult);
        }
    }

    class PairwiseThreadTR implements Runnable {
        private int[] values;
        private int n;

        public PairwiseThreadTR(int[] values) {
            this.values = values;
            this.n = values.length;
        }
	//We implement the algorithm of how the Top Right section of the data.
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int i = 1 + n/2; i < n; ++i) {
                for (int j = n/2; j < i; ++j) {
                    // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                    long diff = Math.abs(values[i] - values[j]);
                    if(diff<localResult) {
                        localResult = diff;
                    }
                }
            }
            updateGlobalResult(localResult);
        }
    }

    class PairwiseThreadC implements Runnable {
        private int[] values;
        private int n;

        public PairwiseThreadC(int[] values) {
            this.values = values;
            this.n = values.length;
        }
	//We implement the algorithm of how the Center section of the data.
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int j = 0; j < n/2; ++j) {
                for (int i = n/2; i < j + n/2; ++i) {
                    // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                    long diff = Math.abs(values[i] - values[j]);
                    if(diff<localResult) {
                        localResult = diff;
                    }
                }
            }
            updateGlobalResult(localResult);
        }
    }

}
