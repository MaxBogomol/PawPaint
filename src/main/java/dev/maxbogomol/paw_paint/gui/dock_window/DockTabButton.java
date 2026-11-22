package dev.maxbogomol.paw_paint.gui.dock_window;

import dev.maxbogomol.paw_paint.gui.dock_window.dock_panel.IDockPanel;
import org.gnome.glib.Type;
import org.gnome.gtk.Button;
import org.javagi.gobject.types.Types;

import java.lang.foreign.MemorySegment;

public class DockTabButton extends Button {
    public static Type gtype = Types.register(DockTabButton.class);

    private DockWindow dockWindow;
    private IDockPanel dockPanel;

    public DockTabButton(MemorySegment address) {
        super(address);
    }

    public DockTabButton() {
        super();
    }

    public void setDockWindow(DockWindow dockWindow) {
        this.dockWindow = dockWindow;
    }

    public void setDockPanel(IDockPanel dockPanel) {
        this.dockPanel = dockPanel;
    }

    public DockWindow getDockWindow() {
        return dockWindow;
    }

    public IDockPanel getDockPanel() {
        return dockPanel;
    }

    public static Type getType() {
        return gtype;
    }
}
