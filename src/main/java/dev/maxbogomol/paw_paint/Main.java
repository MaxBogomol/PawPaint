package dev.maxbogomol.paw_paint;

import org.gnome.gio.ApplicationFlags;
import org.gnome.gtk.*;

public class Main {

    static void main(String[] args) {
        Application app = new Application("dev.maxbogomol.paw_paint", ApplicationFlags.DEFAULT_FLAGS);
        app.onActivate(() -> {
            Window window = new ApplicationWindow(app);
            window.setTitle("Paw Paint");
            window.setDefaultSize(400, 400);

            Label titleLabel = new Label("Paw Paint");
            titleLabel.addCssClass("title");

            HeaderBar headerBar = new HeaderBar();
            headerBar.setTitleWidget(titleLabel);

            window.setTitlebar(headerBar);
            window.present();

            DockWindow dockWindow1 = new DockWindow(window);
            DockWindow dockWindow2 = new DockWindow(window);
            DockWindow dockWindow3 = new DockWindow(window);

            dockWindow1.addDockPanel(new SimpleDockPanel("test1"));
            dockWindow1.addDockPanel(new SimpleDockPanel("test2"));
            dockWindow1.addDockPanel(new SimpleDockPanel("test3"));
            dockWindow1.addDockPanel(new SimpleDockPanel("test4"));
            dockWindow2.addDockPanel(new SimpleDockPanel("test5"));
            dockWindow2.addDockPanel(new SimpleDockPanel("test6"));
            dockWindow3.addDockPanel(new SimpleDockPanel("test7"));
        });
        app.run(args);
    }
}
