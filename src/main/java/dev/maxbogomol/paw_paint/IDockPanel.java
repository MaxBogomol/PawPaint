package dev.maxbogomol.paw_paint;

import org.gnome.gtk.Widget;

public interface IDockPanel {

    default String getTitle() {
        return "Dock panel";
    };

    Widget getWidget();

    default void dock(DockWindow dockWindow) {};
    default void undock(DockWindow dockWindow) {};
}
