package com.artkuznet.converter.ldb.bsp;

public class BSPContainer {
    private BSPVertexContainer vertices = new BSPVertexContainer();
    private BSPPolygonsContainer polygons = new BSPPolygonsContainer();
    private BSPNodesContainer nodes = new BSPNodesContainer();
    private BSPPolygonIndicesContainer indices = new BSPPolygonIndicesContainer();

    public BSPVertexContainer getVertices() {
        return vertices;
    }

    public BSPPolygonsContainer getPolygons() {
        return polygons;
    }

    public BSPNodesContainer getNodes() {
        return nodes;
    }

    public BSPPolygonIndicesContainer getIndices() {
        return indices;
    }
}
