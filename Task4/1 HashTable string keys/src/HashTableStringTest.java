import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HashTableStringTest {
    public static void main(String[] args) throws IOException {
        HashTableString table = new HashTableString(103);
        System.out.println("--Adding all names to the hash table and printing the collisions--");
        table.addAllNames();

        System.out.println("\n--Printing the table--");
        System.out.println(table);
        System.out.println(table.printInfo());

        //table.searchForAllNames();
        String name = "Tverfjell,Julie Isabelle Malmedal";
        System.out.println("Search for: " + name);
        System.out.println(table.search(name).toString() + "\n");

        System.out.println(table.computeHash("Caro"));
        System.out.println(table.computeHash("Cora"));
    }
}

class HashTableString {

    private HashNode[] hashTable;
    private int elements;
    private int collisions;
    private int buckets;
    //The length of the hash table
    private int m;

    public HashTableString(int m) {
        this.m = m;
        hashTable = new HashNode[m];
        elements = 0;
        collisions = 0;
        buckets = 0;
    }

    public HashNode[] getHashTable() {
        return hashTable;
    }

    public int getElements() {
        return elements;
    }

    public int getBuckets() {
        return buckets;
    }


    /**
    Method that takes a string as parameter and converts it to an array
    of bytes with ASCII or unicode values for each letter.
    Gets a hash from a string converted to a byte array. Problem is that
    different strings containing the same letters often get the hash. The
    solution to this is to weight the letters differently and therefore multiply
    by 7 to the power if i, and i is the position in the string.
    */
    

    public int computeHash(String value) {
        // translating text String to 7 bit ASCII encoding.
        byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);
        int hash = 7;
        for (int i = 0; i < bytes.length; i++) {
            hash = hash * i + bytes[i];
        }
        return hash;
    }


    /*
    private long computeHash(String value) {
        int p = 31;
        int m = (int)1e9 + 9;
        long hashValue = 0;
        long powerOfP = 1;
        for(int i=0; i < value.length(); i++) {
            hashValue = (hashValue + (value.charAt(i) - 'a' + 1) * powerOfP) % m;
            powerOfP = (powerOfP * p) % m;
        }
        return hashValue;
    }

     */

    public ArrayList<String> getNames() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("navn.txt"));
        ArrayList<String> names = new ArrayList<>();
        String currentLine = "";
        while ((currentLine = reader.readLine()) != null) {
            names.add(currentLine);
        }
        reader.close();
        return names;
    }

    public int divHash(int k) {
        return k % m;
    }


    /**
     * Hash function based integer multiplication.
     * Research show that A = (sqrt(5)-1)/2 works best.
     * All table sizes works good.
     */

    public int multiHash(int k) {
        double A = (Math.sqrt(5)-1)/2;
        return (int)(m*(k*A - Math.floor(k*A)));
    }

    public void add(String value) {
        int k = computeHash(value);
        int h = multiHash(k);
        HashNode newNode = new HashNode(value);
        if(hashTable[h] == null) {
            hashTable[h] = newNode;
            buckets++;
            elements++;
        } else {
            HashNode current = null;
            HashNode next = hashTable[h];
            while(next != null) {
                if(next.getValue().equals(value)) {
                   break;
                } else {
                    System.out.println("Collision " + value + " -> " + next.getValue());
                    current = next;
                    next = next.getNext();
                    collisions++;
                }
            }
            if(current != null) {
                current.setNext(newNode);
                elements++;
            }

        }
    }

    public HashNode search(String value) {
        int k = computeHash(value);
        int h = multiHash(k);
        HashNode temp = hashTable[h];
        while(!temp.getValue().equals(value)) {
            System.out.println("Collision: " + value + " -> " + temp.getValue());
            temp = temp.getNext();
        }
        System.out.print("Index: " + h + " ");
        return temp;
    }

    public void searchForAllNames() throws IOException {
        ArrayList<String> names = getNames();
        for(int i=0; i < names.size(); i++) {
            String name = names.get(i);
            System.out.println("Search for " + name + ": ");
            System.out.println(search(name) + "\n");
        }
    }

    public void addAllNames() throws IOException {
        getNames().forEach(this::add);
    }

    public String printInfo() {
        return "HashTable information: \n" +
                "Length = " + m + "\n" +
                "Collisions = " + collisions + "\n" +
                "Average collisions per person: " + (double)collisions/elements + "\n" +
                "Number of buckets = " + buckets + "\n" +
                "Load factor = " + (double)elements/m + "\n";
    }

    public String toString() {
        String print = "";
        for(int i=0; i < m; i++) {
            print += "Bucket " + i + ": ";
            HashNode start = hashTable[i];
            while(start != null) {
                print += start.getValue() + "   ";
                start = start.getNext();
            }
            print += "\n";
        }
        return print;
    }
}

class HashNode {
    private String value;
    private HashNode next;

    public HashNode(String value) {
        this.value = value;
        next = null;
    }

    public String getValue() {
        return value;
    }

    public HashNode getNext() {
        return next;
    }

    public void setNext(HashNode next) {
        this.next = next;
    }

    public String toString() {
        if(next != null) {
            return value + " -> " + next.toString();
        } else {
            return value;
        }
        
    }
}
