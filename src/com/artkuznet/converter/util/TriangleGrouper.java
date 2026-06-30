package com.artkuznet.converter.util;


import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.lv2.converter.helper.LdbTriangleDTO;

import java.util.*;

public class TriangleGrouper {

    private static class Edge {
        private final Vector3D v1;
        private final Vector3D v2;

        public Edge(Vector3D v1, Vector3D v2) {
            List<Vector3D> sorted = Arrays.asList(v1, v2);
            sorted.sort(Comparator.comparingDouble(Vector3D::getX).thenComparingDouble(Vector3D::getY).thenComparingDouble(Vector3D::getZ));
            this.v1 = sorted.get(0);
            this.v2 = sorted.get(1);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return v1.equals(edge.v1) && v2.equals(edge.v2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(v1, v2);
        }
    }

    private static class TriangleOnEdge {
        final LdbTriangleDTO triangle;
        final Edge edge;

        TriangleOnEdge(LdbTriangleDTO triangle, Edge edge) {
            this.triangle = triangle;
            this.edge = edge;
        }

        double getArea() {
            return triangle.getArea();
        }
    }

    public static List<List<LdbTriangleDTO>> groupTriangles(List<LdbTriangleDTO> triangles) {
        if (triangles == null || triangles.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Edge, List<TriangleOnEdge>> edgeToTriangles = new HashMap<>();

        for (LdbTriangleDTO triangle : triangles) {
            List<Vector3D> vertices = triangle.getVertices();
            for (int i = 0; i < 3; i++) {
                Vector3D v1 = vertices.get(i);
                Vector3D v2 = vertices.get((i + 1) % 3);
                Edge edge = new Edge(v1, v2);

                edgeToTriangles.computeIfAbsent(edge, k -> new ArrayList<>())
                        .add(new TriangleOnEdge(triangle, edge));
            }
        }

        Map<LdbTriangleDTO, Set<LdbTriangleDTO>> adjacencyGraph = new HashMap<>();

        for (Map.Entry<Edge, List<TriangleOnEdge>> entry : edgeToTriangles.entrySet()) {
            List<TriangleOnEdge> trianglesOnEdge = entry.getValue();

            if (trianglesOnEdge.size() < 2) {
                continue;
            }

            trianglesOnEdge.sort((a, b) -> Double.compare(b.getArea(), a.getArea()));
            processCandidatesForEdge(trianglesOnEdge, adjacencyGraph);
        }

        return findConnectedComponents(adjacencyGraph, triangles);
    }

    private static void processCandidatesForEdge(List<TriangleOnEdge> trianglesOnEdge,
                                                 Map<LdbTriangleDTO, Set<LdbTriangleDTO>> adjacencyGraph) {

        int maxPairsToCheck = Math.min(trianglesOnEdge.size(), 4);

        List<int[]> pairs = new ArrayList<>();
        for (int i = 0; i < maxPairsToCheck; i++) {
            for (int j = i + 1; j < maxPairsToCheck; j++) {
                pairs.add(new int[]{i, j});
            }
        }

        pairs.sort((a, b) -> {
            double sumA = trianglesOnEdge.get(a[0]).getArea() + trianglesOnEdge.get(a[1]).getArea();
            double sumB = trianglesOnEdge.get(b[0]).getArea() + trianglesOnEdge.get(b[1]).getArea();
            return Double.compare(sumB, sumA);
        });

        for (int[] pair : pairs) {
            LdbTriangleDTO t1 = trianglesOnEdge.get(pair[0]).triangle;
            LdbTriangleDTO t2 = trianglesOnEdge.get(pair[1]).triangle;

            if (areTrianglesConnectedAndNotTwisted(t1, t2)) {
                Set<LdbTriangleDTO> connections1 = adjacencyGraph.get(t1);
                Set<LdbTriangleDTO> connections2 = adjacencyGraph.get(t2);

                if ((connections1 == null || !connections1.contains(t2)) &&
                        (connections2 == null || !connections2.contains(t1))) {

                    connectTriangles(t1, t2, adjacencyGraph);
                    return;
                }
            }
        }
    }

    private static void connectTriangles(LdbTriangleDTO t1, LdbTriangleDTO t2,
                                         Map<LdbTriangleDTO, Set<LdbTriangleDTO>> adjacencyGraph) {
        adjacencyGraph.computeIfAbsent(t1, k -> new HashSet<>()).add(t2);
        adjacencyGraph.computeIfAbsent(t2, k -> new HashSet<>()).add(t1);
    }

    private static List<List<LdbTriangleDTO>> findConnectedComponents(
            Map<LdbTriangleDTO, Set<LdbTriangleDTO>> adjacencyGraph,
            List<LdbTriangleDTO> allTriangles) {

        List<List<LdbTriangleDTO>> components = new ArrayList<>();
        Set<LdbTriangleDTO> visited = new HashSet<>();

        for (LdbTriangleDTO triangle : allTriangles) {
            if (!visited.contains(triangle)) {
                List<LdbTriangleDTO> component = new ArrayList<>();
                dfs(triangle, adjacencyGraph, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private static void dfs(LdbTriangleDTO current,
                            Map<LdbTriangleDTO, Set<LdbTriangleDTO>> adjacencyGraph,
                            Set<LdbTriangleDTO> visited,
                            List<LdbTriangleDTO> component) {

        visited.add(current);
        component.add(current);

        Set<LdbTriangleDTO> neighbors = adjacencyGraph.get(current);
        if (neighbors != null) {
            for (LdbTriangleDTO neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, adjacencyGraph, visited, component);
                }
            }
        }
    }


    static boolean areTrianglesConnectedAndNotTwisted(
            LdbTriangleDTO t1,
            LdbTriangleDTO t2
    ) {
        List<Vector3D> commonEdge = findCommonEdge(t1, t2);
        if (commonEdge.size() != 2) {
            return false;
        }

        return areOrientationsConsistent(t1, t2, commonEdge);
    }

    private static boolean areOrientationsConsistent(
            LdbTriangleDTO t1,
            LdbTriangleDTO t2,
            List<Vector3D> edge
    ) {
        Vector3D A = edge.get(0);
        Vector3D B = edge.get(1);

        Vector3D C1 = getThirdVertex(t1, edge);
        Vector3D C2 = getThirdVertex(t2, edge);

        if (C1 == null || C2 == null) {
            return false;
        }

        boolean t1UsesAB = usesEdgeDirection(t1, A, B);
        boolean t2UsesAB = usesEdgeDirection(t2, A, B);

        return t1UsesAB != t2UsesAB;
    }

    private static boolean usesEdgeDirection(
            LdbTriangleDTO t,
            Vector3D A,
            Vector3D B
    ) {
        List<Vector3D> v = t.getVertices();

        for (int i = 0; i < 3; i++) {
            Vector3D curr = v.get(i);
            Vector3D next = v.get((i + 1) % 3);

            if (curr.equals(A) && next.equals(B)) {
                return true;
            }
            if (curr.equals(B) && next.equals(A)) {
                return false;
            }
        }
        return false;
    }

    private static List<Vector3D> findCommonEdge(
            LdbTriangleDTO t1,
            LdbTriangleDTO t2
    ) {
        List<Vector3D> common = new ArrayList<>();

        for (Vector3D v : t1.getVertices()) {
            if (t2.getVertices().contains(v)) {
                common.add(v);
            }
        }
        return common;
    }

    private static Vector3D getThirdVertex(
            LdbTriangleDTO t,
            List<Vector3D> edge
    ) {
        for (Vector3D v : t.getVertices()) {
            if (!edge.contains(v)) {
                return v;
            }
        }
        return null;
    }
}
