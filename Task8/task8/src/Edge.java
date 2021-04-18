
class Edge {
    Edge nextEdge;
    Node toNode;

    int from;
    int to;


    public Edge(Edge nextEdge, Node toNode) {
        this.nextEdge = nextEdge;
        this.toNode = toNode;
    }

    public Edge(int from, int to, Edge nextEdge, Node toNode) {
        this.from = from;
        this.to = to;
        this.nextEdge = nextEdge;
        this.toNode = toNode;
    }

    public Edge() {}

    @Override
    public String toString() {
        return from + " " + to;
    }
}
