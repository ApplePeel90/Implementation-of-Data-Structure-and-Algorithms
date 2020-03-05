/*  Hemali Amitkumar Patel hap170002
 *  Qi Wang qxw170003  */


package qxw170003;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DoublyLinkedList<T> extends SinglyLinkedList<T> {

    /** Class Entry holds two nodes of the list */
    static class Entry<E> extends SinglyLinkedList.Entry<E> {
        Entry<E> prev;
        Entry(E x, Entry<E> nxt, Entry<E> prv) {
            super(x, nxt);
            this.prev = prv;
        }
    }

    //Dummy header and dummy tail is used.
    public DoublyLinkedList() {
        this.head = new Entry<T>(null, null, null);
        this.tail = new Entry<T>(null, null, null);
        head.next = tail;
        ((Entry)tail).prev = (Entry) head;
        size = 0;
    }

    public Iterator<T> iterator() { return new DLLIterator(); }

    protected class DLLIterator extends SLLIterator {
        DLLIterator(){
            super();
        }

        public boolean hasPrev(){
            return ((Entry)cursor).prev != head && ((Entry)cursor).prev != null;
        }


        @Override
        public boolean hasNext() {
            return cursor.next != tail;
        }


        @Override
        public T next(){
            cursor = cursor.next;
            ready = true;
            return cursor.element;
        }

        public T prev(){
            cursor = ((Entry)cursor).prev;
            ready = true;
            return cursor.element;
        }

        // Removes the current element (retrieved by the most recent next() or prev())
        // Remove can be called only if next() or prev() has been called and the element has not been removed
        @Override
        public void remove(){
            if(!ready){
                throw new NoSuchElementException();
            }

            ((Entry)cursor.next).prev = ((Entry)cursor).prev;
            ((Entry)cursor).prev.next = cursor.next;

            //Move cursor to its previous node after removal
            cursor = ((Entry)cursor).prev;
            ready = false; //Calling remove again without calling next() or prev() will result in exception thrown
            size--;
        }


        // Insert x before the element that will be returned by a call to next().
        public void add(T element) {
            add(new Entry<>(element, null, null));
        }

        public void add(Entry<T> ent){
            ent.prev = ((Entry<T>) cursor).prev;
            ent.next = cursor;
            ((Entry)cursor).prev.next = ent;
            ((Entry)cursor).prev = ent;
            size++;
        }

    } // end of class DLLIterator

    // Add new elements to the end of the list
    @Override
    public void add(T x){
        add(new Entry<>(x, null, null));
    }

    public void add(Entry<T> ent){
        ent.prev = ((Entry<T>) tail).prev;
        ent.next = tail;
        ((Entry<T>) tail).prev.next = ent;
        ((Entry<T>) tail).prev = ent;
        size++;
    }

    public static void main(String[] args) throws NoSuchElementException {
        int n = 10;
        if(args.length > 0) {
            n = Integer.parseInt(args[0]);
        }

        DoublyLinkedList<Integer> lst = new DoublyLinkedList<>();
        for(int i=1; i<=n; i++) {
            lst.add(Integer.valueOf(i));
        }
        lst.printList();


        DoublyLinkedList.DLLIterator it = lst.new DLLIterator();


        Scanner in = new Scanner(System.in);
        whileloop:
        while(in.hasNext()) {
            int com = in.nextInt();
            switch(com) {
                case 1:  // Move to next element and print it
                    if (it.hasNext()) {
                        System.out.println(it.next());
                    } else {
                        break whileloop;
                    }
                    break;

                case 2:  // Remove element
                    it.remove();
                    lst.printList();
                    break;


                case 3:  // Insert x before the element that will be returned by a call to next()
                    int x = in.nextInt();
                    it.add(Integer.valueOf(x));
                    lst.printList();
                    break;

                case 4: // Move to previous element and print it
                    if (it.hasPrev()) {
                        System.out.println(it.prev());
                    } else {
                        break whileloop;
                    }
                    break;


                default:  // Exit loop
                    break whileloop;
            }
        }

        lst.printList();
    }
}

/* Sample input:
   1 1 1 4 4 2 3 55 1 1 1 2 1 2
   Sample output:
10: 1 2 3 4 5 6 7 8 9 10
1
2
3
2
1
9: 2 3 4 5 6 7 8 9 10
10: 55 2 3 4 5 6 7 8 9 10
55
2
3
9: 55 2 4 5 6 7 8 9 10
4
8: 55 2 5 6 7 8 9 10
*/