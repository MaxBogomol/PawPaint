package dev.maxbogomol.paw_paint;

import org.gnome.gtk.Label;
import org.gnome.gtk.Widget;

public class SimpleDockPanel implements IDockPanel {
    private final String title;
    private final String icon;
    private final Label label;

    public SimpleDockPanel(String title, String icon) {
        this.title = title;
        this.icon = icon;
        this.label = new Label(title);
    }

    public SimpleDockPanel(String title) {
        this(title, "edit-cut-symbolic");
    }

    public SimpleDockPanel() {
        this("Simple Dock Panel", "edit-cut-symbolic");
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public Widget getWidget() {
        return label;
    }
}
