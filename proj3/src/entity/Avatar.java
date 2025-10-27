package entity;

import core.Point;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class Avatar extends Entity {

    public Avatar(int health, Point place, Random random) {
        super(health, place, random);
    }

    @Override
    public boolean canMove(int dx, int dy, TETile[][] tiles) {
        int x = place.getX();
        int y = place.getY();
        int nx = x + dx;
        int ny = y + dy;

        return tiles[nx][ny] == Tileset.FLOOR;
    }
}
