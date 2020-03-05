package test;
//package idsa;
import java.util.Arrays;
import java.lang.Character;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;



/**
 * A class for handling very big integers.
 * @author Carla Vazquez, Courtney Erbes, Nitesh Kumar, Nymisha Jahagirdar
 * @version 1.0.0
 * @since 02-17-2020
 */
public class Num  implements Comparable<Num> {
    static long defaultBase = (long) Math.floor(Math.sqrt(Long.MAX_VALUE));
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    /**
     * A constctor for Num that takes a string.
     * @param s a string of an integer number in base 10.
     */
    public Num(String s) {
        if (s.trim().charAt(0) == '-') {
            isNegative = true;
            s = s.substring(1);
        }

        // convert s to array in base 10
		arr = new long[s.length()];
		for (int i = 0; i < arr.length; i++) {
			arr[arr.length - i - 1] = s.charAt(i) - 48;
        }
		len = arr.length;
        base = 10;
        
        // convert base to default base
		Num x = convertBase(defaultBase);
		this.arr = x.arr;
		this.len = x.len;
        this.base = x.base;
    }

    /**
     * A private constuctor for Num.
     * @param base the base for the num.
     * @param arr a long array containing the Num's array
     *            representation.
     * @param isNeg a boolean value indicating whether or
     *              not the Num is negative.
     */
    private Num(long base, long[] arr, boolean isNeg){
        this.arr = arr;
        this.base = base;
        this.isNegative = isNeg;

        int i = arr.length; 

        //find the len value
        while(i > 0 && arr[i-1] == 0) {
            i--;
        }
        //if the Num == 0, the length needs to be 1
        this.len = (i == 0 ? 1 : i);
    }

    /**
     * A construtor for Num that takes a long
     * @param x the long representation of the Num.
     */
    public Num(long x) {
        if (x == 0) {
            arr = new long[1];
            arr[0] = 0;
            len = 1;
            isNegative = false;
            return;
        } else if (x < 0) {
            isNegative = true;
        }

        // calculate number of digits needed for x in new base
        if (x == Long.MIN_VALUE) {
            len = (int)((Math.floor(Math.log(Long.MAX_VALUE) / Math.log(base))) + 1);
        } else {
            x = Math.abs(x);
            len = (int) ((Math.floor(Math.log(x) / Math.log(base))) + 1);
        }

        // store converted number in arr
        arr = new long[len];
        int i = 0;
        long remainder = 0;
        while (x != 0) {
            remainder = x % base;
            if (remainder < 0) {
                remainder *= -1;
            }
            arr[i++] = remainder;
            x /= base;
        }
    }

    /**
     * Constructor to create Number with base
     *
     * @param x             Number in base 10
     * @param newNumberBase New base for the Number
     */
    public Num(long x, long newNumberBase) {
        if (x < 0) {
            this.isNegative = true;
            x *= -1;
        }

        if (x == 0) {
            this.len = 1;
            this.arr = new long[1];
            this.arr[0] = 0;
            this.base = newNumberBase;
            return;
        }

        int digits = (int) Math.ceil((Math.log10(x) + 1));
        len = (int) Math.ceil(((digits) / (Math.log10(newNumberBase)) + 1));
        this.arr = new long[len];

        int index = 0;
        while (x > 0) {
            this.arr[index++] = x % newNumberBase;
            x /= newNumberBase;
        }
        this.base = newNumberBase;
        this.len = findActualLength(this.arr);
    }

    /**
     * Takes two Nums and returns a Num equal to 
     * their sum.
     * @param a the first Num to be added
     * @param b the second Num to be added
     * @return a Num equal to the sum of a and b
     */
    public static Num add(Num a, Num b) {

        //Create alias for Nums a and b
        Num A = a, B = b;

        //Convert a and b to the same base
        if(A.base > B.base)
            B = B.convertBase(A.base);
        else if (b.base > a.base)
            A = A.convertBase(B.base);

        long base = A.base; //set the resulting num to 
        //the correct base


        //Create array rep for new Num
        //The max size of the result of an addition opertaion
        //is the size of the larger number plus 1 
        long[] temp = new long[Math.max(A.len, B.len) + 1];

        long carry = 0; //intilize carry to 0
        if(A.isNegative == B.isNegative) { //if a and b are the same sign
            int i = 0; 
            while(i < A.len && i < B.len) {
                temp[i] = ((A.arr[i] + B.arr[i] + carry) % base);
                carry = (A.arr[i] + B.arr[i] + carry) / base;
                i++;
            }
            while(i < A.len) {
                temp[i] = ((A.arr[i] +  carry) % base);
                carry = (A.arr[i] + carry) / base;
                i++;
            }
            while(i < B.len) {
                temp[i] =((B.arr[i] +  carry) % base);
                carry = (B.arr[i] + carry) / base;
                i++;
            }
            if(carry != 0)
                temp[i] = carry;
            
            return new Num(base, temp, A.isNegative);
        } else if (A.isNegative) { // -a + b == b - a
            Num C = new Num(A.base, A.arr,false);
            return Num.subtract(B, C);
        } else { // a + -b == a - b;
            Num C = new Num(B.base, B.arr, false);
            return Num.subtract(A, C);
        }
    }

