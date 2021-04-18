import java.util.PriorityQueue;

public class Dijkstra extends Graph {

    PriorityQueue<Node> pq;
    int visited;

    public Dijkstra(int numberNodes, Node[] nodes) {
        super(numberNodes, nodes);
    }

    public Dijkstra() {}

    public void prepareDijkstra(Node start) {
        resetPQDijkstra();
        pq.add(start);
        initPredDijkstra(start);
        setAllNotVisited();
        visited = 0;
    }

    //Den raskeste veien er funnet når noden plukkes ut av prioritetskøen
    public int dijkstra(Node start, Node end) {
        Node vertex = start;
        while(!pq.isEmpty()) {
            vertex = pq.poll();
            if(vertex.equals(end)) return end.nodeNr;
            if(!vertex.isVisited) {
                for(Edge edge = vertex.edge1; edge != null; edge = edge.nextEdge) {
                    WEdge wedge = (WEdge)edge;
                    reduce(vertex, wedge);
                }
                vertex.isVisited = true;
                visited++;
            }
        }
        return -1;
    }

    //Metode for å finne ressurser nærmest en node start.
    //n er hvor mange ressurser.
    //koden er 1 for stedsnavn, 2 for bensinstasjoner og 4 for ladestasjoner.
    public Node[] findClosestDijkstra(Node start, int code, int n) {
        Node[] resources = new Node[n];
        Node vertex = start;
        int count = 0;
        while(!pq.isEmpty() && count < n) {
            vertex = pq.poll();
            if(!vertex.isVisited) {
                for(Edge edge = vertex.edge1; edge != null; edge = edge.nextEdge) {
                    WEdge wedge = (WEdge)edge;
                    reduce(vertex, wedge);
                }
                for(Place place : vertex.codes) {
                    if(place.code == code) {
                        resources[count] = vertex;
                        count++;
                        break;
                    }
                }
                vertex.isVisited = true;
                visited++;
            }
        }
        return resources;
    }

    public void reduce(Node node, WEdge edge) {
        Predecessor nd = node.pred;
        Predecessor md = edge.toNode.pred;
        if(md.pathLength > nd.pathLength + edge.drivingTime) {
            md.pathLength = nd.pathLength + edge.drivingTime;
            md.previousNode = node;
            pq.add(edge.toNode);
        }
    }

    public void resetPQDijkstra() {
        pq = new PriorityQueue<>((a,b) -> a.pred.pathLength - b.pred.pathLength);
    }


    //start Node s gets a pathLengthance of 0 while all the other nodes
    //get pathLengthance of infinity.
    public void initPredDijkstra(Node start) {
        int N = super.nodes.length;
        for(int i=N; i-->0;) {
            nodes[i].pred = new Predecessor();
        }
        start.pred.pathLength = 0;
    }
}
