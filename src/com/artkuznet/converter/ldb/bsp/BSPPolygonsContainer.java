package com.artkuznet.converter.ldb.bsp;

import java.util.ArrayList;
import java.util.List;

public class BSPPolygonsContainer {

    private List<BSPPolygon> polygons = new ArrayList<>();

    public void add(BSPPolygon polygon) {
        polygons.add(polygon);
    }

    public List<BSPPolygon> getList() {
        return polygons;
    }
}
