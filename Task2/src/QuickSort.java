import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.BiFunction;

public class QuickSort {
    private static final String TABLE_FORMAT = "%-20s%-15s%-15s%-15s%-15s%-15s%-15s%n";

    /**
     * Helping method used single pivot and dual pivot quicksort that swaps element of index i with
     * element of index j in the array.
     */

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Helping method used in single pivot quicksort.
     */

    public static int median3sort(int []t, int v, int h) {
        int m = (v + h) / 2;
        if (t[v] > t[m]) swap(t, v, m);
        if (t[m] > t[h]) {
            swap(t, m, h);
            if (t[v] > t[m]) swap(t, v, m);
        }
        return m;
    }


    /**
     * Helping method used in single pivot quicksort.
     */
    public static int split(int []t, int v, int h) {
        int iv, ih;
        int m = median3sort(t, v, h);
        int dv = t[m];
        swap(t, m, h - 1);
        for (iv = v, ih = h - 1;;) {
            while (t[++iv] < dv) ;
            while (t[--ih] > dv) ;
            if (iv >= ih) break;
            swap(t, iv, ih);
        }
        swap(t, iv, h-1);
        return iv;
    }

    public static void singlePivotQuickSort(int []t, int v, int h) {
        if (h - v > 2) {
            int delepos = split(t, v, h);
            singlePivotQuickSort(t, v, delepos - 1);
            singlePivotQuickSort(t, delepos + 1, h);
        } else median3sort(t, v, h);
    }

    /**
     * Helping method used in dual pivot quicksort.
     */

    private static int[] partition2(int[] arr, int low, int high) {
        swap(arr, low, low+(high-low)/3);
        swap(arr, high, high-(high-low)/3);

        if(arr[low] > arr[high])
            swap(arr, low, high);

        //p is the left pivot, and q is the right pivot
        int j = low + 1;
        int g = high - 1, k = low + 1,
                p = arr[low], q = arr[high];

        while(k <= g) {
            //If elements are less than the left pivot
            if(arr[k] < p) {
                swap(arr, k, j);
                j++;
            }
            //If elements are greater than or equal to the right pivot
            else if(arr[k] >= q) {
                while(arr[g] > q && k < g)
                    g--;

                swap(arr, k, g);
                g--;

                if(arr[k] < p) {
                    swap(arr, k, j);
                    j++;
                }
            }
            k++;
        }
        j--;
        g++;

        //Bring pivot to their appropriate positions.
        swap(arr, low, j);
        swap(arr, high, g);

        // Returning the indices of the pivots
        // because we cannot return two elements
        // from a function, we do that using an array.
        return new int[] {j,g};
    }

    private static void dualPivotQuickSort(int[] arr, int low, int high) {
        if(low < high) {
            //piv[] stores left pivot and right pivot
            //piv[0] means left pivot and
            //piv[1] means right pivot
            int[] piv;
            piv = partition2(arr, low, high);

            dualPivotQuickSort(arr, low, piv[0] - 1);
            dualPivotQuickSort(arr, piv[0] + 1, piv[1] - 1);
            dualPivotQuickSort(arr, piv[1] + 1, high);
        }
    }



    private static String printArray(int[] arr) {
        String println = "";
        for(int i=0; i < arr.length; i++) {
            println += arr[i] + " ";
        }
        return println;
    }

    /**
     * Checks whether the arr is sorted or not.
     * @param arr the sorted array.
     * @return true if sorted or false otherwise.
     */

    private static boolean isSorted(int[] arr) {
        for(int i=0; i < arr.length-2; i++) {
            if(arr[i+1] < arr[i])
                return false;
        }
        return true;
    }

    /**
     * Gets the sum of an array.
     * @param arr the array to get the sum from.
     * @return the sum of the array.
     */

