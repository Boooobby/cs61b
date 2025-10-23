package core;

import utils.RandomUtils;

import java.util.Random;

public class Room {

    private Point bottomLeft;
    private int width;
    private int height;
    public static final int minWidth = 2;
    public static final int minHeight = 2;
    private Random random;

    public Room(int x, int y, int w, int h, Random r) {
        bottomLeft = new Point(x, y);
        width = w;
        height = h;
        random = r;
    }

    public Room(int x, int y, Random r) {
        this(x, y, minWidth, minHeight, r);
    }

    Point chooseAPoint() {
        int x = RandomUtils.uniform(random, bottomLeft.getX(), bottomLeft.getX() + width);
        int y = RandomUtils.uniform(random, bottomLeft.getY(), bottomLeft.getY() + height);
        return new Point(x, y);
    }

}
