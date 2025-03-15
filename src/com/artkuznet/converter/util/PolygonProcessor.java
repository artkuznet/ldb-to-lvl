package com.artkuznet.converter.util;

import com.artkuznet.converter.Vector3D;
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
}
