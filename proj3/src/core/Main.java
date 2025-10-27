package core;

import tileengine.TERenderer;

public class Main {
    public static void main(String[] args) {

        // build your own world!
        int width = 40;
        int height = 40;

        World w = new World(102002302L, width, height);
        w.generateWorld();

        w.runGame();

    }
}
