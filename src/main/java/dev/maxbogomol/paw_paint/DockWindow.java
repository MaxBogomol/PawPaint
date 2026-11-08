package dev.maxbogomol.paw_paint;

import org.gnome.gdk.ContentProvider;
import org.gnome.gdk.Drag;
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
    private final HeaderBar headerBar;

    private final Label titleLabel;
    private final Button icon;
    private final Button settingsButton;

    private final Box barBox;
    private final Box tabsBarBox;
    private final Box settingsBox;
    private final ScrolledWindow scrolledTabsBar;
    private final Stack stack;
    private final Box mainBox;

    public DockWindow(Window mainWindow) {
        this.mainWindow = mainWindow;

        titleLabel = new Label();
        titleLabel.addCssClass("title");
        titleLabel.setEllipsize(EllipsizeMode.END);

        icon = Button.fromIconName("edit-cut-symbolic");
        icon.addCssClass("circular");
        icon.addCssClass("flat");

        headerBar = new HeaderBar();
        headerBar.setTitleWidget(titleLabel);
        headerBar.packStart(icon);

        settingsButton = Button.fromIconName("document-properties-symbolic");
        settingsButton.addCssClass("flat");

        stack = new Stack();
        stack.setTransitionType(StackTransitionType.SLIDE_LEFT_RIGHT);

        barBox = new Box(Orientation.HORIZONTAL, 0);
        barBox.addCssClass("pawpaint-tabs-bar");

        tabsBarBox = new Box(Orientation.HORIZONTAL, 0);
        tabsBarBox.setHalign(Align.START);
        tabsBarBox.setHexpand(true);
        tabsBarBox.addCssClass("linked");

        DropTarget dropTarget = new DropTarget(DockTabButton.getType(), DragAction.MOVE);
        dropTarget.onDrop(this::onTabDrop);
        tabsBarBox.addController(dropTarget);

        scrolledTabsBar = new ScrolledWindow();
        scrolledTabsBar.setChild(tabsBarBox);
        scrolledTabsBar.setPolicy(PolicyType.AUTOMATIC, PolicyType.NEVER);
        scrolledTabsBar.setPropagateNaturalWidth(true);

        settingsBox = new Box(Orientation.HORIZONTAL, 0);
        settingsBox.append(settingsButton);

        barBox.append(scrolledTabsBar);
        barBox.append(settingsBox);

        mainBox = new Box(Orientation.VERTICAL, 0);
        mainBox.append(barBox);
        mainBox.append(stack);

        window = new Window();
        window.setDefaultSize(200, 200);
        window.setTitlebar(headerBar);
        window.setChild(mainBox);
        window.setTransientFor(mainWindow);
        window.present();

        setTitle("Dock window");
    }

    public void setActiveDockPanel(IDockPanel dockPanel) {
        activeDockPanel = dockPanel;
        icon.setIconName(dockPanel.getIcon());
        stack.setVisibleChild(dockPanel.getWidget());
        setTitle(dockPanel.getTitle());
    }

    public void addDockPanel(IDockPanel dockPanel) {
        boolean empty = dockPanels.isEmpty();
        dockPanels.add(dockPanel);

        createTab(dockPanel);

        if (empty) setActiveDockPanel(dockPanel);
    }

    public void addDockPanel(IDockPanel dockPanel, int index) {
        boolean empty = dockPanels.isEmpty();
        if (empty) {
            dockPanels.add(dockPanel);
            createTab(dockPanel);
        } else {
            dockPanels.add(index, dockPanel);
            recreateTabs();
        }

        if (empty) setActiveDockPanel(dockPanel);
    }

    public void removeDockPanel(IDockPanel dockPanel) {
        dockPanels.remove(dockPanel);
        stack.remove(dockPanel.getWidget());

        if (!dockPanels.isEmpty()) {
            recreateTabs();
            if (activeDockPanel == dockPanel) {
                setActiveDockPanel(dockPanels.getFirst());
            }
        } else {
            window.close();
        }
    }

    public List<IDockPanel> getDockPanels() {
        return dockPanels;
    }

    private void setTitle(String title) {
        window.setTitle(title);
        titleLabel.setText(title);
        icon.setTooltipMarkup(title);
    }

    private void createTab(IDockPanel dockPanel) {
        stack.addChild(dockPanel.getWidget());

        DockTabButton tabButton = new DockTabButton();
        tabButton.setIconName(dockPanel.getIcon());
        tabButton.setTooltipText(dockPanel.getTitle());

        tabButton.dockWindow = this;
        tabButton.dockPanel = dockPanel;

        tabsBarBox.append(tabButton);

        tabButton.onClicked(() -> onTabClicked(dockPanel));

        DragSource dragSource = new DragSource();
        Value valueSource = new Value();
        valueSource.init(DockTabButton.getType());
        valueSource.setObject(tabButton);

        dragSource.setContent(ContentProvider.forValue(valueSource));
        dragSource.setActions(DragAction.MOVE);
        dragSource.onDragBegin((drag) -> onTabDragBegin(drag, dockPanel));
        dragSource.onDragEnd((drag, success) -> onTabDragEnd(drag, success, dockPanel));

        tabButton.addController(dragSource);
    }

    private void recreateTabs() {
        Widget child;
        while ((child = tabsBarBox.getFirstChild()) != null) {
            tabsBarBox.remove(child);
        }
        while ((child = stack.getFirstChild()) != null) {
            stack.remove(child);
        }

        for (IDockPanel dockPanel : dockPanels) {
            createTab(dockPanel);
        }
    }

    private void onTabClicked(IDockPanel dockPanel) {
        setActiveDockPanel(dockPanel);
    }

    private void onTabDragBegin(Drag drag, IDockPanel dockPanel) {
        if (drag != null) {
            Button dragButton = Button.fromIconName(dockPanel.getIcon());
            DragIcon dragIcon = DragIcon.getForDrag(drag);
            dragIcon.setChild(dragButton);
        }
    }

    private void onTabDragEnd(Drag drag, boolean success, IDockPanel dockPanel) {
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
    }

    private boolean onTabDrop(Value value, double x, double y) {
        if (value != null) {
            if (value.getObject() instanceof DockTabButton tabButton) {
                DockWindow dockWindow = tabButton.dockWindow;
                IDockPanel dockPanel = tabButton.dockPanel;
                int width = tabsBarBox.getWidth();
                int size = getDockPanels().size();

                int index = (int) Math.round(x / width * size);
                index = Math.clamp(index, 0, size);
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
    }
}
