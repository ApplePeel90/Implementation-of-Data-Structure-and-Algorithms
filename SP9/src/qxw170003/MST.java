package qxw170003;// Starter code for SP9

import qxw170003.Graph.Vertex;
import qxw170003.Graph.Edge;
import qxw170003.Graph.GraphAlgorithm;
import qxw170003.Graph.Factory;
import qxw170003.Graph.Timer;

import qxw170003.BinaryHeap.Index;
import qxw170003.BinaryHeap.IndexedHeap;


import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.io.File;

public class MST extends GraphAlgorithm<MST.MSTVertex> {
    String algorithm;
    public long wmst;
    List<Edge> mst;

    /**
     * Constructor of MST
     * @param g
     */
    MST(Graph g) {
        super(g, new MSTVertex((Vertex) null));
    }

    public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {
        int dis;
        int index;
        boolean visited;
        MSTVertex parent;
        Vertex u;


        /**
         * Constructor of MSTVertex
         * @param u
         */
        MSTVertex(Vertex u) {
            this.dis = -1;
            this.index = -1;
            this.visited = false;
            this.parent = null;
            this.u = u;
        }

        MSTVertex(MSTVertex u) {  // for prim2
        }

        public MSTVertex make(Vertex u) {
            return new MSTVertex(u);
        }

        /**
         * Set index for current element
         * @param index
         */
        public void putIndex(int index) {
            this.index = index;
        }

        /**
         *
         * @return index of the current element
         */
        public int getIndex() {
            return this.index;
        }

        /**
         * Compare current element with other element with dis
         * @param other
         * @return 1 if current.dis is greater, -1 otherwise
         */
        public int compareTo(MSTVertex other) {
            return this.dis > other.dis ? 1 : -1;
        }
    }

    public long kruskal() {
        algorithm = "Kruskal";
        Edge[] edgeArray = g.getEdgeArray();
        mst = new LinkedList<>();
        wmst = 0;
        return wmst;
    }

    /**
     * Find MST of a connected, weighted and undirected graph
     * @param s
     * @return the sum of weights on the MST
     */
    public long prim3(Vertex s) {
        algorithm = "indexed heaps";
        mst = new LinkedList<>();
        wmst = 0;
        IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());

        for(Graph.AdjList adjList : this.g.adjList){
            get(adjList.vertex).visited = false;
            get(adjList.vertex).parent = null;
            get(adjList.vertex).dis = Integer.MAX_VALUE;
        }
        get(s).dis = 0;

        for(Graph.AdjList adjList : this.g.adjList){
            q.add(get(adjList.vertex));
        }

        while(!q.isEmpty()){
            MSTVertex curr = q.poll();
            curr.visited = true;
            wmst += curr.dis;
            for(Edge e : this.g.incident(curr.u)){
                MSTVertex v = get(e.otherEnd(curr.u));
                if(v.visited == false && e.weight < v.dis){
                    v.dis = e.weight;
                    v.parent = curr;
                    q.decreaseKey(v);
                }
            }
        }
        return wmst;
    }

    public long prim2(Vertex s) {
        algorithm = "PriorityQueue<Vertex>";
        mst = new LinkedList<>();
        wmst = 0;
        PriorityQueue<MSTVertex> q = new PriorityQueue<>();
        return wmst;
    }

    public long prim1(Vertex s) {
        algorithm = "PriorityQueue<Edge>";
        mst = new LinkedList<>();
        wmst = 0;
        PriorityQueue<Edge> q = new PriorityQueue<>();



        return wmst;
    }

    public static MST mst(Graph g, Vertex s, int choice) {
        MST m = new MST(g);
        switch (choice) {
            case 0:
                m.kruskal();
                break;
            case 1:
                m.prim1(s);
                break;
            case 2:
                m.prim2(s);
                break;
            case 3:
                m.prim3(s);
                break;
            default:
                // Boruvka to be implemented next
                break;
        }
        return m;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        int choice = 3;  // prim3
        if (args.length == 0 || args[0].equals("-")) {
            in = new Scanner(System.in);
        } else {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        }

        if (args.length > 1) {
            choice = Integer.parseInt(args[1]);
        }

        Graph g = Graph.readGraph(in);
        Vertex s = g.getVertex(1);

        Timer timer = new Timer();
        MST m = mst(g, s, choice);
        System.out.println(m.algorithm + "\n" + m.wmst);
        System.out.println(timer.end());
    }
}
