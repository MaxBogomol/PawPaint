package dev.maxbogomol.paw_paint;

import dev.maxbogomol.paw_paint.gui.dock_window.DockWindow;
import dev.maxbogomol.paw_paint.gui.dock_window.dock_panel.SimpleDockPanel;
import org.gnome.gdk.Display;
import org.gnome.gio.ApplicationFlags;
import org.gnome.gtk.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class PawPaint {
    private static final String APPLICATION_ID = "dev.maxbogomol.paw_paint";
    public static final String APPLICATION_NAME = "Paw Paint";

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
        applicationWindow.setDefaultSize(400, 400);

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

        applicationWindow.setChild(dockWindow1.getMainWidgetBox());
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
