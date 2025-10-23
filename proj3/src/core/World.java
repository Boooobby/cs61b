package core;

import tileengine.TETile;

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
        
    }

}
