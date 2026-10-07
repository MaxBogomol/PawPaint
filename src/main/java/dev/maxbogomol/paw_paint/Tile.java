package dev.maxbogomol.paw_paint;

public class Tile {
    private final byte[] data;
    private final int step;
    private final int bytesPerPixel;

    public Tile(int size, int bytesPerPixel) {
        this.data = new byte[size * size * bytesPerPixel];
        this.step = size * bytesPerPixel;
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
