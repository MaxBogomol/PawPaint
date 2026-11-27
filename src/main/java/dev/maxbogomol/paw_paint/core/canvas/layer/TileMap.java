package dev.maxbogomol.paw_paint.core.canvas.layer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TileMap {
    private final Map<Long, Tile> tiles = new HashMap<>();
    private final int bytesPerPixel;

    public TileMap(int bytesPerPixel) {
        this.bytesPerPixel = bytesPerPixel;
    }

    public static long getTileKey(int x, int y) {
        return ((long) x << 32) | (Integer.toUnsignedLong(y));
    }

    public static int getTileX(long key) {
        return (int) (key >> 32);
    }

    public static int getTileY(long key) {
        return (int) key;
    }

    public void setPixel(int x, int y, byte[] pixelData) {
        int tileX = Math.floorDiv(x, Tile.TILE_SIZE);
        int tileY = Math.floorDiv(y, Tile.TILE_SIZE);
        int localX = Math.floorMod(x, Tile.TILE_SIZE);
        int localY = Math.floorMod(y, Tile.TILE_SIZE);
        long key = getTileKey(tileX, tileY);
        Tile tile = tiles.computeIfAbsent(key, k -> new Tile(bytesPerPixel));
        tile.setPixel(localX, localY, pixelData);
    }

    public void getPixel(int x, int y, byte[] pixelData) {
        int tileX = Math.floorDiv(x, Tile.TILE_SIZE);
        int tileY = Math.floorDiv(y, Tile.TILE_SIZE);
        int localX = Math.floorMod(x, Tile.TILE_SIZE);
        int localY = Math.floorMod(y, Tile.TILE_SIZE);
        long key = getTileKey(tileX, tileY);
        Tile tile = tiles.get(key);
        if (tile == null) {
            Arrays.fill(pixelData, (byte) 0);
        } else {
            tile.getPixel(localX, localY, pixelData);
        }
    }

    public void checkBounds(LayerBounds bounds) {
        if (tiles.isEmpty()) {
            bounds.minX = bounds.minY = 0;
            bounds.maxX = bounds.maxY = 0;
        } else {
            bounds.minX = bounds.minY = Integer.MAX_VALUE;
            bounds.maxX = bounds.maxY = Integer.MIN_VALUE;

            for (long key : tiles.keySet()) {
                int tileX = getTileX(key);
                int tileY = getTileY(key);

                if (tileX < bounds.minX) bounds.minX = tileX;
                if (tileX > bounds.maxX) bounds.maxX = tileX;
                if (tileY < bounds.minY) bounds.minY = tileY;
                if (tileY > bounds.maxY) bounds.maxY = tileY;
            }
        }
    }
}
