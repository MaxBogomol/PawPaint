package dev.maxbogomol.paw_paint;

import org.gnome.gtk.*;
import org.gnome.pango.EllipsizeMode;

import java.util.ArrayList;
import java.util.List;

public class DockWindow {
    private final List<IDockPanel> dockPanels = new ArrayList<>();
    private IDockPanel activeDockPanel;

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
        titleLabel.setEllipsize(EllipsizeMode.END);

        icon = Button.fromIconName("edit-cut-symbolic");

        button = Button.fromIconName("document-properties-symbolic");

        stack = new Stack();
        stack.setTransitionType(StackTransitionType.SLIDE_LEFT_RIGHT);

        StackSwitcher stackSwitcher = new StackSwitcher();
        stackSwitcher.setStack(stack);

        scrolledBar = new ScrolledWindow();
        scrolledBar.setChild(stackSwitcher);
        scrolledBar.setPolicy(PolicyType.AUTOMATIC, PolicyType.NEVER);
        scrolledBar.setPropagateNaturalWidth(true);
        scrolledBar.setVisible(false);

        headerBar = new HeaderBar();
        headerBar.setTitleWidget(titleLabel);
        headerBar.packStart(icon);
        headerBar.packStart(scrolledBar);
        headerBar.packEnd(button);
        headerBar.setShowTitleButtons(false);

        window = new Window();
        window.setTitlebar(headerBar);
        window.setChild(stack);
        window.setTransientFor(mainWindow);
        window.present();

        setTitle("Dock window");
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

        if (dockPanels.size() > 1) {
            scrolledBar.setVisible(true);
            icon.setVisible(false);
            titleLabel.setVisible(false);
        }

        StackPage stackPage = stack.addChild(dockPanel.getWidget());
        stackPage.setTitle(dockPanel.getTitle());
        stackPage.setIconName("edit-cut-symbolic");
    }

    public void setActiveDockPanel(IDockPanel dockPanel) {
        activeDockPanel = dockPanel;
        setTitle(dockPanel.getTitle());
    }
}
