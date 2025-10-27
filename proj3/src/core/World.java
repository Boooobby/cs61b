package core;

import edu.princeton.cs.algs4.StdDraw;
import entity.Avatar;
import entity.Entity;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class World {

    private TETile[][] world;
    private static final int minWidth = 60;
    private static final int minHeight = 60;
    private int width;
    private int height;
    private static final int xOffset = 2;
    private static final int yOffset = 2;

    private Entity avatar;

    private BSP bsp;
    private Random random;
    private long seed;

    private TERenderer ter;

    public World(long seed) {
        this(seed, minWidth, minHeight);
    }

    public World(long seed, int w, int h) {
        random = new Random(seed);
        width = w;
        height = h;
        world = new TETile[w][h];
        bsp = new BSP(width, height, random);
        ter = new TERenderer();
        ter.initialize(width + xOffset * 2, height + yOffset * 2, xOffset, yOffset);
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

    private void updateWorld() {
        if (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();
            switch (key) {
                case 'a':
                    avatar.tryMove(-1, 0, world);
                    break;
                case 'w':
                    avatar.tryMove(0, 1, world);
                    break;
                case 's':
                    avatar.tryMove(0, -1, world);
                    break;
                case 'd':
                    avatar.tryMove(1, 0, world);
                    break;
                default:
                    break;
            }
        }
    }

    private void renderWorld() {
        ter.drawTiles(world);
        StdDraw.show();
    }

    private void takeARound() {
        updateWorld();
        renderWorld();
    }

    public void runGame() {
        while (!isGameOver()) {
            takeARound();
        }
    }

    private boolean isGameOver() {
        return avatar.getHealth() <= 0;
    }

}
