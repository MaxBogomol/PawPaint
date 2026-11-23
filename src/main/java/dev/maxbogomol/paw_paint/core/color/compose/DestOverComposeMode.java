package dev.maxbogomol.paw_paint.core.color.compose;

public class DestOverComposeMode extends ComposeMode {
    @Override
    public float blend(float src, float dest) {
        return dest + (1.0f - dest) * src;
    }
}
