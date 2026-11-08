package dev.maxbogomol.paw_paint;

import org.gnome.glib.Type;
import org.gnome.gtk.Button;
import org.javagi.gobject.types.Types;

import java.lang.foreign.MemorySegment;

public class DockTabButton extends Button {
    public static Type gtype = Types.register(DockTabButton.class);

    public DockWindow dockWindow;
    public IDockPanel dockPanel;

    public DockTabButton(MemorySegment address) {
        super(address);
    }

    public DockTabButton() {
        super();
    }

    public static Type getType() {
        return gtype;
    }
}
