package dev.maxbogomol.paw_paint;

import dev.maxbogomol.paw_paint.core.canvas.layer.Layer;
import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.blend.BlendModes;
import dev.maxbogomol.paw_paint.core.color.compose.ComposeModes;
import dev.maxbogomol.paw_paint.core.color.format.ColorFormat;
import dev.maxbogomol.paw_paint.core.color.format.ColorFormats;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpaces;
import org.freedesktop.cairo.Antialias;
import org.freedesktop.cairo.Format;
import org.freedesktop.cairo.ImageSurface;
import org.gnome.gdk.Display;
import org.gnome.gio.ApplicationFlags;
import org.gnome.gtk.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Objects;

public class PawPaint {
    private static final String APPLICATION_ID = "dev.maxbogomol.paw_paint";
    public static final String APPLICATION_NAME = "Paw Paint";
    private static final int STANDARD_WIDTH = 1024;
    private static final int STANDARD_HEIGHT = 768;

    public static PawPaint instance;
    private final Application application;
    private ApplicationWindow applicationWindow;
    private Label titleLabel;
    private HeaderBar headerBar;

    public PawPaint() {
        application = new Application(APPLICATION_ID, ApplicationFlags.DEFAULT_FLAGS);
        application.onActivate(() -> {
            createWindow();
            loadCssStyle();
        });
    }

    private void createWindow() {
        applicationWindow = new ApplicationWindow(application);
        applicationWindow.setTitle(APPLICATION_NAME);
        applicationWindow.setDefaultSize(STANDARD_WIDTH, STANDARD_HEIGHT);

        titleLabel = new Label(APPLICATION_NAME);
        titleLabel.addCssClass("title");

        headerBar = new HeaderBar();
        headerBar.setTitleWidget(titleLabel);

        applicationWindow.setTitlebar(headerBar);
        applicationWindow.present();

        int size = 256;

        ColorFormat colorFormat = ColorFormats.RGBA8;
        byte[] pixelData = colorFormat.createPixelData();
        Color color = Color.create();
        Color color1 = Color.create();
        Color color2 = Color.create();

        Color color1Space = Color.create();
        Color color2Space = Color.create();

        Layer layer = new Layer(colorFormat, size, size, "test");

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                color1.set((float) x / size, (float) y / size, 0.0f, 1.0f);
                color2.set(0.0f, (float) x / size, (float) y / size, 0.5f);
                BlendModes.ADDITIVE.blend(color2, color1, color1Space, color2Space, color, ComposeModes.SRC_OVER, ColorSpaces.LINEAR_RGB);
                layer.setPixel(x, y, color, pixelData);
            }
        }

        boolean intSurface = colorFormat.getBitDepth() <= 8;

        ImageSurface surface = ImageSurface.create(intSurface ? Format.ARGB32 : Format.RGBA128F, size, size);
        MemorySegment data = surface.getData();
        int stride = surface.getStride();
        int channelValue = 255;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                layer.getPixel(x, y, color, pixelData);

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
        surface.markDirty();

        DrawingArea drawingArea = new DrawingArea();
        drawingArea.setContentWidth(size);
        drawingArea.setContentHeight(size);
        drawingArea.setHalign(Align.CENTER);
        drawingArea.setValign(Align.CENTER);

        drawingArea.setDrawFunc((area, cr, width, height) -> {
            cr.setAntialias(Antialias.NONE);
            cr.setSource(surface, 0, 0);
            cr.paint();
        });

        applicationWindow.setChild(drawingArea);
    }

    private void loadCssStyle() {
        CssProvider cssProvider = new CssProvider();

        try (InputStream inputStream = Main.class.getResourceAsStream("/styles/style.css")) {
            if (inputStream != null) {
                cssProvider.loadFromBytes(inputStream.readAllBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gtk.styleContextAddProviderForDisplay(Objects.requireNonNull(Display.getDefault()),
                cssProvider,
                Gtk.STYLE_PROVIDER_PRIORITY_APPLICATION
        );
    }

    public void runApplication(String[] args) {
        application.run(args);
    }

    public PawPaint getInstance() {
        return instance;
    }

    public Application getApplication() {
        return application;
    }

    public ApplicationWindow getApplicationWindow() {
        return applicationWindow;
    }
}
