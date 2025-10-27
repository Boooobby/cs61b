package core;

import entity.Avatar;
import entity.Entity;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class World {

    private TETile[][] world;
    private static final int minWidth = 60;
    private static final int minHeight = 60;
    private int width;
    private int height;

    private Entity avatar;

    private BSP bsp;
    private Random random;
    private long seed;

    public World(long seed) {
        this(seed, minWidth, minHeight);
    }

    public World(long seed, int w, int h) {
        random = new Random(seed);
        width = w;
        height = h;
        world = new TETile[w][h];
        bsp = new BSP(width, height, random);
    }

    public static void swap(TETile[][] tiles, Point p1, Point p2) {
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();
        TETile temp = tiles[x1][y1];
        tiles[x1][y1] = tiles[x2][y2];
        tiles[x2][y2] = temp;
    }

    public TETile[][] getWorld() {
        return world;
    }

    public void generateWorld() {
        world = bsp.generateTheWorld();
        avatar = new Avatar(3, bsp.getAvatarPlace(), random);
    }

}
