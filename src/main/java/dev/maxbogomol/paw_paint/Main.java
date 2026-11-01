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

            //Gtk.styleContextAddProviderForDisplay(Objects.requireNonNull(Display.getDefault()),
            //        cssProvider,
            //        Gtk.STYLE_PROVIDER_PRIORITY_APPLICATION
            //);

            HeaderBar headerBar = new HeaderBar();

            Label titleLabel = new Label("Paw Paint");
            titleLabel.addCssClass("title");
            headerBar.setTitleWidget(titleLabel);

            window.setTitlebar(headerBar);

            Box box = new Box(Orientation.VERTICAL, 0);
            box.setHalign(Align.CENTER);
            box.setValign(Align.CENTER);

            window.setChild(box);

            window.present();

            Window window1 = new ApplicationWindow(app);
            window1.setTitle("WAWA");
            window1.setDefaultSize(200, 200);

            HeaderBar headerBar1 = new HeaderBar();

            Label titleLabel1 = new Label("WAWA");
            titleLabel1.addCssClass("title");
            headerBar1.setTitleWidget(titleLabel1);
            headerBar1.setShowTitleButtons(false);

            window1.setTitlebar(headerBar1);

            Box box1 = new Box(Orientation.VERTICAL, 0);
            box1.setHalign(Align.CENTER);
            box1.setValign(Align.CENTER);

            window1.setChild(box1);

            window1.setTransientFor(window);

            window1.present();
        });
        app.run(args);
    }
}
