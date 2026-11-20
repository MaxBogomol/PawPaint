package dev.maxbogomol.paw_paint.gui.dock_window.dock_panel;

import org.gnome.gtk.Widget;

public interface IDockPanel {

    default String getTitle() {
        return "Dock panel";
    }

    default String getIcon() {
        return "edit-cut-symbolic";
    }

    Widget getWidget();
}
