package dev.maxbogomol.paw_paint.gui.util;

import org.gnome.gtk.Orientation;

public class GuiUtil {

    public static Orientation oppositeOrientation(Orientation orientation) {
        return (orientation == Orientation.HORIZONTAL) ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }
}
