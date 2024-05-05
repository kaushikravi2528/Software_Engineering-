import edu.princeton.cs.algs4.*;

import java.util.Arrays;

public class KruskalMST_Dataset {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;

    private double weight;                        // weight of MST
    private final Queue<Edge> mst = new Queue<>();  // edges in MST

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     *
     * @param G the edge-weighted graph
     */
    public KruskalMST_Dataset(EdgeWeightedGraph G) {

        // create array of edges, sorted by weight
        Edge[] edges = new Edge[G.E()];
        int t = 0;
        for (Edge e : G.edges()) {
            edges[t++] = e;
        }
        Arrays.sort(edges);

        // run greedy algorithm
        UF uf = new UF(G.V());
        for (int i = 0; i < G.E() && mst.size() < G.V() - 1; i++) {
            Edge e = edges[i];
            int v = e.either();
            int w = e.other(v);

            // v-w does not create a cycle
            if (uf.find(v) != uf.find(w)) {
                uf.union(v, w);     // merge v and w components
                mst.enqueue(e);     // add edge e to mst
                weight += e.weight();
            }
        }

        // check optimality conditions
        assert check(G);
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     *
     * @return the edges in a minimum spanning tree (or forest) as
     * an iterable of edges
     */
    public Iterable<Edge> edges() {
        return mst;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     *
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() {
        return weight;
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph G) {

        // check total weight
        double total = 0.0;
        for (Edge e : edges()) {
            total += e.weight();
        }
        if (Math.abs(total - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(G.V());
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.find(v) == uf.find(w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Edge e : G.edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.find(v) != uf.find(w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : edges()) {

            // all edges in MST except e
            uf = new UF(G.V());
            for (Edge f : mst) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }

            // check that e is min weight edge in crossing cut
            for (Edge f : G.edges()) {
                int x = f.either(), y = f.other(x);
                if (uf.find(x) != uf.find(y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }


    /**
     * Unit tests the {@code KruskalMST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        float x, y, z;
        {
            String input = "C:\\java\\First Programs\\src\\artists_fb.txt";
            In in = new In(input);
            EdgeWeightedGraph G = new EdgeWeightedGraph(in);
            KruskalMST mst = new KruskalMST(G);
            for (Edge e : mst.edges()) {
                StdOut.println(e);
            }
            StdOut.printf("%.5f\n", mst.weight());
            x = (float) mst.weight();
        }
        {
            String input = "C:\\java\\First Programs\\src\\athletes_fb.txt";
            In in = new In(input);
            EdgeWeightedGraph G = new EdgeWeightedGraph(in);
            KruskalMST mst = new KruskalMST(G);
            for (Edge e : mst.edges()) {
                StdOut.println(e);
            }
            StdOut.printf("%.5f\n", mst.weight());
            y = (float) mst.weight();
        }
        {
            String input = "C:\\java\\First Programs\\src\\company_fb.txt";
            In in = new In(input);
            EdgeWeightedGraph G = new EdgeWeightedGraph(in);
            KruskalMST mst = new KruskalMST(G);
            for (Edge e : mst.edges()) {
                StdOut.println(e);
            }
            StdOut.printf("%.5f\n", mst.weight());
            z = (float) mst.weight();
        }
        System.out.println("Mean = " + (x + y + z) / 3);
        float[] arr = {x , y , z};
        sort(arr);
        System.out.println("Median = " + arr[arr.length / 2]);
        System.out.println("Minimum = " + arr[0]);
        System.out.println("Maximum = " + arr[arr.length-1]);
    }



    static void sort(float[] arr) {
        int n = arr.length;
        float temp;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (arr[j - 1] > arr[j]) {
                    //swap elements
                    temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
}