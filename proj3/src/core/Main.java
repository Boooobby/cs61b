package core;

import tileengine.TERenderer;

public class Main {
    public static void main(String[] args) {

        // build your own world!
        int width = 50;
        int height = 50;

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        World w = new World(102002302L, width, height);
        w.generateWorld();

        ter.renderFrame(w.getWorld());

    }
}
