package qxqw170003; /**
 * @author Binary search tree (starter code)
 **/

import java.util.*;

public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }

    Entry<T> root;
    int size;

    public BinarySearchTree() {
        root = null;
        size = 0;
    }


    /**
     * TO DO: Is x contained in tree?
     */
    public boolean contains(T x) {
        if (this.get(x) == null) return false;
        return true;
    }


    /**
     * TO DO: Is there an element that is equal to x in the tree?
     * Element in tree that is equal to x is returned, null otherwise.
     */
    public T get(T x) {
        if (this.root == null || this.size == 0) return null;

        Entry<T> curr = this.root;
        while(curr!= null){
            if(curr.element.compareTo(x) == 0){
                return curr.element;
            }
            else if(curr.element.compareTo(x) < 0){
                if(curr.right != null){
                    if(curr.right.element.compareTo(x) == 0)
                        return curr.right.element;
                    curr = curr.right;
                }
                else return null;
            }
            else {
                if(curr.left != null){
                    if(curr.left.element.compareTo(x) == 0)
                        return curr.left.element;
                    curr = curr.left;
                }
                else return null;
            }
        }
        return null;
    }



    /**
     * TO DO: Add x to tree.
     * If tree contains a  with same element, replace element by x.
     * Returns true if x is a new element added to tree.
     */
    public boolean add(T x) {
        Entry<T> newEntry = new Entry(x, null, null);
        if (this.root == null) {
            this.root = newEntry;
            this.size++;
            return true;
        }
        Entry<T> curr = this.root;
        while (curr != null) {
            if (curr.element.compareTo(x) == 0) {
                curr.element = x;
                return false;
            } else if (curr.element.compareTo(x) < 0) {
                if (curr.right == null) {
                    curr.right = newEntry;
                    this.size++;
                    return true;
                }
                curr = curr.right;
            } else {
                if (curr.left == null) {
                    curr.left = newEntry;
                    this.size++;
                    return true;
                }
                curr = curr.left;
            }
        }
        return true;
    }

    /**
     * TO DO: Remove x from tree.
     * Return x if found, otherwise return null
     */

    public T remove(T x) {
        if(!this.contains(x)) return null;
        this.root = remove(this.root, x);
        this.size--;
        return x;
    }

    Entry remove(Entry<T> root, T x) {
        if (root == null) return root;
        if (root.element.compareTo(x) > 0)
            root.left = remove(root.left, x);
        else if (root.element.compareTo(x) < 0)
            root.right = remove(root.right, x);
        else {
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;
            root.element = min(root.right);
            root.right = remove(root.right, root.element);
        }

        return root;
    }

    T min(Entry<T> root) {
        T minv = root.element;
        while (root.left != null) {
            minv = root.left.element;
            root = root.left;
        }
        return minv;
    }


    public T min() {
        return min(this.root);
    }

    public T max() {
        if (this.root.right == null) return this.root.element;
        Entry<T> curr = this.root;
        while (curr.right != null) {
            if (curr.right == null) break;
            curr = curr.right;
        }

        return curr.element;
    }

    // TODO: Create an array with the elements using in-order traversal of tree
    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        int index = 0;
        /* write code to place elements in array here */
        Stack<Entry<T>> stack = new Stack<>();
        Entry<T> curr = this.root;
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.peek();
            arr[index++] = stack.pop().element;

            curr = curr.right;
        }
        return arr;

    }


// Start of Optional problem 2

    /**
     * Optional problem 2: Iterate elements in sorted order of keys
     * Solve this problem without creating an array using in-order traversal (toArray()).
     */
    public Iterator<T> iterator() {
        return null;
    }

    // Optional problem 2.  Find largest key that is no bigger than x.  Return null if there is no such key.
    public T floor(T x) {
        return null;
    }

    // Optional problem 2.  Find smallest key that is no smaller than x.  Return null if there is no such key.
    public T ceiling(T x) {
        return null;
    }

    // Optional problem 2.  Find predecessor of x.  If x is not in the tree, return floor(x).  Return null if there is no such key.
    public T predecessor(T x) {
        return null;
    }

    // Optional problem 2.  Find successor of x.  If x is not in the tree, return ceiling(x).  Return null if there is no such key.
    public T successor(T x) {
        return null;
    }

// End of Optional problem 2

    public static void main(String[] args) {
        BinarySearchTree<Integer> t = new BinarySearchTree<>();
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int x = in.nextInt();
            if (x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
                System.out.println("Tree Size: " + t.size);
            } else if (x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for (int i = 0; i < t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;
            }
        }
    }


    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    // Inorder traversal of tree
    void printTree(Entry<T> Entry) {
        if (Entry != null) {
            printTree(Entry.left);
            System.out.print(" " + Entry.element);
            printTree(Entry.right);
        }
    }

}
/*
Sample input:
1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0

Output:
Add 1 : [1] 1
Add 3 : [2] 1 3
Add 5 : [3] 1 3 5
Add 7 : [4] 1 3 5 7
Add 9 : [5] 1 3 5 7 9
Add 2 : [6] 1 2 3 5 7 9
Add 4 : [7] 1 2 3 4 5 7 9
Add 6 : [8] 1 2 3 4 5 6 7 9
Add 8 : [9] 1 2 3 4 5 6 7 8 9
Add 10 : [10] 1 2 3 4 5 6 7 8 9 10
Remove -3 : [9] 1 2 4 5 6 7 8 9 10
Remove -6 : [8] 1 2 4 5 7 8 9 10
Remove -3 : [8] 1 2 4 5 7 8 9 10
Final: 1 2 4 5 7 8 9 10 

*/
