package dev.maxbogomol.paw_paint.core.canvas.layer;

public class LayerBoundsContext {
    public LayerBounds src = LayerBounds.create();
    public LayerBounds dest = LayerBounds.create();

    public static LayerBoundsContext create() {
        return new LayerBoundsContext();
    }
}
