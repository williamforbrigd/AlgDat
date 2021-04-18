import java.util.PriorityQueue;

public class Astar extends Graph {

    PriorityQueue<Node> pq;
    int visited;

    public Astar(int numberNodes, Node[] nodes) {
        super(numberNodes, nodes);
    }

    //jordens radius r er 6371 km, høyeste fartsgrense 130 km/h, 3600 sek/time
    //For å få hundredels sekunder: 2*6371/130*3600*100 = 35285538.46153846153846153846
    private int haversine(Node n1, Node n2) {
        double sinBredde = Math.sin((n1.breddegrad - n2.breddegrad)/2.0);
        double sinLengde= Math.sin((n1.lengdegrad - n2.lengdegrad)/2.0);
        double cosBredde1 = n1.cosBredde;
        double cosBredde2 = n2.cosBredde;
        return (int)(35285538.46153846153846153846 * Math.asin(Math.sqrt(sinBredde * sinBredde + cosBredde1 *
                        cosBredde2 * sinLengde * sinLengde)));
    }

    public void prepareAstar(Node start) {
        resetPQAstar();
        pq.add(start);
        initPredAstar(start);
        setAllNotVisited();
        visited = 0;
    }

    public int astar(Node start, Node end) {
        Node vertex = start;
        while(!pq.isEmpty()) {
            vertex = pq.poll();
            if(vertex.equals(end)) return end.nodeNr;
            if(!vertex.isVisited) {
                for(Edge edge = vertex.edge1; edge != null; edge = edge.nextEdge) {
                    WEdge wedge = (WEdge)edge;
                    Node to = wedge.toNode;
                    if(to.pred.estimateToEnd == -1) {
                        to.pred.estimateToEnd = haversine(to, end);
                    }
                    reduce(vertex, wedge);
                }
                vertex.isVisited = true;
                visited++;
            }
        }
        return -1;
    }

    public void reduce(Node node, WEdge wedge) {
        Predecessor nd = node.pred;
        Predecessor md = wedge.toNode.pred;
        Node to = wedge.toNode;
        if(md.pathLength > nd.pathLength + wedge.drivingTime) {
            md.pathLength = nd.pathLength + wedge.drivingTime;
            md.heuristicValue = md.pathLength + md.estimateToEnd;
            md.previousNode = node;
            pq.add(to);
        }
    }

    public void resetPQAstar() {
        pq = new PriorityQueue<>((a,b)-> (int) (a.pred.heuristicValue - b.pred.heuristicValue));
    }

    public void initPredAstar(Node start) {
        int N = super.nodes.length;
        for(int i=N; i-->0;) {
            nodes[i].pred = new Predecessor();
        }
        start.pred.pathLength = 0;
    }
}
