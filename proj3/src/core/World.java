package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class World {

    private TETile[][] world;
    private static final int minWidth = 60;
    private static final int minHeight = 60;
    private int width;
    private int height;

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

    public TETile[][] getWorld() {
        return world;
    }

    public void generateWorld() {
        fillTheWorldWithNothing();
        world = bsp.generateTheWorld();
    }

    public void fillTheWorldWithNothing() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

}
