package dev.maxbogomol.paw_paint.core.canvas.layer;

public class LayerBounds {
    public int minX, minY, maxX, maxY = 0;

    public LayerBounds(int minX, int minY, int maxX, int maxY){
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public LayerBounds() {}

    public static LayerBounds create() {
        return new LayerBounds();
    }

    public static LayerBounds create(int minX, int minY, int maxX, int maxY) {
        return new LayerBounds(minX, minY, maxX, maxY);
    }

    public void bound(LayerBounds dest) {
        this.minX = Math.min(this.minX, dest.minX);
        this.minY = Math.min(this.minY, dest.minY);
        this.maxX = Math.max(this.maxX, dest.maxX);
        this.maxY = Math.max(this.maxY, dest.maxY);
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }
}
