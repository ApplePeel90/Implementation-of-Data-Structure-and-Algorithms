/*
Name: Hemali Amitkumar Patel
Netid: hap170002

Nmae: Qi Wang
NetId: qxw170003
*/

package qxw170003;


public class CuckooHashing<T extends Comparable<? super T>> {
    private static final float INITIAL_LOAD_FACTOR = 0.5f;
    private static final int TABLE_NUM = 3;
    private static final int TABLE_LENGTH = 16;


    private T[][] table;
    private int size;
    private float load_factor;

    /**
     * Constructor with default load factor and table length
     */
    CuckooHashing(){
        size = 0;
        this.load_factor = INITIAL_LOAD_FACTOR;
        table = (T[][]) new Comparable[TABLE_NUM][TABLE_LENGTH];
    }

    /**
     * Constructor that takes customized load factor and table length
     * @param load_factor
     * @param table_length
     */
    CuckooHashing(float load_factor, int table_length){
        size = 0;
        this.load_factor = load_factor;
        this.table = (T[][]) new Comparable[TABLE_NUM][table_length];
    }

    /**
     * Double the table size and add current elements into the new table
     */
    public void rehash(){
        T[][] table_tmp = this.table.clone();
        this.table = (T[][]) new Comparable[TABLE_NUM][table_tmp[0].length*2];
        this.size=0;
        for(int i = 0; i < TABLE_NUM; i++){
            for(int j = 0; j < table_tmp[0].length; j++){
                if(table_tmp[i][j] != null)
                    add(table_tmp[i][j]);
            }
        }
    }

    /**
     * Add a new element
     * Return true if added, false if the element already exists
     * @param x
     */
    public boolean add(T x) {
        if (this.contains(x)) return false;
        if ((((float) size + 1) / (table[0].length * TABLE_NUM)) > load_factor) rehash();

        int i = 0;
        while (true) {
            int idx = index(x, i);
            T tmp = table[i][idx];
            if (tmp != null) {
                table[i][idx] = x;
                x = tmp;
                i++;
                i %= TABLE_NUM;
            } else {
                table[i][idx] = x;

                size++;
                return true;
            }
        }
    }

    /**
     * Check whether the hashmap contains the element
     * Return true if it contains, false otherwise
     * @param x
     */
    public boolean contains(T x){
        for(int i = 0; i < TABLE_NUM; i++){
            int idx = index(x, i);
            if(table[i][idx] != null && table[i][idx].compareTo(x) == 0)
                return true;
        }
        return false;
    }

    /**
     * Remove element from the hashmap
     * Return false if the element doesn't exist in the hashmap, true if the element got removed
     * @param x
     */
    public boolean remove(T x){
        for(int i = 0; i < TABLE_NUM; i++){
            int idx = index(x, i);
            if(table[i][idx] != null && table[i][idx].compareTo(x) == 0){
                table[i][idx] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    /**
     * Return the index of element x in a specific table column
     * @param x
     * @param table_col
     */
    public int index(T x, int table_col){
        int h = x.hashCode();
        if(table_col == 0){
            h ^= (h>>>20)^(h>>>12);
            h ^= (h>>>2) ^ (h>>>4);
        }
        else if(table_col == 1){
            h ^= (h>>>11)^(h>>>16);
            h ^= (h>>>1) ^ (h>>>9);
        }
        else{
            h ^= (h>>>17)^(h>>>13);
            h ^= (h>>>1) ^ (h>>>7);
        }
        return h&(table[0].length-1);
    }

    /**
     * Return the size of the hashmap
     */
    public int size(){
        return this.size;
    }


    /**
     * Add the given array's elements into hashmap.
     * Return the map size
     * @param arr
     */
    public int distinctElements(T[] arr){
        for(int i = 0; i < arr.length; i++){
            this.add(arr[i]);
        }
        return this.size;
    }
}
