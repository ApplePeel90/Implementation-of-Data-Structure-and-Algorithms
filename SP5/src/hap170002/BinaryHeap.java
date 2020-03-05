// Starter code for SP5

// Change to your netid
package hap170002;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class BinaryHeap<T extends Comparable<? super T>> {
    Comparable[] pq;
    int size;

    // Constructor for building an empty priority queue using natural ordering of T
    public BinaryHeap(int maxCapacity) {
        pq = new Comparable[maxCapacity];
        size = 0;
    }

    // add method: resize pq if needed
    public boolean add(T x) {
        if (size == pq.length) {
            this.resize();
        }
        // place element into heap at bottom
        this.pq[size] = x;

        percolateUp(size);

        size++;
        return true;
    }

    public boolean offer(T x) {
        return this.add(x);
    }

    // throw exception if pq is empty
    public T remove() throws NoSuchElementException {
        T result = poll();
        if (result == null) {
            throw new NoSuchElementException("Priority queue is empty");
        } else {
            return result;
        }
    }

    // return null if pq is empty
    public T poll() {
        if (size == 0) {
            return null;
        }
        T result = (T) pq[0];
        move(0, (T) pq[size - 1]);
        size--;
//        move(size, null);
        percolateDown(0);
        return result;
    }

    public T min() {
        return peek();
    }

    // return null if pq is empty
    public T peek() {
        if (size == 0) {
            return null;
        }
        return (T) pq[0];
    }

    int parent(int i) {
        return (i - 1) / 2;
    }

    int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * pq[index] may violate heap order with parent
     */
    void percolateUp(int index) {
        T temp = (T) pq[index];
        while (index > 0 && compare(temp, pq[parent(index)]) < 0) {
            move(index, (T) pq[parent(index)]);
            index = parent(index);
        }
        move(index, temp);
    }

    /**
     * pq[index] may violate heap order with children
     */
    void percolateDown(int index) {

        int child;
        T tmp = (T) pq[index];

        for (; index * 2 + 1 < size; index = child) {
            child = index * 2 + 1;

            if(child >= size) break;
            if(child == size - 1 && compare(pq[child], tmp) < 0)
                pq[index] = pq[child];
            else if(compare(pq[child + 1], pq[child]) < 0)
                child++;

            pq[index] = pq[child];

//
//            if (child != size - 1 && compare(pq[child + 1], pq[child]) < 0)
//                child++;
//            if (compare(pq[child], tmp) < 0)
//
//            else
//                break;
        }
        pq[index] = tmp;

    }


    int compare(Comparable a, Comparable b) {
        return ((T) a).compareTo((T) b);
    }

    /**
     * Create a heap.  Precondition: none.
     */
    void buildHeap() {
        for (int i = parent(size - 1); i >= 0; i--) {
            percolateDown(i);
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    // Resize array to double the current size
//    void resize() {
//        Arrays.copyOf(pq, pq.length * 2);
//    }

    void resize() {
        Comparable[] pq_new = Arrays.copyOf(pq, pq.length * 2);
        this.pq = pq_new;
    }

    // Assign x to pq[i].  Indexed heap will override this method
    void move(int i, T x) {
        pq[i] = x;
    }


    public static void main(String[] args) {
        Integer[] arr = {0, 9, 7, 5, 3, 1, 8, 6, 4, 2};
//        Integer[] arr = {9, 4, 0, 5};
        BinaryHeap<Integer> h = new BinaryHeap(arr.length);

        System.out.print("Before:");
        for (Integer x : arr) {
            h.offer(x);
            System.out.print(" " + x);
        }
        System.out.println();

//        h.add(15);
//
//        int tmp = h.size;
//        for (int i = 0; i < tmp; i++) {
//            System.out.print(h.poll() + " ");
//        }

        for (int i = 0; i < arr.length; i++) {
            arr[i] = h.poll();
        }

        System.out.print("After :");
        for (Integer x : arr) {
            System.out.print(" " + x);
        }
        System.out.println();
    }
}
