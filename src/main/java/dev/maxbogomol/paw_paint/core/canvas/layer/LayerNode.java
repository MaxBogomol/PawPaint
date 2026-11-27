package dev.maxbogomol.paw_paint.core.canvas.layer;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.blend.BlendMode;
import dev.maxbogomol.paw_paint.core.color.blend.BlendModes;
import dev.maxbogomol.paw_paint.core.color.compose.ComposeMode;
import dev.maxbogomol.paw_paint.core.color.compose.ComposeModes;
import dev.maxbogomol.paw_paint.core.color.format.ColorFormat;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpaces;

public abstract class LayerNode {
    protected static final int TILE_SIZE = 64;

    protected byte[] pixelData;
    protected Color color = Color.create();
    protected Color colorSrc = Color.create();
    protected Color colorDest = Color.create();
    protected Color colorSrcSpace = Color.create();
    protected Color colorDestSpace = Color.create();
    protected LayerBounds boundsSrc = LayerBounds.create();
    protected LayerBounds boundsDest = LayerBounds.create();

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
        return new TileMap(Layer.TILE_SIZE, colorFormat.getBytesPerPixel());
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
        getTileMap().checkBounds(boundsSrc);
        node.getTileMap().checkBounds(boundsDest);
        boundsSrc.bound(boundsDest);
        int tileSize = tileMap.getTileSize();
        int minX = boundsSrc.minX * tileSize + offsetX;
        int minY = boundsSrc.minY * tileSize + offsetY;
        int maxX = (boundsSrc.maxX + 1) * tileSize + offsetX;
        int maxY = (boundsSrc.maxY + 1) * tileSize + offsetY;

        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                getPixel(x - offsetX, y - offsetY, colorSrc, pixelData);
                node.getPixel(x, y, colorDest, pixelData);
                blendMode.blend(colorSrc, colorDest, colorSrcSpace, colorDestSpace, color, composeMode, colorSpace);
                node.setPixel(x, y, color, pixelData);
            }
        }
    }
}
