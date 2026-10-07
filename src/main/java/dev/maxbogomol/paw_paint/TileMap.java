package dev.maxbogomol.paw_paint;

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
        int tileX = x / tileSize;
        int tileY = y / tileSize;
        int localX = x % tileSize;
        int localY = y % tileSize;
        long key = getTileKey(tileX, tileY);
        Tile tile = tiles.computeIfAbsent(key, k -> new Tile(tileSize, bytesPerPixel));
        tile.setPixel(localX, localY, pixelData);
    }

    public void getPixel(int x, int y, byte[] pixelData) {
        int tileX = x / tileSize;
        int tileY = y / tileSize;
        int localX = x % tileSize;
        int localY = y % tileSize;
        long key = getTileKey(tileX, tileY);
        Tile tile = tiles.get(key);
        if (tile == null) {
            Arrays.fill(pixelData, (byte) 0);
        } else {
            tile.getPixel(localX, localY, pixelData);
        }
    }
}
