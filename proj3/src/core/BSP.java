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

    public BSP(TETile[][] tiles) {
        world = tiles;
        root = new Node(new Point(0, 0), world.length, world[0].length);
    }

    private Node splitTheWorld(Node node) {
        if (isTooSmall(node)) {
            createRoom();
            return node;
        }

        if (node.width >= node.height) {
            // split by width
        } else {
            // split by height
        }

        node = connectTheRoom(node);
        return node;
    }

    private boolean isTooSmall(Node node) {
        return false;
    }

    private void createRoom() {

    }

    private Node connectTheRoom(Node node) {
        return null;
    }

    public TETile[][] generateTheWorld() {
        return null;
    }

}
