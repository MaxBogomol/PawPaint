package dev.maxbogomol.paw_paint.core.color.blend;

public class LighterColorBlendMode extends SimpleLightColorBlendMode {
    @Override
    public boolean isDarker(float sLuminescence, float dLuminescence) {
        return sLuminescence > dLuminescence;
    }
}