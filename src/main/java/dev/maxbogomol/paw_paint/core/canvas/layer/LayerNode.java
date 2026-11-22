package dev.maxbogomol.paw_paint.core.canvas.layer;

public abstract class LayerNode {
    protected String name;
    protected float opacity = 1f;
    protected boolean visible = true;

    public void setName(String name) {
        this.name = name;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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
}
