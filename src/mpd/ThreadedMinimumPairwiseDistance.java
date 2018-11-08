package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    private long result = Integer.MAX_VALUE;

    synchronized private void updateGlobalResult(long newResult) {
        if(newResult < result) {
            result = newResult;
        }
        return;
    }

    @Override
    public long minimumPairwiseDistance(int[] values){

        int n = values.length;

        Thread threadLL = new Thread(new PairwiseThreadLL(values));
        Thread threadBR = new Thread(new PairwiseThreadBR(values));
        Thread threadTR = new Thread(new PairwiseThreadTR(values));
        Thread threadC = new Thread(new PairwiseThreadC(values));

        threadLL.start();
        threadBR.start();
        threadTR.start();
        threadC.start();

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

    class PairwiseThreadLL implements Runnable {
        private int[] values;
        private int n;

        public PairwiseThreadLL(int[] values) {
            this.values = values;
            this.n = values.length;
        }

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
