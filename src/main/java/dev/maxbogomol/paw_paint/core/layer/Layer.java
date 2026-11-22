package dev.maxbogomol.paw_paint.core.layer;

import dev.maxbogomol.paw_paint.core.color.ColorFormat;

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

    private TileMap createTileMap(ColorFormat colorFormat) {
        return new TileMap(TILE_SIZE, colorFormat.getBytesPerPixel());
    }
}
