public class Node {
    int freq;
    int character;
    Node l, r;

    public Node() {}

    public Node(int character, int freq, Node l, Node r) {
        this.freq = freq;
        this.character = character;
        this.l = l;
        this.r = r;
    }

    public Node(int character, int freq) {
        this.character = character;
        this.freq = freq;
    }

    public boolean isLeaf() {
        assert (l == null && r == null) || (l != null && r != null);
        return l == null && r == null;
    }
    public String toString() {
        return character + "," + freq;
    }

}
