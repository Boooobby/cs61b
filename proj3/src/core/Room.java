package core;

import utils.RandomUtils;

import java.util.Random;

public class Room {

    private Point bottomLeft;
    private int width;
    private int height;
    public static final int minWidth = 2;
    public static final int minHeight = 2;

    public Room(int x, int y, int w, int h) {
        bottomLeft = new Point(x, y);
        width = w;
        height = h;
    }

    public Room(int x, int y) {
        this(x, y, minWidth, minHeight);
    }

    Point chooseAPoint() {
        int x = RandomUtils.uniform(new Random(), bottomLeft.getX(), bottomLeft.getX() + width);
        int y = RandomUtils.uniform(new Random(), bottomLeft.getY(), bottomLeft.getY() + height);
        return new Point(x, y);
    }

}
