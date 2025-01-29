package com.artkuznet.converter.ldb.staticmesh;

import com.artkuznet.converter.ldb.polygon.Geometry;
import com.artkuznet.converter.ldb.polygon.Polygon;
import com.artkuznet.converter.ldb.polygon.PolygonContainer;
import com.artkuznet.converter.ldb.texture.TextureVertex;
import com.artkuznet.converter.ldb.texture.TextureVertexContainer;
import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexContainer;
import com.artkuznet.converter.ldb.vertex.VertexUV;

import java.util.ArrayList;
import java.util.List;

public class StaticMesh {
    private int staticMeshId;
    private VertexContainer vertices;
    private VertexContainer normals;
    private float[][] transform;
    private PolygonContainer polygons;
    private TextureVertexContainer textureVertices;

    public StaticMesh(
            int staticMeshId,
            VertexContainer vertices,
            VertexContainer normals,
            float[][] transform,
            PolygonContainer polygons
    ) {
        this.staticMeshId = staticMeshId;
        this.vertices = vertices;
        this.normals = normals;
        this.transform = transform;
        this.polygons = polygons;
    }

    public int getId() {
        return staticMeshId;
    }

    public void setTextureVertices(TextureVertexContainer textureVertices) {
        this.textureVertices = textureVertices;
    }

    public PolygonContainer getPolygons() {
        return polygons;
    }

    public double[][] getTransformDouble() {
        return new double[][]{
                new double[]{transform[0][0], transform[0][1], transform[0][2]},
                new double[]{transform[1][0], transform[1][1], transform[1][2]},
                new double[]{transform[2][0], transform[2][1], transform[2][2]},
                new double[]{transform[3][0], transform[3][1], transform[3][2]},
        };
    }

    public Geometry constructPolygon(int id) {
        Polygon polygon = polygons.getList().get(id);

        List<Vertex> vertices = new ArrayList<>();
        List<Vertex> normals = new ArrayList<>();
        List<VertexUV> uv = new ArrayList<>();

        for (int i = 0; i < polygon.getNumVertices(); i++) {
            TextureVertex textureVertex = this.textureVertices.getList().get(polygon.getTextureVertexIdx() + i);
            vertices.add(this.vertices.getList().get(textureVertex.getVertexIdx()));
            normals.add(this.normals.getList().get(textureVertex.getVertexIdx()));
            uv.add(textureVertex.getUV());
        }

        return new Geometry(polygon.getId(), vertices, normals, uv, polygon.getMaterial());
    }
}
