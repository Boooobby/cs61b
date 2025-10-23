package core;

import tileengine.TERenderer;

public class Main {
    public static void main(String[] args) {

        // build your own world!
        TERenderer ter = new TERenderer();
        ter.initialize(50, 50);

        World w = new World(102002302L, 50, 50);
        w.generateWorld();

        ter.renderFrame(w.getWorld());

    }
}
