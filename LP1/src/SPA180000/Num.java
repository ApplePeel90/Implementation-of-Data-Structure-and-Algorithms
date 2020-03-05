/*
Long Project 1: Develop a program that implements arithmetic with large integers, of arbitrary
                size.
Team Information:
1) Qi Wang - qwx170003
2) Anuj Shah - aus180000
3) Samarth Agrawal - spa180000
4) Mayank Bhatia - mxb180024
 */
package SPA180000;

import java.util.*;

public class Num  implements Comparable<Num> {

    static long defaultBase = 1000000000;  // Change as needed
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    public Num(String s) {
        this.arr = constructor_helper(s);
        this.len = this.arr.length;
    }

    public Num(long x) {
        String s = Long.toString(x);
        this.arr = constructor_helper(s);
        len = arr.length;
    }

    public Num(long[] arr, boolean isNegative){
        this.arr = arr;
        this.isNegative = isNegative;
        this.len = this.arr.length;
    }

    private long[] constructor_helper(String s){
        List<Long> arr_num = new ArrayList<>();
        int start;
        if (s.charAt(0) == '-') {
            isNegative = true;
            start = 1;
        }
        else {
            start = 0;
        }
        int digits = (int)Math.log10(base);
        for (int i = s.length() - 1; i >= start; i -= digits) {
            int j = i - digits >= 0 ? i - digits+1 : start;
            String sub = s.substring(j, i+1);
            arr_num.add(Long.parseLong(sub));
        }
        long[] arr_out = new long[arr_num.size()];

        int index = 0;
        for (Long value : arr_num ){
            arr_out[index++] = value;
        }
        return arr_out;
    }

    public static Num add(Num a, Num b) {
        if (a.base != b.base) return null;
        if(!a.isNegative && b.isNegative){
            return subtract(a, new Num(b.arr, false));
        }
        else if(a.isNegative && !b.isNegative){
            return subtract(b, new Num(a.arr, false));
        }
        else{
            long carry = 0;
            ArrayList<Long> res = new ArrayList<>();
            int max = Math.max(a.arr.length, b.arr.length);
            for (int i = 0; i < max; i++) {
                long a_val = i < a.arr.length ? a.arr[i] : 0;
                long b_val = i < b.arr.length ? b.arr[i] : 0;

                long sum = a_val + b_val + carry;
                res.add((sum) % a.base);
                carry = sum>=a.base? 1:0;
            }
            res.add(carry);
            long[] res_out = new long[res.size()];

            int index = 0;
            for (Long value : res ){
                res_out[index++] = value;
            }
            return new Num(res_out,a.isNegative);
        }
    }

    public static Num subtract(Num a, Num b) {
        if (a.base != b.base) return null;
        ArrayList<Long> res = new ArrayList<>();
        boolean neg_flag = false;
        Num x, y;

        if(a.isNegative && !b.isNegative){
            return add(a, new Num(b.arr, true));
        }
        else if(!a.isNegative && b.isNegative){
            return add(a, new Num(b.arr, false));
        }

        else if(!a.isNegative){
            if(a.compareTo(b) >= 0){
                x = a;//new Num(b.arr,false);
                y = b;
            }
            else{
                x = b;//new Num(b.arr,false);
                y = a;
                neg_flag = true;
            }
        }

        else{
            if(a.compareTo(b) >= 0){
                x = b;
                y = a;
                neg_flag = false;
            }
            else{
                x = a;
                y = b;
                neg_flag = true;
            }
        }

        int max = Math.max(x.arr.length, y.arr.length);
        int borrow = 0;
        for(int i = 0; i < max; i++){
            long x_val = i < x.arr.length ? x.arr[i] : 0;
            long y_val = i < y.arr.length ? y.arr[i] : 0;
            x_val+=borrow;

            if(x_val >= y_val){
                res.add(x_val - y_val);
                borrow = 0;
            }
            else{
                res.add(x_val+ x.base - y_val);
                borrow = -1;
            }
        }
        long[] res_out = new long[res.size()];

        int index = 0;
        for (Long value : res ){
            res_out[index++] = value;
        }
        return new Num(res_out, neg_flag);

    }