    private static int getSum(int[] arr) {
        int sum = 0;
        for(int i=0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    /**
     * Gets a random array with the given length with numbers from min to max.
     * @param length the given length of the array.
     * @param min the start of the range.
     * @param max the end of the range.
     * @return int[] array with random numbers.
     */
    private static int[] getRandArr(long length, int min, int max) {
        int[] arr = new int[(int) length];
        for(int i=0; i < length; i++) {
            arr[i] = (int) (Math.random() * max)+min;
        }
        return arr;
    }

    /**
     * Gets a random array with every other random duplicate.
     * @param length of the array.
     * @param min start of the range.
     * @param max end of the range.
     * @return int[] array with duplicates.
     */
    private static int[] getRandArrDuplicates(long length, int min, int max) {
        int[] arr = new int[(int) length];
        int duplicate1 = (int) (Math.random()*max)+min;
        int duplicate2 = (int) (Math.random()*max)+min;

        for(int i=0; i < length; i++) {
            if(i % 2 == 0) {
                arr[i] = duplicate1;
            } else if(i % 2 == 1) {
                arr[i] = duplicate2;
            }
        }
        return arr;
    }

    /**
     * Test the single pivot algorithm.
     * Sorts the array and takes the time.
     * Calculates the sum before and after the sort,
     *      in order to measure any potential data loss during the sort.
     * Checks whether the array is sorted or not.
     * @param arr the array to be tested.
     */

    private static void testSinglePivot(int[] arr) {
        int sumBefore = getSum(arr);
        long start = System.nanoTime();
        singlePivotQuickSort(arr, 0, arr.length-1);
        long end = System.nanoTime();
        int sumAfter = getSum(arr);

        System.out.printf(TABLE_FORMAT, "Single pivot", (double)(end-start)/1000000, sumBefore, sumAfter,
                sumBefore==sumAfter ? "yes" : "no", isSorted(arr) ? "yes" : "no", arr.length);
    }

    /**
     * Test the dual pivot algorithm.
     * Sorts the array and takes the time.
     * Calculates the sum before and after the sort,
     *      in order to measure any potential data loss during the sort.
     * Checks whether the array is sorted or not.
     * @param arr the array to be tested.
     */

    private static void testDualPivot(int[] arr) {
        int sumBefore = getSum(arr);
        long start = System.nanoTime();
        dualPivotQuickSort(arr, 0, arr.length-1);
        long end = System.nanoTime();
        int sumAfter = getSum(arr);

        System.out.printf(TABLE_FORMAT, "Dual pivot",(double) (end-start)/1000000, sumBefore, sumAfter,
                sumBefore==sumAfter ? "yes" : "no", isSorted(arr) ? "yes" : "no", arr.length);
    }


    public static void main(String[] args) {
        int[] arr = getRandArr(1000,1,100);
        int[] arr2 = getRandArr(1000, 1,100);
        int[] arr3 = getRandArr(100000000, 1,10);
        int[] arr4 = getRandArr(100000000, 1,10);

        int[] arr5 = getRandArrDuplicates(100, 1, 1000);
        int[] arr6  = getRandArrDuplicates(100, 1, 1000);
        int[] arr7  = getRandArrDuplicates(100000, 100, 500);
        int[] arr8  = getRandArrDuplicates(100000, 100, 500);

        System.out.println("--- Test arrays of random numbers ---");
        System.out.printf(TABLE_FORMAT, "Sort algorithm:", "Time in ms:", "Sum before:", "Sum after:", "Sum is equal:",  "Is sorted:", "Array length:");
        testSinglePivot(arr);
        testDualPivot(arr2);
        testSinglePivot(arr3);
        testDualPivot(arr4);
        System.out.println();

        System.out.println("--- Test arrays of duplicates ---");
        System.out.printf(TABLE_FORMAT, "Sort algorithm:", "Time in ms:", "Sum before:", "Sum after:", "Sum is equal:",  "Is sorted:", "Array length:");
        testSinglePivot(arr5);
        testDualPivot(arr6);
        testSinglePivot(arr7);
        testDualPivot(arr8);
        System.out.println();

        System.out.println("--- Test arrays that is sorted before ---");
        System.out.printf(TABLE_FORMAT, "Sort algorithm:", "Time in ms:", "Sum before:", "Sum after:", "Sum is equal:",  "Is sorted:", "Array length:");
        testSinglePivot(arr);
        testDualPivot(arr2);
        testSinglePivot(arr3);
        testDualPivot(arr4);
        testSinglePivot(arr5);
        testDualPivot(arr6);
        testSinglePivot(arr7);
        testDualPivot(arr8);

    }
}
