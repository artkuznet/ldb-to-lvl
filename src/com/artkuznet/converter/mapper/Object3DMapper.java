package com.artkuznet.converter.mapper;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.maxed.LvlPolygon;
import com.artkuznet.converter.maxed.Mesh;
import com.artkuznet.converter.obj.OBJ;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Object3DMapper {

    public static List<Mesh> convert(OBJ obj, Map<String, String> textureMaterials) {

        short[] polygons = {-1};

        return obj.getObjects().stream()
                .map(object3D -> {

                    System.out.printf("+ %s%n", object3D.getName());

                    com.artkuznet.converter.maxed.Mesh mesh = new com.artkuznet.converter.maxed.Mesh();

                    List<Vector3D> objectVertices = object3D.getVertices().stream()
                            .map(v -> new Vector3D(v.getX(), v.getY(), -v.getZ()))
                            .collect(Collectors.toList());

                    mesh.setName(object3D.getName());
                    mesh.setIsRoom(false);
                    mesh.setVertices(objectVertices.toArray(new Vector3D[0]));

                    int minVertexIndex = object3D.getMinVertexIndex();

                    mesh.setPolygons(object3D.getFaces().stream()
                            .map(face -> {

                                LvlPolygon.Edge[] edges = new LvlPolygon.Edge[face.getVertices().size()];
                                for (int i = 0; i < edges.length; i++) {
                                    int from = face.getVertices().get(i).getIndex() - minVertexIndex;
                                    int to = face.getVertices().get((1 + i) % edges.length).getIndex() - minVertexIndex;
                                    edges[i] = new LvlPolygon.Edge(from, to);
                                }

                                Vector3D normal = Vector3D.calculateNormal(
                                        Vector3D.findTriangle(
                                                Arrays.stream(edges)
                                                        .map(LvlPolygon.Edge::getFrom)
                                                        .map(objectVertices::get)
                                                        .collect(Collectors.toList())
                                        )
                                );

                                LvlPolygon.Triangle triangle = new LvlPolygon.Triangle();
                                triangle.normal = normal;
                                triangle.vertices = Arrays.stream(edges).map(LvlPolygon.Edge::getFrom).collect(Collectors.toList());

                                LvlPolygon polygon = new LvlPolygon(
                                        edges,
                                        textureMaterials.getOrDefault(face.getMaterialName(), "default"),
                                        face.getMaterialName(),
                                        normal
                                );

                                polygon.index = ++polygons[0];

                                polygon.setTriangles(Collections.singletonList(triangle));

                                polygon.UV = face.getUV().stream()
                                        .map(uv -> new VertexUV((float) uv.getU(), (float) uv.getV()))
                                        .collect(Collectors.toList());


                                polygon.textureOffset = new double[]{polygon.UV.get(0).getU(), polygon.UV.get(0).getV()};
                                polygon.setUnkVertex(objectVertices.get(edges[0].getFrom()));
                                polygon.unkVector1 = polygon.getDefaultUnk5();

                                polygon.parentMesh = mesh;

                                polygon.calculateTextureSpace(objectVertices);

                                return polygon;
                            }).toArray(LvlPolygon[]::new));

                    return mesh.optimize().joinPolygons().buildPolyGroups();
                }).collect(Collectors.toList());
    }
}
