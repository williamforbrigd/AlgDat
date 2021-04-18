
class Predecessor implements Comparable<Predecessor> {
    int pathLength;
    int estimateToEnd = -1;
    double heuristicValue;

    long distToLandmark = -1;

    Node previousNode;
    static int infinity = 1000000000;
    public int findPathLength() {
        return pathLength;
    }
    public Node findPred() {
        return previousNode;
    }
    public Predecessor() {
        pathLength = infinity;
    }


    @Override
    public int compareTo(Predecessor a) {
        if(this.pathLength < a.pathLength) {
            return -1;
        } else if(this.pathLength > a.pathLength) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        String print = "";
        if(previousNode != null) {
            print += "Value: " + previousNode.nodeNr+ " Dist: " + pathLength;
        }
        return print;
    }
}
