package com.artkuznet.converter.util;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.maxed.LvlPolygon.VertexEdge;
import com.artkuznet.converter.maxed.LvlPolygon.VertexPolygon;

import java.util.*;

public class ContourFinder {

    public static List<List<VertexEdge>> findContours(List<VertexPolygon> vertexPolygons) {
        Map<NormalizedEdge, Integer> edgeCounts = new HashMap<>();
        for (VertexPolygon polygon : vertexPolygons) {
            for (VertexEdge edge : polygon.edges) {
                NormalizedEdge normalized = new NormalizedEdge(edge.v1, edge.v2);
                edgeCounts.put(normalized, edgeCounts.getOrDefault(normalized, 0) + 1);
            }
        }

        List<VertexEdge> contourEdges = new ArrayList<>();
        for (VertexPolygon polygon : vertexPolygons) {
            for (VertexEdge edge : polygon.edges) {
                NormalizedEdge normalized = new NormalizedEdge(edge.v1, edge.v2);
                if (edgeCounts.get(normalized) == 1) {
                    contourEdges.add(edge);
                }
            }
        }

        Map<Vector3D, List<VertexEdge>> adjacency = new HashMap<>();
        for (VertexEdge edge : contourEdges) {
            adjacency.computeIfAbsent(edge.v1, k -> new ArrayList<>()).add(edge);
            adjacency.computeIfAbsent(edge.v2, k -> new ArrayList<>()).add(edge);
        }

        List<List<VertexEdge>> contours = new ArrayList<>();
        Set<VertexEdge> remainingEdges = new HashSet<>(contourEdges);

        while (!remainingEdges.isEmpty()) {
            Iterator<VertexEdge> iterator = remainingEdges.iterator();
            VertexEdge startEdge = iterator.next();
            remainingEdges.remove(startEdge);

            List<VertexEdge> contour = new ArrayList<>();
            contour.add(startEdge);

            Vector3D currentVertex = startEdge.v2;
            Vector3D startVertex = startEdge.v1;

            while (true) {
                List<VertexEdge> adjacent = adjacency.getOrDefault(currentVertex, Collections.emptyList());
                VertexEdge nextEdge = null;
                for (VertexEdge edge : adjacent) {
                    if (remainingEdges.contains(edge)) {
                        nextEdge = edge;
                        break;
                    }
                }

                if (nextEdge == null) {
                    break;
                }

                contour.add(nextEdge);
                remainingEdges.remove(nextEdge);

                if (nextEdge.v1.equals(currentVertex)) {
                    currentVertex = nextEdge.v2;
                } else {
                    currentVertex = nextEdge.v1;
                }

                if (currentVertex.equals(startVertex)) {
                    break;
                }
            }

            contours.add(contour);
        }

        return contours;
    }

    private static class NormalizedEdge {
        private final Vector3D first;
        private final Vector3D second;

        public NormalizedEdge(Vector3D v1, Vector3D v2) {
            if (compare(v1, v2) < 0) {
                this.first = v1;
                this.second = v2;
            } else {
                this.first = v2;
                this.second = v1;
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
            NormalizedEdge that = (NormalizedEdge) o;
            return first.equals(that.first) && second.equals(that.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }
}
