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
            DockWindow dockWindow3 = new DockWindow(window, false);
            DockWindow dockWindow4 = new DockWindow(window, false);

            dockWindow1.addDockPanel(new SimpleDockPanel("test1", "weather-clear-symbolic"));
            dockWindow1.addDockPanel(new SimpleDockPanel("test2", "document-new-symbolic"));
            dockWindow1.addDockPanel(new SimpleDockPanel("test3", "edit-delete-symbolic"));
            dockWindow1.addDockPanel(new SimpleDockPanel("test4", "format-justify-fill-symbolic"));
            dockWindow2.addDockPanel(new SimpleDockPanel("test5", "document-send-symbolic"));
            dockWindow2.addDockPanel(new SimpleDockPanel("test6", "input-gaming-symbolic"));
            dockWindow3.addDockPanel(new SimpleDockPanel("test7"));
            dockWindow4.addDockPanel(new SimpleDockPanel("test8"));

            Box mainBox = new Box(Orientation.VERTICAL, 0);
            mainBox.setHalign(Align.CENTER);
            mainBox.setValign(Align.CENTER);
            mainBox.setHexpand(true);
            mainBox.setVexpand(true);

            Label mainLabel = new Label("Main WAWA");

            mainBox.append(mainLabel);

            Paned paned = new Paned(Orientation.HORIZONTAL);
            paned.setResizeStartChild(false);
            paned.setResizeEndChild(true);
            paned.setShrinkStartChild(false);
            paned.setShrinkEndChild(false);
            paned.setWideHandle(true);

            Paned paned1 = new Paned(Orientation.HORIZONTAL);
            paned1.setResizeStartChild(true);
            paned1.setResizeEndChild(false);
            paned1.setShrinkStartChild(true);
            paned1.setShrinkEndChild(false);
            paned1.setWideHandle(true);

            paned.setStartChild(dockWindow3.getMainWidgetBox());
            paned.setEndChild(paned1);

            paned1.setStartChild(mainBox);
            paned1.setEndChild(dockWindow4.getMainWidgetBox());

            window.setChild(paned);
        });
        app.run(args);
    }
}
