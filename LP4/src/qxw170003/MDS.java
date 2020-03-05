/**
 * Implementation of Data Structures and Algorithms Long Project 4
 * Authors:
 * Anuj Shah - AUS180000
 * Qi Wang - QXW170003
 * Samarth Agrawal - SPA180000
 * Mayank Bhatia - MXB180024
 */


// Change to your net id
package qxw170003;

// If you want to create additional classes, place them in this file as subclasses of MDS


import java.util.*;

public class MDS {

    private class Item implements Comparable<Item> {
        Long id;
        Money price;
        List<Long> desc;

        Item(Long id, Money price, List<Long> desc) {
            this.id = id;
            this.price = price;
            this.desc = desc;

        }

        /**
         * overriding comparator for Item class
         * @param o
         * @return 1 if greater, 0 if equals, -1 if smaller
         */
        @Override
        public int compareTo(Item o) {
            if(this.equals(o)) return 0;
            if(this.price.compareTo(o.price)==0){
                return this.id>=o.id? 1:-1;
            }
            return this.price.compareTo(o.price);
        }

        /**
         * overriding equals of Item class
         * @param o
         * @return true if equal
         */
        @Override
        public boolean equals(Object o){
            return this.id==((Item)o).id;
        }

    }

    public static final Money ZERO = new Money(0, 0);

    private TreeMap<Long, Item> tree;
    private HashMap<Long, TreeSet<Item>> table;


    // Add fields of MDS here

    /**
     * constructor of MDS class
     */
    public MDS() {
        this.tree = new TreeMap();
        this.table = new HashMap();
    }

    /* Public methods of MDS. Do not change their signatures.
       __________________________________________________________________
       a. Insert(id,price,list): insert a new item whose description is given
       in the list.  If an entry with the same id already exists, then its
       description and price are replaced by the new values, unless list
       is null or empty, in which case, just the price is updated.
       Returns 1 if the item is new, and 0 otherwise.
    */

    /**
     * Method to insert a new item whose description is given in the list
     * @param id
     * @param price
     * @param list
     * @return int 1 if the item is new, and 0 otherwise.
     */
    public int insert(long id, Money price, java.util.List<Long> list) {
        int output = 1;
        Item item = this.tree.get(id);
        List<Long> names = new LinkedList<>(list);
        if (item != null) {
            if (list==null || list.size() == 0) {
                item.price = price;
                return 0;
            }
            delete(id);
            output = 0;
        }

        item = new Item(id, price, names);
        this.tree.put(id, item);

//        assert list != null;
        for (Long name : names) {
            TreeSet<Item> treeSet = this.table.get(name);
            if (treeSet == null) {
                treeSet = new TreeSet<>();
            }
            treeSet.add(item);
            this.table.put(name, treeSet);
        }

        return output;
    }


    // b. Find(id): return price of item with given id (or 0, if not found).

    /**
     * Method to find price of item with given id
     * @param id
     * @return  price of item with given id, 0 if not found
     */
    public Money find(long id) {
        Item item = tree.get(id);
        return item != null ? item.price : ZERO;

    }

    /*
       c. Delete(id): delete item from storage.  Returns the sum of the
       long ints that are in the description of the item deleted,
       or 0, if such an id did not exist.
    */

    /**
     * Method delete item from MDS
     * @param id
     * @return  the sum of the long ints that are in the description of the item deleted, or 0
     */
    public long delete(long id) {
        if (find(id) == ZERO) return 0;
        List<Long> names = new LinkedList<>(tree.get(id).desc);
        Long total = Long.valueOf(0);
        for (Long name : names) {
            Item tmp = this.tree.get(id);
            this.table.get(name).remove(tmp);
            total += name;
        }
        for (Long name : names) {
            if (this.table.get(name).size() == 0) this.table.remove(name);
        }
        this.tree.remove(id);

        return total;
    }

    /*
       d. FindMinPrice(n): given a long int, find items whose description
       contains that number (exact match with one of the long ints in the
       item's description), and return lowest price of those items.
       Return 0 if there is no such item.
    */

    /**
     * find item with minimum price whose description contains input number
     * @param n
     * @return  lowest price of those items. Return 0 if there is no such item.
     */
    public Money findMinPrice(long n) {
        TreeSet<Item> set = this.table.get(n);
        if (set == null) return ZERO;
        return set.first().price;
    }

    /*
       e. FindMaxPrice(n): given a long int, find items whose description
       contains that number, and return highest price of those items.
       Return 0 if there is no such item.
    */

    /**
     * find item with maximum price whose description contains input number
     * @param n
     * @return  highest price of those items. Return 0 if there is no such item.
     */
    public Money findMaxPrice(long n) {
        TreeSet<Item> set = this.table.get(n);
        if (set == null) return ZERO;

        return set.last().price;
    }

