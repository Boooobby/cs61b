package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.Random;

public class BSP {

    private TETile[][] world;
    private Node root;
    private Random random;

    private static final int tooSmallFactor = 8;

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
        if (node.width <= tooSmallFactor || node.height <= tooSmallFactor) {
            return true;
        }
        // can be more flexible
        return false;
    }

    private boolean createRoom(Node node) {
        int xLBound = node.bottomLeft.getX() + 1;
        int xUBound = node.bottomLeft.getX() + node.width - 1;
        int yLBound = node.bottomLeft.getY() + 1;
        int yUBound = node.bottomLeft.getY() + node.height - 1;

        int xp = RandomUtils.uniform(random, xLBound, xUBound);
        int yp = RandomUtils.uniform(random, yLBound, yUBound);
        int w = RandomUtils.uniform(random, Room.minWidth, node.width - 1);
        int h = RandomUtils.uniform(random, Room.minHeight, node.height - 1);

        if (failToCreate(xp, yp, w, h)) {
            return false;
        } else {
            node.room = new Room(xp, yp, w, h, random);
            fillWithFloor(xp, yp, w, h);
            return true;
        }
    }

    private boolean failToCreate(int xp, int yp, int w, int h) {
        for (int i = xp; i < xp + w; i++) {
            for (int j = yp; j < yp + h; j++) {
                if (world[i][j] != Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }

    private void fillWithFloor(int xp, int yp, int w, int h) {
        for (int i = xp; i < xp + w; i++) {
            for (int j = yp; j < yp + h; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }

    // connect two children of node
    private Node connectTheRoom(Node node) {
        Point p1 = node.left.room.chooseAPoint();
        Point p2 = node.right.room.chooseAPoint();

        // x-axis
        if (p1.getX() < p2.getX()) {
            fillTheYRoad(p1.getX(), p2.getX(), p1.getY());
        } else {
            fillTheXRoad(p2.getX(), p1.getX(), p1.getY());
        }

        // y-axis
        if (p1.getY() < p2.getY()) {
            fillTheYRoad(p1.getY(), p2.getY(), p2.getX());
        } else {
            fillTheYRoad(p2.getY(), p1.getY(), p2.getX());
        }

        // choose a room from one child
        if (RandomUtils.bernoulli(random)) {
            node.room = node.left.room;
        } else {
            node.room = node.right.room;
        }
        return node;
    }

    private void fillTheXRoad(int x1, int x2, int y) {
        for (int i = x1; i <= x2; i++) {
            world[i][y] = Tileset.FLOOR;
        }
    }

    private void fillTheYRoad(int y1, int y2, int x) {
        for (int i = y1; i <= y2; i++) {
            world[x][i] = Tileset.FLOOR;
        }
    }

    public TETile[][] generateTheWorld() {
        splitTheWorld(root);
        return world;
    }

}
