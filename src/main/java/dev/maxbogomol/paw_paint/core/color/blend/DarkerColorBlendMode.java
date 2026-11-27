package dev.maxbogomol.paw_paint.core.color.blend;

public class DarkerColorBlendMode extends SimpleLightColorBlendMode {
    @Override
    public boolean isDarker(float sLuminescence, float dLuminescence) {
        return sLuminescence < dLuminescence;
    }
}