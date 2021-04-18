public class Heap {
    int length;
    Node[] node;

    public Heap(int length){
        this.length = length;
        node = new Node[length];
    }

    public void createHeap(){
        int i = length / 2;
        while(i -- > 0){
            fixHeap(i);
        }
    }

    //Note that the integers in this method is the index in
    //the node array.
    public void fixHeap(int i){
        int m = left(i);

        if(m < length){
            int right = m + 1;
            if(right < length && node[right].freq < node[m].freq){
                m = right;
            }
            if(node[m].freq < node[i].freq){
                switchNodes(node, i, m);
                fixHeap(m);
            }
        }
    }

    public Node getMin(){
        Node min = node[0];
        node[0] = node[--length];
        fixHeap(0);
        return min;
    }

    public void addNode(Node n){
        int i = length++;
        node[i] = n;
        prioUp(i, 0);
    }

    public void prioUp(int i, int p){
        int f;
        node[i].freq += p;
        while(i > 0 && node[i].freq < node[f=over(i)].freq){
            switchNodes(node, i, f);
            i = f;
        }
    }

    private void switchNodes(Node[] node, int i, int m){
        Node tmp = node[i];
        node[i] = node[m];
        node[m] = tmp;
    }

    public int over(int i){
        return (i-1) >> 1;
    }
    public int left(int i){
        return (i << 1) + 1;
    }
    public int right(int i){
        return (i + 1) << 1;
    }
}
