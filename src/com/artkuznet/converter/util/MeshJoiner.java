package com.artkuznet.converter.util;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.maxed.LvlPolygon;
import com.artkuznet.converter.maxed.Mesh;

import java.util.*;

public class MeshJoiner {

    public static List<List<Mesh>> joinMeshes(List<Mesh> meshes) {
        int n = meshes.size();
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        Map<String, List<Integer>> edgeMap = new HashMap<>();

        for (int i = 0; i < meshes.size(); i++) {
            Mesh mesh = meshes.get(i);
            Set<String> currentEdges = computeMeshEdges(mesh);
            for (String edgeKey : currentEdges) {
                int finalI = i;
                edgeMap.computeIfAbsent(edgeKey, k -> new ArrayList<>())
                        .forEach(j -> union(parent, finalI, j));
                edgeMap.computeIfAbsent(edgeKey, k -> new ArrayList<>()).add(i);
            }
        }

        Map<Integer, List<Mesh>> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = find(parent, i);
            groups.computeIfAbsent(root, k -> new ArrayList<>()).add(meshes.get(i));
        }

        return new ArrayList<>(groups.values());
    }

    private static Set<String> computeMeshEdges(Mesh mesh) {
        Set<String> edges = new HashSet<>();
        for (LvlPolygon polygon : mesh.getPolygons()) {
            String material = polygon.getMaterialName();
            String bitmap = polygon.getBitmapName();
            for (LvlPolygon.Edge edge : polygon.getEdges()) {
                Vector3D v1 = mesh.getVertices()[edge.getFrom()];
                Vector3D v2 = mesh.getVertices()[edge.getTo()];
                edges.add(createCompositeKey(material, bitmap, v1, v2));
            }
        }
        return edges;
    }

    private static String createCompositeKey(String material, String bitmap, Vector3D a, Vector3D b) {
        String edgeKey = canonicalEdgeKey(a, b);
        return material + "|" + bitmap + "|" + edgeKey;
    }

    private static String canonicalEdgeKey(Vector3D a, Vector3D b) {
        int hashA = a.hashCode();
        int hashB = b.hashCode();
        return hashA <= hashB ? hashA + "|" + hashB : hashB + "|" + hashA;
    }

    private static int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]);
        return parent[x];
    }

    private static void union(int[] parent, int x, int y) {
        int fx = find(parent, x);
        int fy = find(parent, y);
        if (fx != fy) parent[fy] = fx;
    }
}
