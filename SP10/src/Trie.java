// starter code for Tries

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Trie<V> {
    private class Entry {
        V value;
        HashMap<Character, Entry> child;
        int depth;
        boolean endOfWord;

        Entry(V value, int depth, boolean endOfWord) {
            this.value = value;
            child = new HashMap<>();
            this.depth = depth;
            this.endOfWord = endOfWord;
        }
    }

    private Entry root;
    private int size;

    public Trie() {
        root = new Entry(null, 0, false);
        size = 0;
    }

    private V put(Iterator<Character> iter, V value) {
        Entry curr = root;
        char c  = iter.next();
        while(true){
            Entry node = root.child.get(c);
            if(node == null){
                node = new Entry(value, curr.depth + 1, false);
                curr.child.put(c, node);
            }
            curr = node;
            if(iter.hasNext()) c = iter.next();
            else break;

        }
        curr.endOfWord = true;
        return curr.value;
    }

    private V get(Iterator<Character> iter) {
        Entry curr = root;
        char c = iter.next();
        while(iter.hasNext()){
            Entry node = curr.child.get(c);
            if(node == null) return null;

            curr = node;
            c = iter.next();
        }
        if(curr.endOfWord) return curr.value;
        return null;
    }

    private V remove(Iterator<Character> iter) {
        return null;
    }


    // public methods

    public V put(String s, V value) {
        if(s.length() == 0) return null;
        StringIterator iter = new StringIterator(s);
        return put(iter, value);
    }

    public V get(String s) {
        if(s.length() == 0) return null;
        StringIterator iter = new StringIterator(s);
        return get(iter);
    }

    public V remove(String s) {
        return null;
    }

    // How many words in the dictionary start with this prefix?
    public int prefixCount(String s) {
        return 0;
    }

    public int size() {
        return size;
    }

    public static class StringIterator implements Iterator<Character> {
        char[] arr;
        int index;

        public StringIterator(String s) {
            arr = s.toCharArray();
            index = 0;
        }

        public boolean hasNext() {
            return index < arr.length;
        }

        public Character next() {
            return arr[index++];
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Trie<Integer> trie = new Trie<>();
        int wordno = 0;
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String s = in.next();
            if (s.equals("End")) {
                break;
            }
            wordno++;
            trie.put(s, wordno);
        }

        while (in.hasNext()) {
            String s = in.next();
            Integer val = trie.get(s);
            System.out.println(s + "\t" + val);
        }
    }
}
