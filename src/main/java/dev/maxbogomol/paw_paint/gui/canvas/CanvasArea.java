package dev.maxbogomol.paw_paint.gui.canvas;

import dev.maxbogomol.paw_paint.core.canvas.Canvas;
import dev.maxbogomol.paw_paint.core.color.Color;
import org.freedesktop.cairo.Antialias;
import org.freedesktop.cairo.Format;
import org.freedesktop.cairo.ImageSurface;
import org.gnome.gtk.DrawingArea;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class CanvasArea {
    private final Canvas canvas;

    private final DrawingArea drawingArea;
    private ImageSurface imageSurface;
    private byte[] pixelData;
    private Color color = Color.create();

    public CanvasArea(Canvas canvas) {
        this.canvas = canvas;
        this.pixelData = canvas.getColorFormat().createPixelData();

        createSurface();

        drawingArea = new DrawingArea();

        drawingArea.setContentWidth(canvas.getWidth());
        drawingArea.setContentHeight(canvas.getHeight());

        drawingArea.setDrawFunc((drawingArea, cr, width, height) -> {
            cr.setAntialias(Antialias.NONE);
            cr.setSource(imageSurface, 0, 0);
            cr.paint();
        });
        blendSurface();
    }

    private void createSurface() {
        boolean intSurface = canvas.getColorFormat().getBitDepth() <= 8;
        imageSurface = ImageSurface.create(intSurface ? Format.ARGB32 : Format.RGBA128F, canvas.getWidth(), canvas.getHeight());
    }

    private void blendSurface() {
        boolean intSurface = canvas.getColorFormat().getBitDepth() <= 8;

        MemorySegment data = imageSurface.getData();
        int stride = imageSurface.getStride();
        int channelValue = 255;

        for (int y = 0; y < canvas.getHeight(); y++) {
            for (int x = 0; x < canvas.getWidth(); x++) {
                canvas.getMainLayerGroup().getPixel(x, y, color, pixelData);

                float red = color.r;
                float green = color.g;
                float blue = color.b;
                float alpha = color.a;

                if (intSurface) {
                    int argb = 0;

                    if (alpha > 0) {
                        float mult = alpha * channelValue;
                        int r = (int) (red * mult);
                        int g = (int) (green * mult);
                        int b = (int) (blue * mult);
                        int a = (int) (mult);

                        argb = (a << 24) | (r << 16) | (g << 8) | b;
                    }

                    long pixelOffset = (long) y * stride + x * 4L;
                    data.set(ValueLayout.JAVA_INT, pixelOffset, argb);
                } else {
                    if (alpha == 0) red = green = blue = alpha = 0;

                    long pixelOffset = (long) y * stride + x * 16L;
                    data.set(ValueLayout.JAVA_FLOAT, pixelOffset, red);
                    data.set(ValueLayout.JAVA_FLOAT, pixelOffset + 4, green);
                    data.set(ValueLayout.JAVA_FLOAT, pixelOffset + 8, blue);
                    data.set(ValueLayout.JAVA_FLOAT, pixelOffset + 12, alpha);
                }
            }
        }
        imageSurface.markDirty();
    }

    public DrawingArea getDrawingArea() {
        return drawingArea;
    }
}
