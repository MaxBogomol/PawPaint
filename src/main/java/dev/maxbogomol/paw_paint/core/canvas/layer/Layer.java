package dev.maxbogomol.paw_paint.core.canvas.layer;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.Colors;
import dev.maxbogomol.paw_paint.core.color.format.ColorFormat;

public class Layer extends LayerNode {
    private int width;
    private int height;
    private int x = 0;
    private int y = 0;

    public Layer(ColorFormat colorFormat, int width, int height, String name) {
        super(colorFormat, name);
        this.width = width;
        this.height = height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void blend(LayerNode node) {
        blend(node, this.x, this.y);
    }

    public void setTileMapPixel(int x, int y, Color color, byte[] pixelData) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            super.setPixel(x, y, color, pixelData);
        }
    }

    public void getTileMapPixel(int x, int y, Color color, byte[] pixelData) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            super.setPixel(x, y, color, pixelData);
        } else {
            Colors.BLACK.copy(color);
        }
    }
}
