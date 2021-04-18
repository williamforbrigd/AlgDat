import java.io.*;

public class Main {

    private static String pathToFileNodes = "norden/noder.txt";
    private static String pathToFileEdges = "norden/kanter.txt";
    private static String pathToFilePlaces = "norden/interessepkt.txt";

    private static Graph g;

    public static void main(String[] args) throws IOException {
        g = new Graph();
        readNodes(g);
        readEdges(g);
        readPlaces(g);

        testDijkstra("\"Stjørdal\"", "\"Steinkjer\"");
        testAstar("\"Stjørdal\"", "\"Steinkjer\"");
        testDijkstra("\"Kårvåg\"", "\"Gjemnes\"");
        testAstar("\"Kårvåg\"", "\"Gjemnes\"");
        testDijkstra("\"Trondheim\"", "\"Helsinki\"");
        testAstar("\"Trondheim\"", "\"Helsinki\"");

        testFindResourcesDijkstra("\"Trondheim lufthavn, Værnes\"", 2, 10);
        testFindResourcesDijkstra("\"Røros hotell\"", 4, 10);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean finished = false;
        while(!finished) {
            System.out.println("Enter names with \" \"");
            System.out.println("1: Find the shortest path with dijkstra.");
            System.out.println("2: Find the shortest path with Astar");
            System.out.println("3: Find the closest resources with dijkstra.");
            System.out.println("4: End.");
            int read = Integer.parseInt(reader.readLine());
            String startName = "", endName = "";
            switch(read) {
                case 1:
                    System.out.println("Enter the start name: ");
                    startName = reader.readLine();
                    System.out.println("Enter the end name: ");
                    endName = reader.readLine();
                    testDijkstra(startName, endName);
                    break;
                case 2:
                    System.out.println("Enter the start name:");
                    startName = reader.readLine();
                    System.out.println("Enter the end name:");
                    endName = reader.readLine();
                    testAstar(startName, endName);
                    break;
                case 3:
                    System.out.println("Enter the start name:");
                    startName = reader.readLine();
                    System.out.println("What code has the resources?");
                    int code = Integer.parseInt(reader.readLine());
                    System.out.println("How many resources to be found?");
                    int n = Integer.parseInt(reader.readLine());
                    testFindResourcesDijkstra(startName, code, n);
                    break;
                case 4:
                    finished = true;
                    System.out.println("Exits the program.");
                    break;
                default:
                    System.out.println("Please enter a number from 1 to 3.");
                    break;
            }
        }
    }

    public static void testDijkstra(String startName, String endName) {
        Dijkstra dijkstra = new Dijkstra(g.numberNodes, g.nodes);
        int nodeNrStart = g.map.getOrDefault(startName, -1);
        int nodeNrEnd = g.map.getOrDefault(endName, -1);
        int index;
        if(nodeNrStart != -1 && nodeNrEnd != -1) {
            Node start = g.nodes[nodeNrStart];
            Node end = g.nodes[nodeNrEnd];
            dijkstra.prepareDijkstra(start);
            long s = System.nanoTime();
            index = dijkstra.dijkstra(start, end);
            long e = System.nanoTime();
            if(index == -1 || index != end.nodeNr) {
                System.out.println("Could not find the shortest path");
                return;
            }
            double sek = (double)(e-s)/1000000000;
            System.out.println("-- Dijkstra from " + startName + " to " + endName + " --");
            System.out.println("Time for dijkstra algorithm: " + sek + " seconds");
            System.out.println("Nodes visited: " + dijkstra.visited);
            g.printDrivingTime(end);
            printRouteToFile(start, end, "dijkstra.csv");
            System.out.println();
        } else {
            System.out.println("Please type a valid name found in interessepkt.txt");
        }
    }

    public static void testAstar(String startName, String endName) {
        Astar astar = new Astar(g.numberNodes, g.nodes);
        int nodeNrStart = g.map.getOrDefault(startName, -1);
        int nodeNrEnd = g.map.getOrDefault(endName, -1);
        int index;
        if(nodeNrStart != -1 && nodeNrEnd != -1) {
            Node start = g.nodes[nodeNrStart];
            Node end = g.nodes[nodeNrEnd];
            astar.prepareAstar(start);
            long s = System.nanoTime();
            index = astar.astar(start, end);
            long e = System.nanoTime();
            if(index == -1 || index != end.nodeNr) {
                System.out.println("Could not find the shortest path");
                return;
            }
            double sek = (double)(e-s)/1000000000;
            System.out.println("-- Astar from " + startName + " to " + endName + " --");
            System.out.println("Time for Astar algorithm: " + sek + " seconds");
            System.out.println("Nodes visited: " + astar.visited);
            g.printDrivingTime(end);
            printRouteToFile(start, end, "astar.csv");
            System.out.println();
        } else {
            System.out.println("Please type a name found in interessepkt.txt");
        }
    }

    public static void testFindResourcesDijkstra(String startName, int code, int n) {
        Dijkstra dijkstra = new Dijkstra(g.numberNodes, g.nodes);
        int nodeNrStart = g.map.getOrDefault(startName, -1);
        Node[] resources;
        if(nodeNrStart != -1) {
            Node start = g.nodes[nodeNrStart];
            dijkstra.prepareDijkstra(start);
            long s = System.nanoTime();
            resources = dijkstra.findClosestDijkstra(start, code, n);
            long e = System.nanoTime();
            double sek = (double)(e-s)/1000000000;
            System.out.println("-- " + n + " closest resources with code " + code + " from " + startName + " --");
            System.out.println("Time for dijkstra algorithm to find resources: " + sek + " seconds");
            System.out.println("Nodes visited: " + dijkstra.visited);
            for(int i=0; i < resources.length; i++) {
                System.out.println(resources[i].name);
                printRouteToFile(start, resources[i], "resources/resource" + (i+1) + ".csv");
            }
            System.out.println();
        } else {
            System.out.println("Please type a name found in interessepkt.txt");
        }
    }

    private static void printRouteToFile(Node start, Node end, String pathName) {
        try {
            //Setting append to false to overwrite or clear the file.
            FileWriter writer = new FileWriter(pathName, false);
            for(Node current = end; !current.equals(start); current = current.pred.previousNode) {
                writer.write(current.printBreddeLengde());
            }
            writer.write(start.printBreddeLengde());
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void readNodes(Graph g) {
        ReadFile.readNodes(pathToFileNodes, g);
    }

    private static void readEdges(Graph g) {
        ReadFile.readEdges(pathToFileEdges, g);
    }

    private static void readPlaces(Graph g) {
        ReadFile.readPlaces(pathToFilePlaces, g);
    }
}