    /**
     * Takes two Nums and returns a Num
     * equal to their difference
     * @param a the Num to be subtracted from
     * @param b the Num being subtracted
     * @return a Num equal to the difference
     * of Nums a and b
     */
    public static Num subtract(Num a, Num b) {
        boolean isNegative = false;
        //Create alias for Nums a and b
        Num A = a, B = b;

        //Convert a and b to the same base
        if(a.base > b.base)
            B = b.convertBase(a.base);
        else if (b.base > a.base)
            A = a.convertBase(b.base);

        long base = A.base; //set the resulting num to the correct base

        long[] temp = new long[Math.max(A.len, B.len)]; //the max length of
        //a difference is the length of the longer number

        if(A.isNegative == B.isNegative){ //if they have the same sign
            
            int compare = A.compareTo(B); //evaluate A in regards to B

            //if A and B are equal the difference is 0
            if( compare ==  0) return new Num(A.base, new long[]{0}, false);
            //if A is larger and positive, or if A is smaller and negative
            else if( (compare == 1 && !A.isNegative) || (compare == -1 && A.isNegative)){
               int i = 0;
                while(i < A.len && i < B.len) {
                    //if the number on top is smaller than the one on bottom
                    if(B.arr[i] > (A.arr[i] + temp[i])) {
                        int j = i + 1;

                        //look for the next largest number on top
                        while( j < A.len && (A.arr[j] + temp[j]) == 0) {
                            j++;
                        }
                        //pass the carry back to the number that needs it
                        while(j > -1 && j != i) {
                            temp[j]--;
                            temp[j - 1]+=base; 
                            j--;
                        }                       
                    }

                    //perform the subtraction
                    temp[i] = (A.arr[i] + temp[i]) - B.arr[i];
                    i++;
                }
                while(i < A.len) { //A is the longer number
                    temp[i] = A.arr[i] + temp[i];
                    i++;
                }
                isNegative = (A.isNegative ? true : false );

            } else { //if B is larger and positive or B is smaller and negative
                int i = 0;
                while(i < A.len && i < B.len) {
                    //if the number on top is smaller than the one on bottom
                    if(A.arr[i] > (B.arr[i] + temp[i])) {
                        int j = i + 1;
                        //look for the next largest number on top
                        while( j < B.len && (B.arr[j] + temp[j]) == 0) {
                            j++;
                        }
                        //pass the carry back to the number that needs it
                        while(j > -1 && j != i) {
                            temp[j]--;
                            temp[j - 1]+= defaultBase; 
                            j--;
                        }                       
                    }

                    //perform the subtraction
                    temp[i] = (B.arr[i] + temp[i]) - A.arr[i];
                    i++;
                }
                while(i < B.len) { //B is the longer number
                    temp[i] = B.arr[i] + temp[i];
                    i++;
                }
                isNegative = (A.isNegative ? false : true);
            }
            return new Num(base, temp, isNegative);
        } else if (A.isNegative) { //-a - b == -a + -b
           Num C = new Num(B.base,B.arr,true);
           return Num.add(A, C); 
        } else { // a - -b == a + b
            Num C = new Num(B.base,B.arr,false); 
            return Num.add(A, C);         
        }	    
    }


    /**
     * Takes two Nums and returns a Num
     * equal to their product
     * @param a the first Num
     * @param b the second Num
     * @return a Num that is equal to the 
     * product of a and b
     */
    public static Num product(Num x, Num y) {
        Num a = x, b = y;

        if(a.base > b.base)
            b = y.convertBase(a.base);
        else if (b.base > a.base)
            a = x.convertBase(b.base);


        Num zero = new Num(0, a.base);

        if(a.compareTo(zero)==0 || b.compareTo(zero)==0)
            return zero;

        boolean isNeg = a.isNegative ^ b.isNegative;

        long[] prod = new long[a.len + b.len];

        long carry, carry2, temp2;

        for(int i = 0; i < a.len; i++) {
            carry = 0;
            carry2 = 0;
            for (int j = 0; j < b.len; j++) {
                long temp = ((a.arr[i] * b.arr[j]) + carry) % a.base;
                carry = ((a.arr[i] * b.arr[j]) + carry) / a.base;

                temp2 = (prod[i+j] + temp + carry2) % a.base;
                carry2 = (prod[i+j] + temp + carry2) / a.base;
                prod[i+j] = temp2;

            }
            prod[i+b.len] = carry + carry2;
        }

        if(prod[prod.length-1]==0)
            return new Num(a.base, Arrays.copyOfRange(prod,0,prod.length-1), isNeg);
        else
            return new Num(a.base, prod, isNeg);

    }

