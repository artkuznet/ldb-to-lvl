package com.artkuznet.converter.ldb2;

import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexUV;

import java.util.List;

public interface Shape {

    List<Integer> getIndices();

    int getMaterialId();

    List<VertexUV> getUvs();

    List<Vertex> getVertices();
}
