package com.artkuznet.converter.util;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.maxed.LvlPolygon;

import java.util.*;
import java.util.stream.Collectors;

public class MeshBuilder {
    public static List<List<LvlPolygon>> groupPolygons(List<LvlPolygon> lvlPolygons, List<Vector3D> lvlVertexList) {
        Map<EdgeKey, List<LvlPolygon>> edgeMap = new HashMap<>();

        for (LvlPolygon polygon : lvlPolygons) {
            for (LvlPolygon.Edge edge : polygon.getEdges()) {
                EdgeKey key = new EdgeKey(edge.getFrom(), edge.getTo());
                edgeMap.computeIfAbsent(key, k -> new ArrayList<>()).add(polygon);
            }
        }

        Map<LvlPolygon, Integer> polygonIndices = new HashMap<>();
        for (int i = 0; i < lvlPolygons.size(); i++) {
            polygonIndices.put(lvlPolygons.get(i), i);
        }

        int[] parent = new int[lvlPolygons.size()];
        int[] rank = new int[lvlPolygons.size()];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }

        for (List<LvlPolygon> polygons : edgeMap.values()) {
            if (polygons.size() == 2) {
                LvlPolygon p1 = polygons.get(0);
                LvlPolygon p2 = polygons.get(1);

                boolean isCollinearOpposite = areNormalsCollinearAndOpposite(p1.getNormal(), p2.getNormal());
                boolean bothDummy = "dummy".equalsIgnoreCase(p1.getMaterialName()) && "dummy".equalsIgnoreCase(p2.getMaterialName());

                if (!(isCollinearOpposite && bothDummy) && validMaterials(p1, p2)) {
                    Integer idx1 = polygonIndices.get(p1);
                    Integer idx2 = polygonIndices.get(p2);
                    if (idx1 != null && idx2 != null) {
                        union(parent, rank, idx1, idx2);
                    }
                }
            }
        }

        Map<Integer, List<LvlPolygon>> groups = new HashMap<>();
        for (int i = 0; i < lvlPolygons.size(); i++) {
            int root = find(parent, i);
            groups.computeIfAbsent(root, k -> new ArrayList<>()).add(lvlPolygons.get(i));
        }

        for (LvlPolygon polygon : lvlPolygons) {
            List<Vector3D> vertices = getOrderedVertices(polygon, lvlVertexList);
            double area = computeArea(vertices);
            polygon.setArea(area);
        }

        return new ArrayList<>(groups.values());
    }

    private static boolean validMaterials(LvlPolygon p1, LvlPolygon p2) {
        List<String> isolated = Arrays.asList("ai_node_collision_nodraw", "cameracollision", "charactercollision_nodraw");

        String m1 = p1.getMaterialName().toLowerCase();
        String m2 = p2.getMaterialName().toLowerCase();

        if (m1.equals(m2)) {
            return true;
        }

        return !isolated.contains(m1) && !isolated.contains(m2);
    }

    private static boolean areNormalsCollinearAndOpposite(Vector3D n1, Vector3D n2) {
        if (crossProduct(n1, n2).magnitude() > 1e-8) {
            return false;
        }

        return dotProduct(n1, n2) < -1e-8;
    }

    private static Vector3D crossProduct(Vector3D a, Vector3D b) {
        return new Vector3D(
                a.getY() * b.getZ() - a.getZ() * b.getY(),
                a.getZ() * b.getX() - a.getX() * b.getZ(),
                a.getX() * b.getY() - a.getY() * b.getX()
        );
    }

    private static double dotProduct(Vector3D a, Vector3D b) {
        return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
    }

    private static List<Vector3D> getOrderedVertices(LvlPolygon polygon, List<Vector3D> vertexList) {
        LvlPolygon.Edge[] edges = polygon.getEdges();
        if (edges.length < 3) {
            throw new IllegalArgumentException("Polygon must have at least 3 edges");
        }

        Map<Integer, List<Integer>> adjacency = new HashMap<>();
        for (LvlPolygon.Edge edge : edges) {
            adjacency.computeIfAbsent(edge.getFrom(), k -> new ArrayList<>()).add(edge.getTo());
            adjacency.computeIfAbsent(edge.getTo(), k -> new ArrayList<>()).add(edge.getFrom());
        }

        List<Integer> orderedIndices = new ArrayList<>();
        Integer startVertex = edges[0].getFrom();
        Integer currentVertex = startVertex;
        Integer prevVertex = null;

        orderedIndices.add(startVertex);

        for (int i = 0; i < edges.length; i++) {
            List<Integer> nextVertices = adjacency.get(currentVertex);
            if (nextVertices == null || nextVertices.isEmpty()) {
                throw new IllegalArgumentException("Edges do not form a closed loop");
            }

            Integer nextVertex = null;
            for (Integer next : nextVertices) {
                if (!next.equals(prevVertex)) {
                    nextVertex = next;
                    break;
                }
            }

            if (nextVertex == null) {
                nextVertex = nextVertices.get(0);
            }

            orderedIndices.add(nextVertex);
            prevVertex = currentVertex;
            currentVertex = nextVertex;

            if (currentVertex.equals(startVertex)) {
                break;
            }
        }

        if (!orderedIndices.isEmpty() && orderedIndices.get(orderedIndices.size() - 1).equals(startVertex)) {
            orderedIndices.remove(orderedIndices.size() - 1);
        }

        return orderedIndices.stream()
                .map(index -> vertexList.get(index))
                .collect(Collectors.toList());
    }

    private static double computeArea(List<Vector3D> vertices) {
        double sumX = 0.0, sumY = 0.0, sumZ = 0.0;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            Vector3D current = vertices.get(i);
            Vector3D next = vertices.get((i + 1) % n);
            sumX += current.getY() * next.getZ() - current.getZ() * next.getY();
            sumY += current.getZ() * next.getX() - current.getX() * next.getZ();
            sumZ += current.getX() * next.getY() - current.getY() * next.getX();
        }
        double magnitude = Math.sqrt(sumX * sumX + sumY * sumY + sumZ * sumZ);
        return 0.5 * magnitude;
    }

    private static int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }

    private static void union(int[] parent, int[] rank, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);
        if (rootX != rootY) {
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootY] > rank[rootX]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }

    private static class EdgeKey {
        private final int v1;
        private final int v2;

        EdgeKey(int a, int b) {
            this.v1 = Math.min(a, b);
            this.v2 = Math.max(a, b);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EdgeKey edgeKey = (EdgeKey) o;
            return v1 == edgeKey.v1 && v2 == edgeKey.v2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(v1, v2);
        }
    }
}
