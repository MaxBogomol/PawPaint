package dev.maxbogomol.paw_paint.core.layer;

import dev.maxbogomol.paw_paint.core.color.ColorFormat;
import dev.maxbogomol.paw_paint.core.color.Colors;
import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public class Layer {
    private static final int TILE_SIZE = 64;

    private TileMap tileMap;
    private ColorFormat colorFormat;

    private int width = 0;
    private int height = 0;
    private int x = 0;
    private int y = 0;
    private float opacity = 1f;

    public Layer(ColorFormat colorFormat, int width, int height) {
        this.colorFormat = colorFormat;
        this.tileMap =  createTileMap(colorFormat);
        this.width = width;
        this.height = height;
    }

    public ColorFormat getColorFormat() {
        return colorFormat;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPixel(int x, int y, float[] color, byte[] pixelData) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            colorFormat.encode(color, pixelData);
            tileMap.setPixel(x, y, pixelData);
        }
    }

    public void getPixel(int x, int y, float[] color, byte[] pixelData) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tileMap.getPixel(x, y, pixelData);
            colorFormat.decode(color, pixelData);
        } else {
            ColorUtil.copyColor(Colors.ALPHA, color);
        }
    }

    private TileMap createTileMap(ColorFormat colorFormat) {
        return new TileMap(TILE_SIZE, colorFormat.getBytesPerPixel());
    }
}
