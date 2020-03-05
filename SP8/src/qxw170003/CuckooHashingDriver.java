/*
Name: Hemali Amitkumar Patel
Netid: hap170002

Nmae: Qi Wang
NetId: qxw170003
*/

package qxw170003;

import java.util.HashSet;
import java.util.Random;

public class CuckooHashingDriver {
    public static void main(String[] args){

        // Build an array with random numbers
        Random rd = new Random();
        Integer[] nums = new Integer[100000000];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = rd.nextInt(10000);
        }

        // Test for Java HashSet
//        Timer timer = new Timer();
//        HashSet<Integer> set = new HashSet<>(Math.max((int) (nums.length/.75f) + 1, 16), 0.5f);
//        for(int i = 0; i < nums.length; i++){
//            set.add(nums[i]);
//        }
//        timer.end();
//        System.out.println(timer);


        // Test for Cuckoo Hash
        Timer timer1 = new Timer();
        CuckooHashing cuckoo = new CuckooHashing(0.5f, 16);
        int res = cuckoo.distinctElements(nums);
        timer1.end();
        System.out.println(timer1);
    }
}
