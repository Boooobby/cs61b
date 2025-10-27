package core;

import tileengine.TERenderer;

public class Main {
    public static void main(String[] args) {

        // build your own world!
        int width = 30;
        int height = 30;

        World w = new World(102002302L, width, height);
        w.generateWorld();

        w.runGame();

    }
}
