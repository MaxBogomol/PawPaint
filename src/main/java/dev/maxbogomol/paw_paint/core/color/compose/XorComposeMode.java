package dev.maxbogomol.paw_paint.core.color.compose;

public class XorComposeMode extends ComposeMode {
    @Override
    public float blend(float src, float dest) {
        return src * (1.0f - dest) + dest * (1.0f - src);
    }
}
