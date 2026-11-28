package dev.maxbogomol.paw_paint.gui.dock_window.dock_panel;

import dev.maxbogomol.paw_paint.core.canvas.Canvas;
import dev.maxbogomol.paw_paint.core.canvas.layer.LayerGroup;
import dev.maxbogomol.paw_paint.core.canvas.layer.LayerNode;
import org.gnome.gtk.*;
import org.gnome.pango.EllipsizeMode;

import java.util.List;

public class LayersDockPanel implements IDockPanel {
    private Canvas canvas;

    private final Box box;

    private final Box rowsBox;

    public LayersDockPanel() {
        box = new Box(Orientation.HORIZONTAL, 0);
        box.setHexpand(true);
        box.setVexpand(true);

        rowsBox = new Box(Orientation.VERTICAL, 4);

        box.append(rowsBox);
    }

    @Override
    public String getTitle() {
        return "Layers";
    }

    @Override
    public String getIcon() {
        return "edit-cut-symbolic";
    }

    @Override
    public Widget getWidget() {
        return box;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        recreateRows();
    }

    private void createRow(LayerNode layerNode, int depth) {
        Box rowBox = new Box(Orientation.HORIZONTAL, 4);
        rowBox.setMarginStart(depth * 8);

        Button visibleButton = new Button();
        visibleButton.setIconName(layerNode.getVisible() ? "view-reveal-symbolic" : "view-conceal-symbolic");
        visibleButton.addCssClass("circular");
        visibleButton.addCssClass("flat");
        visibleButton.onClicked(() -> {
            boolean visible = !layerNode.getVisible();
            layerNode.setVisible(visible);
            visibleButton.setIconName(visible ? "view-reveal-symbolic" : "view-conceal-symbolic");
        });

        Label nameLabel = new Label(layerNode.getName());
        nameLabel.setEllipsize(EllipsizeMode.END);

        rowBox.append(visibleButton);
        rowBox.append(nameLabel);

        rowsBox.append(rowBox);
    }

    private void createRows(LayerGroup group, int depth) {
        List<LayerNode> nodes = group.getLayerNodes();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            LayerNode node = nodes.get(i);
            createRow(node, depth);

            if (node instanceof LayerGroup subGroup) {
                createRows(subGroup, depth + 1);
            }
        }
    }

    private void recreateRows() {
        Widget child;
        while ((child = rowsBox.getFirstChild()) != null) {
            rowsBox.remove(child);
        }

        if (canvas != null) {
            createRows(canvas.getMainLayerGroup(), 0);
        }
    }
}
