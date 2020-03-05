/*
Name: Hemali Amitkumar Patel
Netid: hap170002

Nmae: Qi Wang
NetId: qxw170003
*/

package qxw170003;
/**
 * Starter code for Red-Black Tree
 */

import java.util.Scanner;


public class RedBlackTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    static class Entry<T> extends BinarySearchTree.Entry<T> {
        boolean color;

        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            color = RED;
        }

        boolean isRed() {
            return color == RED;
        }

        boolean isBlack() {
            return color == BLACK;
        }
    }

    RedBlackTree() {
        super();
        root = createNIL();
    }

    Entry<T> createNIL(){
        Entry<T> NIL = new Entry<T>(null, null, null);
        NIL.parent = NIL;
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.color = BLACK;
        return NIL;
    }

    /**
     * Add Entry with element value t into the tree.
     * @param t
     * @return true if added, false if t already exists
     */
    public boolean addRBT(T t) {
        Entry<T> NIL1 = createNIL();
        Entry<T> NIL2 = createNIL();

        Entry<T> newEntry = new Entry<T>(t, NIL1, NIL2);
        NIL1.parent = newEntry;
        NIL2.parent = newEntry;
        Entry<T> par = (Entry<T>) find(t);

        if (!add(newEntry)) return false;
        else {
            newEntry.parent = par;
            newEntry.color = RED;
        }
        Entry<T> x = newEntry;

        while (x != root && ((Entry<T>) x.parent).isRed()) {
            // x.parent is left child of x.grandparent
            if (x.parent.element.compareTo(x.parent.parent.element) < 0) {
                Entry<T> y = (Entry<T>) x.parent.parent.right; // y is uncle
                if (y.element != null && y.isRed()) {
                    ((Entry<T>) x.parent).color = BLACK;
                    y.color = BLACK;
                    ((Entry<T>) x.parent.parent).color = RED;
                    x = (Entry<T>) x.parent.parent;
                }
                // x is right child, zig-zag to straight line
                else if (x.element.compareTo(x.parent.element) > 0) {
                    Entry<T> tmp = (Entry<T>) x.parent;
                    rotateLeft((Entry<T>) x.parent);
                    x = tmp;
                }
                // x is left child
                else {
                    ((Entry<T>) x.parent).color = BLACK;
                    ((Entry<T>) x.parent.parent).color = RED;
                    rotateRight((Entry<T>) x.parent.parent);
                }
            }
            // x.parent is right child of x.grandparent
            else {
                Entry<T> y = (Entry<T>) x.parent.parent.left; // y is uncle
                if (y.element != null && y.isRed()) {
                    ((Entry<T>) x.parent).color = BLACK;
                    y.color = BLACK;
                    ((Entry<T>) x.parent.parent).color = RED;
                    x = (Entry<T>) x.parent.parent;
                }
                // x is left child
                else if (x.element.compareTo(x.parent.element) < 0) {
                    Entry<T> tmp = (Entry<T>) x.parent;
                    rotateRight((Entry<T>) x.parent);
                    x = tmp;
                }
                // x is right child
                else {
                    ((Entry<T>) x.parent).color = BLACK;
                    ((Entry<T>) x.parent.parent).color = RED;
                    rotateLeft((Entry<T>) x.parent.parent);
                }
            }
        }
        Entry<T> rot = (Entry<T>) this.root;
        rot.color = BLACK;
        return true;
    }

    /**
     * Left rotate on the tree, p is the top of Entry chain
     * @param p
     */
    void rotateLeft(Entry<T> p) {
        if(p.parent.element != null) {
            if (p == p.parent.left) {
                p.parent.left = p.right;
            }
            else {
                p.parent.right = p.right;
            }
            p.right.parent = p.parent;
            p.parent = (Entry<T>) p.right;
            if (p.right.left.element != null) {
                p.right.left.parent = p;
            }
            p.right = p.right.left;
            p.parent.left = p;
        }

        else {
            Entry<T> right = (Entry<T>) root.right;
            root.right = right.left;
            if (right.left.element != null) right.left.parent = (Entry<T>) root;
            root.parent = right;
            right.left = root;
            right.parent = createNIL();
            root = right;
        }
    }

    /**
     * Right rotate on the tree, p is the top of Entry chain
     * @param p
     */
    void rotateRight(Entry<T> p) {
        if (p.parent.element != null) {
            if (p == p.parent.left) {
                p.parent.left = p.left;
            }
            else {
                p.parent.right = p.left;
            }
            p.left.parent = p.parent;
            p.parent = p.left;
            if (p.left.right.element != null) {
                p.left.right.parent = p;
            }
            p.left = p.left.right;
            p.parent.right = p;
        }
        else {  //Need to rotate root
            Entry<T> left = (Entry<T>) root.left;
            root.left = root.left.right;
            if (left.right.element != null) left.right.parent = (Entry<T>) root;
            root.parent = left;
            left.right = root;
            left.parent = createNIL();
            root = left;
        }
    }

    /**
     * Remove y and replace with x
     * @param y
     * @param v
     */
    protected void splice(Entry<T> y, Entry<T> v) {
        super.splice(y, v);
        v.parent = y.parent;
    }

    /**
     * TO DO: Remove x from tree.
     * Return x if found, otherwise return null
     */
    public boolean removeRBT(T t) {
        if (!this.contains(t)) return false;
        // z is the Entry to be removed
        Entry<T> z = (Entry<T>) find(t);
        // y is the actually removed element
        Entry<T> y = z;
        boolean fixUP = y.isBlack();
        Entry<T> x;
        if (z.right.element == null){
            x = (Entry<T>) z.left;
            splice(z, x);
        }

        else if (z.left.element == null){
            x = (Entry<T>) z.right;
            splice(z, x);
        }
        else {
            y = (Entry<T>) min(z.right);
            fixUP = y.isBlack();
            x = (Entry<T>) y.right;

            if(y.parent == z) x.parent = y;
            else{
                splice(y,x);
                y.right = z.right;
                y.right.parent = y;
            }

            splice(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        this.size--;

        if (fixUP) fixUp(x);

        return true;
    }


    /**
     * Adjust structure and colors to keep to properties
     * @param x is the position of problem Entry
     */
    public void fixUp(Entry<T> x){

        while(x!=root && x.color == BLACK){
            // x is left child
            if(x == x.parent.left){
                Entry<T> sibling = (Entry<T>)x.parent.right;

                // sibling is red
                if(sibling.color == RED){
                    sibling.color = BLACK;
                    ((Entry<T>)x.parent).color = RED;
                    rotateLeft((Entry<T>) x.parent);
                    sibling = (Entry<T>)x.parent.right;
                }

                // sibling is black and both its childern are black
                else if(((Entry<T>)sibling.left).isBlack() && ((Entry<T>)sibling.right).isBlack()){
                    sibling.color = RED;
                    x = (Entry<T>) x.parent;
                    continue;
                }

                // sibling is black and right child is black
                else if(((Entry<T>)sibling.right).isBlack()){
                    ((Entry<T>)sibling.left).color = BLACK;
                    sibling.color = RED;
                    rotateRight(sibling);
                    sibling = (Entry<T>)x.parent.right;
                }

                // sibling is black and left child is black
                else{
                    sibling.color = ((Entry<T>)x.parent).color;
                    ((Entry<T>)x.parent).color = BLACK;
                    ((Entry<T>)sibling.right).color = BLACK;
                    rotateLeft((Entry<T>) x.parent);
                    x = (Entry<T>)root;
                }
            }
            // x is right child
            else{
                Entry<T> sibling = (Entry<T>)x.parent.left;

                // sibling is red
                if(sibling.color == RED){
                    sibling.color = BLACK;
                    ((Entry<T>)x.parent).color = RED;
                    rotateRight((Entry<T>) x.parent);
                    sibling = (Entry<T>)x.parent.left;
                }

                // sibling is black and both its childern are black
                else if(((Entry<T>)sibling.right).isBlack() && ((Entry<T>)sibling.left).isBlack()){
                    sibling.color = RED;
                    x = (Entry<T>) x.parent;
                    continue;
                }

                // sibling is black and right child is black
                else if(((Entry<T>)sibling.left).isBlack()){
                    ((Entry<T>)sibling.right).color = BLACK;
                    sibling.color = RED;
                    rotateLeft(sibling);
                    sibling = (Entry<T>)x.parent.left;
                }

                // sibling is black and left child is black
                else{
                    sibling.color = ((Entry<T>)x.parent).color;
                    ((Entry<T>)x.parent).color = BLACK;
                    ((Entry<T>)sibling.left).color = BLACK;
                    rotateRight((Entry<T>) x.parent);
                    x = (Entry<T>)root;
                }
            }
        }
        x.color = BLACK;
    }


    public static void main(String[] args) {
        RedBlackTree<Integer> t = new RedBlackTree<>();
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int x = in.nextInt();
            if (x > 0) {
                System.out.print("Add " + x + " : ");
                t.addRBT(x);
                t.printTree();
                System.out.println("Tree Size: " + t.size);
            } else if (x < 0) {
                System.out.print("Remove " + x + " : ");
                t.removeRBT(-x);
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
        printTree((Entry<T>) root);
        System.out.println();
    }

    // Inorder traversal of tree
    void printTree(Entry<T> Entry) {
        if (Entry.element != null) {
            printTree((Entry<T>) Entry.left);
            System.out.print(" " + Entry.element + " " + Entry.color);
            printTree((Entry<T>) Entry.right);
        }
    }
}

