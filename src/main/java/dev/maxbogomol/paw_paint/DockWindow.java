package dev.maxbogomol.paw_paint;

import org.gnome.gdk.ContentProvider;
import org.gnome.gdk.Drag;
import org.gnome.gdk.DragAction;
import org.gnome.gobject.Value;
import org.gnome.gtk.*;
import org.gnome.pango.EllipsizeMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DockWindow {
    private final List<IDockPanel> dockPanels = new ArrayList<>();
    private IDockPanel activeDockPanel;

    private final Window mainWindow;
    private final Window window;
    private final HeaderBar headerBar;

    private final Label titleLabel;
    private final Button icon;
    private final MenuButton settingsButton;

    private final Box barBox;
    private final Box tabsBarBox;
    private final Box settingsBox;
    private final ScrolledWindow scrolledTabsBar;
    private final Stack stack;
    private final Box mainBox;

    private Orientation barOrientation = Orientation.HORIZONTAL;
    private boolean barOrientationFront = true;
    private boolean barPositionFront = true;

    public DockWindow(Window mainWindow) {
        this.mainWindow = mainWindow;

        titleLabel = new Label();
        titleLabel.setEllipsize(EllipsizeMode.END);
        titleLabel.addCssClass("title");

        icon = Button.fromIconName("edit-cut-symbolic");
        icon.addCssClass("circular");
        icon.addCssClass("flat");

        headerBar = new HeaderBar();
        headerBar.setTitleWidget(titleLabel);
        headerBar.packStart(icon);

        settingsButton = new MenuButton();
        settingsButton.setIconName("open-menu-symbolic");
        settingsButton.setHalign(Align.CENTER);
        settingsButton.setValign(Align.CENTER);
        settingsButton.addCssClass("flat");

        stack = new Stack();
        stack.setTransitionType(StackTransitionType.SLIDE_LEFT_RIGHT);

        barBox = new Box(Orientation.HORIZONTAL, 0);
        barBox.addCssClass("pawpaint-tabs-bar");

        tabsBarBox = new Box(Orientation.HORIZONTAL, 0);
        tabsBarBox.addCssClass("linked");

        DropTarget dropTarget = new DropTarget(DockTabButton.getType(), DragAction.MOVE);
        dropTarget.onDrop(this::onTabDrop);
        tabsBarBox.addController(dropTarget);

        scrolledTabsBar = new ScrolledWindow();
        scrolledTabsBar.setChild(tabsBarBox);

        settingsBox = new Box(Orientation.HORIZONTAL, 0);
        settingsBox.append(settingsButton);

        mainBox = new Box(Orientation.VERTICAL, 0);

        window = new Window();
        window.setDefaultSize(200, 200);
        window.setTitlebar(headerBar);
        window.setChild(mainBox);
        window.setTransientFor(mainWindow);
        window.present();

        setTitle("Dock window");

        setupBarOrientation(barOrientation, barOrientationFront);
        setupBarPosition(barPositionFront);
        
        Popover popoverMenu = new Popover();

        Box mainScreen = new Box(Orientation.VERTICAL, 2);

        Button button1 = new Button();
        button1.setLabel("Reverse");
        button1.addCssClass("flat");
        button1.onClicked(() -> {
            Collections.reverse(dockPanels);
            recreateTabs();
            setActiveDockPanel(dockPanels.getFirst());
        });

        Button button2 = new Button();
        button2.setLabel("bwap");
        button2.addCssClass("flat");
        button2.onClicked(() -> {
            barPositionFront = !barPositionFront;
            setupBarPosition(barPositionFront);
        });

        Button button3 = new Button();
        button3.setLabel("bwap2");
        button3.addCssClass("flat");
        button3.onClicked(() -> {
            barOrientationFront = !barOrientationFront;
            setupBarOrientation(barOrientation, barOrientationFront);
        });

        Button button4 = new Button();
        button4.setLabel("bwap3");
        button4.addCssClass("flat");
        button4.onClicked(() -> {
            if (barOrientation == Orientation.HORIZONTAL) {
                barOrientation = Orientation.VERTICAL;
            } else {
                barOrientation = Orientation.HORIZONTAL;
            }
            setupBarOrientation(barOrientation, barOrientationFront);
        });

        mainScreen.append(button1);
        mainScreen.append(button2);
        mainScreen.append(button3);
        mainScreen.append(button4);

        popoverMenu.setChild(mainScreen);

        settingsButton.setPopover(popoverMenu);
    }

    private void setupBarOrientation(Orientation orientation, boolean front) {
        barBox.setOrientation(orientation);
        tabsBarBox.setOrientation(orientation);

        if (orientation == Orientation.HORIZONTAL) {
            tabsBarBox.setHexpand(true);
            tabsBarBox.setVexpand(false);
            tabsBarBox.setHalign(front ? Align.START : Align.END);
            tabsBarBox.setValign(Align.FILL);

            scrolledTabsBar.setPolicy(PolicyType.AUTOMATIC, PolicyType.NEVER);

            mainBox.setOrientation(Orientation.VERTICAL);
        } else {
            tabsBarBox.setHexpand(false);
            tabsBarBox.setVexpand(true);
            tabsBarBox.setHalign(Align.FILL);
            tabsBarBox.setValign(front ? Align.START : Align.END);

            scrolledTabsBar.setPolicy(PolicyType.NEVER, PolicyType.AUTOMATIC);

            mainBox.setOrientation(Orientation.HORIZONTAL);
        }

        scrolledTabsBar.unparent();
        settingsBox.unparent();

        if (front) {
            barBox.append(scrolledTabsBar);
            barBox.append(settingsBox);
        } else {
            barBox.append(settingsBox);
            barBox.append(scrolledTabsBar);
        }
    }

    private void setupBarPosition(boolean front) {
        barBox.unparent();
        stack.unparent();

        if (front) {
            mainBox.append(barBox);
            mainBox.append(stack);
        } else {
            mainBox.append(stack);
            mainBox.append(barBox);
        }
    }

    private void setTitle(String title) {
        window.setTitle(title);
        titleLabel.setText(title);
        icon.setTooltipMarkup(title);
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

        recreateTabs();

        if (!dockPanels.isEmpty()) {
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

    private void createTab(IDockPanel dockPanel) {
        stack.addChild(dockPanel.getWidget());

        DockTabButton tabButton = new DockTabButton();
        tabButton.setIconName(dockPanel.getIcon());
        tabButton.setTooltipText(dockPanel.getTitle());
        tabButton.setHalign(Align.CENTER);
        tabButton.setValign(Align.CENTER);

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
                int height = tabsBarBox.getHeight();
                int size = getDockPanels().size();
                boolean add = true;

                double pos = (tabsBarBox.getOrientation() == Orientation.HORIZONTAL) ? x : y;
                double posTotal = (tabsBarBox.getOrientation() == Orientation.HORIZONTAL) ? width : height;
                int index = (posTotal > 0) ? (int) Math.round(pos / posTotal * size) : 0;
                index = Math.clamp(index, 0, size);

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
