package com.artkuznet.converter.util;

import com.artkuznet.converter.maxed.LvlPolygon;

import java.util.*;

public class PolyGroupAssigner {

    private static class EdgeKey {
        private final int from;
        private final int to;

        public EdgeKey(int from, int to) {
            this.from = Math.min(from, to);
            this.to = Math.max(from, to);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EdgeKey edgeKey = (EdgeKey) o;
            return from == edgeKey.from && to == edgeKey.to;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }

    public static void assignGeometryPolyGroups(LvlPolygon[] polygons) {
        Map<EdgeKey, List<Integer>> edgeMap = new HashMap<>();
        for (int i = 0; i < polygons.length; i++) {
            LvlPolygon poly = polygons[i];
            for (LvlPolygon.Edge edge : poly.getEdges()) {
                EdgeKey key = new EdgeKey(edge.getFrom(), edge.getTo());
                edgeMap.computeIfAbsent(key, k -> new ArrayList<>()).add(i);
            }
        }

        List<List<Integer>> graph = new ArrayList<>(polygons.length);
        for (int i = 0; i < polygons.length; i++) {
            graph.add(new ArrayList<>());
        }

        for (List<Integer> polyIndices : edgeMap.values()) {
            if (polyIndices.size() < 2) continue;

            for (int i = 0; i < polyIndices.size(); i++) {
                for (int j = i + 1; j < polyIndices.size(); j++) {
                    int p1Idx = polyIndices.get(i);
                    int p2Idx = polyIndices.get(j);
                    LvlPolygon p1 = polygons[p1Idx];
                    LvlPolygon p2 = polygons[p2Idx];

                    if (p1.getGeometryPolyGroup() != 0 || p2.getGeometryPolyGroup() != 0) continue;

                    double angle = p1.getNormal().angle(p2.getNormal());

                    if (angle >= 1 && angle <= 67) { // todo params
                        graph.get(p1Idx).add(p2Idx);
                        graph.get(p2Idx).add(p1Idx);
                    }
                }
            }
        }

        boolean[] visited = new boolean[polygons.length];
        int currentGroup = 0;

        for (int i = 0; i < polygons.length; i++) {
            if (visited[i] || polygons[i].getGeometryPolyGroup() != 0) continue;

            List<Integer> component = new ArrayList<>();
            Queue<Integer> queue = new LinkedList<>();
            queue.add(i);
            visited[i] = true;
            component.add(i);

            while (!queue.isEmpty()) {
                int current = queue.poll();
                for (int neighbor : graph.get(current)) {
                    if (!visited[neighbor] && polygons[neighbor].getGeometryPolyGroup() == 0) {
                        visited[neighbor] = true;
                        component.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }

            if (component.size() > 2) { // todo param
                currentGroup--;
                for (int idx : component) {
                    polygons[idx].setGeometryPolyGroup(currentGroup);
                }
            }
        }
    }
}
