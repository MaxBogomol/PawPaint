package dev.maxbogomol.paw_paint.core.color.compose;

public class ClearComposeMode extends ComposeMode {
    @Override
    public float blend(float src, float dest) {
        return 0.0f;
    }
}
