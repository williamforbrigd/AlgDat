package test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WeightedGraphMain {
    public static void main(String[] args) {
        String pathToFile = "vg2";
        WeightedGraph graph = readFile(pathToFile);
        int index = 7;
        Node start = graph.nodes[index];
        graph.dijkstra(start);
        System.out.printf("%-7s%-13s%-15s%n", "Node", "Predecessor", "Distance");
        graph.printInfo();
    }

    static WeightedGraph readFile(String pathToFile) {
        WeightedGraph graph = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
            StringTokenizer st = new StringTokenizer(reader.readLine());
            int N = Integer.parseInt(st.nextToken());
            int noEdges = Integer.parseInt(st.nextToken());
            Node[] nodes = new Node[N];
            graph = new WeightedGraph(N, nodes);
            for(int i=0; i<N; i++) nodes[i] = new Node(i);
            for(int i=0; i<noEdges; i++) {
                st = new StringTokenizer(reader.readLine());
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());
                int weight = Integer.parseInt(st.nextToken());
                WEdge k = new WEdge(nodes[to], (WEdge)nodes[from].edge1, weight);
                nodes[from].edge1 = k;
            }
            return graph;
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

class WeightedGraph {
    Node[] pri;
    int N; //the length of the priority ueue.
    Predecessor pred;
    Node[] nodes;
    PriorityQueue<Node> queue;

    public WeightedGraph(int N, Node[] nodes){
        this.N = N;
        this.nodes = nodes;
        pred = new Predecessor();
        pri = new Node[N];
    }

    public void dijkstra(Node s) {
        pred.initPred(s, nodes);
        makePriorityQueue();
        for(int i=N; i>1; --i) {
            Node n = queue.poll();
            for(Edge k = n.edge1; k!= null; k = k.next) {
                WEdge edge = (WEdge)k;
                reduce(n,edge);
            }
        }
    }

    private void makePriorityQueue() {
        queue = new PriorityQueue<>(this.N,new Node());
        for(int i=0; i < N; i++) {
            queue.add(nodes[i]);
        }
    }

    private void reduce(Node n, WEdge k) {
        Predecessor nd = n.pred;
        Predecessor md = k.to.pred;
        if(md.dist > nd.dist + k.weight) {
            md.dist = nd.dist + k.weight;
            md.nodePred = n;
            this.queue.remove(k.to);
            this.queue.add(k.to);
        }
    }

    public void printInfo() {
        String pred = "";
        String dist = "";
        for(int i=0; i < N; i++) {
            if(nodes[i].pred.dist == Predecessor.infinity) {
                pred = "";
                dist = "Not reached";
            } else if(nodes[i].pred.dist == 0) {
                pred = "Start";
                dist = "0";
            } else {
                pred = nodes[i].pred.nodePred.value + "";
                dist = nodes[i].pred.dist + "";
            }
            System.out.printf("%-7s%-13s%-15s%n", nodes[i].value, pred, dist);
        }
    }
}

class Predecessor implements Comparable<Predecessor> {
    int dist;
    Node nodePred;
    static int infinity = 1000000000;
    public int findDist() {
        return dist;
    }
    public Node findPred() {
        return nodePred;
    }
    public Predecessor() {
        dist = infinity;
    }

    //start Node s gets a distance of 0 while all the other nodes
    //get distance of infinity.
    public void initPred(Node s, Node[] nodes) {
        int N = nodes.length;
        for(int i=N; i-->0;) {
            nodes[i].pred = new Predecessor();
        }
        s.pred.dist = 0;
    }

    @Override
    public int compareTo(Predecessor a) {
        if(this.dist < a.dist) {
            return -1;
        } else if(this.dist > a.dist) {
            return 1;
        } else {
            return 0;
        }
    }

    public String toString() {
        String print = "";
        if(nodePred != null) {
            print += "Value: " + nodePred.value + " Dist: " + dist;
        }
        return print;
    }
}

class Node implements Comparator<Node> {
    int value;
    Edge edge1;
    Predecessor pred;

    public Node(int value, Edge edge1, Predecessor pred) {
        this.value = value;
        this.edge1 = edge1;
        this.pred = pred;
    }

    public Node(int value) {
        this.value = value;
    }

    public Node() {}

    @Override
    public int compare(Node a, Node b) {
        return a.pred.dist - b.pred.dist;
    }
    @Override
    public boolean equals(Object o) {
        if(o == null) return true;
        if(this == o) return true;
        if(!(o instanceof Node)) return false;
        Node n = (Node)o;
        return this.value == n.value && this.pred.dist == n.pred.dist;
    }
    @Override
    public String toString() {
        return value + " ";
    }
}

class WEdge extends Edge {
    int weight;

    public WEdge(Node n, WEdge next, int weight) {
        super(next, n);
        this.weight = weight;
    }

    public String toString() {
        return weight + " ";
    }
}

class Edge {
    Edge next;
    Node to;

    public Edge(Edge next, Node to) {
        this.next = next;
        this.to = to;
    }

    public Edge() {}
}
