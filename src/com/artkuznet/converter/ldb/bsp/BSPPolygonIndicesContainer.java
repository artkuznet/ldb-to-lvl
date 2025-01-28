package com.artkuznet.converter.ldb.bsp;

import java.util.ArrayList;
import java.util.List;

public class BSPPolygonIndicesContainer {
    private List<BSPPolygonIndex> indices = new ArrayList<>();

    public void add(BSPPolygonIndex index) {
        indices.add(index);
    }
}
