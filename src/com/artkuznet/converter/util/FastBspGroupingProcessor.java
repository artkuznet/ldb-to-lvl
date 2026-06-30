package com.artkuznet.converter.util;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.lv2.converter.helper.LdbTriangleDTO;

import java.util.*;

public class FastBspGroupingProcessor {

    public static List<List<LdbTriangleDTO>> splitIntoValidBspGroups(
            List<LdbTriangleDTO> triangles
    ) {
        if (triangles == null || triangles.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Edge, List<LdbTriangleDTO>> edgeMap = buildEdgeMap(triangles);

        Map<LdbTriangleDTO, List<LdbTriangleDTO>> graph =
                buildAdjacencyGraph(edgeMap);

        return extractConnectedComponents(triangles, graph);
    }

    private static Map<Edge, List<LdbTriangleDTO>>
    buildEdgeMap(List<LdbTriangleDTO> triangles) {

        Map<Edge, List<LdbTriangleDTO>> edgeMap = new HashMap<>();

        for (LdbTriangleDTO t : triangles) {
            List<Vector3D> v = t.getVertices();
            for (int i = 0; i < 3; i++) {
                Edge e = new Edge(v.get(i), v.get((i + 1) % 3));
                edgeMap.computeIfAbsent(e, k -> new ArrayList<>()).add(t);
            }
        }
        return edgeMap;
    }

    private static Map<LdbTriangleDTO, List<LdbTriangleDTO>>
    buildAdjacencyGraph(Map<Edge, List<LdbTriangleDTO>> edgeMap) {

        Map<LdbTriangleDTO, List<LdbTriangleDTO>> graph = new HashMap<>();

        for (List<LdbTriangleDTO> list : edgeMap.values()) {
            if (list.size() != 2) continue;

            LdbTriangleDTO t1 = list.get(0);
            LdbTriangleDTO t2 = list.get(1);

            if (TriangleGrouper.areTrianglesConnectedAndNotTwisted(t1, t2)) {

                graph.computeIfAbsent(t1, k -> new ArrayList<>()).add(t2);
                graph.computeIfAbsent(t2, k -> new ArrayList<>()).add(t1);
            }
        }
        return graph;
    }

    private static List<List<LdbTriangleDTO>>
    extractConnectedComponents(
            List<LdbTriangleDTO> triangles,
            Map<LdbTriangleDTO, List<LdbTriangleDTO>> graph
    ) {
        List<List<LdbTriangleDTO>> result = new ArrayList<>();
        Set<LdbTriangleDTO> visited = new HashSet<>();

        for (LdbTriangleDTO t : triangles) {
            if (visited.contains(t)) continue;

            List<LdbTriangleDTO> component = new ArrayList<>();
            Queue<LdbTriangleDTO> queue = new ArrayDeque<>();

            queue.add(t);
            visited.add(t);

            while (!queue.isEmpty()) {
                LdbTriangleDTO current = queue.poll();
                component.add(current);

                for (LdbTriangleDTO neighbor :
                        graph.getOrDefault(current, Collections.emptyList())) {

                    if (visited.add(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }

            result.add(component);
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

        private static int compare(Vector3D a, Vector3D b) {
            int cmp = Double.compare(a.getX(), b.getX());
            if (cmp != 0) return cmp;
            cmp = Double.compare(a.getY(), b.getY());
            if (cmp != 0) return cmp;
            return Double.compare(a.getZ(), b.getZ());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Edge)) return false;
            return Objects.equals(v1, ((Edge) o).v1)
                    && Objects.equals(v2, ((Edge) o).v2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(v1, v2);
        }
    }
}
