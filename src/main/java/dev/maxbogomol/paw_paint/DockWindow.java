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
        window.setDefaultSize(200, 200);
        window.setTitlebar(headerBar);
        window.setChild(box);
        window.setTransientFor(mainWindow);
        window.present();

        setTitle("Dock window");

        DropTarget dropTarget = new DropTarget(Button.getType(), DragAction.MOVE);

        dropTarget.onDrop((value, x, y) -> {
            if (value != null) {
                if (value.getObject() instanceof TabButton tabButton) {
                    DockWindow dockWindow = tabButton.dockWindow;
                    IDockPanel dockPanel = tabButton.dockPanel;
                    int width = barTabs.getWidth();
                    int size = dockWindow.getDockPanels().size();

                    int index = (int) Math.round(x / width * size);
                    boolean add = true;

                    if (dockWindow == this) {
                        int oldIndex = dockPanels.indexOf(dockPanel);
                        if (oldIndex < index) index--;
                        index = Math.clamp(index, 0, dockPanels.size() - 1);

                        if (dockPanels.size() == 1) add = false;
                    }

                    if (add) {
                        dockWindow.removeDockPanel(dockPanel);
                        addDockPanel(dockPanel, index);
                        setActiveDockPanel(dockPanel);
                    }
                    return true;
                }
            }
            return false;
        });

        barTabs.addController(dropTarget);
    }

    private void setTitle(String title) {
        window.setTitle(title);
        titleLabel.setText(title);
        icon.setTooltipMarkup(title);
    }

    public List<IDockPanel> getDockPanels() {
        return dockPanels;
    }

    public void addDockPanel(IDockPanel dockPanel) {
        boolean empty = dockPanels.isEmpty();
        dockPanels.add(dockPanel);

        addTabButton(dockPanel);

        if (empty) setActiveDockPanel(dockPanel);
    }

    public void addDockPanel(IDockPanel dockPanel, int index) {
        boolean empty = dockPanels.isEmpty();
        if (empty) {
            dockPanels.add(dockPanel);
            addTabButton(dockPanel);
        } else {
            dockPanels.add(index, dockPanel);
            createTabButtons();
        }

        if (empty) setActiveDockPanel(dockPanel);
    }

    public void removeDockPanel(IDockPanel dockPanel) {
        dockPanels.remove(dockPanel);
        stack.remove(dockPanel.getWidget());

        if (!dockPanels.isEmpty()) {
            createTabButtons();
            if (activeDockPanel == dockPanel) {
                setActiveDockPanel(dockPanels.get(0));
            }
        } else {
            window.close();
        }
    }

    public void setActiveDockPanel(IDockPanel dockPanel) {
        activeDockPanel = dockPanel;
        stack.setVisibleChild(dockPanel.getWidget());
        setTitle(dockPanel.getTitle());
    }

    private void addTabButton(IDockPanel dockPanel) {
        stack.addChild(dockPanel.getWidget());

        TabButton tabButton = new TabButton();
        tabButton.setIconName("edit-cut-symbolic");
        tabButton.setTooltipText(dockPanel.getTitle());

        tabButton.dockWindow = this;
        tabButton.dockPanel = dockPanel;

        barTabs.append(tabButton);

        tabButton.onClicked(() -> {
            setActiveDockPanel(dockPanel);
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
                if (dockPanels.size() > 1) {
                    removeDockPanel(dockPanel);

                    DockWindow newWindow = new DockWindow(this.mainWindow);
                    newWindow.addDockPanel(dockPanel);
                }

                if (drag != null) {
                    drag.dropDone(true);
                }
            }
        });

        tabButton.addController(dragSource);
    }

    private void createTabButtons() {
        Widget child;
        while ((child = barTabs.getFirstChild()) != null) {
            barTabs.remove(child);
        }

        while ((child = stack.getFirstChild()) != null) {
            stack.remove(child);
        }

        for (IDockPanel dockPanel : dockPanels) {
            addTabButton(dockPanel);
        }
    }
}