    public static Num product(Num a, Num b) {
        if (a.base != b.base) return null;
        Num zero = new Num(0);
        if (a.compareTo(zero) == 0 || b.compareTo(zero) == 0) return zero;
        long[] c = new long[a.len + b.len];
        long carry;
        for (int i=0; i<a.len; i++) {
            carry = 0;
            for (int j=0; j<b.len; j++) {
                c[i+j] += (a.arr[i] * b.arr[j]) + carry;
                carry = c[i+j] / a.base;
                c[i+j] = c[i+j] % a.base;
            }
            if (carry != 0) c[i+b.len] = carry;
        }
        Num prod;
        if (c[c.length-1] == 0)
            prod = new Num(Arrays.copyOf(c, c.length -1), a.isNegative ^ b.isNegative);
        else
            prod = new Num(Arrays.copyOf(c, c.length), a.isNegative ^ b.isNegative);

        return prod;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
        assert a!=null;
        if (n < 0) return new Num(0);
        if (n == 0) return new Num(1);
        else if (n%2 == 0) return product(power(a, n/2), power(a, n/2));
        else return product(a, product(power(a, n/2), power(a, n/2) ));
    }

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {
        Num ZERO = new Num(0);

        if (b.compareTo(ZERO) == 0) return null;
        if (a.base != b.base) return null;

        Num x = new Num(a.arr, false);
        Num y = new Num(b.arr, false);
        int INITIALISATION = x.compareTo(y);
        if (a.compareTo(ZERO) == 0 || INITIALISATION < 0) return ZERO;

        boolean SIGN = a.isNegative ^ b.isNegative;
        if (INITIALISATION == 0) return new Num(new long[] {1}, SIGN);

        Num ONE = new Num(1);
        Num start = new Num(1);
        Num end = new Num(a.arr, false);
        Num quotient = new Num(0);

        while (start.compareTo(end) < 1) {
            Num mid = add(start, end).by2();
            int condition = product(mid, y).compareTo(x);
            if (condition == 0) {
                mid.isNegative = SIGN;
                return mid;
            }
            else if (condition < 0) {
                start = add(mid, ONE);
                quotient = new Num(mid.arr, SIGN);
            }
            else {
                end = subtract(mid, ONE);
            }
        }
        return quotient;
    }

    // return a%b
    public static Num mod(Num a, Num b) {
        if (a.base != b.base) return null;
        Num ZERO = new Num(0);
        if (b.compareTo(ZERO) == 0) return null;

        Num rem = subtract(a, product(divide(a, b), b));
        if (a.isNegative == b.isNegative) return rem;
        return add(b, rem);
    }

    // Use binary search
    public static Num squareRoot(Num a) {
        if (a.isNegative) return  null  ;
        Num ZERO = new Num(0);
        Num ONE = new Num(1);
        if (a.compareTo(ZERO) == 0 || a.compareTo(ONE) == 0) return a;

        Num start = new Num(1);
        Num end = new Num(a.arr, a.isNegative);
        Num ans = new Num(0);
        while (start.compareTo(end) < 1) {
            assert end != null;
            Num mid = Objects.requireNonNull(add(start, end)).by2();
            int condition = Objects.requireNonNull(product(mid, mid)).compareTo(a);
            if (condition == 0) return mid;
            else if (condition < 0)
            {
                start = add(mid, ONE);
                ans = new Num(mid.arr, mid.isNegative);
            }
            else end = subtract(mid, ONE);
        }
        return ans;
    }