    /*
       f. FindPriceRange(n,low,high): given a long int n, find the number
       of items whose description contains n, and in addition,
       their prices fall within the given range, [low, high].
    */
    /**
     * find items within price range whose description contains input number
     * @param n
     * @return  number of items in the range
     */
    public int findPriceRange(long n, Money low, Money high) {
        TreeSet<Item> set = this.table.get(n);

        return set.subSet(
                new Item(Long.MIN_VALUE, low, new LinkedList<>()),
                true,
                new Item(Long.MAX_VALUE, high, new LinkedList<>()),
                true)
                .size();

    }

    /*
       g. PriceHike(l,h,r): increase the price of every product, whose id is
       in the range [l,h] by r%.  Discard any fractional pennies in the new
       prices of items.  Returns the sum of the net increases of the prices.
    */

    /**
     * Method to increase the price of every product, whose id is in the range [l,h] by r%.
     * @param l
     * @param h
     * @param rate
     * @return the sum of the net increases of the prices.
     */
    public Money priceHike(long l, long h, double rate) {
        //we are excluding at right interval
        Money sumIncrease = new Money(0, 0);
        SortedMap<Long, Item> navMap = this.tree.subMap(l, true, h, true);
        for (Item item : navMap.values()) {
            Money old = new Money(item.price.dollars(), item.price.cents());
            item.price.hike(rate);
            sumIncrease = Money.add(sumIncrease, Money.subtract(item.price, old));
        }
        return sumIncrease;
    }

    /*
      h. RemoveNames(id, list): Remove elements of list from the description of id.
      It is possible that some of the items in the list are not in the
      id's description.  Return the sum of the numbers that are actually
      deleted from the description of id.  Return 0 if there is no such id.
    */

    /**
     * Remove elements of list from the description of id.
     * @param id
     * @param list
     * @return the sum of the numbers that are actually deleted from the description of id.  Return 0 if there is no such id.
     */
    public long removeNames(long id, java.util.List<Long> list) {
        long total =  0;
        Item item = this.tree.get(id);
        if(item==null) return 0;

        for(Long name: list){

            this.tree.get(id).desc.remove(name);

            if(this.table.containsKey(name)) {
                this.table.get(name).remove(this.tree.get(id));

                if (this.table.get(name).size() == 0) {
                    this.table.remove(name);
                }
                total +=name;
            }
        }
        return total;
    }

    // Do not modify the Money class in a way that breaks LP3Driver.java
    public static class Money implements Comparable<Money> {
        long d;
        int c;

        public Money() {
            d = 0;
            c = 0;
        }

        public Money(long d, int c) {
            this.d = d;
            this.c = c;
        }

        public Money(String s) {
            String[] part = s.split("\\.");
            int len = part.length;
            if (len < 1) {
                d = 0;
                c = 0;
            } else if (part.length == 1) {
                d = Long.parseLong(s);
                c = 0;
            } else {
                d = Long.parseLong(part[0]);
                c = Integer.parseInt(part[1]);
                if (part[1].length() == 1) {
                    c *= 10;
                }
            }
        }

        public long dollars() {
            return d;
        }

        public int cents() {
            return c;
        }

        /**
         * Method to add money objects
         * @param a
         * @param b
         * @return result of addition
         */
        public static Money add(Money a, Money b) {
            Money res = new Money();
            int dollarCarry = (int) (a.cents() + b.cents()) / 100;
            res.c = (int) (a.cents() + b.cents()) % 100;
            res.d = a.dollars() + b.dollars() + dollarCarry;
            return res;
        }

        /**
         * Method to substract money objects
         * @param a
         * @param b
         * @return result of subtraction
         */
        public static Money subtract(Money a, Money b) {
            if (a.compareTo(b) <= 0) {
                return ZERO;
            }

            Money res = new Money();
            int dollarCarry = (a.cents() - b.cents()) < 0 ? 1 : 0;
            res.c = Math.floorMod((int) (a.cents() - b.cents()), 100);
            res.d = a.dollars() - b.dollars() - dollarCarry;
            return res;
        }

        /**
         * Method to increase price of money, using truncate for cents
         * @param r
         */
        public void hike(double r) {
            double percent = r + 100;
            double centCarry = ((double) this.d * percent) % 100;

            int dollarCarry = (int) (this.c * percent) / 10000;

            this.c = (int) ((int) (this.c * percent) % 10000 + centCarry * 100) / 100;

            this.d = ((long) (this.d * percent) / 100) + dollarCarry;
            if (this.c > 99) {
                this.d += 1;
                this.c %= 100;
            }

        }

        /**
         * Method for comparing two Money objects
         * @param other
         * @return 1 if greater, 0 if equals, -1 if smaller
         */
        public int compareTo(Money other) { // Complete this, if needed
            if (this.d != other.d) {
                return this.d > other.d ? 1 : -1;
            }
            if (this.c == other.c) return 0;
            return this.c > other.c ? 1 : -1;
        }

        public String toString() {
            return d + "." + c;
        }
    }

}
