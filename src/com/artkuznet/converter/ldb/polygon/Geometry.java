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
    private List<VertexUV> lightmapUv;
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
            List<VertexUV> lightmapUv,
            Material material
    ) {
        this.polygonId = polygonId;
        this.vertices = vertices;
        this.normals = normals;
        this.uv = uv;
        this.lightmapUv = lightmapUv;
        this.material = material;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<VertexUV> getUv() {
        return uv;
    }

    public List<VertexUV> getLightmapUv() {
        return lightmapUv;
    }

    public Material getMaterial() {
        return material;
    }

    public double[] getTextureOffset() {
        return new double[]{uv.get(0).getU(), uv.get(0).getV()};
    }

    public Vector3D getFirstVertex() {
        Vertex v = vertices.get(0);

        return new Vector3D(v.getX(), v.getY(), v.getZ());
    }
}
