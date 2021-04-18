import java.util.ArrayList;

class Node {
    Edge edge1;
    Predecessor pred;

    int nodeNr;
    double breddegrad;
    double lengdegrad;
    double cosBredde;
    boolean isVisited;

    ArrayList<Place> codes;
    String name;

    public Node(int nodeNr, double breddegrad, double lengdegrad) {
        this.nodeNr = nodeNr;
        this.breddegrad = breddegrad;
        this.lengdegrad = lengdegrad;
        codes = new ArrayList<>();
        toRadians();
    }

    public Node(Edge edge1, Predecessor pred) {
        this.edge1 = edge1;
        this.pred = pred;
    }

    public Node(int nodeNr) {
        this.nodeNr = nodeNr;
        codes = new ArrayList<>();
    }

    public Node() {}

    public double toDegrees(double radians) {
        return radians * 180 / Math.PI;
    }

    public void toRadians() {
        breddegrad = breddegrad * Math.PI/180;
        lengdegrad = lengdegrad * Math.PI/180;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(this == o) return true;
        if(!(o instanceof Node)) return false;
        Node n = (Node)o;
        return this.nodeNr == n.nodeNr;
    }

    //Bredde og lengdegrad er allerede gjort om til radianer i konstruktøren.
    public String printBreddeLengde() {
        return toDegrees(this.breddegrad) + ", " + toDegrees(this.lengdegrad) + "\n";
    }

    @Override
    public String toString() {
        return nodeNr + " " + breddegrad + " " + lengdegrad + " " + name;
    }
}
