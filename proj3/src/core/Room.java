package core;

public class Room {

    private Point bottomLeft;
    private int width;
    private int height;
    private static final int minWidth = 4;
    private static final int minHeight = 4;

    public Room(int x, int y, int w, int h) {
        bottomLeft = new Point(x, y);
        width = w;
        height = h;
    }

    public Room(int x, int y) {
        this(x, y, minWidth, minHeight);
    }



}
