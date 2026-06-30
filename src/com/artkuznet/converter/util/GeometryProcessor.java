package com.artkuznet.converter.util;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.lv2.converter.helper.LdbTriangleDTO;

import java.util.*;

public class GeometryProcessor {

    private static final double NORMAL_OPPOSITE_EPSILON = 1e-4;

    public static List<List<LdbTriangleDTO>> splitIntoRoomAndObjects(List<LdbTriangleDTO> triangles) {

        Map<Edge, List<LdbTriangleDTO>> edgeMap = new HashMap<>();

        for (LdbTriangleDTO triangle : triangles) {
            List<Vector3D> vertices = triangle.getVertices();
            for (int i = 0; i < 3; i++) {
                Vector3D v1 = vertices.get(i);
                Vector3D v2 = vertices.get((i + 1) % 3);
                Edge edge = new Edge(v1, v2);
                edgeMap.computeIfAbsent(edge, k -> new ArrayList<>()).add(triangle);
            }
        }

        Map<LdbTriangleDTO, List<LdbTriangleDTO>> graph = new HashMap<>();

        for (List<LdbTriangleDTO> edgeTriangles : edgeMap.values()) {
            if (edgeTriangles.size() == 2) {

                LdbTriangleDTO t1 = edgeTriangles.get(0);
                LdbTriangleDTO t2 = edgeTriangles.get(1);

                Vector3D n1 = t1.getNormal();
                Vector3D n2 = t2.getNormal();

                double dotProduct = n1.dotProduct(n2);

                if (dotProduct > -1.0 + NORMAL_OPPOSITE_EPSILON
                        && TriangleGrouper.areTrianglesConnectedAndNotTwisted(t1, t2)) {

                    graph.computeIfAbsent(t1, k -> new ArrayList<>()).add(t2);
                    graph.computeIfAbsent(t2, k -> new ArrayList<>()).add(t1);
                }
            }
        }

        List<List<LdbTriangleDTO>> components = new ArrayList<>();
        Set<LdbTriangleDTO> visited = new HashSet<>();

        for (LdbTriangleDTO triangle : triangles) {
            if (!visited.contains(triangle)) {

                List<LdbTriangleDTO> component = new ArrayList<>();
                Queue<LdbTriangleDTO> queue = new LinkedList<>();

                queue.add(triangle);
                visited.add(triangle);

                while (!queue.isEmpty()) {
                    LdbTriangleDTO current = queue.poll();
                    component.add(current);

                    for (LdbTriangleDTO neighbor :
                            graph.getOrDefault(current, Collections.emptyList())) {

                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }

                components.add(component);
            }
        }

        components.sort((a, b) -> Integer.compare(b.size(), a.size()));

        List<List<LdbTriangleDTO>> result = new ArrayList<>();
        if (!components.isEmpty()) {
            result.add(components.get(0));
            result.addAll(components.subList(1, components.size()));
        }

        return result;
    }

    static class Edge {

        private final Vector3D v1;
        private final Vector3D v2;

        public Edge(Vector3D a, Vector3D b) {
            if (compare(a, b) <= 0) {
                this.v1 = a;
                this.v2 = b;
            } else {
                this.v1 = b;
                this.v2 = a;
            }
        }

        private int compare(Vector3D a, Vector3D b) {
            int cmp = Double.compare(a.getX(), b.getX());
            if (cmp != 0) return cmp;

            cmp = Double.compare(a.getY(), b.getY());
            if (cmp != 0) return cmp;

            return Double.compare(a.getZ(), b.getZ());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return Objects.equals(v1, edge.v1)
                    && Objects.equals(v2, edge.v2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(v1, v2);
        }
    }
}
