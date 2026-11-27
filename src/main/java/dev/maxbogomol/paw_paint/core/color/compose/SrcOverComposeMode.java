package dev.maxbogomol.paw_paint.core.color.compose;

public class SrcOverComposeMode extends ComposeMode {
    @Override
    public float blend(float src, float dest) {
        return src + dest * (1.0f - src);
    }
}
