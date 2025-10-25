package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.Random;
import java.util.Timer;

public class BSP {

    private TETile[][] world;
    private int width, height;
    private Node root;
    private Random random;
    private int tooSmallFactor;

    Point avatarPlace = null;

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

    public BSP(int w, int h, Random r) {
        width = w;
        height = h;
        world = new TETile[w][h];
        fillTheWorldWithNothing();
        root = new Node(new Point(0, 0), w, h);
        random = r;
        tooSmallFactor = optionalFactor();
    }

    private int optionalFactor() {
        int avgSize = (width + height) / 2;
        if (avgSize < 40) {
            return 8;
        } else if (avgSize < 80) {
            return 12;
        } else {
            return 16;
        }
    }

    private void fillTheWorldWithNothing() {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
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
        /*
        int xLBound = node.bottomLeft.getX() + 1;
        int xUBound = node.bottomLeft.getX() + node.width / 2;
        int yLBound = node.bottomLeft.getY() + 1;
        int yUBound = node.bottomLeft.getY() + node.height / 2;

        int xp = RandomUtils.uniform(random, xLBound, xUBound);
        int yp = RandomUtils.uniform(random, yLBound, yUBound);
        int w = Math.max(RandomUtils.uniform(random, Room.minWidth, node.width - 1) - 4, Room.minWidth);
        int h = Math.max(RandomUtils.uniform(random, Room.minHeight, node.height - 1) - 4, Room.minHeight);
        */

        int maxWidth = node.width - 2;
        int maxHeight = node.height - 2;

        int w = RandomUtils.uniform(random, Room.minWidth, maxWidth);
        int h = RandomUtils.uniform(random, Room.minHeight, maxHeight);

        int minX = node.bottomLeft.getX() + 1;
        int maxX = node.bottomLeft.getX() + node.width - w;
        int minY = node.bottomLeft.getY() + 1;
        int maxY = node.bottomLeft.getY() + node.height - h;
        if (minX >= maxX || minY >= maxY) {
            return false;
        }

        int xp = RandomUtils.uniform(random, minX, maxX);
        int yp = RandomUtils.uniform(random, minY, maxY);

        if (failToCreate(xp, yp, w, h)) {
            return false;
        } else {
            node.room = new Room(xp, yp, w, h, random);
            fillWithFloor(xp, yp, w, h);
            return true;
        }
    }

    private boolean failToCreate(int xp, int yp, int w, int h) {
        if (xp + w >= width || yp + h >= height) {
            return true;
        }
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

        // randomly choose a path
        int x1 = Math.min(p1.getX(), p2.getX());
        int x2 = Math.max(p1.getX(), p2.getX());
        int y1 = Math.min(p1.getY(), p2.getY());
        int y2 = Math.max(p1.getY(), p2.getY());
        if (RandomUtils.bernoulli(random)) {
            fillTheXRoad(x1, x2, p1.getY());
            fillTheYRoad(y1, y2, p2.getX());
        } else {
            fillTheYRoad(y1, y2, p2.getX());
            fillTheXRoad(x1, x2, p1.getY());
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

    private void createWall() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (world[x][y] != Tileset.NOTHING) {
                    continue;
                }
                if (hasFloorNeighbors(x, y)) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
    }

    private boolean hasFloorNeighbors(int x, int y) {
        int[] dx = {-1, 0, 1};
        int[] dy = {-1, 0, 1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && i == j) {
                    continue;
                }
                int nx = x + dx[i];
                int ny = y + dy[j];
                if (nx < 0 || ny < 0 || nx >= width || ny >= height) {
                    continue;
                }

                if (world[nx][ny] == Tileset.FLOOR) {
                    return true;
                }
            }
        }

        return false;
    }

    private void setAvatar() {
        Point avatar = root.room.chooseAPoint();
        world[avatar.getX()][avatar.getY()] = Tileset.AVATAR;
        avatarPlace = avatar;
    }

    public Point getAvatarPlace() {
        return avatarPlace;
    }

    public TETile[][] generateTheWorld() {
        splitTheWorld(root);
        createWall();
        setAvatar();
        return world;
    }

}
