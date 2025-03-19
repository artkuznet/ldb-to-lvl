package com.artkuznet.converter.ldb.dynamicmesh;

import com.artkuznet.converter.ldb.animation.AnimationContainer;
import com.artkuznet.converter.ldb.polygon.Geometry;
import com.artkuznet.converter.ldb.polygon.Polygon;
import com.artkuznet.converter.ldb.polygon.PolygonContainer;
import com.artkuznet.converter.ldb.property.EntityProperties;
import com.artkuznet.converter.ldb.texture.TextureVertex;
import com.artkuznet.converter.ldb.texture.TextureVertexContainer;
import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexContainer;
import com.artkuznet.converter.ldb.vertex.VertexUV;

import java.util.ArrayList;
import java.util.List;

public class LdbDynamicMesh {
    private String sharedName;
    private EntityProperties properties;
    private VertexContainer vertices;
    private VertexContainer normals;
    private float[][] transform;
    private PolygonContainer polygons;
    private AnimationContainer animations;
    private DynamicMeshConfig config;
    private TextureVertexContainer textureVertices;

    public LdbDynamicMesh(
            String sharedName,
            EntityProperties properties,
            VertexContainer vertices,
            VertexContainer normals,
            float[][] transform,
            PolygonContainer polygons,
            AnimationContainer animations,
            DynamicMeshConfig config
    ) {
        this.sharedName = sharedName;
        this.properties = properties;
        this.vertices = vertices;
        this.normals = normals;
        this.transform = transform;
        this.polygons = polygons;
        this.animations = animations;
        this.config = config;
    }

    public void setTextureVertices(TextureVertexContainer textureVertices) {
        this.textureVertices = textureVertices;
    }

    public Geometry constructPolygon(int id) {
        Polygon polygon = polygons.getList().get(id);

        List<Vertex> vertices = new ArrayList<>();
        List<Vertex> normals = new ArrayList<>();
        List<VertexUV> uv = new ArrayList<>();
        List<VertexUV> lightmapUv = new ArrayList<>();

        for (int i = 0; i < polygon.getNumVertices(); i++) {
            TextureVertex textureVertex = this.textureVertices.getList().get(polygon.getTextureVertexIdx() + i);
            vertices.add(this.vertices.getList().get(textureVertex.getVertexIdx()));
            normals.add(this.normals.getList().get(textureVertex.getVertexIdx()));
            uv.add(textureVertex.getUV());
            lightmapUv.add(textureVertex.getLightmapUV());
        }

        return new Geometry(polygon.getId(), vertices, normals, uv, lightmapUv, polygon.getMaterial());
    }

    public String getSharedName() {
        return sharedName;
    }

    public String getShortName() {
        String name = sharedName.substring(sharedName.lastIndexOf("::") + 2);
        name =  name.substring(0, name.length() - 3);

        if(name.length() > Byte.MAX_VALUE) {
            throw new RuntimeException(name);
        }

        return name;
    }

    public String getRoomName() {
        return sharedName.substring(0, sharedName.substring(2).indexOf("::") + 2);
    }

    public EntityProperties getProperties() {
        return properties;
    }

    public VertexContainer getVertices() {
        return vertices;
    }

    public VertexContainer getNormals() {
        return normals;
    }

    public float[][] getTransform() {
        return transform;
    }

    public double[][] getTransformDouble() {
        return new double[][]{
                new double[]{transform[0][0], transform[0][1], transform[0][2]},
                new double[]{transform[1][0], transform[1][1], transform[1][2]},
                new double[]{transform[2][0], transform[2][1], transform[2][2]},
                new double[]{transform[3][0], transform[3][1], transform[3][2]},
        };
    }

    public PolygonContainer getPolygons() {
        return polygons;
    }

    public AnimationContainer getAnimations() {
        return animations;
    }

    public DynamicMeshConfig getConfig() {
        return config;
    }

    public TextureVertexContainer getTextureVertices() {
        return textureVertices;
    }
}
