import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ReadFile {

    public static void readNodes(String pathToFile, Graph graph) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
            String[] splitString = hsplit(reader.readLine(), 1);
            int numberNodes = Integer.parseInt(splitString[0]);
            Node[] nodes = new Node[numberNodes];
            graph.numberNodes = numberNodes;
            graph.nodes = nodes;
            int nodeNr;
            double breddegrad, lengdegrad;
            for(int i=0; i < numberNodes; i++) {
                splitString = hsplit(reader.readLine(), 3);
                nodeNr = Integer.parseInt(splitString[0]);
                breddegrad = Double.parseDouble(splitString[1]);
                lengdegrad = Double.parseDouble(splitString[2]);
                nodes[i] = new Node(nodeNr, breddegrad, lengdegrad);
                nodes[i].cosBredde = Math.cos(breddegrad);
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void readEdges(String pathToFile, Graph graph) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
            String[] splitString = hsplit(reader.readLine(), 1);
            int numberEdges = Integer.parseInt(splitString[0]);
            Edge[] edges = new Edge[numberEdges];
            graph.numberEdges = numberEdges;
            graph.edges = edges;
            int from, to, time, length, speedLimit;
            for(int i=0; i < numberEdges; i++) {
                splitString = hsplit(reader.readLine(), 5);
                from = Integer.parseInt(splitString[0]);
                to= Integer.parseInt(splitString[1]);
                time = Integer.parseInt(splitString[2]);
                length = Integer.parseInt(splitString[3]);
                speedLimit = Integer.parseInt(splitString[4]);
                WEdge nextWEdge = (WEdge)graph.nodes[from].edge1;
                Node toNode = graph.nodes[to];
                WEdge k = new WEdge(from, to, nextWEdge,toNode, time, length, speedLimit);
                graph.nodes[from].edge1 = k;
                edges[i] = k;
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void readPlaces(String pathToFile, Graph graph) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
            StringTokenizer st = new StringTokenizer(reader.readLine());
            int numberPlaces = Integer.parseInt(st.nextToken());
            HashMap<String, Integer> map = new HashMap<>();
            graph.numberPlaces = numberPlaces;
            graph.map = map;
            for (int i = 0; i < numberPlaces; i++) {
                st = new StringTokenizer(reader.readLine());
                int nodeNr = Integer.parseInt(st.nextToken());
                int code = Integer.parseInt(st.nextToken());
                String name = "";
                while(st.hasMoreElements()) {
                    name += " " + st.nextToken();
                }
                //Fjerne hermetegn i strengen og mellomrommet på starten
                //name = name.substring(2, name.length()-1);
                name = name.substring(1,name.length());
                map.put(name, nodeNr);
                //Multiple places have sometimes the same node number if they are close.
                graph.nodes[nodeNr].codes.add(new Place(nodeNr, code, name));
                graph.nodes[nodeNr].name = name;
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] hsplit(String line, int numWords) {
        String[] splitString = new String[10];
        int j = 0;
        int length = line.length();
        for (int i = 0; i < numWords; ++i) {
        //Hopp over innledende blanke, finn starten på ordet
            while (line.charAt(j) <= ' ') ++j;
            int startIndex = j;
            //Finn slutten på ordet, hopp over ikke-blanke
            while (j < length && line.charAt(j) > ' ') ++j;
            splitString[i] = line.substring(startIndex, j);
        }
        return splitString;
    }
}

