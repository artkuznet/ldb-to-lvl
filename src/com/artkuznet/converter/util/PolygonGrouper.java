package com.artkuznet.converter.util;

import com.artkuznet.converter.Options;
import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.maxed.LvlPolygon;
import com.artkuznet.converter.maxed.LvlPolygon.VertexEdge;
import com.artkuznet.converter.maxed.LvlPolygon.VertexPolygon;

import java.util.*;
import java.util.stream.Collectors;

public class PolygonGrouper {

    private static final double EPSILON = 1e-6;

    public static List<List<VertexPolygon>> groupPolygons(List<VertexPolygon> vertexPolygons) {
        return vertexPolygons.stream()
                .collect(Collectors.groupingBy(p -> new GroupKey(p.materialName, p.bitmapName, p.normal, p.uvNormal)))
                .values().stream()
                .flatMap(group -> {
                    Map<VertexPolygon, List<VertexPolygon>> adjacency = buildAdjacency(group);
                    return findConnectedComponents(group, adjacency).stream();
                })
//                .map(PolygonGrouper::split)
//                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<List<VertexPolygon>> split(List<VertexPolygon> vertexPolygons) {
        if (Options.getInstance().skipJoinPolygons) {
            return vertexPolygons.stream().map(Arrays::asList).collect(Collectors.toList());
        }

        List<List<VertexEdge>> contours = ContourFinder.findContours(vertexPolygons);

        if (contours.size() == 1) {
            Set<Vector3D> vertices = vertexPolygons.stream()
                    .map(p -> p.edges)
                    .flatMap(List::stream)
                    .map(e -> e.v1)
                    .collect(Collectors.toSet());

            List<LvlPolygon.VertexEdge> edges = contours.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            Set<Vector3D> vEdgesVerticesFrom = edges.stream().map(e -> e.v1).collect(Collectors.toSet());
            if (vertices.stream().anyMatch(v -> !vEdgesVerticesFrom.contains(v))) {
                return vertexPolygons.stream().map(Arrays::asList).collect(Collectors.toList());
            }
        }

        return Collections.singletonList(vertexPolygons);
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
        return a.edges.stream().anyMatch(edgeA -> b.edges.stream().anyMatch(edgeB -> edgesMatch(edgeA, edgeB)));
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
        final Vector3D uvNormal;

        GroupKey(String materialName, String bitmapName, Vector3D normal, Vector3D uvNormal) {
            this.materialName = materialName;
            this.bitmapName = bitmapName;
            this.normal = new Vector3D(
                    round(normal.getX()),
                    round(normal.getY()),
                    round(normal.getZ())
            );
            this.uvNormal = new Vector3D(
                    round(uvNormal.getX()),
                    round(uvNormal.getY()),
                    round(uvNormal.getZ())
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
                    vectorsEqual(normal, groupKey.normal) &&
                    vectorsEqual(uvNormal, groupKey.uvNormal);
        }

        @Override
        public int hashCode() {
            return Objects.hash(materialName, bitmapName,
                    round(normal.getX()), round(normal.getY()), round(normal.getZ()),
                    round(uvNormal.getX()), round(uvNormal.getY()), round(uvNormal.getZ()));
        }
    }
}
