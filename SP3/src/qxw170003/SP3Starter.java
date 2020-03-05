/* Hemali Amitkumar Patel hap170002
 *  Qi Wang qxw170003


/**
 * Sample starter code for SP3.
 *
 * @author rbk
 */

package qxw170003;

import java.util.Arrays;
import java.util.Random;

public class SP3Starter {
    public static Random random = new Random();
    public static int numTrials = 1;

    public static void main(String[] args) {
        int n = 5;
//        int choice = 1 + random.nextInt(4);
        int choice = 5;
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            choice = Integer.parseInt(args[1]);
        }
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }

        Timer timer = new Timer();
        switch (choice) {
            case 1: // Take 1
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    mergeSort1(arr);
                }
                break;
            case 2: // Take 2
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    System.out.print("Original: " + Arrays.toString(arr) + ", ");
                    System.out.println();
                    mergeSort2(arr);
                    System.out.print("Sorted " + Arrays.toString(arr) + ", ");
                    System.out.println();

                }
                break;
            case 3: // Take 3
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    System.out.print("Original: " + Arrays.toString(arr) + ", ");
                    System.out.println();
                    mergeSort3(arr);
                    System.out.print("Sorted " + Arrays.toString(arr) + ", ");
                    System.out.println();
                }
                break;
            case 4: // Take 4
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    System.out.print("Original: " + Arrays.toString(arr) + ", ");
                    System.out.println();
                    mergeSort4(arr);
                    System.out.print("Sorted " + Arrays.toString(arr) + ", ");
                    System.out.println();
                }
                break;
            case 5: // Take 5
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    System.out.print("Original: " + Arrays.toString(arr) + ", ");
                    System.out.println();
                    mergeSort_5(arr);
                    System.out.print("Sorted " + Arrays.toString(arr) + ", ");
                    System.out.println();
                }
                break;
            case 6: // Take 5 with swap
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    System.out.print("Original: " + Arrays.toString(arr) + ", ");
                    System.out.println();
                    mergeSort5(arr);
                    System.out.print("Sorted " + Arrays.toString(arr) + ", ");
                    System.out.println();
                }
                break;
        }
        timer.end();
        timer.scale(numTrials);

        System.out.println("Choice: " + choice + "\n" + timer);
    }

    public static void insertionSort(int[] arr, int p, int r) {
        for (int i = p + 1; i < r + 1; ++i) {
            int key = arr[i];
            int j = i - 1;
            while (j >= p && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[++j] = key;
        }
    }

    // Take 1:
    public static void mergeSort1(int[] arr) {
        int len = arr.length;
        if (arr == null || len == 0) return;
        mergeSort1(arr, 0, len - 1);
    }

    public static void mergeSort1(int[] A, int p, int r) {
        if (p < r) {
            int q = p + (r - p) / 2;
            mergeSort1(A, p, q);
            mergeSort1(A, q + 1, r);
            merge1(A, p, q, r);
        }
    }

    public static void merge1(int[] A, int p, int q, int r) {
        int[] B = Arrays.copyOf(A, A.length);
        int i = p;
        int j = q + 1;
        int k = p;
        while (i <= q && j <= r) {
            if (B[i] <= B[j]) {
                A[k++] = B[i++];
            } else {
                A[k++] = B[j++];
            }
        }
        while (i <= q) A[k++] = B[i++];
        while (j <= r) A[k++] = B[j++];
    }


    // Take 2:
    public static void mergeSort2(int[] arr) {
        if (arr == null || arr.length == 0) return;
        int[] B = Arrays.copyOf(arr, arr.length);
        mergeSort2(arr, B, 0, arr.length - 1);
    }

    public static void mergeSort2(int[] A, int[] B, int p, int r) {
        if (p < r) {
            int q = p + (r - p) / 2;
            mergeSort2(A, B, p, q);
            mergeSort2(A, B, q + 1, r);
            merge2(A, B, p, q, r);
        }
    }

    public static void merge2(int[] A, int[] B, int p, int q, int r) {
        B = Arrays.copyOfRange(A, p, r + 1);
        int i = 0;
        int j = q - p + 1;
        int k = p;
        while (i <= q - p && j <= r - p) {
            if (B[i] <= B[j]) {
                A[k++] = B[i++];
            } else {
                A[k++] = B[j++];
            }
        }
        while (i <= q - p) A[k++] = B[i++];
        while (j <= r - p) A[k++] = B[j++];

    }


    //Take 3:
    public static void mergeSort3(int[] arr) {
        if (arr == null || arr.length == 0) return;
        int[] B = Arrays.copyOf(arr, arr.length);
        mergeSort3(arr, B, 0, arr.length - 1);
    }

    public static void mergeSort3(int[] A, int[] B, int p, int r) {
        if (p < r) {
            int q = p + (r - p) / 2;
            mergeSort3(B, A, p, q);
            mergeSort3(B, A, q + 1, r);
            merge3(A, B, p, q, r);
        }
    }

    public static void merge3(int[] A, int[] B, int p, int q, int r) {
        int i = p;
        int j = q + 1;
        int k = p;
        while (i <= q && j <= r) {
            if (B[i] <= B[j]) {
                A[k++] = B[i++];
            } else {
                A[k++] = B[j++];
            }
        }
        while (i <= q) A[k++] = B[i++];
        while (j <= r) A[k++] = B[j++];
    }


    //Take 4:
    public static void mergeSort4(int[] arr) {
        if (arr == null || arr.length == 0) return;
        int[] B = Arrays.copyOf(arr, arr.length);
        mergeSort4(arr, B, 0, arr.length - 1);
    }

    public static void mergeSort4(int[] A, int[] B, int p, int r) {
        int THRESHOLD = 8;
        if (r - p <= THRESHOLD) {
            insertionSort(A, p, r);
        }
        else if (p < r) {
            int q = p + (r - p) / 2;
            mergeSort4(B, A, p, q);
            mergeSort4(B, A, q + 1, r);
            merge4(A, B, p, q, r);
        }
    }

    public static void merge4(int[] A, int[] B, int p, int q, int r) {
        int i = p;
        int j = q + 1;
        int k = p;
        while (i <= q && j <= r) {
            if (B[i] <= B[j]) {
                A[k++] = B[i++];
            } else {
                A[k++] = B[j++];
            }
        }
        while (i <= q) A[k++] = B[i++];
        while (j <= r) A[k++] = B[j++];
    }


    // Take 5:
    public static void mergeSort_5(int[] arr) {
        if (arr == null || arr.length == 0) return;
        int[] B = Arrays.copyOf(arr, arr.length);
        mergeSort_5(B, arr);
    }

    public static void mergeSort_5(int[] A, int[] B) {
        int n = A.length;
        if (n == 1) return;
        else {
            for (int i = 1; i < n; i = 2 * i) {
                for (int j = 0; j < n; j = j + 2 * i) {
                    merge_5(B, A, j, Math.min(n - 1, j + i - 1), Math.min(n - 1, j + 2 * i - 1));
                }
                int[] temp = A;
                A = B;
                B = temp;
            }
        }
    }

    public static void merge_5(int[] A, int[] B, int p, int q, int r) {
//        B = Arrays.copyOfRange(A, p, r + 1);
        int i = p;
        int j = q + 1;
        int k = p;
        while (i <= q && j <= r) {
            if (B[i] <= B[j]) {
                A[k++] = B[i++];
            } else {
                A[k++] = B[j++];
            }
        }
        while (i <= q) A[k++] = B[i++];
        while (j <= r) A[k++] = B[j++];
    }


    // Take 5 with swap:
    public static void mergeSort5(int[] arr) {
        if (arr == null || arr.length == 0) return;
        int[] B = Arrays.copyOf(arr, arr.length);
        mergeSort5(arr, B);
    }

    public static void mergeSort5(int[] A, int[] B) {
        int n = A.length;
        if (n == 1) return;
        else {
            for (int i = 1; i < n; i = 2 * i) {
                for (int j = 0; j < n; j = j + 2 * i) {
                    merge5(B, A, j,
                            j + i - 1 > n - 1 ? n - 1 : j + i - 1,
                            j + 2 * i - 1 > n - 1 ? n - 1 : j + 2 * i - 1);
                }
                int[] t = A;
                A = B;
                B = t;
            }
        }
    }

    public static void merge5(int[] A, int[] B, int p, int q, int r) {
//        B = Arrays.copyOfRange(A, p, r + 1);
        int i = p;
        int j = q + 1;
        int k = p;
        while (i <= q && j <= r) {
            if (B[i] <= B[j]) {
                A[k++] = B[i++];
            } else {
                A[k++] = B[j++];
            }
        }
        while (i <= q) A[k++] = B[i++];
        while (j <= r) A[k++] = B[j++];
    }

    /**
     * Timer class for roughly calculating running time of programs
     *
     * @author rbk
     * Usage:  Timer timer = new Timer();
     * timer.start();
     * timer.end();
     * System.out.println(timer);  // output statistics
     */

    public static class Timer {
        long startTime, endTime, elapsedTime, memAvailable, memUsed;
        boolean ready;

        public Timer() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public void start() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public Timer end() {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            memAvailable = Runtime.getRuntime().totalMemory();
            memUsed = memAvailable - Runtime.getRuntime().freeMemory();
            ready = true;
            return this;
        }

        public long duration() {
            if (!ready) {
                end();
            }
            return elapsedTime;
        }

        public long memory() {
            if (!ready) {
                end();
            }
            return memUsed;
        }

        public void scale(int num) {
            elapsedTime /= num;
        }

        public String toString() {
            if (!ready) {
                end();
            }
            return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed / 1048576) + " MB / " + (memAvailable / 1048576) + " MB.";
        }
    }

    /**
     * @author rbk : based on algorithm described in a book
     */


    /* Shuffle the elements of an array arr[from..to] randomly */
    public static class Shuffle {

        public static void shuffle(int[] arr) {
            shuffle(arr, 0, arr.length - 1);
        }

        public static <T> void shuffle(T[] arr) {
            shuffle(arr, 0, arr.length - 1);
        }

        public static void shuffle(int[] arr, int from, int to) {
            int n = to - from + 1;
            for (int i = 1; i < n; i++) {
                int j = random.nextInt(i);
                swap(arr, i + from, j + from);
            }
        }

        public static <T> void shuffle(T[] arr, int from, int to) {
            int n = to - from + 1;
            Random random = new Random();
            for (int i = 1; i < n; i++) {
                int j = random.nextInt(i);
                swap(arr, i + from, j + from);
            }
        }

        static void swap(int[] arr, int x, int y) {
            int tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }

        static <T> void swap(T[] arr, int x, int y) {
            T tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }

        public static <T> void printArray(T[] arr, String message) {
            printArray(arr, 0, arr.length - 1, message);
        }

        public static <T> void printArray(T[] arr, int from, int to, String message) {
            System.out.print(message);
            for (int i = from; i <= to; i++) {
                System.out.print(" " + arr[i]);
            }
            System.out.println();
        }
    }
}

