package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public class NormalLegacyBlendMode extends BlendMode {
    @Override
    public void blend(float[] src, float[] dest, float[] out) {
        if (src[3] == 0) {
            ColorUtil.copyColor(dest, out);
        } else if (dest[3] == 0) {
            ColorUtil.copyColor(src, out);
        } else {
            for (int i = 0; i < 3; i++) {
                out[i] = src[3] * src[i] + (1 - src[3]) * dest[i];
            }
            out[3] = src[3] + (1.0f - src[3]) * dest[3];
        }
    }
}
