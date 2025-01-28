package com.artkuznet.converter.ldb.bsp;

import com.artkuznet.converter.ldb.vertex.Vertex;

public class BSPPolygon {
    private int vertexIdx;
    private int numVertices;
    private int polygonId;
    private int staticMeshId;
    private Vertex normal;
    private Vertex pivot;

    public BSPPolygon(
            int vertexIdx,
            int numVertices,
            int polygonId,
            int staticMeshId,
            Vertex normal,
            Vertex pivot
    ) {
        this.vertexIdx = vertexIdx;
        this.numVertices = numVertices;
        this.polygonId = polygonId;
        this.staticMeshId = staticMeshId;
        this.normal = normal;
        this.pivot = pivot;
    }

    public int getVertexIdx() {
        return vertexIdx;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getPolygonId() {
        return polygonId;
    }

    public int getStaticMeshId() {
        return staticMeshId;
    }

    public Vertex getNormal() {
        return normal;
    }

    public Vertex getPivot() {
        return pivot;
    }
}
