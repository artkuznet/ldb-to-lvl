package com.artkuznet.converter.lv2.converter.helper;

import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.ldb2.staticmesh.StaticMesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeshDummy {

    private static final float DEFAULT_SIZE = 0.2f;

    public static List<StaticMesh> generate() {
        return generate(DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE);
    }

    public static List<StaticMesh> generate(float size) {
        return generate(size, size, size);
    }

    public static List<StaticMesh> generate(float sizeX, float sizeY, float sizeZ) {
        float halfX = sizeX / 2.0f;
        float halfY = sizeY / 2.0f;
        float halfZ = sizeZ / 2.0f;

        Vertex center = new Vertex(0, 0, 0);

        float cx = center.getX();
        float cy = center.getY();
        float cz = center.getZ();

        Vertex v0 = new Vertex(cx - halfX, cy - halfY, cz - halfZ);
        Vertex v1 = new Vertex(cx + halfX, cy - halfY, cz - halfZ);
        Vertex v2 = new Vertex(cx + halfX, cy + halfY, cz - halfZ);
        Vertex v3 = new Vertex(cx - halfX, cy + halfY, cz - halfZ);
        Vertex v4 = new Vertex(cx - halfX, cy - halfY, cz + halfZ);
        Vertex v5 = new Vertex(cx + halfX, cy - halfY, cz + halfZ);
        Vertex v6 = new Vertex(cx + halfX, cy + halfY, cz + halfZ);
        Vertex v7 = new Vertex(cx - halfX, cy + halfY, cz + halfZ);

        List<Vertex> vertices = new ArrayList<>();
        List<Vertex> normals = new ArrayList<>();
        List<VertexUV> uvs = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        addFace(vertices, normals, uvs, indices, v0, v3, v2, v1, new Vertex(0, 0, -1));
        addFace(vertices, normals, uvs, indices, v4, v5, v6, v7, new Vertex(0, 0, 1));
        addFace(vertices, normals, uvs, indices, v0, v1, v5, v4, new Vertex(0, -1, 0));
        addFace(vertices, normals, uvs, indices, v3, v7, v6, v2, new Vertex(0, 1, 0));
        addFace(vertices, normals, uvs, indices, v0, v4, v7, v3, new Vertex(-1, 0, 0));
        addFace(vertices, normals, uvs, indices, v1, v2, v6, v5, new Vertex(1, 0, 0));

        return Collections.singletonList(new StaticMesh(vertices, normals, indices, 0, uvs, Collections.emptyList(), Collections.emptyList()));
    }

    public static List<StaticMesh> generate(float sizeX, float sizeZ) {
        float halfX = sizeX / 2.0f;
        float halfZ = sizeZ / 2.0f;

        Vertex center = new Vertex(0, 0, 0);
        float cx = center.getX();
        float cy = center.getY();
        float cz = center.getZ();

        Vertex v0 = new Vertex(cx - halfX, cy, cz - halfZ);
        Vertex v1 = new Vertex(cx + halfX, cy, cz - halfZ);
        Vertex v2 = new Vertex(cx + halfX, cy, cz + halfZ);
        Vertex v3 = new Vertex(cx - halfX, cy, cz + halfZ);

        List<Vertex> vertices = new ArrayList<>();
        List<Vertex> normals = new ArrayList<>();
        List<VertexUV> uvs = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

//        addFace(vertices, normals, uvs, indices, v0, v1, v2, v3, new Vertex(0, -1, 0));
        addFace(vertices, normals, uvs, indices, v3, v2, v1, v0, new Vertex(0, 1, 0));

        return Collections.singletonList(new StaticMesh(vertices, normals, indices, 0, uvs, Collections.emptyList(), Collections.emptyList()));
    }

    private static void addFace(
            List<Vertex> vertices,
            List<Vertex> normals,
            List<VertexUV> uvs,
            List<Integer> indices,
            Vertex v0,
            Vertex v1,
            Vertex v2,
            Vertex v3,
            Vertex normal
    ) {
        int startIndex = vertices.size();

        vertices.add(v0);
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);

        for (int i = 0; i < 4; i++) {
            normals.add(normal);
        }

        uvs.add(new VertexUV(0, 0));
        uvs.add(new VertexUV(1, 0));
        uvs.add(new VertexUV(1, 1));
        uvs.add(new VertexUV(0, 1));

        indices.add(startIndex);
        indices.add(startIndex + 1);
        indices.add(startIndex + 2);

        indices.add(startIndex);
        indices.add(startIndex + 2);
        indices.add(startIndex + 3);
    }
}
