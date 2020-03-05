/*
Name: Hemali Amitkumar Patel
Netid: hap170002

Nmae: Qi Wang
NetId: qxw170003
*/

package qxw170003; /**
 * @author Binary search tree (starter code)
 **/

import java.util.*;

public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {
    static class Entry<T> {
        public Entry<T> parent;
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


    Entry<T> createNIL() {
        Entry<T> NIL = new Entry<T>(null, null, null);
        NIL.parent = NIL;
        NIL.left = NIL;
        NIL.right = NIL;
        return NIL;
    }

    public BinarySearchTree() {
        root = createNIL();
        size = 0;
    }


    /**
     * TO DO: Find the Entry that has the same element value with input element
     * Return the entry of the target element if it exists, return it's parent's Entry if not
     */
    public Entry<T> find(T x) {
        Entry<T> curr = this.root;

        while (curr.element != null) {
            if (curr.element.compareTo(x) == 0) {
                break;
            } else if (curr.element.compareTo(x) < 0) {
                if (curr.right.element == null) break;
                else curr = curr.right;
            } else {
                if (curr.left.element == null) break;
                else curr = curr.left;
            }
        }
        return curr;
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
        if (this.root.element == null || this.size == 0) return null;
        Entry<T> tmp = find(x);
        return tmp.element.equals(x) ? tmp.element : null;
    }


    /**
     * TO DO: Add x to tree.
     * If tree contains a  with same element, replace element by x.
     * Returns true if x is a new element added to tree.
     */
    public boolean add(T x) {
        Entry<T> NIL1 = createNIL();
        Entry<T> NIL2 = createNIL();
        Entry<T> newEntry = new Entry(x, NIL1, NIL2);
        newEntry.parent = null;
        return add(newEntry);
    }

    public boolean add(Entry<T> ent) {
        if (this.root.element == null) {
            this.root = ent;
            this.size++;
            return true;
        }
        Entry<T> tmp = find(ent.element);
        if (tmp.element.equals(ent.element)) return false;
        else if (tmp.element.compareTo(ent.element) > 0) {
            tmp.left = ent;
            ent.parent = tmp;
        } else {
            tmp.right = ent;
            ent.parent = tmp;
        }
        this.size++;
        return true;
    }


    void splice(Entry<T> y, Entry<T> v) {
        if (y.parent.element == null)
            this.root = v;

        else if (y == y.parent.left)
            y.parent.left = v;

        else
            y.parent.right = v;
        v.parent = y.parent;
    }

    /**
     * TO DO: Remove x from tree.
     * Return x if found, otherwise return null
     */

    public T remove(T x) {
        if (!this.contains(x)) return null;
        remove(this.root, x);
        this.size--;
        return x;
    }

//    public Entry<T> remove(Entry<T> root, T x) {
//        if (root == null) return root;
//        if (root.element.compareTo(x) > 0)
//            root.left = remove(root.left, x);
//        else if (root.element.compareTo(x) < 0)
//            root.right = remove(root.right, x);
//        else {
//            if (root.left == null)
//                return root.right;
//            else if (root.right == null)
//                return root.left;
//            root.element = min(root.right).element;
//            root.right = remove(root.right, root.element);
//        }
//        return root;
//    }


    public void remove(Entry<T> node, T x) {
        if (node.element == null) return;
        Entry<T> toBeRemoved = find(x);
        if (toBeRemoved.left.element == null)
            splice(toBeRemoved, toBeRemoved.right);

        else if (toBeRemoved.right.element == null)
            splice(toBeRemoved, toBeRemoved.left);

        else {
            Entry<T> toReplace = min(toBeRemoved.right);
            if (toReplace.parent != toBeRemoved) {
                splice(toReplace, toReplace.right);
                toReplace.right = toBeRemoved.right;
                toReplace.right.parent = toReplace;
            }
            splice(toBeRemoved, toReplace);
            toReplace.left = toBeRemoved.left;
            toReplace.left.parent = toReplace;
        }
    }


    public Entry<T> min(Entry<T> root) {
        if (root.element == null) return null;
        Entry<T> curr = root;
        while (curr.left.element != null) {
            curr = curr.left;
        }
        return curr;
    }

    public T min() {
        if (this.size == 0) return null;
        return min(this.root).element;
    }


    public Entry<T> max(Entry<T> root) {
        if (root.element == null) return null;
        Entry<T> curr = root;
        while (curr.right.element != null) {
            curr = curr.right;
        }
        return curr;
    }

    public T max() {
        if (this.size == 0) return null;
        return max(this.root).element;
    }

    // TODO: Create an array with the elements using in-order traversal of tree
    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        int index = 0;
        /* write code to place elements in array here */
        Stack<Entry<T>> stack = new Stack<>();
        Entry<T> curr = this.root;
        while (curr.element != null || !stack.isEmpty()) {
            while (curr.element != null) {
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
            //fsdfsdgfsd
        }
    }

    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    // Inorder traversal of tree
    void printTree(Entry<T> Entry) {
        if (Entry.element != null) {
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
