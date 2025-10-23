package core;

import tileengine.TETile;
import utils.RandomUtils;

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
            int newWidth = (int)(RandomUtils.uniform(random, 0.4, 0.6) * node.width);
            Point rightChild = new Point(node.bottomLeft.getX() + newWidth, node.bottomLeft.getY());

            node.left = new Node(node.bottomLeft, newWidth, node.height);
            node.right = new Node(rightChild, node.width - newWidth, node.height);

            node.left = splitTheWorld(node.left);
            node.right = splitTheWorld(node.right);
        } else {
            // split by height and split recursively
            int newHeight = (int)(RandomUtils.uniform(random, 0.4, 0.6) * node.height);
            Point rightChild = new Point(node.bottomLeft.getX(), node.bottomLeft.getY() + newHeight);

            node.left = new Node(node.bottomLeft, node.width, newHeight);
            node.right = new Node(rightChild, node.width, node.height - newHeight);

            node.left = splitTheWorld(node.left);
            node.right = splitTheWorld(node.right);
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
