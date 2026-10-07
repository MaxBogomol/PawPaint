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
        });
        app.run(args);
    }
}
