import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class UnweightedGraphMain {
    public static void main(String[] args) {
        String pathToFile = "L7g6";
        UnweightedGraph graph = newGraph(pathToFile);
        ArrayList<String> components = graph.SCC();
        if(graph.getComponent() < 100) {
            System.out.println("Grafen " + pathToFile + " har " + graph.getComponent() + " sterkt sammenhengende " +
                    "komponenter.");
            System.out.printf("%-13s%-15s%n", "Komponent", "Noder i komponenten");
            for(int i=0; i < components.size(); i++) {
                String[] arr = components.get(i).split(":");
                System.out.printf("%-13s%-15s%n", arr[0], arr[1]);
            }
        }
    }

    static UnweightedGraph newGraph(String pathToFile) {
        UnweightedGraph graph = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
            StringTokenizer st = new StringTokenizer(reader.readLine());
            int noNodes = Integer.parseInt(st.nextToken());
            int noEdges = Integer.parseInt(st.nextToken());
            graph = new UnweightedGraph(noNodes, noEdges);
            for(int i=0; i < noEdges; i++) {
                st = new StringTokenizer(reader.readLine());
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());
                graph.addEdge(from,to);
            }
            return graph;
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

class UnweightedGraph {
    int noNodes, noEdges;
    LinkedList[] adj;
    Stack stack;
    int component = 0;

    public UnweightedGraph(int noNodes, int noEdges) {
        this.noNodes = noNodes;
        this.noEdges = noEdges;
        stack = new Stack();
        adj = new LinkedList[noNodes];
        for(int i=0; i < noNodes; i++) {
            adj[i] = new LinkedList();
        }
    }

    public UnweightedGraph() {}

    public int getComponent() {
        return component;
    }

    public void setNoEdges(int noEdges) {
        this.noEdges = noEdges;
    }

    public void setNoNodes(int noNodes) {
        this.noNodes = noNodes;
    }

    void addEdge(int from, int to) {
        if(from < noNodes && to < noNodes)
            adj[from].addNode(to);
    }

    public void sortDesc(int index, boolean[] visited, Stack stack) {
        visited[index] = true;
        Node temp = adj[index].head;
        while(temp != null) {
            int n = temp.value;
            temp = temp.next;
            if(!visited[n])
                sortDesc(n, visited, stack);
        }
        stack.push(index);
    }

    public UnweightedGraph transpose() {
        UnweightedGraph transposed = new UnweightedGraph(noNodes, noEdges);
        for(int i=0; i < noNodes; i++) {
            Node temp = adj[i].head;
            while(temp != null) {
                int n = temp.value;
                temp = temp.next;
                transposed.adj[n].addNode(i);
            }
        }
        return transposed;
    }

    private void dfs(int index, boolean[] visited, Stack stack) {
        visited[index] = true;
        Node temp = adj[index].head;
        while(temp != null) {
            int n = temp.value;
            temp = temp.next;
            if(!visited[n]) {
                dfs(n, visited, stack);
            }
        }
        stack.push(index);
    }

    private void dfsPrint(int index, boolean[] visited, ArrayList<String> components, int component) {
        visited[index] = true;
        components.set(component, components.get(component) + index + " ");
        Node temp = adj[index].head;
        while(temp != null) {
            int n = temp.value;
            temp = temp.next;
            if(!visited[n]) {
                dfsPrint(n, visited, components, component);
            }
        }
    }

    /**
     * Method to find the strongly connected components;
     * @return list with a string for each component
     */

    public ArrayList<String> SCC() {
        Stack stack = new Stack();
        boolean[] visited = new boolean[noNodes];

        //Marking all the nodes as not visited to prepare for the first DFS.
        for(int i=0; i < noNodes; i++)
            visited[i] = false;

        //the first DFS
        for(int i=0; i < noNodes; i++)
            if(!visited[i])
                dfs(i,visited, stack);

        //Sorting the graph in descending order based when they finished in the DFS.
        for(int i=0; i < noNodes; i++)
            if(!visited[i])
                sortDesc(i, visited, stack);

        //Getting the transposed or reversed graph.
        UnweightedGraph transposed = transpose();

        //Setting all the nodes as  not visited in order to prepare for the second DFS
        for(int i=0; i < noNodes; i++)
            visited[i] = false;

        //Creating an array of components.
        ArrayList<String> components = new ArrayList<>();
        while(!stack.isEmpty()) {
            int v = stack.pop();
            //Checks whether the node has been visited or not. If not, a DFS is performed to find the other
            //nodes in the component.
            if(!visited[v]) {
                components.add(component+1 + ": ");
                transposed.dfsPrint(v, visited, components, component);
                component++;
            }
        }
        return components;
    }
}

/**
 * Made this class for better understanding.
 */
class Stack {
    ArrayList<Integer> stack;

    public Stack() {
        stack = new ArrayList<>();
    }

    public boolean isEmpty() {
        return stack.size()==0;
    }

    public void push(int index) {
        stack.add(index);
    }

    public int pop() {
        int elem = stack.get(stack.size()-1);
        stack.remove(stack.size()-1);
        return elem;
    }
}


class LinkedList {
    Node head = null;

    public void addNode(int value) {
        if(head == null) {
            head = new Node(value);
        } else {
            Node tmp = head;
            while(tmp.next != null) {
                tmp = tmp.next;
            }
            tmp.next = new Node(value);
        }
    }
}

/**
 * Represents a node in the graph.
 */
class Node {
    int value;
    Node next;

    public Node(int value) {
        this.value = value;
    }

    public String toString() {
        return this.value + " ";
    }
}


