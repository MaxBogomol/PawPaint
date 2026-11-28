package dev.maxbogomol.paw_paint;

import dev.maxbogomol.paw_paint.core.canvas.Canvas;
import dev.maxbogomol.paw_paint.core.canvas.layer.Layer;
import dev.maxbogomol.paw_paint.core.canvas.layer.LayerBoundsContext;
import dev.maxbogomol.paw_paint.core.canvas.layer.LayerGroup;
import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.blend.BlendContext;
import dev.maxbogomol.paw_paint.core.color.blend.BlendModes;
import dev.maxbogomol.paw_paint.core.color.compose.ComposeModes;
import dev.maxbogomol.paw_paint.core.color.format.ColorFormat;
import dev.maxbogomol.paw_paint.core.color.format.ColorFormats;
import dev.maxbogomol.paw_paint.gui.canvas.CanvasArea;
import dev.maxbogomol.paw_paint.gui.dock_window.DockWindow;
import dev.maxbogomol.paw_paint.gui.dock_window.dock_panel.LayersDockPanel;
import org.gnome.gdk.Display;
import org.gnome.gio.ApplicationFlags;
import org.gnome.gtk.*;

import java.io.IOException;
import java.io.InputStream;
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

    public static BlendContext blendContext = BlendContext.create();
    public static LayerBoundsContext layerBoundsContext = LayerBoundsContext.create();

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
        int layerSize = size;

        ColorFormat colorFormat = ColorFormats.RGBA8;
        byte[] pixelData = colorFormat.createPixelData();
        blendContext.pixelData = colorFormat.createPixelData();
        Color color = Color.create();

        Canvas canvas = new Canvas(colorFormat, size, size);

        Layer layer1 = new Layer(colorFormat, layerSize, layerSize, "layer1");
        Layer layer2 = new Layer(colorFormat, layerSize, layerSize, "layer2");
        Layer layer3 = new Layer(colorFormat, layerSize, layerSize, "layer3");

        LayerGroup layerGroup1 = new LayerGroup(colorFormat, "group1");
        layerGroup1.addNode(layer3);

        canvas.getMainLayerGroup().addNode(layer1);
        canvas.getMainLayerGroup().addNode(layer2);
        canvas.getMainLayerGroup().addNode(layerGroup1);

        layer1.setX(-size / 4);
        layer1.setY(-size / 4);

        layer2.setBlendMode(BlendModes.NORMAL);
        layer2.setComposeMode(ComposeModes.SRC_OVER);
        layer2.setX(size / 4);
        layer2.setY(size / 4);

        layer3.setOpacity(0.25f);
        layer3.setBlendMode(BlendModes.ADDITION);

        for (int y = 0; y < layerSize; y++) {
            for (int x = 0; x < layerSize; x++) {
                color.set((float) x / layerSize, (float) y / layerSize, 0.0f, 1.0f);
                color.set(1.0f, 1.0f, 0.0f, 0.5f);
                layer1.setTileMapPixel(x, y, color, pixelData);

                color.set(0.0f, (float) x / layerSize, (float) y / layerSize, 1.0f);
                color.set(0.0f, 0.0f, 1.0f, 0.5f);
                layer2.setTileMapPixel(x, y, color, pixelData);

                color.set((float) x / layerSize, (float) y / layerSize, 0.0f, 1.0f);
                layer3.setTileMapPixel(x, y, color, pixelData);
            }
        }

        canvas.getMainLayerGroup().blend();

        CanvasArea canvasArea = new CanvasArea(canvas);

        applicationWindow.setChild(canvasArea.getMainWidgetBox());
        //applicationWindow.addCssClass("pawpaint-transparent-window");

        DockWindow dockWindow = new DockWindow(applicationWindow);
        LayersDockPanel layersDockPanel = new LayersDockPanel();
        layersDockPanel.setCanvas(canvas);
        dockWindow.addDockPanel(layersDockPanel);
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
