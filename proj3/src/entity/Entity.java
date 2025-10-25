package entity;

import core.Point;
import core.World;
import tileengine.TETile;

public abstract class Entity {

    private int health;
    private Point place;

    public void tryMove(int dx, int dy, TETile[][] tiles) {
        if (canMove(dx, dy, tiles)) {
            Point newPlace = new Point(place.getX() + dx, place.getY() + dy);
            World.swap(tiles, place, newPlace);
        }
    }

    public abstract boolean canMove(int dx, int dy, TETile[][] tiles);

}
