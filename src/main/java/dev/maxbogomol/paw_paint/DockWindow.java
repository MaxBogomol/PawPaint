package dev.maxbogomol.paw_paint;

import org.gnome.gdk.ContentProvider;
import org.gnome.gdk.DragAction;
import org.gnome.gobject.Value;
import org.gnome.gtk.*;
import org.gnome.pango.EllipsizeMode;

import java.util.ArrayList;
import java.util.List;

public class DockWindow {
    private final List<IDockPanel> dockPanels = new ArrayList<>();
    private IDockPanel activeDockPanel;

    private final Window mainWindow;
    private final Window window;
    private final Label titleLabel;
    private final HeaderBar headerBar;
    private final Button icon;
    private final Button button;
    private final Stack stack;
    private final ScrolledWindow scrolledBar;
    private final Box barTabs;

    public DockWindow(Window mainWindow) {
        this.mainWindow = mainWindow;

        titleLabel = new Label();
        titleLabel.addCssClass("title");
        titleLabel.setEllipsize(EllipsizeMode.END);

        icon = Button.fromIconName("edit-cut-symbolic");
        icon.addCssClass("circular");
        icon.addCssClass("flat");

        button = Button.fromIconName("document-properties-symbolic");
        button.addCssClass("flat");

        stack = new Stack();
        stack.setTransitionType(StackTransitionType.SLIDE_LEFT_RIGHT);

        headerBar = new HeaderBar();
        headerBar.setTitleWidget(titleLabel);
        headerBar.packStart(icon);

        Box bar = new Box(Orientation.HORIZONTAL, 0);
        bar.addCssClass("pawpaint-bar");

        barTabs = new Box(Orientation.HORIZONTAL, 0);
        barTabs.setHalign(Align.START);
        barTabs.setHexpand(true);
        barTabs.addCssClass("linked");

        scrolledBar = new ScrolledWindow();
        scrolledBar.setChild(barTabs);
        scrolledBar.setPolicy(PolicyType.AUTOMATIC, PolicyType.NEVER);
        scrolledBar.setPropagateNaturalWidth(true);

        Box barButton = new Box(Orientation.HORIZONTAL, 0);
        barButton.append(button);

        bar.append(scrolledBar);
        bar.append(barButton);

        Box box = new Box(Orientation.VERTICAL, 0);
        box.append(bar);
        box.append(stack);

        window = new Window();
        window.setTitlebar(headerBar);
        window.setChild(box);
        window.setTransientFor(mainWindow);
        window.present();

        setTitle("Dock window");

        DropTarget dropTarget = new DropTarget(Button.getType(), DragAction.MOVE);

        dropTarget.onDrop((value, x, y) -> {
            System.out.println(value + " x: " + x + " y: " + y);
            return false;
        });

        barTabs.addController(dropTarget);
    }

    private void setTitle(String title) {
        window.setTitle(title);
        titleLabel.setText(title);
        icon.setTooltipMarkup(title);
    }

    public void addDockPanel(IDockPanel dockPanel) {
        boolean empty = dockPanels.isEmpty();
        dockPanels.add(dockPanel);

        if (empty) setActiveDockPanel(dockPanel);

        stack.addChild(dockPanel.getWidget());

        Button tabButton = Button.fromIconName("edit-cut-symbolic");

        barTabs.append(tabButton);

        tabButton.onClicked(() -> {
            stack.setVisibleChild(dockPanel.getWidget());
        });

        DragSource dragSource = new DragSource();
        Value valueSource = new Value();
        valueSource.init(Button.getType());
        valueSource.setObject(tabButton);

        dragSource.setContent(ContentProvider.forValue(valueSource));
        dragSource.setActions(DragAction.MOVE);

        dragSource.onDragBegin((drag) -> {
            if (drag != null) {
                Button dragButton = Button.fromIconName("edit-cut-symbolic");
                DragIcon dragIcon = DragIcon.getForDrag(drag);
                dragIcon.setChild(dragButton);
            }
        });

        dragSource.onDragEnd((drag, success) -> {
            if (!success) {
                tabButton.unparent();
                stack.remove(dockPanel.getWidget());

                DockWindow newWindow = new DockWindow(this.mainWindow);
                newWindow.addDockPanel(dockPanel);

                if (drag != null) {
                    drag.dropDone(true);
                }
            }
        });

        tabButton.addController(dragSource);
    }

    public void setActiveDockPanel(IDockPanel dockPanel) {
        activeDockPanel = dockPanel;
        setTitle(dockPanel.getTitle());
    }
}
