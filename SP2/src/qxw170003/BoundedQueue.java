package qxw170003;

import java.util.Scanner;

public class BoundedQueue<T> {
    Object[] queue;
    int maxSize;
    int front = -1;
    int rear = -1;
    int count = 0;

    public BoundedQueue(int size) {
        queue = new Object[size];
        maxSize = size;
    }

    protected boolean offer(T x) {
        if (count == maxSize) {
            return false;
        }
        queue[(++rear) % maxSize] = x;
        rear %= maxSize;
        count++;
        return true;
    }

    public T poll() {
        if (isEmpty()) return null;
        front = (front + 1)%maxSize;
        Object tmp = queue[front];
        count--;
//        if (isEmpty())
//            rear = front;
        return (T) tmp;
    }

    public boolean isEmpty() {
        if (count == 0) {
            return true;
        }
        return false;
    }

    public T peek() {
        if(isEmpty()) return null;
        return (T) queue[(front+1)%maxSize];
    }

    public int size() {
        return count;
    }

    public void clear() {
        count = 0;
        front = -1;
        rear = -1;
    }

    // Assume that array a is large enough to copy all the elements in queue;
    public void toArray(Object[] a) {
        int index = 0;
        int size = count;
        int i = (front + 1) % maxSize;
        while(size > 0){
            a[index++] = queue[i++];
            i %= maxSize;
            size--;
        }
    }


    public void printQueue() {
        int size = count;
        int i = (front + 1) % maxSize;
        while(size > 0){
            System.out.print(queue[i++] + " ");
            i %= maxSize;
            size--;
        }

        System.out.println();
    }

    public static void main(String[] args) {
        BoundedQueue<Integer> q = new BoundedQueue<>(3);

        Scanner in = new Scanner(System.in);
        whileloop:
        while (in.hasNext()) {
            int com = in.nextInt();
            switch (com) {
                case 1:  // queue.offer()   Enqueue an element x to the queue;
                    int x = in.nextInt();
                    q.offer(x);
                    q.printQueue();
                    break;
                case 2:  // queue.poll()   Remove and return the element at the front of the queue, return null if the queue is empty
                    System.out.println(q.poll());
                    q.printQueue();
                    break;
                case 3: // queue.peek()   Return front element, without removing it (null if queue is empty)
                    System.out.println(q.peek());
                    q.printQueue();
                    break;
                case 4: // queue.size()   Return the number of elements in the queue
                    int queueSize = q.size();
                    System.out.println(queueSize);
                    q.printQueue();
                    break;
                case 5: // queue.isEmpty()   Check if the queue is empty
                    boolean isEmpty = q.isEmpty();
                    System.out.println(q.isEmpty());
                    q.printQueue();
                    break;
                case 6: // queue.clear()   Clear the queue (size=0)
                    q.clear();
                    q.printQueue();
                    break;
                case 7: // queue.toArray(T[] a)   Fill user supplied array with the elements of the queue, in queue order
                    Integer[] arr = new Integer[10];
                    q.toArray(arr);
                    System.out.print("arr: ");
                    for (int i = 0; i < q.size(); i++) {
                        System.out.print(arr[i] + " ");
                    }
                    System.out.println();
                    q.printQueue();
                    break;


                default:  // Exit loop
                    break whileloop;
            }
        }
    }
}

/*
Sample input            Sample output:
1 1                     (1)
1 2                     (1 2)
1 3                     (1 2 3)
2                       (1, 2 3)
1 5                     (2 3 5)
3                       (2, 2 3 5)
4                       (3, 2 3 5)
5                       (false, 2 3 5)
7                       (arr: 2 3 5, 2 3 5)
6                       ()
5                       (true)
 */