    private static Num makeEqualLength(Num a, int len){
        long[] newArr = new long[len];

        if (a.len >= 0)
            System.arraycopy(a.arr, 0, newArr, 0, a.len);

        return new Num(a.base, newArr, a.isNegative);
    }

    public static Num shift(Num a, int n){
        long[] arr = new long[a.len + n];

        if (arr.length - n >= 0)
            System.arraycopy(a.arr, 0, arr, n, arr.length - n);

        return new Num( a.base, arr, a.isNegative);
    }

    public  static Num oldProduct(Num a, Num b){

        if(a.len == 1 || b.len == 1){
            return product(a,b);
        }

        Num x = a;
        Num y = b;

        if(x.base > y.base)
            y = b.convertBase(x.base);
        else if (y.base > x.base)
            x = a.convertBase(y.base);

        if (a.len < b.len) {
            x = makeEqualLength(a, b.len);
        } else if (a.len > b.len) {
            y = makeEqualLength(b, a.len);
        }

        int totalLength = Math.max(a.len, b.len);
        int halfLength1 = totalLength/2;
        int halfLength2 = totalLength - halfLength1;

        long[] arr_low1 = new long[halfLength1];
        long[] arr_low2 = new long[halfLength1];
        long[] arr_high1 = new long[halfLength2];
        long[] arr_high2 = new long[halfLength2];
        
        for (int i = 0; i < halfLength1; i++) {
            arr_low1[i] = x.arr[i];
            arr_low2[i] = y.arr[i];
        }
        
        for (int i = halfLength2; i < totalLength; i++) {
            if (i < x.len) {
                arr_high1[i] = x.arr[i];
            }
            if (i < y.len) {
                arr_high2[i] = y.arr[i];
            }
        }

        Num low1 = new Num(x.base, arr_low1, x.isNegative);
        Num high1 = new Num(x.base, arr_high1, x.isNegative);
        Num low2 = new Num(y.base, arr_low2, y.isNegative);
        Num high2 = new Num(y.base, arr_high2, y.isNegative);

        Num p1 = product(high1, high2);
        Num p3 = product(low1, low2);
        Num p2 = subtract(subtract(product(add(low1,high1), add(low2, high2)), p1), p3);

        Num a1 = shift(p1, halfLength1*2);
        Num a2 = shift(p2, halfLength1);

        Num result = add(a1,add(a2, p3));
        result.isNegative = a.isNegative ^ b.isNegative;

        return result;

    }

    /**
     * Takes a Num and a long and returns
     * a Num to the power of the long
     * @param a a Num
     * @param n a long
     * @return a Num to the power of the long
     */
    public static Num power(Num a, long n) {
        if( n == 0)
            return new Num(1, a.base);
        Num pow = power(a, n/2);
        if(n % 2 == 0)
            return product(pow, pow);
        else
            return product(a, product(pow, pow));
    }

    /**
     * Takes two Nums and returns a Num
     * equal to the dividen
     * @param a the Num being divided
     * @param b the Num a is being divided by
     * @return a Num equal to the dividen
     * of a divided by b
     */
    public static Num divide(Num a, Num b) {
        if(a.base > b.base)
            b = b.convertBase(a.base);
        else if (b.base > a.base)
            a = a.convertBase(b.base);

        long currentBase = a.base();

        Num zeroNum = new Num(0, currentBase);
        Num negativeOneNum = new Num(-1, currentBase);
        Num oneNum = new Num(1, currentBase);

        // If the 'b' Num is 0, return null
        if (b.compareTo(zeroNum) == 0) {
            return null;
        }

        int currentState = getCurrentSign(a, b);
        Num startNum, midNum, endNum;
        startNum = new Num(0, currentBase);
        endNum = a;

        // Threshold will help in deciding when to break the loop
        Num threshold;
        Num product;

        while (startNum.compareTo(endNum) < 0) {
            // Get the middle of start and end Num and store it in midNum
            midNum = add(startNum, endNum).by2();
            // Get the product of midNum and 'b' Num
            product = product(midNum, b);
            threshold = subtract(a, product);

            // If product is 0 then break the loops
            if (product.compareTo(a) == 0) {
                startNum = midNum;
                break;
            }

            // If product is greater than 'a' Num, then update endNum and go with lower half
            // else if threshold is below b, return midNum
            // otherwise update startNum and go with upper half
            if (product.compareTo(a) == 1) {
                endNum = add(midNum, negativeOneNum);
            } else if (threshold.compareTo(b) == -1) {
                startNum = midNum;
                break;
            } else {
                startNum = add(midNum, oneNum);
            }
        }

        // Change the numbers back to old state
        restoreOldSign(a, b, currentState);

        if (((!a.isNegative && b.isNegative) || (a.isNegative && !b.isNegative)) && startNum.compareTo(zeroNum) != 0) {
            startNum.isNegative = true;
        }

        return startNum;
    }

