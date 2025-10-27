package entity;

import core.Point;
import core.World;
import tileengine.TETile;

import java.util.Random;

public abstract class Entity {

    protected int health;
    protected Point place;
    protected Random random;

    public Entity(int health, Point place, Random random) {
        this.health = health;
        this.place = place;
        this.random = random;
    }

    public void tryMove(int dx, int dy, TETile[][] tiles) {
        if (canMove(dx, dy, tiles)) {
            Point newPlace = new Point(place.getX() + dx, place.getY() + dy);
            World.swap(tiles, place, newPlace);
        }
    }

    public abstract boolean canMove(int dx, int dy, TETile[][] tiles);

    public Point getPlace() {
        return place;
    }

}
