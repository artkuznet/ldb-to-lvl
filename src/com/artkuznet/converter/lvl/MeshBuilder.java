package com.artkuznet.converter.lvl;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.maxed.LvlPolygon;

import java.util.*;
import java.util.stream.Collectors;

public class MeshBuilder {

    public static List<List<LvlPolygon>> groupPolygons(List<LvlPolygon> lvlPolygons, List<Vector3D> lvlVertexList, boolean containsRoom, String roomName) {
        Map<LvlPolygon.Edge, Integer> edgesPolygons = new HashMap<>();
        for (int i = 0; i < lvlPolygons.size(); i++) {
            for (int j = 0; j < lvlPolygons.get(i).getEdges().length; j++) {
                var edg = lvlPolygons.get(i).getEdges()[j];
                if (edgesPolygons.containsKey(edg)) {
                    edgesPolygons.put(edg, edgesPolygons.get(edg) + 1);
                } else {
                    edgesPolygons.put(edg, 1);
                }
            }
        }

        var megaEdges = edgesPolygons.entrySet().stream()
                .filter(edgeIntegerEntry -> edgeIntegerEntry.getValue() > 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        var singleEdges = edgesPolygons.entrySet().stream()
                .filter(edgeIntegerEntry -> edgeIntegerEntry.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        for (int j = 0; j < lvlPolygons.size(); j++) {
            var p1 = lvlPolygons.get(j);
            var poly1Edges = p1.getEdges();

            for (int i = 0; i < lvlPolygons.size(); i++) {
                if (i == j) {
                    continue;
                }

                var p2 = lvlPolygons.get(i);

                if (p1.geometryPolyGroup != 0
                        && p2.geometryPolyGroup != 0
                        && p1.geometryPolyGroup == p2.geometryPolyGroup
                ) {
                    p1.neighborIndices.add(p2.index);
                    continue;
                }

                var poly2Edges = p2.getEdges();

                var b = false;

                for (var e1 : poly1Edges) {
                    for (var e2 : poly2Edges) {

                        if ((megaEdges.contains(e1) || megaEdges.contains(e2)) && containsRoom) {
                            continue;
                        }

                        if ((e1.getFrom() == e2.getFrom() && e1.getTo() == e2.getTo())
                                || (e1.getFrom() == e2.getTo() && e1.getTo() == e2.getFrom())
                        ) {
                            b = true;
                            break;
                        }

                        var vtx1 = List.of(lvlVertexList.get(e1.getFrom()).clone(), lvlVertexList.get(e1.getTo()).clone());
                        var vtx2 = List.of(lvlVertexList.get(e2.getFrom()).clone(), lvlVertexList.get(e2.getTo()).clone());

                        var nodrawMaterials = List.of(
                                "ai_node_collision_nodraw",
                                "cameracollision",
                                "charactercollision_nodraw",
                                "dummy",
                                "Dummy"
                        );

                        if ((Vector3D.roughEquals(vtx1.get(0), vtx2.get(0))
                                || Vector3D.roughEquals(vtx1.get(1), vtx2.get(1))
                                || Vector3D.roughEquals(vtx1.get(0), vtx2.get(1))
                                || Vector3D.roughEquals(vtx1.get(1), vtx2.get(0)))

                                && singleEdges.contains(e1) && singleEdges.contains(e2)

                                && p1.getMaterialName().equals(p2.getMaterialName())
                                && p1.getBitmapName().equals(p2.getBitmapName())

                                && !nodrawMaterials.contains(p1.getMaterialName())
                                && !nodrawMaterials.contains(p2.getMaterialName())
                        ) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        break;
                    }
                }

                if (b) {
                    p1.neighborIndices.add(lvlPolygons.get(i).index);
                }
            }
        }

        var groups = new ArrayList<List<LvlPolygon>>();
        lvlPolygons.forEach(lvlPolygon -> lvlPolygon.grouped = false);

        var allCollected = false;
        do {
            var notGroupedPolygon = lvlPolygons.stream().filter(p -> !p.grouped).findFirst().orElse(null);

            if (Objects.isNull(notGroupedPolygon)) {
                allCollected = true;
                break;
            }

            var collection = new ArrayList<Short>();
            collectPolygons(collection, lvlPolygons, notGroupedPolygon.index);

            if (collection.isEmpty()) {
                continue;
            }

            groups.add(
                    collection.stream()
                            .map(idx -> lvlPolygons.stream()
                                    .filter(p -> p.index == idx).findFirst()
                                    .orElseThrow()
                            ).toList()
            );

        } while (!allCollected);

        System.out.println("polygon groups: " + groups.size());

        return groups;
    }

    private static void collectPolygons(List<Short> container, List<LvlPolygon> polygons, Short pIndex) {
        var p = polygons.stream().filter(lvlPolygon -> lvlPolygon.index == pIndex).findFirst().orElseThrow();

        if (p.grouped || container.contains(p.index)) {
            p.grouped = true;
            return;
        }

        p.grouped = true;
        container.add(p.index);

        for (var n : p.neighborIndices) {
            collectPolygons(container, polygons, n);
        }
    }
}
