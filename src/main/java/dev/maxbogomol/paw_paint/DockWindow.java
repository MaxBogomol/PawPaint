package dev.maxbogomol.paw_paint;

import org.gnome.gdk.ContentProvider;
import org.gnome.gdk.Drag;
import org.gnome.gdk.DragAction;
import org.gnome.gio.Menu;
import org.gnome.gio.SimpleAction;
import org.gnome.gio.SimpleActionGroup;
import org.gnome.glib.Variant;
import org.gnome.gobject.Value;
import org.gnome.gtk.*;
import org.gnome.pango.EllipsizeMode;
import org.javagi.glib.types.VariantTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DockWindow {
    private final List<IDockPanel> dockPanels = new ArrayList<>();
    private IDockPanel activeDockPanel;

    private final Window mainWindow;
    private final Window window;
    private final HeaderBar headerBar;
    private final Box mainWidgetBox;
    private final boolean isWindow;

    private final Label titleLabel;
    private final Button icon;
    private final MenuButton settingsButton;

    private final Box barBox;
    private final Box tabsBarBox;
    private final Box settingsBox;
    private final ScrolledWindow scrolledTabsBar;
    private final Stack mainStack;
    private final Box mainBox;

    private Menu settingsSettingsPositionMenu;

    private PositionType barPosition = PositionType.TOP;

    private Align settingsPosition = Align.END;
    private boolean headerBarShow = true;
    private Orientation barOrientation = Orientation.HORIZONTAL;
    private Align barAlign = Align.START;
    private boolean barBoxPositionFront = true;
    private StackTransitionType dockPanelsAnimation = StackTransitionType.SLIDE_LEFT_RIGHT;

    public DockWindow(Window mainWindow, boolean isWindow) {
        this.mainWindow = mainWindow;
        this.isWindow = isWindow;

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
        settingsButton.setHasFrame(false);

        mainStack = new Stack();

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

        mainWidgetBox = new Box(Orientation.VERTICAL, 0);

        if (isWindow) {
            window.setDefaultSize(200, 200);
            window.setTitlebar(headerBar);
            window.setChild(mainBox);
            window.setTransientFor(mainWindow);
            window.present();
        } else {
            headerBar.setShowTitleButtons(false);
            mainWidgetBox.append(headerBar);
            mainWidgetBox.append(mainBox);
        }

        setTitle("Dock window");

        if (!isWindow) {
            headerBarShow = false;
        }

        createSettingsMenu();

        setupSettingsPosition();
        setupBarOrientation();
        setupBarBoxPosition();
        setupDockPanelsAnimation();
        setupHeaderBarShow();
    }

    public DockWindow(Window mainWindow) {
        this(mainWindow, true);
    }

    private void createSettingsMenu() {
        PopoverMenu settingsPopoverMenu = new PopoverMenu();
        settingsPopoverMenu.setFlags(PopoverMenuFlags.NESTED);

        Menu settingsMainMenu = new Menu();

        //Settings position
        settingsSettingsPositionMenu = new Menu();
        setupSettingsSettingsPositionMenu(headerBarShow);

        //Header bar show
        Menu settingsHeaderBarShowMenu = new Menu();
        settingsHeaderBarShowMenu.append("Show", "menu.header-show::show");
        settingsHeaderBarShowMenu.append("Hide", "menu.header-show::hide");

        //Bar
        Menu settingsBarMenu = new Menu();

        Menu settingsBarPositionMenu = new Menu();
        settingsBarPositionMenu.append("Left", "menu.bar-position::left");
        settingsBarPositionMenu.append("Right", "menu.bar-position::right");
        settingsBarPositionMenu.append("Top", "menu.bar-position::top");
        settingsBarPositionMenu.append("Bottom", "menu.bar-position::bottom");

        Menu settingsBarAlignMenu = new Menu();
        settingsBarAlignMenu.append("Start", "menu.bar-align::start");
        settingsBarAlignMenu.append("Center", "menu.bar-align::center");
        settingsBarAlignMenu.append("End", "menu.bar-align::end");

        settingsBarMenu.appendSubmenu("Position", settingsBarPositionMenu);
        settingsBarMenu.appendSubmenu("Align", settingsBarAlignMenu);

        //Dock-panels
        Menu settingsDockPanelsMenu = new Menu();

        Menu settingsDockPanelsAnimationMenu = new Menu();
        settingsDockPanelsAnimationMenu.append("None", "menu.dock-panels-animation::none");
        settingsDockPanelsAnimationMenu.append("Fade", "menu.dock-panels-animation::fade");
        settingsDockPanelsAnimationMenu.append("Left-Right", "menu.dock-panels-animation::left-right");
        settingsDockPanelsAnimationMenu.append("Up-Down", "menu.dock-panels-animation::up-down");

        settingsDockPanelsMenu.appendSubmenu("Animation", settingsDockPanelsAnimationMenu);
        settingsDockPanelsMenu.append("Reverse", "menu.dock-panels-reverse");

        //Main
        settingsMainMenu.appendSubmenu("Position", settingsSettingsPositionMenu);
        if (!isWindow) settingsMainMenu.appendSubmenu("Header", settingsHeaderBarShowMenu);
        settingsMainMenu.appendSubmenu("Bar", settingsBarMenu);
        settingsMainMenu.appendSubmenu("Dock-panels", settingsDockPanelsMenu);

        settingsPopoverMenu.setMenuModel(settingsMainMenu);
        settingsButton.setPopover(settingsPopoverMenu);

        createSettingMenuActions(settingsPopoverMenu);
    }

    private void setupSettingsSettingsPositionMenu(boolean header) {
        settingsSettingsPositionMenu.removeAll();
        settingsSettingsPositionMenu.append("Start", "menu.settings-position::start");
        settingsSettingsPositionMenu.append("End", "menu.settings-position::end");
        if (header) settingsSettingsPositionMenu.append("Header", "menu.settings-position::header");
    }

    private void createSettingMenuActions(PopoverMenu popoverMenu) {
        SimpleAction actionSettingsPosition = SimpleAction.stateful("settings-position", VariantTypes.STRING, Variant.string("end"));
        actionSettingsPosition.onActivate(parameter -> {
            if (parameter != null) {
                String value = parameter.getString(null);
                actionSettingsPosition.setState(Variant.string(value));

                switch (value) {
                    case "start": {
                        settingsPosition = Align.START;
                        break;
                    }
                    case "end": {
                        settingsPosition = Align.END;
                        break;
                    }
                    case "header": {
                        settingsPosition = Align.CENTER;
                        break;
                    }
                }

                setupSettingsPosition();
            }
        });

        SimpleAction actionHeaderBarShow = SimpleAction.stateful("header-show", VariantTypes.STRING, Variant.string("hide"));
        actionHeaderBarShow.onActivate(parameter -> {
            if (parameter != null) {
                String value = parameter.getString(null);
                actionHeaderBarShow.setState(Variant.string(value));

                switch (value) {
                    case "show": {
                        headerBarShow = true;
                        break;
                    }
                    case "hide": {
                        headerBarShow = false;
                        if (settingsPosition == Align.CENTER) {
                            settingsPosition = Align.END;
                            actionSettingsPosition.setState(Variant.string("end"));
                            setupSettingsPosition();
                        }
                        break;
                    }
                }

                setupSettingsSettingsPositionMenu(headerBarShow);
                setupHeaderBarShow();
            }
        });

        SimpleAction actionBarPosition = SimpleAction.stateful("bar-position", VariantTypes.STRING, Variant.string("top"));
        actionBarPosition.onActivate(parameter -> {
            if (parameter != null) {
                String value = parameter.getString(null);
                actionBarPosition.setState(Variant.string(value));

                switch (value) {
                    case "left": {
                        barPosition = PositionType.LEFT;
                        break;
                    }
                    case "right": {
                        barPosition = PositionType.RIGHT;
                        break;
                    }
                    case "top": {
                        barPosition = PositionType.TOP;
                        break;
                    }
                    case "bottom": {
                        barPosition = PositionType.BOTTOM;
                        break;
                    }
                }

                applyBarPosition();
                setupBarOrientation();
                setupBarBoxPosition();
            }
        });

        SimpleAction actionBarAlign = SimpleAction.stateful("bar-align", VariantTypes.STRING, Variant.string("start"));
        actionBarAlign.onActivate(parameter -> {
            if (parameter != null) {
                String value = parameter.getString(null);
                actionBarAlign.setState(Variant.string(value));

                switch (value) {
                    case "start": {
                        barAlign = Align.START;
                        break;
                    }
                    case "center": {
                        barAlign = Align.CENTER;
                        break;
                    }
                    case "end": {
                        barAlign = Align.END;
                        break;
                    }
                }

                setupBarOrientation();
            }
        });

        SimpleAction actionDockPanelsAnimation = SimpleAction.stateful("dock-panels-animation", VariantTypes.STRING, Variant.string("left-right"));
        actionDockPanelsAnimation.onActivate(parameter -> {
            if (parameter != null) {
                String value = parameter.getString(null);
                actionDockPanelsAnimation.setState(Variant.string(value));

                switch (value) {
                    case "none": {
                        dockPanelsAnimation = StackTransitionType.NONE;
                        break;
                    }
                    case "fade": {
                        dockPanelsAnimation = StackTransitionType.CROSSFADE;
                        break;
                    }
                    case "left-right": {
                        dockPanelsAnimation = StackTransitionType.SLIDE_LEFT_RIGHT;
                        break;
                    }
                    case "up-down": {
                        dockPanelsAnimation = StackTransitionType.SLIDE_UP_DOWN;
                        break;
                    }
                }

                setupDockPanelsAnimation();
            }
        });

        SimpleAction actionDockPanelsReverse = new SimpleAction("dock-panels-reverse", null);
        actionDockPanelsReverse.onActivate(parameter -> {
            Collections.reverse(dockPanels);
            recreateTabs();
            setActiveDockPanel(dockPanels.getFirst());
        });

        SimpleActionGroup customGroup = new SimpleActionGroup();
        customGroup.addAction(actionSettingsPosition);
        customGroup.addAction(actionHeaderBarShow);
        customGroup.addAction(actionBarPosition);
        customGroup.addAction(actionBarAlign);
        customGroup.addAction(actionDockPanelsAnimation);
        customGroup.addAction(actionDockPanelsReverse);

        popoverMenu.insertActionGroup("menu", customGroup);
    }

    private void setupSettingsPosition(Align align) {
        scrolledTabsBar.unparent();
        settingsBox.unparent();

        switch (align) {
            case START: {
                barBox.append(settingsBox);
                barBox.append(scrolledTabsBar);
                break;
            }
            case END: {
                barBox.append(scrolledTabsBar);
                barBox.append(settingsBox);
                break;
            }
            case CENTER: {
                barBox.append(scrolledTabsBar);
                headerBar.packEnd(settingsBox);
                break;
            }
        }
    }

    private void setupHeaderBarShow(boolean show) {
        headerBar.setVisible(show);
    }

    private void setupBarOrientation(Orientation orientation, Align align) {
        barBox.setOrientation(orientation);
        tabsBarBox.setOrientation(orientation);
        mainBox.setOrientation(GuiUtil.oppositeOrientation(orientation));

        if (orientation == Orientation.HORIZONTAL) {
            tabsBarBox.setHexpand(true);
            tabsBarBox.setVexpand(false);
            tabsBarBox.setHalign(align);
            tabsBarBox.setValign(Align.FILL);

            scrolledTabsBar.setPolicy(PolicyType.AUTOMATIC, PolicyType.NEVER);

            mainBox.setOrientation(Orientation.VERTICAL);
        } else {
            tabsBarBox.setHexpand(false);
            tabsBarBox.setVexpand(true);
            tabsBarBox.setHalign(Align.FILL);
            tabsBarBox.setValign(align);

            scrolledTabsBar.setPolicy(PolicyType.NEVER, PolicyType.AUTOMATIC);
        }
    }

    private void setupBarBoxPosition(boolean front) {
        barBox.unparent();
        mainStack.unparent();

        if (front) {
            mainBox.append(barBox);
            mainBox.append(mainStack);
        } else {
            mainBox.append(mainStack);
            mainBox.append(barBox);
        }
    }

    private void setupDockPanelsAnimation(StackTransitionType stackTransitionType) {
        mainStack.setTransitionType(stackTransitionType);
    }

    private void applyBarPosition(PositionType positionType) {
        switch (positionType) {
            case LEFT: {
                barOrientation = Orientation.VERTICAL;
                barBoxPositionFront = true;
                break;
            }
            case RIGHT: {
                barOrientation = Orientation.VERTICAL;
                barBoxPositionFront = false;
                break;
            }
            case TOP: {
                barOrientation = Orientation.HORIZONTAL;
                barBoxPositionFront = true;
                break;
            }
            case BOTTOM: {
                barOrientation = Orientation.HORIZONTAL;
                barBoxPositionFront = false;
                break;
            }
        }
    }

    private void setupSettingsPosition() {
        setupSettingsPosition(settingsPosition);
    }

    private void setupHeaderBarShow() {
        setupHeaderBarShow(headerBarShow);
    }

    private void setupBarOrientation() {
        setupBarOrientation(barOrientation, barAlign);
    }

    private void setupBarBoxPosition() {
        setupBarBoxPosition(barBoxPositionFront);
    }

    private void applyBarPosition() {
        applyBarPosition(barPosition);
    }

    private void setupDockPanelsAnimation() {
        setupDockPanelsAnimation(dockPanelsAnimation);
    }

    private void setTitle(String title) {
        window.setTitle(title);
        titleLabel.setText(title);
        icon.setTooltipMarkup(title);
    }

    public void setActiveDockPanel(IDockPanel dockPanel) {
        activeDockPanel = dockPanel;
        icon.setIconName(dockPanel.getIcon());
        mainStack.setVisibleChild(dockPanel.getWidget());
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
            mainWidgetBox.unparent();
        }
    }

    public List<IDockPanel> getDockPanels() {
        return dockPanels;
    }

    public Box getMainWidgetBox() {
        return mainWidgetBox;
    }

    public boolean getIsWindow() {
        return isWindow;
    }

    private void createTab(IDockPanel dockPanel) {
        mainStack.addChild(dockPanel.getWidget());

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
        while ((child = mainStack.getFirstChild()) != null) {
            mainStack.remove(child);
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
            if (dockPanels.size() > 1 || !isWindow) {
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
