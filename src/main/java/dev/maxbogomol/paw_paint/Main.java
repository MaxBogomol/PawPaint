package dev.maxbogomol.paw_paint;

import org.gnome.gdk.Display;
import org.gnome.gio.ApplicationFlags;
import org.gnome.gtk.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Main {

    static void main(String[] args) {
        Application app = new Application("dev.maxbogomol.paw_paint", ApplicationFlags.DEFAULT_FLAGS);
        app.onActivate(() -> {
            Window window = new ApplicationWindow(app);
            window.setTitle("Paw Paint");
            window.setDefaultSize(400, 400);

            CssProvider cssProvider = new CssProvider();

            try (InputStream inputStream = Main.class.getResourceAsStream("/style.css")) {
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

            Label titleLabel = new Label("Paw Paint");
            titleLabel.addCssClass("title");

            HeaderBar headerBar = new HeaderBar();
            headerBar.setTitleWidget(titleLabel);

            window.setTitlebar(headerBar);
            window.present();

            DockWindow dockWindow1 = new DockWindow(window);
            DockWindow dockWindow2 = new DockWindow(window);
            DockWindow dockWindow3 = new DockWindow(window);

            dockWindow1.addDockPanel(new SimpleDockPanel("test1", "weather-clear-symbolic"));
            dockWindow1.addDockPanel(new SimpleDockPanel("test2", "document-new-symbolic"));
            dockWindow1.addDockPanel(new SimpleDockPanel("test3", "edit-delete-symbolic"));
            dockWindow1.addDockPanel(new SimpleDockPanel("test4", "format-justify-fill-symbolic"));
            dockWindow2.addDockPanel(new SimpleDockPanel("test5", "document-send-symbolic"));
            dockWindow2.addDockPanel(new SimpleDockPanel("test6", "input-gaming-symbolic"));
            dockWindow3.addDockPanel(new SimpleDockPanel("test7"));
        });
        app.run(args);
    }
}
