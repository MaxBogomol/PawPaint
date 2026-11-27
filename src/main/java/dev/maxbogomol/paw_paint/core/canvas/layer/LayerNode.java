package dev.maxbogomol.paw_paint.core.canvas.layer;

import dev.maxbogomol.paw_paint.PawPaint;
import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.blend.BlendContext;
import dev.maxbogomol.paw_paint.core.color.blend.BlendMode;
import dev.maxbogomol.paw_paint.core.color.blend.BlendModes;
import dev.maxbogomol.paw_paint.core.color.compose.ComposeMode;
import dev.maxbogomol.paw_paint.core.color.compose.ComposeModes;
import dev.maxbogomol.paw_paint.core.color.format.ColorFormat;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpaces;

public abstract class LayerNode {
    protected ColorFormat colorFormat;
    protected TileMap tileMap;

    protected BlendMode blendMode = BlendModes.NORMAL;
    protected ComposeMode composeMode = ComposeModes.SRC_OVER;
    protected ColorSpace colorSpace = ColorSpaces.LINEAR_RGB;

    protected String name;
    protected float opacity = 1f;
    protected boolean visible = true;

    public LayerNode(ColorFormat colorFormat, String name) {
        this.colorFormat = colorFormat;
        this.tileMap =  createTileMap(colorFormat);
        this.name = name;
    }

    public ColorFormat getColorFormat() {
        return colorFormat;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public void setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
    }

    public void setComposeMode(ComposeMode composeMode) {
        this.composeMode = composeMode;
    }

    public void setColorSpace(ColorSpace colorSpace) {
        this.colorSpace = colorSpace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public BlendMode getBlendMode() {
        return blendMode;
    }

    public ComposeMode getComposeMode() {
        return composeMode;
    }

    public ColorSpace getColorSpace() {
        return colorSpace;
    }

    public String getName() {
        return name;
    }

    public float getOpacity() {
        return opacity;
    }

    public boolean getVisible() {
        return visible;
    }

    private TileMap createTileMap(ColorFormat colorFormat) {
        return new TileMap(colorFormat.getBytesPerPixel());
    }

    public void setPixel(int x, int y, Color color, byte[] pixelData) {
        colorFormat.encode(color, pixelData);
        tileMap.setPixel(x, y, pixelData);
    }

    public void getPixel(int x, int y, Color color, byte[] pixelData) {
        tileMap.getPixel(x, y, pixelData);
        colorFormat.decode(color, pixelData);
    }

    public abstract void blend(LayerNode node);

    public void blend(LayerNode node, int offsetX, int offsetY) {
        BlendContext c = PawPaint.blendContext;
        LayerBoundsContext lc = PawPaint.layerBoundsContext;
        this.getTileMap().checkBounds(lc.src);
        node.getTileMap().checkBounds(lc.dest);
        lc.src.bound(lc.dest);
        int minX = lc.src.minX * Tile.TILE_SIZE + offsetX;
        int minY = lc.src.minY * Tile.TILE_SIZE + offsetY;
        int maxX = (lc.src.maxX + 1) * Tile.TILE_SIZE + offsetX;
        int maxY = (lc.src.maxY + 1) * Tile.TILE_SIZE + offsetY;

        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                this.getPixel(x - offsetX, y - offsetY, c.src, c.pixelData);
                node.getPixel(x, y, c.dest, c.pixelData);
                c.src.a = c.src.a * opacity;
                blendMode.blend(c, composeMode, colorSpace);
                node.setPixel(x, y, c.out, c.pixelData);
            }
        }
    }
}
