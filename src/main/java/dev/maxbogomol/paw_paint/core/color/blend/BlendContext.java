package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.Color;

public class BlendContext {
    public byte[] pixelData;
    public Color color = Color.create();
    public Color src = Color.create();
    public Color dest = Color.create();
    public Color srcSpace = Color.create();
    public Color destSpace = Color.create();
    public Color out = Color.create();

    public static BlendContext create() {
        return new BlendContext();
    }
}
