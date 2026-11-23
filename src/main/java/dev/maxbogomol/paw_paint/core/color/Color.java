package dev.maxbogomol.paw_paint.core.color;

public class Color {
    public float r, g, b, a = 0;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color() {}

    public static Color create() {
        return new Color();
    }

    public static Color create(float r, float g, float b, float a) {
        return new Color(r, g, b, a);
    }

    public static void copy(Color src, Color dest) {
        src.copy(dest);
    }

    public static void swap(Color src, Color dest) {
        float r = dest.r;
        float g = dest.g;
        float b = dest.b;
        float a = dest.a;
        src.copy(dest);
        src.set(r, g, b, a);
    }

    public Color copy() {
        return create(r, g, b, a);
    }

    public void copy(Color src) {
        src.set(r, g, b, a);
    }

    public void set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float getRed() {
        return r;
    }

    public float getGreen() {
        return g;
    }

    public float getBlue() {
        return b;
    }

    public float getAlpha() {
        return a;
    }
}