    /**
     * Takes two Nums and returns the result
     * of a modulus operation
     * @param a the Num being modded
     * @param b the Num a is being moded by
     * @return the Num result of a mod b, returns null
     * if trying to mod by zero
     */
    public static Num mod(Num a, Num b) {
        if(b.len == 1 && b.arr[0] == 0) return null;
        Num c = Num.divide(a, b); //get the quotient
        c = Num.product(c, b); //multiply the quotient by the divisor
        c = Num.subtract(a, c); //subtract from the dividen the product of the
            //quotient and the divisor to get the remainder
        return c;
    }

    /**
     * Given a Num, returns the squareroot of said Num
     * @param a a Num
     * @return the squareroot of the given num
     */
    public static Num squareRoot(Num a) {
        Num zeroNum = new Num(0, a.base);

        // If the Number is 0, return null
        if (a.compareTo(zeroNum) == -1) {
            return null;
        }

        Num startNum, endNum, midNum, midSquareNum;

        startNum = zeroNum;
        endNum = a;
        while (true) {
            // Get the Middle Number
            midNum = add(startNum, endNum).by2();
            // Get the Square of middle number
            midSquareNum = product(midNum, midNum);
            // If Square of middle number is equal to 'a' number, return middle number
            if (midSquareNum.compareTo(a) == 0) {
                return midNum;
            }

            // If middle number is same as start number or end number, return middle number
            if (midNum.compareTo(startNum) == 0 || midNum.compareTo(endNum) == 0) {
                return midNum;
            }

            // If square of middle number is greater than 'a' number
            // Update endNum and focus of left half
            // else update startNum and focus right half
            if (midSquareNum.compareTo(a) == 1) {
                endNum = midNum;
            } else {
                startNum = midNum;
            }
        }
    }


    /**
     * The compareTo function for Num.
     * @param other the Num, this Num is compared to
     * @return 1 if this Num is greater than the other Num,
     *         0 if this Num equals the other Num,
     *         -1 if this Num is smaller than the other Num.
     */
    public int compareTo(Num other) {
        int toReturn = 0;
        if(this.isNegative == other.isNegative) {
            if(this.len > other.len) {
                toReturn = (isNegative? -1: 1);
            } else if (this.len == other.len) {
                int i = this.len -1;
                while(i > -1 && this.arr[i] == other.arr[i]){i--;}
                toReturn = ( i == -1? 0: (this.arr[i] > other.arr[i] ? (isNegative? -1: 1) : (isNegative? 1: -1) ) );
            } else {
                toReturn = (isNegative? 1: -1);
            }
        } else {
            toReturn = (isNegative? -1: 1);
        }
        return toReturn;
    }
    
