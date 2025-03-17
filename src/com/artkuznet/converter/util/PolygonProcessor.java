package com.artkuznet.converter.util;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.maxed.LvlPolygon.VertexPolygon;
import com.artkuznet.converter.maxed.LvlPolygon.VertexEdge;

import java.util.*;

public class PolygonProcessor {

    public static List<List<VertexPolygon>> processPolygons(List<VertexPolygon> vertexPolygons) {
        List<List<VertexPolygon>> result = new ArrayList<>();
        List<VertexPolygon> remaining = new ArrayList<>(vertexPolygons);

        while (true) {
            Map<VertexEdge, Integer> edgeCounts = countEdges(remaining);

            Set<VertexEdge> contourEdges = new HashSet<>();
            for (Map.Entry<VertexEdge, Integer> entry : edgeCounts.entrySet()) {
                if (entry.getValue() == 1) {
                    contourEdges.add(entry.getKey());
                }
            }

            Set<Vector3D> contourVertices = new HashSet<>();
            for (VertexEdge edge : contourEdges) {
                contourVertices.add(edge.v1);
                contourVertices.add(edge.v2);
            }

            List<VertexPolygon> currentGroup = new ArrayList<>();
            Iterator<VertexPolygon> iterator = remaining.iterator();
            while (iterator.hasNext()) {
                VertexPolygon polygon = iterator.next();
                if (!hasCommonVertices(polygon, contourVertices)) {
                    currentGroup.add(polygon);
                    iterator.remove();
                }
            }

            if (currentGroup.isEmpty()) {
                if (!remaining.isEmpty()) {
                    result.add(new ArrayList<>(remaining));
                }
                break;
            } else {
                result.add(currentGroup);
            }
        }

        return result;
    }

    public static VertexPolygon fixClockwise(VertexPolygon polygon) {
        if (polygon.edges.isEmpty()) {
            return polygon;
        }

        VerticesWithUVs verticesWithUVs = extractVerticesWithUVs(polygon.edges);
        List<Vector3D> vertices = verticesWithUVs.vertices;
        if (vertices.size() < 3) {
            return polygon;
        }

        Vector3D computedNormal = computeNewellNormal(vertices);
        double dot = dotProduct(computedNormal, polygon.normal);

        if (dot <= 0) {
            List<Vector3D> reversedVertices = new ArrayList<>(vertices);
            Collections.reverse(reversedVertices);
            List<VertexUV> reversedUVs = new ArrayList<>(verticesWithUVs.uvs);
            Collections.reverse(reversedUVs);

            List<VertexEdge> newEdges = createEdgesFromVerticesAndUVs(reversedVertices, reversedUVs);
            return createNewPolygon(
                    newEdges,
                    polygon.normal,
                    polygon.uvNormal,
                    polygon.bitmapName,
                    polygon.materialName,
                    polygon.index
            );
        } else {
            return polygon;
        }
    }

    private static boolean hasCommonVertices(VertexPolygon polygon, Set<Vector3D> contourVertices) {
        for (VertexEdge edge : polygon.edges) {
            if (contourVertices.contains(edge.v1) || contourVertices.contains(edge.v2)) {
                return true;
            }
        }
        return false;
    }

    private static Map<VertexEdge, Integer> countEdges(List<VertexPolygon> polygons) {
        Map<VertexEdge, Integer> counts = new HashMap<>();
        for (VertexPolygon polygon : polygons) {
            for (VertexEdge edge : polygon.edges) {
                counts.put(edge, counts.getOrDefault(edge, 0) + 1);
            }
        }
        return counts;
    }

    private static class VerticesWithUVs {
        List<Vector3D> vertices;
        List<VertexUV> uvs;

        VerticesWithUVs(List<Vector3D> vertices, List<VertexUV> uvs) {
            this.vertices = vertices;
            this.uvs = uvs;
        }
    }

    private static VerticesWithUVs extractVerticesWithUVs(List<VertexEdge> edges) {
        List<Vector3D> vertices = new ArrayList<>();
        List<VertexUV> uvs = new ArrayList<>();

        if (edges.isEmpty()) {
            return new VerticesWithUVs(vertices, uvs);
        }

        vertices.add(edges.get(0).v1);
        uvs.add(edges.get(0).uv1);

        for (VertexEdge edge : edges) {
            vertices.add(edge.v2);
            uvs.add(edge.uv2);
        }

        if (vertices.size() > 1 && areEqual(vertices.get(0), vertices.get(vertices.size() - 1))) {
            vertices.remove(vertices.size() - 1);
            uvs.remove(uvs.size() - 1);
        }

        return new VerticesWithUVs(vertices, uvs);
    }

    private static boolean areEqual(Vector3D a, Vector3D b) {
        return a.equals(b);
    }

    private static Vector3D computeNewellNormal(List<Vector3D> vertices) {
        double x = 0, y = 0, z = 0;
        int size = vertices.size();
        for (int i = 0; i < size; i++) {
            Vector3D current = vertices.get(i);
            Vector3D next = vertices.get((i + 1) % size);
            x += (current.getY() - next.getY()) * (current.getZ() + next.getZ());
            y += (current.getZ() - next.getZ()) * (current.getX() + next.getX());
            z += (current.getX() - next.getX()) * (current.getY() + next.getY());
        }
        return new Vector3D(x, y, z);
    }

    private static double dotProduct(Vector3D a, Vector3D b) {
        return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
    }

    private static List<VertexEdge> createEdgesFromVerticesAndUVs(List<Vector3D> vertices, List<VertexUV> uvs) {
        List<VertexEdge> edges = new ArrayList<>();
        int size = vertices.size();
        for (int i = 0; i < size; i++) {
            Vector3D v1 = vertices.get(i);
            Vector3D v2 = vertices.get((i + 1) % size);
            VertexUV uv1 = uvs.get(i);
            VertexUV uv2 = uvs.get((i + 1) % size);

            VertexEdge edge = new VertexEdge();
            edge.v1 = v1;
            edge.v2 = v2;
            edge.uv1 = uv1;
            edge.uv2 = uv2;
            edges.add(edge);
        }
        return edges;
    }

    private static VertexPolygon createNewPolygon(
            List<VertexEdge> edges,
            Vector3D normal,
            Vector3D uvNormal,
            String bitmapName,
            String materialName,
            short index
    ) {
        VertexPolygon polygon = new VertexPolygon();

        polygon.edges = edges;
        polygon.normal = normal;
        polygon.uvNormal = uvNormal;
        polygon.bitmapName = bitmapName;
        polygon.materialName = materialName;
        polygon.index = index;

        return polygon;
    }
}
