package com.artkuznet.converter.ldb.bsp;

import java.util.ArrayList;
import java.util.List;

public class BSPVertexContainer {
    private List<BSPVertex> vertices = new ArrayList<>();

    public void add(BSPVertex vertex) {
        vertices.add(vertex);
    }
}
