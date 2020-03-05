/* Starter code for LP3 */

// Change this to netid of any member of team
package spa180000;


import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
    static final int PossibleLevels = 33;
    Entry<T> head;
    int size;
    Entry<T>[] last;

    static class Entry<E> {
        E element;
        Entry<E>[] next;
        Entry<E> prev;
        int[] span;

        public Entry(E x, int lev) {
            element = x;
            next = new Entry[lev];
            // add more code if needed
            span = new int[lev];
        }

        public E getElement() {
            return element;
        }

    }

    // Constructor
    public SkipList() {
        head = new Entry<>(null, PossibleLevels);
        last = new Entry[PossibleLevels];
        size = 0;
    }

    /**
     * Helper function to find an element
     *
     * @param x
     */
    private int find(T x) {
        Entry<T> p = head;
        int index = 0;
        for (int i = PossibleLevels - 1; i >= 0; i--) {
            while (p.next[i] != null && p.next[i].element.compareTo(x) < 0) {
                index += p.span[i];
                p = p.next[i];
            }
            last[i] = p;
        }
        return index;
    }

    /**
     * Choose Height for an element randomly
     *
     * @return height
     */
    private int chooseHeight() {
        int i = 1;
        Random r = new Random();
        while (r.nextBoolean()) {
            if (i++ >= PossibleLevels) break;
        }
        return i;
    }

    // Add x to list. If x already exists, reject it. Returns true if new node is added to list
    public boolean add(T x) {
        return add(x, chooseHeight());
    }

    public boolean add(T x, int height) {
        int index = find(x); // Index of element to be inserted
        if (last[0].next[0] != null && last[0].next[0].element.equals(x)) return false;

        Entry<T> node = new Entry<>(x, height);
        int j = -1;
        Entry<T> u = head;
        for (int i = PossibleLevels - 1; i >= 0; i--) {
            while (u.next[i] != null && j + u.span[i] < index) {
                j += u.span[i];
                u = u.next[i];
            }
            u.span[i]++;
            if (i <= height - 1) {
                node.next[i] = u.next[i];
                u.next[i] = node;
                node.span[i] = u.span[i] - (index - j);
                u.span[i] = index - j;
            }
        }
        size++;
        return true;
    }

    public void printSpans() {
        Entry<T> ent = head;
        for (int i = 0; i <= size; i++) {
            System.out.println(Arrays.toString(ent.span));
            ent = ent.next[0];
        }
    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {
        find(x);
        if (last[0].next[0] == null) return null;
        return last[0].next[0].element;
    }

    // Does list contain x?
    public boolean contains(T x) {
        find(x);
        if (last[0].next[0] == null) return false;
        return last[0].next[0].element.equals(x);
    }

    // Return first element of list
    public T first() {
        return get(0);
    }

    // Find largest element that is less than or equal to x
    public T floor(T x) {
        find(x);
        if (last[0].next[0] != null && last[0].next[0].element.equals(x)) return last[0].next[0].element;
        return last[0].element;
    }

    // Return element at index n of list.  First element is at index 0.
    public T get(int n) {
        return getLog(n);
//        return getLinear(n);
    }

    // O(n) algorithm for get(n)
    public T getLinear(int n) {
        Entry<T> ent = head.next[0];
        if (ent == null || n >= size) return null;
        if (n == 0) return ent.element;
        for (int i = 1; i <= n; i++) {
            ent = ent.next[0];
        }
        return ent.element;
    }

    // Optional operation: Eligible for EC.
    // O(log n) expected time for get(n).
    public T getLog(int n) {
        Entry<T> node = head;
        n++;
        if (n > size || n < 0) return null;
        for (int level = PossibleLevels - 1; level >= 0; level--) {
            while (n >= node.span[level] && node.next[level] != null) {
                n -= node.span[level];
                node = node.next[level];
            }
        }
        return node.element;
    }

    // Is the list empty?
    public boolean isEmpty() {
        if (size == 0) return true;
        return false;
    }


    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() {

        return new SkipListIterator();
    }

    public class SkipListIterator implements Iterator<T> {
        Entry<T> cursor;

        SkipListIterator(){
            cursor = head;
//            prev = null;
        }

        @Override
        public boolean hasNext() {
            return cursor.next[0]!=null;
        }

        @Override
        public T next() {
            cursor = cursor.next[0];
            return cursor.element;
        }
    }

    // Return last element of list
    public T last() {
        return get(size - 1);
    }

    // Optional operation: Reorganize the elements of the list into a perfect skip list
    // Not a standard operation in skip lists. Eligible for EC.

    public void rebuild() {
        printSpans();
        int[] newLevels = new int[size];
        int maxLevel = (int) Math.ceil(Math.log(size) / Math.log(2));
        int x = 1;

        boolean flag = false;
        Entry<T> node = head;
        for (int i = 0; i < size; i++) {
            int newHeight = 1;
            node = node.next[0];

            if (x >= maxLevel - 1) {
                flag = true;
            }

            if (i % 2 == 1) {
                newHeight+=x;
                if (flag) x--;
                else x++;
            }
            replace(node.element, newHeight);
        }
        printSpans();
    }

    public void replace(T x, int h) {
        remove(x);
        add(x,h);
        return;
    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
        if (!contains(x)) return null;
        Entry<T> ent = last[0].next[0];
        for (int i = 0; i < PossibleLevels; i++) {
            if (i <= ent.next.length - 1) {
                last[i].span[i] += ent.span[i] - 1;
                last[i].next[i] = ent.next[i];
            } else {
                last[i].span[i] -= 1;
            }
        }
        size--;
        return ent.element;
    }

    // Return the number of elements in the list
    public int size() {
        return this.size;
    }
}
