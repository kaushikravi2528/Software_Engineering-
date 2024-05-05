import edu.princeton.cs.algs4.*;

public class PrimMST_Dataset{
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;

    private final Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private final double[] distTo;      // distTo[v] = weight of shortest such edge
    private final boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private final IndexMinPQ<Double> pq;

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    public PrimMST_Dataset(EdgeWeightedGraph G) {
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<>(G.V());
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;

        for (int v = 0; v < G.V(); v++)      // run from each vertex to find
            if (!marked[v]) prim(G, v);      // minimum spanning forest

        // check optimality conditions
        assert check(G);
    }

    // run Prime's algorithm in graph G, starting from vertex s
    private void prim(EdgeWeightedGraph G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    // scan vertex v
    private void scan(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;         // v-w is obsolete edge
            if (e.weight() < distTo[w]) {
                distTo[w] = e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     * @return the edges in a minimum spanning tree (or forest) as
     *    an iterable of edges
     */
    public Iterable<Edge> edges() {
        Queue<Edge> mst = new Queue<>();
        for (Edge e : edgeTo) {
            if (e != null) {
                mst.enqueue(e);
            }
        }
        return mst;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() {
        double weight = 0.0;
        for (Edge e : edges())
            weight += e.weight();
        return weight;
    }


    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph G) {

        // check weight
        double totalWeight = 0.0;
        for (Edge e : edges()) {
            totalWeight += e.weight();
        }
        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
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
            for (Edge f : edges()) {
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
     * Unit tests the {@code PrimMST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        float x, y, z;
        {
            String input = "C:\\java\\First Programs\\src\\artists_fb.txt";
            In in = new In(input);
            EdgeWeightedGraph G = new EdgeWeightedGraph(in);
            PrimMST mst = new PrimMST(G);
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
            PrimMST mst = new PrimMST(G);
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
            PrimMST mst = new PrimMST(G);
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
