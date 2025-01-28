package com.artkuznet.converter.ldb.polygon;

import com.artkuznet.converter.ldb.lightmap.LightmapTexture;
import com.artkuznet.converter.ldb.material.Material;
import com.artkuznet.converter.ldb.vertex.Vertex;

public class Polygon {
    private int id;
    private int textureVertexIdx;
    private int numVertices;
    private Vertex normal;
    private int type; // unsigned;
    private Material material;
    private LightmapTexture lightmap;
    private float maxEdgeLength;
    private float maxAngle;
    private int smoothingGroup;

    public Polygon(
            int id,
            int textureVertexIdx,
            int numVertices,
            Vertex normal,
            int type,
            Material material,
            LightmapTexture lightmap,
            float maxEdgeLength,
            float maxAngle,
            int smoothingGroup
    ) {
        this.id = id;
        this.textureVertexIdx = textureVertexIdx;
        this.numVertices = numVertices;
        this.normal = normal;
        this.type = type;
        this.material = material;
        this.lightmap = lightmap;
        this.maxEdgeLength = maxEdgeLength;
        this.maxAngle = maxAngle;
        this.smoothingGroup = smoothingGroup;
    }

    public int getId() {
        return id;
    }

    public int getTextureVertexIdx() {
        return textureVertexIdx;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public Material getMaterial() {
        return material;
    }

    public Vertex getNormal() {
        return normal;
    }

    public int getType() {
        return type;
    }

    public LightmapTexture getLightmap() {
        return lightmap;
    }

    public float getMaxEdgeLength() {
        return maxEdgeLength;
    }

    public float getMaxAngle() {
        return maxAngle;
    }

    public int getSmoothingGroup() {
        return smoothingGroup;
    }
}
