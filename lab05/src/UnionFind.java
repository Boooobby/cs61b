import java.util.Arrays;

public class UnionFind {
    // TODO: Instance variables
    private int[] p;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        // TODO: YOUR CODE HERE
        p = new int[N];
        Arrays.fill(p, -1);
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        // TODO: YOUR CODE HERE
        return -p[find(v)];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        // TODO: YOUR CODE HERE
        return p[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO: YOUR CODE HERE
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        // TODO: YOUR CODE HERE
        if (v >= p.length) {
            throw new IllegalArgumentException();
        }
        if (p[v] >= 0) {
            p[v] = find(p[v]);
        } else {
            return v;
        }
        return p[v];
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie-break by connecting V1's
       root to V2's root. Union-ing an item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        // TODO: YOUR CODE HERE
        if (connected(v1, v2)) {
            return;
        }

        int s1 = -p[find(v1)], s2 = -p[find(v2)];
        if (s1 > s2) {
            p[find(v1)] += p[find(v2)];
            p[find(v2)] = find(v1);
        } else {
            p[find(v2)] += p[find(v1)];
            p[find(v1)] = find(v2);
        }
    }

}
