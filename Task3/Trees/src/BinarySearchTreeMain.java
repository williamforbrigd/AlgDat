import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class BinarySearchTreeMain {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type words:");
        String[] input = reader.readLine().split(" ");

        //String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        //String[] input = {"hode", "bein", "hals", "arm", "tann",  "hånd",  "tå"};
        BinarySearchTree tree = new BinarySearchTree();

        for (int i = 0; i < input.length; i++) {
            tree.addNode(input[i]);
        }

        tree.printTreeFormat();
    }
}

/**
 * Class that represents a binary search tree with text in every tree node.
 */
class BinarySearchTree {
    private TreeNode root;

    /**
     * Adds a node to the tree using recursion.
     * @param root of the tree.
     * @param key to the node.
     * @return the root of the tree.
     */
    public TreeNode add(TreeNode root, String key) {
        if(root == null) {
            root = new TreeNode(key);
            return root;
        }
        if(key.compareToIgnoreCase(root.key) < 0) {
            root.left = add(root.left,key);
        } else if(key.compareToIgnoreCase(root.key) > 0) {
            root.right = add(root.right,key);
        }
        return root;
    }


    public void addNode(String key) {
        root = add(root, key);
    }

    public void inOrderRecursion(TreeNode node) {
        if(node != null) {
            inOrderRecursion(node.left);
            System.out.println(node.key + " ");
            inOrderRecursion(node.right);
        }
    }

    public void inOrderTraversal() {
        inOrderRecursion(root);
    }


    private void preOrderRecursion(TreeNode node) {
        if (node == null)
            return;

        System.out.print(node.key + " ");
        preOrderRecursion(node.left);
        preOrderRecursion(node.right);
    }

    private void preOrderTraversal() {
        preOrderRecursion(root);
    }

    /**
     * Finds the height of the tree using recursion.
     * The height of a tree is the number of links between the root node
     * and the leaf node furthest away.
     * @param root of the tree.
     * @return the height of the tree.
     */

    private int findTreeHeight(TreeNode root) {
        if(root != null) {
            return 1 + Math.max(findTreeHeight(root.left), findTreeHeight(root.right));
        }
        return 0;
    }

    /**
     * Prints the tree using level order traversal.
     * The order is first the root, then the children of the root, then their children and so on.
     * Puts the root node in a queue.
     * Then the next node is picked, and its children are put in the queue.
     * This process continues until the que is empty.
     * @param root of the tree
     */

    private void printTreeFormat(TreeNode root) {
        if(root == null) return;

        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(root);

        int height = findTreeHeight(root);
        boolean finished = false;
        int i = 0;

        while(i++ < height && !finished) {
            //To print out the spaces.
            for(int j=0; j < height-i; j++) {
                System.out.print("   ");
            }
            int nodeCount = queue.size();
            if(nodeCount == 0)
                finished = true;

            //Nodes from the current level is removed and nodes for the next level is added in the queue.
            while(nodeCount > 0) {
                //To retrieve but not remove the first element in the list.
                TreeNode node = queue.peek();
                System.out.print(node.key + " ");
                queue.remove();
                if(node.left != null)
                    queue.add(node.left);
                if(node.right != null)
                    queue.add(node.right);
                nodeCount--;
            }
            System.out.println();

        }

    }

    public void printTreeFormat() {
        printTreeFormat(root);
    }

}

/**
 * Class that represents a tree node that has a string as key.
 */
class TreeNode {
    String key;
    TreeNode left, right;

    public TreeNode(String key) {
        this.key = key;
        left = right = null;
    }


}




