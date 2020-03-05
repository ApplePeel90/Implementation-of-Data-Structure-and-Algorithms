// Starter code for SP9

// Change to your netid
package qxw170003;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class BinaryHeap<T extends Comparable<? super T>> {
    Comparable[] pq;
    int size;

    /**
     * Constructor for building an empty priority queue using natural ordering of T
     * @param maxCapacity
     */
    public BinaryHeap(int maxCapacity) {
        pq = new Comparable[maxCapacity];
        size = 0;
    }

    /**
     * Add one element to the heap
     * @param x
     * @return true if added
     */
    public boolean add(T x) {
        if (size == pq.length) {
            this.resize();
        }
        this.pq[size] = x;

        percolateUp(size);

        size++;
        return true;
    }

    /**
     * Add one element to the heap
     * @param x
     * @return true if added
     */
    public boolean offer(T x) {
        return this.add(x);
    }

    /**
     * Pop one element from heap
     * @return the element if heap is not empty
     * @throws NoSuchElementException
     */
    public T remove() throws NoSuchElementException {
        T result = poll();
        if (result == null) {
            throw new NoSuchElementException("Priority queue is empty");
        } else {
            return result;
        }
    }

    /**
     * Pop one element from heap
     * @return the element if heap is not empty, null otherwise
     */
    public T poll() {
        if (size == 0) return null;
        T result = (T) pq[0];
        pq[0] = pq[size-1];
        size--;
        percolateDown(0);
        return result;
    }

    /**
     * Find the minimum element from the heap
     * @return the minimum element if heap is not empty, null otherwise
     */
    public T min() {
        return peek();
    }

    /**
     * Find the minimum element from the heap
     * @return the minimum element if heap is not empty, null otherwise
     */
    public T peek() {
        if (size == 0) {
            return null;
        }
        return (T) pq[0];
    }

    /**
     * Find the index of parent element
     * @param i
     * @return the index of parent element
     */
    int parent(int i) {
        return (i - 1) / 2;
    }

    /**
     * Find the index of left child element
     * @param i
     * @return the index of left child element
     */
    int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * Percolate the element up to its correct position
     * @param index
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
     * Percolate the element down to its correct position
     * @param index
     */
    void percolateDown(int index) {
        int left = leftChild(index);
        int right = left + 1;
        int smallest = index;
        if(left < size && compare(pq[left], pq[index]) < 0) smallest = left;
        if(right < size && compare(pq[right], pq[smallest]) < 0) smallest = right;
        if(smallest != index){
            T tmp = (T) pq[index];
            move(index, (T) pq[smallest]);
            move(smallest, tmp);
            percolateDown(smallest);
        }
    }

    /**
     * Move the element to dest position
     * use this whenever an element moved/stored in heap. Will be overridden by IndexedHeap
     * @param dest
     * @param x
     */
    void move(int dest, T x) {
        pq[dest] = x;
    }

    /**
     * Compare element a and b
     * @param a
     * @param b
     * @return 1 id a is greater, b otherwise
     */
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

    /**
     * Check whether the heap is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Check the size of heap
     * @return the size
     */
    public int size() {
        return size;
    }

    /**
     * Resize array to double the current size
     */
    void resize() {
        Comparable[] pq_new = Arrays.copyOf(pq, pq.length * 2);
        this.pq = pq_new;
    }






    public interface Index {
        public void putIndex(int index);

        public int getIndex();
    }

    public static class IndexedHeap<T extends Index & Comparable<? super T>> extends BinaryHeap<T> {

        /**
         * Constructor for IndexedHeap
         * @param capacity
         */
        IndexedHeap(int capacity) {
            super(capacity);
        }

        /**
         * Override BinaryHeap add(), add a new element to heap
         * @param x
         * @return
         */
        public boolean add(T x) {
            if (size == pq.length) this.resize();
            // place element into heap at bottom
            this.pq[size++] = x;

            percolateUp(size-1);
            return true;
        }

        /**
         * restore heap order property after the priority of x has decreased
         * @param x
         */
        void decreaseKey(T x) {
            int index = x.getIndex();
            percolateUp(index);
        }

        /**
         * Override move() in BinaryHeap, move element x to position i
         * @param i
         * @param x
         */
        @Override
        void move(int i, T x) {
            super.move(i, x);
            x.putIndex(i);
        }
    }

    public static void main(String[] args) {
        Integer[] arr = {0, 9, 7, 5, 3, 1, 8, 6, 4, 2};
        BinaryHeap<Integer> h = new BinaryHeap(arr.length);

        System.out.print("Before:");
        for (Integer x : arr) {
            h.offer(x);
            System.out.print(" " + x);
        }
        System.out.println();

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
