package com.artkuznet.converter.ldb.vertex;

import java.util.ArrayList;
import java.util.List;

public class VertexContainer {

    private List<Vertex> vertices = new ArrayList<>();

    public void add(Vertex vertex) {
        vertices.add(vertex);
    }

    public List<Vertex> getList() {
        return vertices;
    }
}
