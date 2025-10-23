package core;

import tileengine.TETile;

import java.util.Random;

public class BSP {

    private TETile[][] world;
    private Node root;
    private Random random;

    private class Node {
        Point bottomLeft;
        int width, height;
        Node left, right;
        Room room;

        Node(Point p, int w, int h) {
            bottomLeft = p;
            width = w;
            height = h;
            left = right = null;
            room = null;
        }
    }

    public BSP(int width, int height, Random r) {
        world = new TETile[width][height];
        root = new Node(new Point(0, 0), width, height);
        random = r;
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
