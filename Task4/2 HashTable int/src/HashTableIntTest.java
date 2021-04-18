import java.util.HashMap;

public class HashTableIntTest {
    public static void main(String[] args) {
        //2 to the power of 24 is 16777216 which is the closes to 10000000.
        //The task said no more than 35 % bigger, but this is the closest exponent of 2.
        int exponent = 24;
        HashTableInt table = new HashTableInt(exponent);
        int length = 10000000;
        int lengthHashTable = table.getLengthTable();

        int[] randArr = new int[length];
        int randNr;
        for(int i=0; i < length; i++) {
            //In order to spread the numbers over an interval greater than the table size.
            randArr[i] = (int)(Math.random() * length * 10);
        }

        long start, end, duration=0;
        for(int i=0; i < length; i++) {
            start = System.nanoTime();
            table.add(randArr[i]);
            end = System.nanoTime();
            duration += end - start;
        }
        System.out.println(table.printInfo());
        System.out.println("\nTime of my hash table: " + duration/1000000 + " ms");

        duration = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        for(int i=0; i < length; i++) {
            start = System.nanoTime();
            map.put(i, randArr[i]);
            end = System.nanoTime();
            duration += end - start;
        }
        System.out.println("Time of java hash map: " + duration/1000000 + " ms");

        int k = 777717;
        table.add(k);
        String print = table.get(k) == k ?  "Found the correct element " + k :  "Did not find the correct element";
        System.out.println("\n"+print);
    }
}

class HashTableInt {
    private int[] hashTable;
    private int buckets;
    private int collisions;
    private int m;

    public HashTableInt(int exponent) {
        this.m = (int)Math.pow(2,exponent);
        this.hashTable = new int[m];
        this.collisions = 0;
        this.buckets = 0;
    }

    public int getLengthTable() {
        return hashTable.length;
    }

    public int getCollisions() {
        return collisions;
    }

    public int[] getHashTable() {
        return hashTable;
    }

    public int multiHash(int k) {
        double A = (Math.sqrt(5)-1)/2;
        return (int)(m*(k*A - Math.floor(k*A)));
    }

    public int hash2(int k) {
        return (2 * Math.abs(k) + 1) % m;
    }


    public int divHash(int k) {
        return k % m;
    }

    public int hash2_2(int k) {
        return k % (m-1) + 1;
    }

    /**
     * Function that calculates a position in the hashtable.
     * Decided to not multiply h2 by i since, i can be very large when the table is close to full. 
     * Can cause arithmetic overflow.
     */

    public int probe(int h1, int h2) {
        return (h1 + h2) % m;
    }

    public int gcd(int h2, int m) {
        if(m==0) return h2;
        return gcd(m,h2%m);
    }

    /**
     * h2(k) must not be 0
     * h2(k) and the size m must be relative prime, no common factors for any k.
     * If they have a common factor, the probe sequence will not go through all the positions and it will
     * be impossible to place an element. They are relative prime if the greatest common divisor is 1.
     */

    public int add(int k) {
        if(buckets == m) {
            return -1;
        }
        int pos = multiHash(k);
        if(hashTable[pos] == 0) {
            hashTable[pos] = k;
            buckets++;
            return pos;
        } else {
            int jump = hash2(k);
            while(true) {
                //System.out.println("Collision: " + k + " -> " + hashTable[pos]);
                collisions++;
                if(jump != 0 || jump != m) { //&& gcd(jump,m) == 1) {
                    pos = probe(pos, jump);
                }
                if(hashTable[pos] == 0) {
                    hashTable[pos] = k;
                    buckets++;
                    return pos;
                }
            }
        }
    }

    public int get(int k) {
        int pos = multiHash(k);
        if(hashTable[pos] == k) {
            return hashTable[pos];
        } else {
            int jump = hash2(k);
            while(true) {
                collisions++;
                if(jump != 0 || jump != m) { //&& gcd(jump,m) == 1) {
                    pos = probe(pos, jump);
                }
                if(hashTable[pos] == k) {
                    return hashTable[pos];
                }
            }
        }
    }

    public String printInfo() {
        return "\nHashTable information: \n" + 
                "\tCollisions: " + collisions + "\n" +
                "\tBuckets: " + buckets + "\n" +
                "\tLoad factor: " + (double)buckets/m;
    }

    public String toString() {
        String print = "";
        for(int i=0; i < hashTable.length; i++) {
            print += hashTable[i] + " ";
        }
        return print;
    }
}