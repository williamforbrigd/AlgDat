import java.util.function.BiFunction;

/**
 * Exercise 1 in AlgDat.
 * @author William
 */
public class Pow {
    //Used in order to format at table.
    private static final String CHECK_FORMAT = "%-20s%-15s%-15s%-15s%n";
    private static final String TIME_FORMAT = "%-8s%-20s%-20s%n";

    private static final int SECONDS_PR_TEST = 1;
    private static final int SEC_TO_NS = 1000000000;


    /**
     * The first recursive algorithm with a time complexity of O(n)
     * @param x The base.
     * @param n The exponent.
     * @return the power of x to n.
     */
    private static double pow1(double x, int n) {
        if(n==0) return 1;
        return x*pow1(x,n-1);
    }


    /**
     * The second recursive algorithm with a time complexity O(log n).
     * @param x The base.
     * @param n The exponent.
     * @return the power of x to n.
     */
    private static double pow2(double x, int n) {
        if(n==0) {
            return 1;
        } else if(n%2 == 1) {
            return x*pow2(x*x, (n-1)/2);
        } else if(n%2 == 0){
            return pow2(x*x, n/2);
        }
        return 0;
    }


    /**
     * Using the Math class to calculate power.
     * The complexity is O(1).
     * @param x The base.
     * @param n The exponent.
     * @return the power of x to n.
     */
    private static double javaPow(double x, int n) {
        return Math.pow(x,n);
    }

    /**
     * Method to calculate both the amount of rounds the algorithms can run each second and the time.
     * Using a BiFunction, which is a functional interface used for a method reference.
     * System.nanoTime() is used to measure the time.
     * Prints out a formatted string with respectively n, rounds per sec and time in nanoseconds.
     * @param x The base.
     * @param n The exponent.
     * @param func the functional interface that can take a method reference as a parameter.
     */
    public static void testTime(double x, int n, BiFunction<Double, Integer, Double> func) {
        long start = System.nanoTime();
        long end;
        int count = 0;
        int nsPrTest = SEC_TO_NS * SECONDS_PR_TEST;
        long duration;
        do {
            func.apply(x,n);
            end = System.nanoTime();
            duration = end-start;
            count++;
        } while(duration < nsPrTest);
        int timeInNs = Math.round(nsPrTest / count);
        int countPrSec = count / SECONDS_PR_TEST;
        System.out.printf(TIME_FORMAT, n, countPrSec, timeInNs + " ns");
    }

    /**
     * Check whether the algorithms calculates the correct answer or not.
     * @param x The base.
     * @param n The exponent.
     * @param ans the correct answer of x to the power of n.
     */
    private static void checkAnswer(double x, int n, double ans) {
        System.out.printf(CHECK_FORMAT, x + "^" + n + "=" + ans, booleanToString(pow1(x,n) == ans),
                booleanToString(pow2(x,n) == ans),
                booleanToString(javaPow(x,n) == ans));
    }

    /**
     * Helping method to create a string with boolean.
     */
    private static String booleanToString(boolean b) {
        return b ? "yes" : "no";
    }

    public static void main(String[] args) {
        System.out.println("Check the answer of the different methods:");
        System.out.printf(CHECK_FORMAT, "Equation", "Algorithm 1", "Algorithm 2", "Power in Java");
        checkAnswer(2,10, 1024.0);
        checkAnswer(3,14,4782969.0);
        checkAnswer(4,11, 4194304.0);

        double x = 2;
        System.out.println("\nHow many times the algorithms can run per second and time per second:");
        System.out.println("Algorithm 1 -  time complexity O(n):");
        System.out.printf(TIME_FORMAT, "n", "Rounds per sec", "Time per round");
        testTime(x,10, Pow::pow1);
        testTime(x,100, Pow::pow1);
        testTime(x,1000, Pow::pow1);
        testTime(x,10000, Pow::pow1);

        System.out.println("\nAlgorithm 2 - time complexity O(log n):");
        System.out.format(TIME_FORMAT, "n", "Rounds per sec", "Time per round");
        testTime(x,10,Pow::pow2);
        testTime(x,100,Pow::pow2);
        testTime(x,1000,Pow::pow2);
        testTime(x,10000,Pow::pow2);

        System.out.println("\nPower in Java - time complexity O(1):");
        System.out.printf(TIME_FORMAT, "n", "Rounds per sec", "Time per round");
        testTime(x,10,Pow::javaPow);
        testTime(x,100,Pow::javaPow);
        testTime(x,1000,Pow::javaPow);
        testTime(x,10000,Pow::javaPow);
    }
}
