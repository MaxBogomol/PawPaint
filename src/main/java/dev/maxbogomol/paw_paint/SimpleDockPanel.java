package dev.maxbogomol.paw_paint;

import org.gnome.gtk.*;

public class SimpleDockPanel implements IDockPanel {
    private final String title;
    private final String icon;
    private final Box box;
    private final Label label;

    public SimpleDockPanel(String title, String icon) {
        this.title = title;
        this.icon = icon;
        label = new Label(title);
        box = new Box(Orientation.HORIZONTAL, 0);
        box.setHalign(Align.CENTER);
        box.setValign(Align.CENTER);
        box.setHexpand(true);
        box.setVexpand(true);
        box.append(label);
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
        return box;
    }
}
