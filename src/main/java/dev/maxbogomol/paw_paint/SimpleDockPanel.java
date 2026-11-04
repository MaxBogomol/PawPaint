package dev.maxbogomol.paw_paint;

import org.gnome.gtk.Label;
import org.gnome.gtk.Widget;

public class SimpleDockPanel implements IDockPanel {
    private final String title;
    private final Label label;

    public SimpleDockPanel(String title) {
        this.title = title;
        this.label = new Label(title);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Widget getWidget() {
        return label;
    }
}
