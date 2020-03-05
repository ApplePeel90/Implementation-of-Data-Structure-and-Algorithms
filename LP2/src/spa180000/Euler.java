/**
 * Implementation of Data Structures and Algorithms Long Project 2
 * Authors:
 * Anuj Shah - AUS180000
 * Qi Wang - QXW170003
 * Samarth Agrawal - SPA180000
 * Mayank Bhatia - MXB180024
 */
package spa180000;

import spa180000.Graph.*;
import spa180000.Graph.Timer;

import java.util.*;


public class Euler extends GraphAlgorithm<Euler.EulerVertex> {
    static int VERBOSE = 1;
    Vertex start;
    List<Vertex> tour;
    Deque<Vertex> eulerStack;

    // You need this if you want to store something at each node
    static class EulerVertex implements Factory {
        boolean visited;                    // Flag for checking whether the given vertex is visited or not
        Boolean[] visitedEdges;             // Flag for checking whether outgoing edges are visited for a given vertex

        EulerVertex(Vertex u) {
            if (u != null) {
                visitedEdges = new Boolean[u.outDegree()];
                for (int i = 0; i < u.outDegree(); i++) visitedEdges[i] = false;
            }
            this.visited = false;

        }

        public EulerVertex make(Vertex u) {
            return new EulerVertex(u);
        }

    }

    // To do: function to find an Euler tour
    public Euler(Graph g, Vertex start) {
        super(g, new EulerVertex(null));
        this.start = start;
        eulerStack = new ArrayDeque<>();
        tour = new LinkedList<>();
    }

    /* To do: test if the graph is Eulerian.
     * If the graph is not Eulerian, it prints the message:
     * "Graph is not Eulerian" and one reason why, such as
     * "inDegree = 5, outDegree = 3 at Vertex 37" or
     * "Graph is not strongly connected"
     */

    /**
     * checks whether or not the given graph is Eulerian and prints a reason failure message if the graph is not Eulerian
     *
     * @return boolean that depicts whether or not the given graph is Eulerian
     */
    public boolean isEulerian() {
        boolean finishDFS = this.DFS();
        if (finishDFS) {
            g.reverseGraph();
            //reset visitedEdges on each vertex
            for (Vertex v : g.getVertexArray()) {
                get(v).visited = false;
            }
            finishDFS = this.DFS();
            g.reverseGraph();
            return finishDFS;
        } else return finishDFS;
    }

    /***
     * Perform DFS traversal on a given graph
     * @return false if DFS doesnt reach all the nodes or if InDegree!=OutDegree at any vertex
     */
    public boolean DFS() {
        Deque<Vertex> dfsStack = new ArrayDeque<>();
        int visitedCount = 0;
        dfsStack.push(start);
        while (!dfsStack.isEmpty()) {
            Vertex u = dfsStack.pop();              // Pops the stack and checks the in and out degree of the given vertex
            if (u.inDegree() != u.outDegree()) {
                System.out.println("InDegree: " + u.inDegree() + " OutDegree: " + u.outDegree() + " at Vertex " + u.getName());
                return false;
            }
            if (!get(u).visited) {
                get(u).visited = true;             // Sets the visited property of a given vertex as True
                visitedCount++;                    // Increments the count of the total number of vertices that have been visited
                for (Edge e : g.outEdges(u)) {
                    dfsStack.push(e.otherEnd(u));
                }
            }
        }
        // Checks whether the visited counter is equal to the size of the graph, returns false if not equal
        if (visitedCount != g.size()) {
            System.out.println("Graph is not strongly connected");
            return false;
        }
        return true;        // The given graph is strongly connected
    }


    /**
     * Checks if the given graph is Eulerian or not. For Eulerian graphs we calculate their Euler tour otherwise return an empty LinkedList
     *
     * @return a LinkedList containing the Euler Tour of the given graph
     */
    public List<Vertex> findEulerTour() {
        if (!isEulerian()) {
            System.out.println("Graph is not Eulerian");
            return new LinkedList<Vertex>();
        }
        // Graph is Eulerian...find the tour and return tour
        eulerStack.push(start);
        while (!eulerStack.isEmpty()) {
            Vertex u = eulerStack.peek();
            int i = 0;
            boolean notVisitedAllEdges = true;                  // Initially set to true, if we have visited all the edges for a given vertex
            for (Edge edge : g.outEdges(u)) {
                if (!get(u).visitedEdges[i]) {                  // If the ith edge of the given vertex is not explored, the we push the other end vertex of that edge to the stack
                    eulerStack.push(edge.otherEnd(u));
                    get(u).visitedEdges[i] = true;
                    notVisitedAllEdges = false;
                    break;
                }
                i++;
            }
            if (notVisitedAllEdges)
                ((LinkedList) tour).addFirst(eulerStack.pop());     // If all the Edges are visited for a given Vertex ,we pop the Stack and store the result in the LinkedList
        }
        return tour;
    }

    public static void main(String[] args) throws Exception {
        Scanner in;
        if (args.length > 1) {
            in = new Scanner(System.in);
        } else {
            String input = "9 13 1 2 1  2 3 1  3 1 1  3 4 1  4 5 1  5 6 1  6 3 1  4 7 1  7 8 1  8 4 1  5 7 1  7 9 1  9 5 1";
            in = new Scanner(input);
        }
        int start = 1;
        if (args.length > 1) {
            start = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            VERBOSE = Integer.parseInt(args[2]);
        }
        Graph g = Graph.readDirectedGraph(in);      // Reads the given directed graph
        g.printGraph(false);           // Prints the given graph
        Vertex startVertex = g.getVertex(start);
        Timer timer = new Timer();


        Euler euler = new Euler(g, startVertex);        // Creates a new Instance of the Euler Graph class
        List<Vertex> tour = euler.findEulerTour();      // Returns a list of Vertices containing the Euler Tour of the input Directed Graph


        timer.end();
        if (VERBOSE > 0) {
            System.out.println("Output:");
            // print the tour as sequence of vertices (e.g., 3,4,6,5,2,5,1,3)
            System.out.println(tour);
        }
        System.out.println(timer);
    }

    public void setVerbose(int ver) {
        VERBOSE = ver;
    }
}
