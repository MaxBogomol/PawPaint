package dev.maxbogomol.paw_paint.core.canvas.layer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TileMap {
    private final Map<Long, Tile> tiles = new HashMap<>();
    private final int tileSize;
    private final int bytesPerPixel;

    public TileMap(int tileSize, int bytesPerPixel) {
        this.tileSize = tileSize;
        this.bytesPerPixel = bytesPerPixel;
    }

    private long getTileKey(int x, int y) {
        return ((long) x << 32) | (Integer.toUnsignedLong(y));
    }

    public void setPixel(int x, int y, byte[] pixelData) {
        int tileX = Math.floorDiv(x, tileSize);
        int tileY = Math.floorDiv(y, tileSize);
        int localX = Math.floorMod(x, tileSize);
        int localY = Math.floorMod(y, tileSize);
        long key = getTileKey(tileX, tileY);
        Tile tile = tiles.computeIfAbsent(key, k -> new Tile(tileSize, bytesPerPixel));
        tile.setPixel(localX, localY, pixelData);
    }

    public void getPixel(int x, int y, byte[] pixelData) {
        int tileX = Math.floorDiv(x, tileSize);
        int tileY = Math.floorDiv(y, tileSize);
        int localX = Math.floorMod(x, tileSize);
        int localY = Math.floorMod(y, tileSize);
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
                int tileX = (int) (key >> 32);
                int tileY = (int) key;

                if (tileX < bounds.minX) bounds.minX = tileX;
                if (tileX > bounds.maxX) bounds.maxX = tileX;
                if (tileY < bounds.minY) bounds.minY = tileY;
                if (tileY > bounds.maxY) bounds.maxY = tileY;
            }
        }
    }

    public int getTileSize() {
        return tileSize;
    }
}
