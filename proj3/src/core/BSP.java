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

    private Node splitTheWorld() {
        return null;
    }

    private boolean isTooSmall(Node node) {
        return false;
    }

    private void createRoom() {

    }

    private void connectTheRoom() {

    }

    public TETile[][] generateTheWorld() {
        return null;
    }

}
