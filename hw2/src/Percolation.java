import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    // TODO: Add any necessary instance variables.
    private final WeightedQuickUnionUF uf;
    private final boolean[][] g;
    private final int size;
    private int openSites;
    private final int virtualTop;
    private final int virtualBottom;

    public Percolation(int N) {
        // TODO: Fill in this constructor.
        size = N;
        openSites = 0;
        g = new boolean[N][N];
        uf = new WeightedQuickUnionUF(N * N + 2);
        virtualTop = N * N;
        virtualBottom = N * N + 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                g[i][j] = false;
            }
        }
    }

    public void open(int row, int col) {
        // TODO: Fill in this method.
        if (isOpen(row, col)) {
            return;
        }
        openSites++;
        g[row][col] = true;
        unionAround(row, col);

        int n = coordinateToNum(row, col);
        if (row == 0) {
            uf.union(n, virtualTop);
        }
        if (row == size - 1) {
            uf.union(n, virtualBottom);
        }
    }

    public boolean isOpen(int row, int col) {
        // TODO: Fill in this method.
        if (!isValidCoordinate(row, col)) {
            throw new IllegalArgumentException();
        }
        return g[row][col];
    }

    public boolean isFull(int row, int col) {
        // TODO: Fill in this method.
        if (isOpen(row, col)) {
            int n = coordinateToNum(row, col);
            return uf.connected(virtualTop, n);
        }
        return false;
    }

    public int numberOfOpenSites() {
        // TODO: Fill in this method.
        return openSites;
    }

    public boolean percolates() {
        // TODO: Fill in this method.
        return uf.connected(virtualTop, virtualBottom);
    }

    // TODO: Add any useful helper methods (we highly recommend this!).
    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private int coordinateToNum(int row, int col) {
        return row * size + col;
    }

    private void unionAround(int row, int col) {
        int n1 = coordinateToNum(row, col);

        int[] drow = {1, -1, 0, 0};
        int[] dcol = {0, 0, 1, -1};
        for (int i = 0; i < 4; i++) {
            int x = drow[i] + row, y = dcol[i] + col;
            int n2 = coordinateToNum(x, y);
            if (isValidCoordinate(x, y) && isOpen(x, y) && !uf.connected(n1, n2)) {
                uf.union(n1, n2);
            }
        }
    }

    // TODO: Remove all TODO comments before submitting.

}
