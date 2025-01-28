package com.artkuznet.converter.ldb.polygon;

import java.util.ArrayList;
import java.util.List;

public class PolygonContainer {
    private List<Polygon> polygons = new ArrayList<>();

    public void add(Polygon polygon) {
        polygons.add(polygon);
    }

    public List<Polygon> getList() {
        return polygons;
    }

    public Polygon getById(int id) {
        return polygons.stream().filter(p -> p.getId() == id).findFirst().orElseThrow();
    }
}
