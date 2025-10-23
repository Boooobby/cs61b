package core;

import tileengine.TETile;

public class BSP {

    private TETile[][] world;
    private Node root;

    private class Node {
        Point bottomLeft;
        int width, height;
        Node left, right;

        Node(Point p, int w, int h) {
            bottomLeft = p;
            width = w;
            height = h;
            left = right = null;
        }
    }

    public BSP(int width, int height) {
        world = new TETile[width][height];
        root = new Node(new Point(0, 0), width, height);
    }

    private Node splitTheWorld(Node node) {
        if (isTooSmall(node)) {
            while (!createRoom(node));
            return node;
        }

        if (node.width >= node.height) {
            // split by width and split recursively
        } else {
            // split by height and split recursively
        }

        node = connectTheRoom(node);
        return node;
    }

    private boolean isTooSmall(Node node) {
        return false;
    }

    private boolean createRoom(Node node) {
        return false;
    }

    // connect two children of node
    private Node connectTheRoom(Node node) {
        return null;
    }

    public TETile[][] generateTheWorld() {
        return null;
    }

}