    // Utility functions
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1 otherwise
    public int compareTo(Num other) {
        if(this.isNegative && !other.isNegative) return -1;
        else if(!this.isNegative && other.isNegative) return 1;
        if(other.base() != this.base()){
            other.convertBase((int) this.base());
        }
        int cmpr = 0;
        int max = Math.max(this.arr.length, other.arr.length);
        for (int i = max - 1; i >= 0; i--) {
            long this_val = i < this.arr.length ? this.arr[i] : 0;
            long other_val = i < other.arr.length ? other.arr[i] : 0;
            if(this_val > other_val) {
                cmpr = 1;
                break;
            }
            else if(this_val < other_val){
                cmpr = -1;
                break;
            }
        }
        return this.isNegative? cmpr*-1 : cmpr;
    }

    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"
    public void printList() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.base);
        sb.append(":");
        int len = this.arr.length - 1;
        while(this.arr[len] == 0) len--;
        for(int i = 0; i <= len; i++){
            sb.append(" "+ this.arr[i]);
        }
        System.out.println(sb);
    }

    // Return number to a string in base 10
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (this.isNegative) {
            s.append("-");
        }
        int DIGITS = ((int) Math.log10(base));
        String stringFormat = "%0" + DIGITS + "d";


        for (int i=len-1; i>=0; i--) {
            s.append(String.format(stringFormat, arr[i]));
        }
        String t = s.toString().replaceAll("^0+","");
        if (t.length() == 0) return "0";
        return t;
    }

    public long base() { return base; }

    public Num convertBase(int newBase) {
        ArrayList<Long> new_arr = new ArrayList<>();
        long r;
        Num ZERO = new Num(0);
        Num NEW_BASE = new Num(newBase);
        Num q = new Num(this.arr, this.isNegative);

        while(q.compareTo(ZERO)!=0){
            r = Long.parseLong(mod(q, NEW_BASE).toString());
            q = divide(q, NEW_BASE);
            new_arr.add(r);
        }

        long[] res_out = new long[new_arr.size()];

        int index = 0;
        for (Long value : new_arr ){
            res_out[index++] = value;
        }
        Num res = new Num(res_out, this.isNegative);
        res.base = newBase;
        return res;
    }

    // Divide by 2, for using in binary search
    public Num by2() {
        Num ZERO = new Num(0);
        Num ONE = new Num(1);

        if (this.compareTo(ZERO) == 0 || this.compareTo(ONE) == 0) return ZERO;
        long[] numBy2Array = new long[this.len];
        long remainder = 0;
        for (int i=this.len-1; i>=0; i--) {
            numBy2Array[i] = this.arr[i] + remainder;
            if (numBy2Array[i] % 2 == 1) remainder = this.base;
            else remainder = 0;
            numBy2Array[i] /= 2;
        }

        Num numBy2;

        if (numBy2Array[numBy2Array.length-1] == 0)
            numBy2 = new Num(Arrays.copyOf(numBy2Array, numBy2Array.length-1), this.isNegative);
        else
            numBy2 = new Num(Arrays.copyOf(numBy2Array, numBy2Array.length), this.isNegative);
        return numBy2;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
        Deque<Num> stack = new ArrayDeque<>();
        Num a,b;
        for(String s: expr) {
            switch (s) {
                case "+" :
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(add(a,b));
                    break;
                case "-":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(subtract(b,a));
                    break;
                case "*" :
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(product(a,b));
                    break;
                case "/":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(divide(b,a));
                    break;
                case "%" :
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(mod(b,a));
                    break;
                default:
                    stack.push(new Num(s));
                    break;
            }
        }

        return stack.pop();
    }

    // Evaluate an expression in infix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluateInfix(String[] expr) {
        ArrayList<String> arrPostFix = new ArrayList<String>();
        Deque<String> stack = new ArrayDeque<>();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("^",  4);
        map.put("*",  3);
        map.put("/",  3);
        map.put("%",  3);
        map.put("+",  2);
        map.put("-",  2);
        map.put("(",  0 );

        for(String s: expr) {
            if(!map.containsKey(s) && !s.equals("(") && !s.equals(")")){
                arrPostFix.add(s);
            }
            else if(map.containsKey(s) && !s.equals("(") &&  !s.equals(")")){
                while(!stack.isEmpty() && map.get(stack.peek())>= map.get(s)){
                    arrPostFix.add(stack.pop());
                }
                stack.push(s);
            }
            else if(s.equals("(")){
                stack.push(s);
            }
            else if(s.equals(")")){
                while (!stack.peek().equals("(")){
                    if(map.containsKey(stack.peek())){
                        arrPostFix.add(stack.pop());
                    }
                }
                stack.pop();
            }
        }
        while(!stack.isEmpty()){
            arrPostFix.add(stack.pop());
        }
        return evaluatePostfix(arrPostFix.toArray(new String[0]));
    }


    public static void main(String[] args) {
        Num x = new Num(999);
        Num y = new Num("8");
        Num z = Num.add(x, y);
        System.out.println(z);
        Num a = Num.power(x, 8);
        System.out.println(a);
        if(z != null) z.printList();
    }
}

