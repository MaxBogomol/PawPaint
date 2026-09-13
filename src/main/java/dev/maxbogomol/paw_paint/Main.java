package dev.maxbogomol.paw_paint;

import org.gnome.gtk.*;
import org.gnome.gio.ApplicationFlags;

public class Main {

    static void main(String[] args) {
        Application app = new Application("dev.maxbogomol.paw_paint", ApplicationFlags.DEFAULT_FLAGS);
        app.onActivate(() -> {
            Window window = new ApplicationWindow(app);
            window.setTitle("Paw Paint");
            window.setDefaultSize(400, 400);

            Box box = new Box(Orientation.VERTICAL, 0);
            box.setHalign(Align.CENTER);
            box.setValign(Align.CENTER);

            window.setChild(box);

            Button button = Button.withLabel("hot gay sex");
            box.append(button);

            Button button1 = Button.withLabel("wiwi wawa");
            box.append(button1);

            window.present();
        });
        app.run(args);
    }
}