package dev.maxbogomol.paw_paint.core.util;

import dev.maxbogomol.paw_paint.core.color.Color;

public class ColorUtil {
    //ITU-R BT.601-7
    //https://www.itu.int/dms_pubrec/itu-r/rec/bt/r-rec-bt.601-7-201103-i!!pdf-e.pdf
    public static float rgbToGrayscaleLegacy(float r, float g, float b) {
        return 0.299f * r + 0.587f * g + 0.114f * b;
    }

    //ITU-R BT.709-6
    //https://www.itu.int/dms_pubrec/itu-r/rec/bt/r-rec-bt.709-6-201506-i!!pdf-e.pdf
    public static float rgbToGrayscaleModern(float r, float g, float b) {
        return 0.2126f * r + 0.7152f * g + 0.0722f * b;
    }

    //ITU-R BT.709-6
    //https://www.itu.int/dms_pubrec/itu-r/rec/bt/r-rec-bt.709-6-201506-i!!pdf-e.pdf
    public static float rgbToLuminescence(float r, float g, float b) {
        return 0.2126f * r + 0.7152f * g + 0.0722f * b;
    }

    //IEC 61966-2-1
    public static float srgbToLinearRgb(float c) {
        if (c <= 0.04045f) return c / 12.92f;
        return (float) Math.pow((c + 0.055f) / 1.055f, 2.4f);
    }

    //IEC 61966-2-1
    public static float linearRgbToSrgb(float c) {
        if (c <= 0.0031308f) return c * 12.92f;
        return (float) (1.055f * Math.pow(c, 1.0f / 2.4f) - 0.055f);
    }

    //https://bottosson.github.io/posts/oklab/
    public static Color linearRgbToOklab(Color color) {
        float l = 0.4122214708f * color.r + 0.5363325363f * color.g + 0.0514459929f * color.b;
        float m = 0.2119034982f * color.r + 0.6806995451f * color.g + 0.1073969566f * color.b;
        float s = 0.0883024619f * color.r + 0.2817188376f * color.g + 0.6299787005f * color.b;

        float l_ = (float) Math.cbrt(l);
        float m_ = (float) Math.cbrt(m);
        float s_ = (float) Math.cbrt(s);

        color.r = 0.2104542553f * l_ + 0.7936177850f * m_ - 0.0040720468f * s_;
        color.g = 1.9779984951f * l_ - 2.4285922050f * m_ + 0.4505937099f * s_;
        color.b = 0.0259040371f * l_ + 0.7827717662f * m_ - 0.8086757660f * s_;

        return color;
    }

    //https://bottosson.github.io/posts/oklab/
    public static Color oklabToLinearRgb(Color color) {
        float l_ = color.r + 0.3963377774f * color.g + 0.2158037573f * color.b;
        float m_ = color.r - 0.1055613458f * color.g - 0.0638541728f * color.b;
        float s_ = color.r - 0.0894841775f * color.g - 1.2914855480f * color.b;

        float l = l_ * l_ * l_;
        float m = m_ * m_ * m_;
        float s = s_ * s_ * s_;

        float lr = +4.0767416621f * l - 3.3077115913f * m + 0.2309699292f * s;
        float lg = -1.2684380046f * l + 2.6097574011f * m - 0.3413193965f * s;
        float lb = -0.0041960863f * l - 0.7034186147f * m + 1.7076147010f * s;

        color.r = Math.clamp(lr, 0.0f, 1.0f);
        color.g = Math.clamp(lg, 0.0f, 1.0f);
        color.b = Math.clamp(lb, 0.0f, 1.0f);

        return color;
    }

    public static Color srgbToOklab(Color color) {
        color.r = ColorUtil.srgbToLinearRgb(color.r);
        color.g = ColorUtil.srgbToLinearRgb(color.g);
        color.b = ColorUtil.srgbToLinearRgb(color.b);
        return linearRgbToOklab(color);
    }

    public static Color oklabToSrgb(Color color) {
        oklabToLinearRgb(color);
        color.r = ColorUtil.linearRgbToSrgb(color.r);
        color.g = ColorUtil.linearRgbToSrgb(color.g);
        color.b = ColorUtil.linearRgbToSrgb(color.b);
        return color;
    }
}
