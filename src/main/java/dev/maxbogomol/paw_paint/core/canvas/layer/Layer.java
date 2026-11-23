package dev.maxbogomol.paw_paint.core.canvas.layer;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.Colors;
import dev.maxbogomol.paw_paint.core.color.format.ColorFormat;

public class Layer extends LayerNode {
    private static final int TILE_SIZE = 64;

    private TileMap tileMap;
    private ColorFormat colorFormat;

    private int width = 0;
    private int height = 0;
    private int x = 0;
    private int y = 0;

    public Layer(ColorFormat colorFormat, int width, int height, String name) {
        this.colorFormat = colorFormat;
        this.tileMap =  createTileMap(colorFormat);
        this.width = width;
        this.height = height;
        this.name = name;
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

    public void setPixel(int x, int y, Color color, byte[] pixelData) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            colorFormat.encode(color, pixelData);
            tileMap.setPixel(x, y, pixelData);
        }
    }

    public void getPixel(int x, int y, Color color, byte[] pixelData) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tileMap.getPixel(x, y, pixelData);
            colorFormat.decode(color, pixelData);
        } else {
            Colors.BLACK.copy(color);
        }
    }

    private TileMap createTileMap(ColorFormat colorFormat) {
        return new TileMap(TILE_SIZE, colorFormat.getBytesPerPixel());
    }
}
