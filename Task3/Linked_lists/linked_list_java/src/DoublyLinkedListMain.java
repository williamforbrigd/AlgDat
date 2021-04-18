import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;

public class DoublyLinkedListMain {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter two numbers with + or - in between (For example:43 + 17)");
        System.out.println("Press enter to exit");

        DoublyLinkedList list = new DoublyLinkedList();
        boolean finished = false;

        do {
            String line = reader.readLine();
            if(line.startsWith(" ") || line.startsWith("  ") || line.startsWith("   ")) {
                line = line.substring(1);
            }
            String[] input = line.split(" ");
            if(input.length == 3) {
                list.calculateAns(input);
            } else {
                finished = true;
            }
        } while(!finished);
    }
}

/**
 * Class used for storing numbers ass doubly linked lists.
 * Can add or subtract two numbers.
 */
class DoublyLinkedList {

    private Node head = null, tail = null;
    private int noNodes = 0;

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public int getNoNodes() {
        return noNodes;
    }

    /**
     * Adds a new node to the list.
     * @param value of the new node.
     */

    public void addNode(int value) {
        Node newNode = new Node(head, null, value);
        if(head == null) {
            head = tail = newNode;
            head.previous = null;
            tail.next = null;
            noNodes++;
        } else {
            tail.next = newNode;
            newNode.previous = tail;
            tail = newNode;
            tail.next = null;
            noNodes++;
        }
    }

    public void calculateAns(String[] input) {
        if(input == null) return;
        else if(input.length == 3) {
            DoublyLinkedList list1 = createListFromInput(input[0]);
            DoublyLinkedList list2 = createListFromInput(input[2]);
            String[] print = calculate(list1, list2, input[1]);

            String ans = reverseString(print[2]);

            String operator = "";
            if(input[1].equals("+")) {
                operator = "+";
            } else {
                operator = "-";
                BigInteger num1 = new BigInteger(list1.toString());
                BigInteger num2 = new BigInteger(list2.toString());
                if(num1.compareTo(num2) < 0) {
                    BigInteger ans1 = new BigInteger(ans);
                    BigInteger ans2 = BigInteger.valueOf(10).pow(ans.length());
                    ans = String.valueOf(ans1.subtract(ans2));
                }
            }
            System.out.println("  " + print[0] + "\n" + operator + " " + print[1] + "\n" + "= " + ans);
        }
    }

    /**
     * Adds or subtracts two doubly linked lists.
     * Gets the tail of the linked list to ensure that the digits with the same indexes are added together.
     * Retrieves the length from the list that has the most nodes.
     * Calculates the sum of the digits and if there is any carry when adding or subtracting.
     * At the end of the loop, the nodes are set to their previous ones.
     * @param list1 list to be added.
     * @param list2 list to be added.
     * @param operator the operator.
     * @return an array with the two numbers and the answer.
     */

    private String[] calculate(DoublyLinkedList list1, DoublyLinkedList list2, String operator) {
        DoublyLinkedList result = new DoublyLinkedList();
        Node node1 = list1.getTail();
        Node node2 = list2.getTail();
        String[] temp = new String[3];
        temp[0] = list1.toString();
        temp[1] = list2.toString();

        int carry = 0, sum = 0, first = 0, second = 0;

        int length = Math.max(list1.getNoNodes(), list2.getNoNodes());

        for(int i=0; i < length; i++) {
            first = (node1!=null) ? node1.getValue() : 0;
            second = (node2!=null) ? node2.getValue() : 0;

            if(operator.equals("+")) {
                sum = first + second + carry;
                carry = sum / 10;
                sum %= 10;
            } else {
                sum = first + carry - second;
                if(sum < 0) {
                    sum+=10;
                    carry = -1;
                } else {
                    carry = 0;
                }
            }
            if(i == (length-1) && carry > 0) {
                result.addNode(sum);
                result.addNode(carry);
            } else {
                result.addNode(sum);
            }
            node1 = (node1 != null) ? node1.getPrevious() : null;
            node2 = (node2 != null) ? node2.getPrevious() : null;
        }
        temp[2] = result.toString();
        return temp;
    }

    /**
     * Need this method due to the nodes being added first in the doubly linked list.
     */
    private String reverseString(String str) {
        char[] arr = str.toCharArray();
        String temp = "";
        for(int i=arr.length-1; i >= 0; i--) {
            temp += arr[i];
        }
        return temp;
    }

    /**
     * Creates a list and adds each node as a digit to the list.
     * @param input the number to list will be made from.
     * @return list
     */
    private DoublyLinkedList createListFromInput(String input) {
        if(input == null || input.equals("")) return null;

        DoublyLinkedList list = new DoublyLinkedList();
        Arrays.stream(input.split("")).forEach(s -> list.addNode(Integer.parseInt(s)));
        return list;
    }

    @Override
    public String toString() {
        Node current = getHead();
        String print = "";
        while(current != null) {
            print += ((current.getValue() >= 0) ? String.valueOf(current.getValue()) : "");
            current = current.getNext();
        }
        return print;
    }
}


class Node {
    Node previous;
    Node next;
    int value;

    public Node(Node previous, Node next, int value) {
        this.previous = previous;
        this.next = next;
        this.value = value;
    }

    public Node(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Node getPrevious() {
        return previous;
    }

    public Node getNext() {
        return next;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
