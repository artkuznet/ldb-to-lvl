package com.artkuznet.converter.ldb.bsp;

import java.util.ArrayList;
import java.util.List;

public class BSPNodesContainer {

    private List<BSPNode> nodes = new ArrayList<>();

    public void add(BSPNode node) {
        nodes.add(node);
    }
}
