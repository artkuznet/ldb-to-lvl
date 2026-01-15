package com.artkuznet.converter.ldb2.staticmesh;

import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexUV;

import java.util.List;

public class StaticMesh {
    private List<Vertex> vertices;
    private List<Vertex> normals;
    private List<Integer> indices;
    private int materialId;
    private List<VertexUV> uvs;
    private List<VertexUV> lightmapUVs;
    private List<VertexUV> detailTextureUVs;

    public StaticMesh(
            List<Vertex> vertices,
            List<Vertex> normals,
            List<Integer> indices,
            int materialId,
            List<VertexUV> uvs,
            List<VertexUV> lightmapUVs,
            List<VertexUV> detailTextureUVs
    ) {
        this.vertices = vertices;
        this.normals = normals;
        this.indices = indices;
        this.materialId = materialId;
        this.uvs = uvs;
        this.lightmapUVs = lightmapUVs;
        this.detailTextureUVs = detailTextureUVs;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Vertex> getNormals() {
        return normals;
    }

    public void setNormals(List<Vertex> normals) {
        this.normals = normals;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public List<VertexUV> getUvs() {
        return uvs;
    }

    public void setUvs(List<VertexUV> uvs) {
        this.uvs = uvs;
    }

    public List<VertexUV> getLightmapUVs() {
        return lightmapUVs;
    }

    public void setLightmapUVs(List<VertexUV> lightmapUVs) {
        this.lightmapUVs = lightmapUVs;
    }

    public List<VertexUV> getDetailTextureUVs() {
        return detailTextureUVs;
    }

    public void setDetailTextureUVs(List<VertexUV> detailTextureUVs) {
        this.detailTextureUVs = detailTextureUVs;
    }
}
