package dev.maxbogomol.paw_paint.core.color.compose;

public class SrcComposeMode extends ComposeMode {
    @Override
    public float blend(float src, float dest) {
        return src;
    }
}
