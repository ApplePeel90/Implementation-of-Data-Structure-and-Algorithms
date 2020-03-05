/*  Hemali Amitkumar Patel hap170002
 *  Qi Wang qxw170003
 *
 */


/**
 * Starter code for SP4
 *
 * @author rbk
 */

// change to your netid
package qxw170003;

import qxw170003.Graph.Vertex;
import qxw170003.Graph.Edge;
import qxw170003.Graph.GraphAlgorithm;
import qxw170003.Graph.Factory;

import java.io.File;
import java.util.*;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {

    public boolean isDAG;
    public int numOfComponents;

    public static class DFSVertex implements Factory {
        int cno;
        boolean visiting;
        boolean visited;

        public DFSVertex(Vertex u) {
            this.visiting = false;
            this.visited = false;
        }

        public DFSVertex make(Vertex u) {
            return new DFSVertex(u);
        }
    }

    public DFS(Graph g) {
        super(g, new DFSVertex(null));
        this.isDAG = true;
    }


    public static DFS depthFirstSearch(Graph g) {
        DFS dfs = new DFS(g);
        dfs.depthFirstSearch();
        return dfs;
    }

    // Find the number of components of an undirected graph
    // Each node gets a component number
    private void depthFirstSearch() {
        int cno = 0;
        for (Graph.AdjList adlist : this.g.adjList) {
            if (get(adlist.vertex).visiting) continue;
            Stack<Vertex> stack = new Stack<>();
            stack.add(adlist.vertex);

            while (!stack.isEmpty()) {
                Vertex cur = stack.pop();
                get(cur).visiting = true;
                get(cur).cno = cno;
                for (Edge edge : this.g.adj(cur).outEdges) {
                    if (!get(edge.to).visiting) {
                        stack.add(edge.to);
                    } else continue;
                }
            }
            // Increase the component number when visited visiting one component
            cno++;
        }
        this.numOfComponents = cno;
    }


    // Member function to find topological order
    public ArrayList<Vertex> topologicalOrder1() {

        // Use res to store the sorted order
        ArrayList<Vertex> res = new ArrayList<>();
        if (this.g.directed == false) isDAG = false;

        for (Graph.AdjList n : this.g.adjList) {
            // Visit all the unvisited vertex
            if (get(n.vertex).visited) continue;
            visit(n, this, res);
        }
        return isDAG ? res : null;
    }

    private void visit(Graph.AdjList n, DFS dfs, List<Vertex> res) {
        /*
        @param : a graph AdjList
        @param : Dfs parallel store
        @param : res topological-sort result
         */
        get(n.vertex).visiting = true;
        for (Edge e : n.outEdges) {
            if (!get(e.to).visiting) {
                visit(dfs.g.adjList[e.to.getIndex()], dfs, res);
            }
            // Meeting a visiting vertex means that the graph is cyclic
            else dfs.isDAG = false;
        }
        if (!get(n.vertex).visited) {
            // Push the vertex into the front of list when it has no outgoing edge
            get(n.vertex).visited = true;
            res.add(0, n.vertex);
        }
        get(n.vertex).visiting = false;
    }

    // Find the number of connected components of the graph g by running dfs.
    // Enter the component number of each vertex u in u.cno.
    // Note that the graph g is available as a class field via GraphAlgorithm.
    public int connectedComponents() {
        return this.numOfComponents;
    }

    // After running the connected components algorithm, the component no of each vertex can be queried.
    public int cno(Vertex u) {
        return get(u).cno;
    }

    // Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder1(Graph g) {
        DFS d = new DFS(g);
        return d.topologicalOrder1();
    }

    // Find topological oder of a DAG using the second algorithm. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder2(Graph g) {
        return null;
    }

    public static void main(String[] args) throws Exception {
        String string1 =  "7 8   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 7   6 7 1   7 6 1 0";
        String string2 =  "5 6   1 2 2   2 3 3   2 4 5   3 4 4   4 5 1   5 1 7";
        String string3 =  "5 5   1 2 2   2 3 3   2 4 5   3 4 4   4 5 1";
        String string4 =  "7 5   2 1 2   2 3 3   3 4 4   4 5 1   7 6 2";
        String string5 =  "5 4   1 2 2   2 1 2   2 3 3   5 4 2";
        String string6 =  "5 3   2 1 2   2 3 3   5 4 2";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string6);

        // Read graph from input
        Graph g = Graph.readGraph(in);
        g.printGraph(false);


//        DFS dfs = new DFS(g);

        List<Vertex> res = topologicalOrder1(g);

        System.out.println("Topo Order:");
        if (res == null) System.out.println("null");
        else{
            for(Vertex v : res){
                System.out.print(v.name + ", ");
            }
        }

    }
}


/*======== Results for topologicalOrder1() =========*/
/*
Input:  7 8   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 7   6 7 1   7 6 1 0
Output: null

Input:  5 6   1 2 2   2 3 3   2 4 5   3 4 4   4 5 1   5 1 7
Output: null

Input: 5 5   1 2 2   2 3 3   2 4 5   3 4 4   4 5 1
Output: [1, 2, 3, 4, 5]

Input: 7 5   2 1 2   2 3 3   3 4 4   4 5 1   7 6 2
Output: [7, 6, 2, 3, 4, 5, 1]

Input: 5 4   1 2 2   2 1 2   2 3 3   5 4 2
Output: null

Input: 5 3    2 1 2   2 3 3   5 4 2
Output: [5, 4, 2, 3, 1]
*/


/*======== Results for connectedComponents() =========*/
/*
Input: 5 0
Output: 5

Input: 5 4    1 2 2    2 1 2    3 4 1    4 3 1
Output: 3

Input: 3 4    1 2 2    2 1 2    2 3 1    3 2 1
Output: 1
 */