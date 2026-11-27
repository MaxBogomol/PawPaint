package dev.maxbogomol.paw_paint.core.canvas.layer;

import dev.maxbogomol.paw_paint.core.color.format.ColorFormat;

import java.util.ArrayList;
import java.util.List;

public class LayerGroup extends LayerNode {
    private final List<LayerNode> layerNodes = new ArrayList<>();

    public LayerGroup(ColorFormat colorFormat, String name) {
        super(colorFormat, name);
    }

    public void addNode(LayerNode layerNode) {
        layerNodes.add(layerNode);
    }

    @Override
    public void blend(LayerNode node) {
        blend();
        blend(node,0, 0);
    }

    public void blend() {
        for (LayerNode layerNode : layerNodes) {
            layerNode.blend(this);
        }
    }
}
