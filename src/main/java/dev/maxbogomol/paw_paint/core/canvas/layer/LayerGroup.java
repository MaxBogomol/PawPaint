package dev.maxbogomol.paw_paint.core.canvas.layer;

import java.util.ArrayList;
import java.util.List;

public class LayerGroup extends LayerNode {
    private final List<LayerNode> layerNodes = new ArrayList<>();

    public LayerGroup(String name) {
        this.name = name;
    }
}
