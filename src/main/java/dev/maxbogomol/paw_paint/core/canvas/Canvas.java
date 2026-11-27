package dev.maxbogomol.paw_paint.core.canvas;

import dev.maxbogomol.paw_paint.core.canvas.layer.LayerGroup;
import dev.maxbogomol.paw_paint.core.color.format.ColorFormat;

public class Canvas {
    protected ColorFormat colorFormat;
    private final LayerGroup mainLayerGroup;

    private int width = 0;
    private int height = 0;

    public Canvas(ColorFormat colorFormat, int width, int height) {
        this.colorFormat = colorFormat;
        this.mainLayerGroup = new LayerGroup(colorFormat, "main");
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

    public LayerGroup getMainLayerGroup() {
        return mainLayerGroup;
    }
}
