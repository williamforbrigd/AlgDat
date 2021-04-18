import java.util.HashMap;

class Graph {
    int numberNodes;
    int numberEdges;
    int numberPlaces;

    Node[] nodes;
    Edge[] edges;
    HashMap<String, Integer> map;

    public Graph(int numberNodes, Node[] nodes){
        this.numberNodes = numberNodes;
        this.nodes = nodes;
    }

    public Graph() {}

    public void setAllNotVisited() {
        for(int i=0; i < nodes.length; i++) {
            nodes[i].isVisited = false;
        }
    }

    public void printDrivingTime(Node node) {
        System.out.println("Driving time:");
        double sec = (double)(node.pred.pathLength/100);
        System.out.println("Total seconds: " + sec + " seconds");
        int hours = (int)(sec/3600);
        int min = (int)((sec%3600)/60);
        int seconds = (int)((sec%3600)%60);
        System.out.println( hours + " hours, " + min + " minutes, " + seconds + " seconds");
    }
}
