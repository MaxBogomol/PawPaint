package dev.maxbogomol.paw_paint.core.color.compose;

public class SrcAtopComposeMode extends ComposeMode {
    @Override
    public float blend(float src, float dest) {
        return dest;
    }
}
