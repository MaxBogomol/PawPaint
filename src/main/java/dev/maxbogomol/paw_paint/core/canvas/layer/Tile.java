package dev.maxbogomol.paw_paint.core.canvas.layer;

public class Tile {
    public static final int TILE_SIZE = 64;

    private final byte[] data;
    private final int step;
    private final int bytesPerPixel;

    public Tile(int bytesPerPixel) {
        this.data = new byte[TILE_SIZE * TILE_SIZE * bytesPerPixel];
        this.step = TILE_SIZE * bytesPerPixel;
        this.bytesPerPixel = bytesPerPixel;
    }

    public void setPixel(int x, int y, byte[] pixelData) {
        int id = (x * bytesPerPixel) + (y * step);
        System.arraycopy(pixelData, 0, data, id, bytesPerPixel);
    }

    public void getPixel(int x, int y, byte[] pixelData) {
        int id = (x * bytesPerPixel) + (y * step);
        System.arraycopy(data, id, pixelData, 0, bytesPerPixel);
    }
}
