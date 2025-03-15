package com.artkuznet.converter.util;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.maxed.LvlPolygon.VertexEdge;
import com.artkuznet.converter.maxed.LvlPolygon.VertexPolygon;

import java.util.*;
import java.util.stream.Collectors;

public class PolygonGrouper {

    private static final double EPSILON = 1e-6;

    public static List<List<VertexPolygon>> groupPolygons(List<VertexPolygon> vertexPolygons) {
        return vertexPolygons.stream()
                .collect(Collectors.groupingBy(p -> new GroupKey(p.materialName, p.bitmapName, p.normal)))
                .values().stream()
                .flatMap(group -> {
                    Map<VertexPolygon, List<VertexPolygon>> adjacency = buildAdjacency(group);
                    return findConnectedComponents(group, adjacency).stream();
                })
                .collect(Collectors.toList());
    }

    private static Map<VertexPolygon, List<VertexPolygon>> buildAdjacency(List<VertexPolygon> polygons) {
        Map<VertexPolygon, List<VertexPolygon>> adjacency = new HashMap<>();
        for (VertexPolygon p : polygons) {
            adjacency.put(p, new ArrayList<>());
            for (VertexPolygon q : polygons) {
                if (p != q && haveSharedEdges(p, q)) {
                    adjacency.get(p).add(q);
                }
            }
        }
        return adjacency;
    }

    private static boolean haveSharedEdges(VertexPolygon a, VertexPolygon b) {
        return a.edges.stream().anyMatch(edgeA ->
                b.edges.stream().anyMatch(edgeB -> edgesMatch(edgeA, edgeB) && a.uvNormal.equals(b.uvNormal))
        );
    }

    private static List<List<VertexPolygon>> findConnectedComponents(
            List<VertexPolygon> polygons,
            Map<VertexPolygon, List<VertexPolygon>> adjacency
    ) {
        Set<VertexPolygon> visited = new HashSet<>();
        List<List<VertexPolygon>> components = new ArrayList<>();
        for (VertexPolygon p : polygons) {
            if (!visited.contains(p)) {
                List<VertexPolygon> component = new ArrayList<>();
                dfs(p, adjacency, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    private static void dfs(
            VertexPolygon p, Map<VertexPolygon, List<VertexPolygon>> adjacency,
            Set<VertexPolygon> visited, List<VertexPolygon> component
    ) {
        visited.add(p);
        component.add(p);
        for (VertexPolygon neighbor : adjacency.get(p)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, adjacency, visited, component);
            }
        }
    }

    private static boolean edgesMatch(VertexEdge e1, VertexEdge e2) {
        return (vectorsEqual(e1.v1, e2.v1) && vectorsEqual(e1.v2, e2.v2) &&
                uvEqual(e1.uv1, e2.uv1) && uvEqual(e1.uv2, e2.uv2)) ||
                (vectorsEqual(e1.v1, e2.v2) && vectorsEqual(e1.v2, e2.v1) &&
                        uvEqual(e1.uv1, e2.uv2) && uvEqual(e1.uv2, e2.uv1));
    }

    private static boolean vectorsEqual(Vector3D v1, Vector3D v2) {
        return Math.abs(v1.getX() - v2.getX()) < EPSILON &&
                Math.abs(v1.getY() - v2.getY()) < EPSILON &&
                Math.abs(v1.getZ() - v2.getZ()) < EPSILON;
    }

    private static boolean uvEqual(VertexUV uv1, VertexUV uv2) {
        return Math.abs(uv1.getU() - uv2.getU()) < EPSILON &&
                Math.abs(uv1.getV() - uv2.getV()) < EPSILON;
    }

    private static class GroupKey {
        final String materialName;
        final String bitmapName;
        final Vector3D normal;

        GroupKey(String materialName, String bitmapName, Vector3D normal) {
            this.materialName = materialName;
            this.bitmapName = bitmapName;
            this.normal = new Vector3D(
                    round(normal.getX()),
                    round(normal.getY()),
                    round(normal.getZ())
            );
        }

        private double round(double value) {
            return Math.round(value / EPSILON) * EPSILON;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GroupKey groupKey = (GroupKey) o;
            return Objects.equals(materialName, groupKey.materialName) &&
                    Objects.equals(bitmapName, groupKey.bitmapName) &&
                    vectorsEqual(normal, groupKey.normal);
        }

        @Override
        public int hashCode() {
            return Objects.hash(materialName, bitmapName,
                    round(normal.getX()), round(normal.getY()), round(normal.getZ()));
        }
    }
}
