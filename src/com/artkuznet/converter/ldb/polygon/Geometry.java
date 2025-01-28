package com.artkuznet.converter.ldb.polygon;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.material.Material;
import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexUV;

import java.util.List;

public class Geometry {
    private List<Vertex> vertices;
    private List<Vertex> normals;
    private List<VertexUV> uv;
    private Material material;

    public int getPolygonId() {
        return polygonId;
    }

    private int polygonId;

    public Geometry(
            int polygonId,
            List<Vertex> vertices,
            List<Vertex> normals,
            List<VertexUV> uv,
            Material material
    ) {
        this.polygonId = polygonId;
        this.vertices = vertices;
        this.normals = normals;
        this.uv = uv;
        this.material = material;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<VertexUV> getUv() {
        return uv;
    }

    public Material getMaterial() {
        return material;
    }

    public VertexUV getUvSize() {
        var minU = uv.stream().map(VertexUV::getU).min(Float::compareTo).orElseThrow();
        var maxU = uv.stream().map(VertexUV::getU).max(Float::compareTo).orElseThrow();

        var minV = uv.stream().map(VertexUV::getV).min(Float::compareTo).orElseThrow();
        var maxV = uv.stream().map(VertexUV::getV).max(Float::compareTo).orElseThrow();

        return new VertexUV(maxU - minU, maxV - minV);
    }

    public double[] getTextureOffset() {
        var maxU = uv.stream().map(VertexUV::getU).max(Float::compareTo).orElseThrow();
        var maxV = uv.stream().map(VertexUV::getV).max(Float::compareTo).orElseThrow();

        maxU = uv.get(0).getU();
        maxV = uv.get(0).getV();

        return new double[]{maxU, maxV};
    }

    public Vector3D getFirstVertex() {
        var v = vertices.get(0);

        return new Vector3D(v.getX(), v.getY(), v.getZ());
    }
}