    /** 
     * Output using the format "base: elements of list ..."
     */
    public void printList() {
        System.out.print(base + ": ");
        for (int i = 0; i < len; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println((isNegative?"-":""));
    }
    

    /**
     * Returns the string representation of this Num
     * @return arr as a string in base 10
     */
    public String toString() {
        long[] arr2 = this.convertBase(10).arr;
        String s = "";

        // get rid of extra zeros to add to s
        int maxLength = findActualLength(arr2);
        
        for (int i = 0; i < maxLength; i++) {
            s = arr2[i] + s;
        }
        if (isNegative) {
            s = "-" + s;
        }
        
        return s;
    }

    /**
     * gets the len value of the given long array
     * @param input a long array
     * @return the index of the rightmost element in the array
     *  that is not zero + 1
     */
    private static int findActualLength(long[] input) {
        int maxLength = input.length;
        for (int i = input.length-1; i > 0; i--) {
            if (input[i] != 0) {
                break;
            }
            maxLength--;
        }
        return maxLength;
    }

    /**
     * Retrieves the base of this Num
     * @return the base of this Num
     */
    public long base() { return base; }

    // Return number equal to "this" number, in base=newBase
    public Num convertBase(long newBase) {
		Num sum = new Num(0, newBase);
		Num baseConverted = new Num(base, newBase);
		for (int i = len - 1; i >= 0; i--) {
			Num prod = product(sum, baseConverted);
			Num arrConverted = new Num(this.arr[i], newBase);
			sum = add(prod, arrConverted);
		}
		sum.isNegative = this.isNegative;
		return sum;
	}

	// Return number equal to "this" number, in base=newBase
	public Num convertBase(int newBase) {
		return convertBase((long)newBase);
	}

    /**
     * Divides the given Num by 2 and returns the
     * dividen.
     * @return the dividen of the Num divided by 2.
     */
    public Num by2() {
		long[] newArr = new long[this.len];
        long c = 0;
        int inx = this.len - 1;
		while(inx >= 0) {
			newArr[inx] = (c * this.base() + arr[inx]) / 2;
			c = (c * this.base() + arr[inx]) % 2;
            inx--;
		}
		return new Num(this.base, newArr, false);
    }

    /**
     * Evaluate an expression in postfix and return resulting number
     * Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
     * a number: [1-9][0-9]*.  There is no unary minus operator.
     * @param expr a string array representing an expression in postfix notation
     * @return the Num result of evaluating the given expression
     */
    public static Num evaluatePostfix(String[] expr) {
        Stack<Num> operands = new Stack<Num>();
        Num op2, op1;
        for(int i = 0; i < expr.length; i++) {
            expr[i] = expr[i].trim();
            switch(expr[i]) {
                case "*":
                    op2 = operands.pop();
                    op1 = operands.pop();
                    operands.push(Num.product(op1, op2));
                    break;
                case "+":
                    op2 = operands.pop();
                    op1 = operands.pop();
                    operands.push(Num.add(op1, op2));
                    break;
                case "-":
                    op2 = operands.pop();
                    op1 = operands.pop();
                    operands.push(Num.subtract(op1, op2));
                    break;
                case "/":
                    op2 = operands.pop();
                    op1 = operands.pop();
                    operands.push(Num.divide(op1, op2));
                    break;
                case "%":
                    op2 = operands.pop();
                    op1 = operands.pop();
                    operands.push(Num.mod(op1, op2));
                    break;
                case "^":
                    op2 = operands.pop();
                    op1 = operands.pop();
                    String temp = op2.toString();
                    try {
                        long temp2 = Long.parseLong(temp);
                        operands.push(Num.power(op1, temp2));
                    } catch (Exception e){
                        e.printStackTrace();
                        return new Num("-1");
                    }
                    break;
                default:
                    operands.push(new Num(expr[i]));
                    break;
            }
        }
	    return (operands.isEmpty() ? null : operands.pop());
    }

    /** 
     * Parse/evaluate an expression in infix and return resulting number
     * Input expression is a string, e.g., "(3 + 4) * 5"
     * @param expr a string representing an expression in infix notation
     * @return the Num result of evaluating the given expression
     * @throws IllegalArgumentException when invalid expr given as argument
     */
    public static Num evaluateExp(String expr) {
        Queue<String> q = tokenizeString(expr);
        Num res = evalE(q);

        if (!q.isEmpty()) {
	       throw new IllegalArgumentException("Expression can't be generated by this grammar");
        }
        return res;
    }
    
    public static Queue<String> tokenizeString(String expr) {
        StringBuilder sb = new StringBuilder(expr.trim());
        
        for (int i = 0; i < sb.length()-1; i++) {
            char cur = sb.charAt(i);
            char nxt = sb.charAt(i+1);
            
            if (Character.isDigit(cur) && Character.isDigit(nxt)) { continue; }
            
            while (Character.isWhitespace(nxt)) {
                sb.deleteCharAt(i+1);
                if (i >= sb.length()) { break; }
                nxt = sb.charAt(i+1);
            }
            sb.insert(++i, ' ');
        }
        
        return new LinkedList<>(Arrays.asList(sb.toString().trim().split(" ")));
    }
    
    static Num evalE(Queue<String> qt) {
        Num val1 = evalT(qt);
        while(
            !qt.isEmpty() && (qt.peek().equals("+") ||
             qt.peek().equals("-"))) {
            String oper = qt.remove();
            Num val2 = evalT(qt);
            if (oper.equals("+")) {
                val1 = add(val1, val2);
            } else {
                val1 = subtract(val1, val2);
            }
        }
        return val1;
    }

    static Num evalT(Queue<String> qt) {
        Num val1 = evalF(qt);
        while(!qt.isEmpty() && (qt.peek().equals("*") ||
             qt.peek().equals("/"))) {
            String oper = qt.remove();
            Num val2 = evalF(qt);
            if (oper.equals("*")) {
                val1 = product(val1, val2);
            } else {
                val1 = divide(val1, val2);
            }
        }
        return val1;
    }

    static Num evalF(Queue<String> qt) {
        Num val;
        if (qt.peek().equals("(")) {
            String oper = qt.remove();
            val = evalE(qt);
            oper = qt.remove(); // ")"
        } else {
            String num = qt.remove();
            val = new Num(num);
        }
        return val;
    }

    // Utility methods start

    /**
     * Get the current sign of the Num
     * + + = 0
     * + - = 1
     * - + = 2
     * - - = 3
     *
     * @param a {@link Num} a
     * @param b {@link Num} a
     * @return Current sign
     */
    public static int getCurrentSign(Num a, Num b) {
        int sign;
        if (!a.isNegative && !b.isNegative) {
            sign = 0;
        } else if (!a.isNegative && b.isNegative) {
            sign = 1;
            b.isNegative = false;
        } else if (a.isNegative && !b.isNegative) {
            sign = 2;
            a.isNegative = false;
        } else {
            sign = 3;
            a.isNegative = false;
            b.isNegative = false;
        }
        return sign;
    }

    /**
     * To restore back the old sign
     *
     * @param a    {@link Num} a
     * @param b    {@link Num} a
     * @param sign Old sign
     */
    public static void restoreOldSign(Num a, Num b, int sign) {
        switch (sign) {
            case 0:
                break;
            case 1:
                b.isNegative = true;
                break;
            case 2:
                a.isNegative = true;
                break;
            case 3:
                a.isNegative = true;
                b.isNegative = true;
                break;
            default:
                break;
        }
    }

    // Utility methods end

    public static void main(String[] args) {
        /*
        Num x = new Num(999);
        Num y = new Num("8");
        Num z = Num.add(x, y);
        System.out.println(z);
        Num a = Num.power(x, 8);
        System.out.println(a);
        if(z != null) z.printList();
        */

        testAddSub();
        testCompare();
        testProduct();
        testPower();
        testDivide();
        testSquareRoot();
        testMod();
        testEvaluatePostfix();
        testEvaluateExp();
    }
    
    // Methods to test class start
    
    /**
     * A function that test the functionality of add, sub,
     * and compareTo
     */
    public static void testAddSub() {
        System.out.println("Add/sub  tests");

        Timer t  = new Timer();
        Num z = new Num(Num.defaultBase, new long[]{ 3985095L, 9302348L, 9293993L, 9999L, 0L, 7777L}, false);
        Num y = new Num(Num.defaultBase, new long[]{3000909088L, 0L, 0, 1666L, 0L, 17L }, false);

        System.out.print("z: ");
        z.printList();
        System.out.print("y: ");
        y.printList();
        System.out.print("z + y:");
        t.start();
        Num.add(z,y).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("y + z ");
        t.start();
        Num.add(y,z).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("-z + y:");
        z.isNegative = true;
        t.start();
        Num.add(z,y).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("y + -z:");
        t.start();
        Num.add(y,z).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("-z + -y:");
        y.isNegative = true;
        t.start();
        Num.add(z,y).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("-y + -z:");
        t.start();
        Num.add(y,z).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        z.isNegative = false;
        System.out.print("z + -y:");
        t.start();
        Num.add(z,y).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("-y + z:");
        t.start();
        Num.add(y,z).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        y.isNegative = false;

        System.out.println();

        System.out.print("z - y:");
        t.start();
        Num.subtract(z,y).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("y - z ");
        t.start();
        Num.subtract(y,z).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("-z - y:");
        z.isNegative = true;
        t.start();
        Num.subtract(z,y).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("y - -z:");
        t.start();
        Num.subtract(y,z).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("-z - -y:");
        y.isNegative = true;
        t.start();
        Num.subtract(z,y).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("-y - -z:");
        t.start();
        Num.subtract(y,z).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        z.isNegative = false;
        System.out.print("z - -y:");
        t.start();
        Num.subtract(z,y).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        System.out.print("-y - z:");
        t.start();
        Num.subtract(y,z).printList();
        t.end();
        System.out.println("time: "+ t.elapsedTime + "ms");
        y.isNegative = false;

        System.out.println();
    }
    
    public static void testCompare() {
        System.out.println("Compare  tests");
        
        Num z = new Num("5000");
        System.out.print("z: ");
        z.printList();
        Num y = new Num(5000);

        System.out.println("z.compareTo(y): " + z.compareTo(y));
        System.out.println("y.compareTo(z): " + y.compareTo(z));
        z.isNegative = true;
        System.out.println("-z.compareTo(y): " + z.compareTo(y));
        System.out.println("y.compareTo(-z): " + y.compareTo(z));
        y.isNegative = true;
        System.out.println("-z.compareTo(-y): " + z.compareTo(y));
        System.out.println("-y.compareTo(-z): " + y.compareTo(z));
        z.isNegative = false;
        System.out.println("z.compareTo(-y): " + z.compareTo(y));
        System.out.println("-y.compareTo(z): " + y.compareTo(z));

        System.out.println();
    }
    
    public static void testProduct() {
        System.out.println("Product  tests");

        Num z = new Num(123);
        System.out.print("z: ");
        z.printList();
        Num y = new Num(45);
        System.out.print("y: ");
        y.printList();
        Num.product(y,z).printList();
        z = new Num(123);
        System.out.print("z: ");
        z.printList();
        y = new Num(0);
        System.out.print("y: ");
        y.printList();
        Num.product(y,z).printList();

        z = new Num(0);
        System.out.print("z: ");
        z.printList();
        y = new Num(45);
        System.out.print("y: ");
        y.printList();
        Num.product(y,z).printList();

        z = new Num(12345);
        System.out.print("z: ");
        z.printList();
        y = new Num(1);
        System.out.print("y: ");
        y.printList();
        Num.product(y,z).printList();

        z = new Num(-123);
        System.out.print("z: ");
        z.printList();
        y = new Num(45);
        System.out.print("y: ");
        y.printList();
        Num.product(y,z).printList();

        z = new Num(-123);
        System.out.print("z: ");
        z.printList();
        y = new Num(-45);
        System.out.print("y: ");
        y.printList();
        Num.product(y,z).printList();

        z = new Num(-123);
        System.out.print("z: ");
        z.printList();
        y = new Num(-45);
        System.out.print("y: ");
        y.printList();
        Num.product(y,z).printList();

        z = new Num(12345);
        System.out.print("z: ");
        z.printList();
        y = new Num(67890);
        System.out.print("y: ");
        y.printList();
        Num.product(y,z).printList();

        z = new Num(12345);
        System.out.print("z: ");
        z.printList();
        y = new Num(67890);
        System.out.print("y: ");
        y.printList();
        Num.product(y,z).printList();

        z = new Num("12345678901234567890");
        System.out.print("z: ");
        z.printList();
        y = new Num("98765432109876543210");
        System.out.print("y: ");
        y.printList();
        Num.product(y,z).printList();

        System.out.println();
    }
    
    public static void testPower() {
        System.out.println("Power tests");

        Num z = new Num(12);
        System.out.print("z: ");
        z.printList();

        Num.power(z,10).printList();
        System.out.println("Answer should be: " + Math.pow(12, 10));

        Num.power(z,0).printList();
        System.out.println("Answer should be: " + Math.pow(12, 0));

        Num.power(z,1).printList();
        System.out.println("Answer should be: " + Math.pow(12, 1));

        Num.power(z,5).printList();
        System.out.println("Answer should be: " + Math.pow(12, 5));
        
        z = new Num(-3);
        System.out.print("z: ");
        z.printList();
        
        Num.power(z,0).printList();
        System.out.println("Answer should be: " + Math.pow(-3, 0));

        Num.power(z,1).printList();
        System.out.println("Answer should be: " + Math.pow(-3, 1));
        
        Num.power(z,4).printList();
        System.out.println("Answer should be: " + Math.pow(-3, 4));

        Num.power(z,5).printList();
        System.out.println("Answer should be: " + Math.pow(-3, 5));

        System.out.println();
    }

    public static void testDivide() {
        System.out.println("Divide  tests");
        
        Num z = new Num("5000");
        System.out.print("z: ");
        z.printList();
        Num y = new Num(100);
        y = y.convertBase(100);
        System.out.print("y: ");
        y.printList();
        System.out.print("z / y: ");
        Num.divide(z, y).printList();
        System.out.println("Answer should be: " + (5000/100));
        
        z = new Num("5000");
        System.out.print("z: ");
        z.printList();
        y = new Num(105);
        y = y.convertBase(16);
        System.out.print("y: ");
        y.printList();
        System.out.print("z / y: ");
        Num.divide(z, y).printList();
        System.out.println("Answer should be: " + (5000/105));
        
        z = new Num("5000");
        System.out.print("z: ");
        z.printList();
        y = new Num(-105);
        y = y.convertBase(16);
        System.out.print("y: ");
        y.printList();
        System.out.print("z / y: ");
        Num.divide(z, y).printList();
        System.out.println("Answer should be: " + (5000/-105));

        z = new Num("-5000");
        z.convertBase(100);
        System.out.print("z: ");
        z.printList();
        y = new Num(5000);
        System.out.print("y: ");
        y.printList();
        System.out.print("z / y: ");
        Num.divide(z, y).printList();
        System.out.println("Answer should be: " + (-5000/5000));

        z = new Num("5000");
        z.convertBase(16);
        System.out.print("z: ");
        z.printList();
        y = new Num(-5005);
        System.out.print("y: ");
        y.printList();
        System.out.print("z / y: ");
        Num.divide(z, y).printList();
        System.out.println("Answer should be: " + (5000/-5005));

        System.out.println();
    }
    
    public static void testSquareRoot() {
        System.out.println("Square root tests");
        
        Num z = new Num(100).convertBase(100);
        System.out.print("z: ");
        z.printList();
        System.out.print("sqrt(z): ");
        Num.squareRoot(z).printList();
        
        z = new Num(105).convertBase(100);
        System.out.print("z: ");
        z.printList();
        System.out.print("sqrt(z): ");
        Num.squareRoot(z).printList();

        System.out.println();
    }
    
    public static void testMod() {
        System.out.println("Mod tests");

        Num z = new Num("5000");
        System.out.print("z: ");
        z.printList();
        Num y = new Num(100);
        y = y.convertBase(100);
        System.out.print("y: ");
        y.printList();
        System.out.print("z % y: ");
        Num.mod(z, y).printList();
        
        z = new Num("5000");
        System.out.print("z: ");
        z.printList();

        y = new Num(105);
        y = y.convertBase(16);
        System.out.print("y: ");
        y.printList();
        System.out.print("z % y: ");
        Num.mod(z, y).printList();

        z = new Num("5000");
        z.convertBase(100);
        System.out.print("z: ");
        z.printList();
        y = new Num(5000);
        System.out.print("y: ");
        y.printList();
        System.out.print("z % y: ");
        Num.mod(z, y).printList();

        z = new Num("5000");
        z.convertBase(16);
        System.out.print("z: ");
        z.printList();
        y = new Num(5005);
        System.out.print("y: ");
        y.printList();
        System.out.print("z % y: ");
        Num.mod(z, y).printList();

        System.out.println();
    }
    
    public static void testEvaluateExp() {
        System.out.println("Tokenizing tests");
        System.out.println(tokenizeString("  34 + 5 +        78"));
        System.out.println(tokenizeString("(3+4) * 5"));
        System.out.println(tokenizeString("( 3 + 4 ) *5"));

        System.out.println("\nEvalExp tests");
        
        String test = "(34 + 5) -        40";
        System.out.println(test + " = " + evaluateExp(test).toString());

        test = "( (35 + 5)/10 + 5)*2*4";
        System.out.println(test + " = " + evaluateExp(test).toString());

        test = "( (35 + 5)/10 + 5)*2*4 7";
        System.out.println(test + " = " + evaluateExp(test));
    }

    public static void testEvaluatePostfix() {
        System.out.println("Evaluate Postfix tests");
        String expr = "15 7 ^ 5 3 * / 10 +";
        Num result =  Num.evaluatePostfix(expr.split(" "));
        System.out.println("expr: " + expr + ", result should be: "+ 11390635);
        System.out.println("result is: " + result);

        expr = "90 7 * 6 * 4 % 3 / 2 7 5 * + +";
        result = Num.evaluatePostfix(expr.split(" "));
        System.out.println("expr: " + expr + ", result should be: "+ 37);
        System.out.println("result is: " + result);

        expr = "989809 2394850934502 636728378 ^ * 99098375 + 7874618374 03984 + 884839 - 4 ^ +";
        result = Num.evaluatePostfix(expr.split(" "));
        System.out.println("expr: " + expr + ", result should be: "+ (989809L * 2394850934502L ^ 636728378L + 99098375L + (7874618374L + 3984L - 884839L) ^ 4L));
        System.out.println("result is: " + result);

        expr = "90 7 * 6 4 ^ * 3 % 2 7 5 * + +";
        result = Num.evaluatePostfix(expr.split(" "));
        System.out.println("expr: " + expr + ", result should be: "+ 37);
        System.out.println("result is: " + result);

    }

    // Methods to test class end
}
