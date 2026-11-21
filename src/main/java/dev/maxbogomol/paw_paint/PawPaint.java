package dev.maxbogomol.paw_paint;

import dev.maxbogomol.paw_paint.core.color.ColorFormat;
import dev.maxbogomol.paw_paint.core.color.ColorFormats;
import dev.maxbogomol.paw_paint.core.layer.TileMap;
import dev.maxbogomol.paw_paint.gui.dock_window.DockWindow;
import dev.maxbogomol.paw_paint.gui.dock_window.dock_panel.SimpleDockPanel;
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

        DockWindow dockWindow1 = new DockWindow(applicationWindow, false);

        dockWindow1.addDockPanel(new SimpleDockPanel("test1", "weather-clear-symbolic"));
        dockWindow1.addDockPanel(new SimpleDockPanel("test2", "document-new-symbolic"));
        dockWindow1.addDockPanel(new SimpleDockPanel("test3", "edit-delete-symbolic"));
        dockWindow1.addDockPanel(new SimpleDockPanel("test4", "format-justify-fill-symbolic"));

        int size = 256;

        ColorFormat colorFormat = ColorFormats.RGBA8;
        colorFormat.createPixelData();
        float[] color = ColorFormat.createColor();

        TileMap tileMap = new TileMap(64, colorFormat.getBytesPerPixel());

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                colorFormat.encode((float) x / size, (float) y / size, 0.0f, 1.0f, colorFormat.getPixelData());
                tileMap.setPixel(x, y, colorFormat.getPixelData());
            }
        }

        ImageSurface surface = ImageSurface.create(Format.ARGB32, size, size);
        MemorySegment data = surface.getData();
        int stride = surface.getStride();
        int channelValue = 255;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                tileMap.getPixel(x, y, colorFormat.getPixelData());
                colorFormat.decode(color, colorFormat.getPixelData());

                int argb = 0;
                float alpha = ColorFormat.getAlpha(color);

                if (alpha > 0) {
                    float mult = alpha * channelValue;
                    int r = (int) (ColorFormat.getRed(color) * mult);
                    int g = (int) (ColorFormat.getGreen(color) * mult);
                    int b = (int) (ColorFormat.getBlue(color) * mult);
                    int a = (int) (mult);

                    argb = (a << 24) | (r << 16) | (g << 8) | b;
                }

                data.set(ValueLayout.JAVA_INT, (long) y * stride + x * 4L, argb);
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

        //applicationWindow.setChild(dockWindow1.getMainWidgetBox());
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
