package dev.maxbogomol.paw_paint.core.color.blend;

public abstract class BlendMode {
    public abstract void blend(float[] in1, float[] in2, float[] out);
}
