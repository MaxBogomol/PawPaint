package dev.maxbogomol.paw_paint;

import org.gnome.gtk.*;

import java.util.ArrayList;
import java.util.List;

public class DockWindow {
    private final List<DockPanel> dockPanels = new ArrayList<>();
    private DockPanel activeDockPanel;

    private final Window window;
    private final Label titleLabel;
    private final HeaderBar headerBar;
    private final Button icon;
    private final Button button;
    private final Stack stack;
    private final ScrolledWindow scrolledBar;

    public DockWindow(Window mainWindow) {
        titleLabel = new Label();
        titleLabel.addCssClass("title");

        icon = Button.fromIconName("edit-cut-symbolic");
        icon.setCanTarget(false);

        button = Button.fromIconName("document-properties-symbolic");

        stack = new Stack();

        StackSwitcher stackSwitcher = new StackSwitcher();
        stackSwitcher.setStack(stack);

        scrolledBar = new ScrolledWindow();
        scrolledBar.setChild(stackSwitcher);
        scrolledBar.setPolicy(PolicyType.AUTOMATIC, PolicyType.NEVER);
        scrolledBar.setPropagateNaturalWidth(true);

        headerBar = new HeaderBar();
        headerBar.setTitleWidget(titleLabel);
        headerBar.packStart(icon);
        headerBar.packEnd(button);
        headerBar.setShowTitleButtons(false);

        window = new Window();
        window.setTitlebar(headerBar);
        window.setTransientFor(mainWindow);
        window.present();

        setTitle("Dock window");
    }

    private void setTitle(String title) {
        window.setTitle(title);
        titleLabel.setText(title);
    }

    public boolean isEmpty() {
        return dockPanels.isEmpty();
    }

    public void addDockPanel(DockPanel dockPanel) {
        boolean empty = isEmpty();
        dockPanels.add(dockPanel);

        if (empty) setActiveDockPanel(dockPanel);

        if (dockPanels.size() > 1) {
            headerBar.packStart(scrolledBar);
            icon.setVisible(false);
            titleLabel.setVisible(false);
        }

        Label label = new Label();
        StackPage stackPage = stack.addChild(label);
        stackPage.setTitle(dockPanel.getTitle());
        stackPage.setIconName("edit-cut-symbolic");
    }

    public void setActiveDockPanel(DockPanel dockPanel) {
        activeDockPanel = dockPanel;
        setTitle(dockPanel.getTitle());
    }
}
