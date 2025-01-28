package com.artkuznet.converter.ldb.bsp;

import com.artkuznet.converter.ldb.vertex.Vertex;

public class BSPVertex extends Vertex {
    public BSPVertex(float x, float y, float z) {
        super(x, y, z);
    }

    public BSPVertex(Vertex vertex) {
        super(vertex.getX(), vertex.getY(), vertex.getX());
    }
}
