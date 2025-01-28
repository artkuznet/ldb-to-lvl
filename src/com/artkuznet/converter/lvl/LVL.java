package com.artkuznet.converter.lvl;

import com.artkuznet.converter.*;
import com.artkuznet.converter.ldb.MaxLDB;
import com.artkuznet.converter.ldb.dynamicmesh.LdbDynamicMesh;
import com.artkuznet.converter.ldb.material.Material;
import com.artkuznet.converter.mapper.DynamicDataMapper;
import com.artkuznet.converter.mapper.FsmDataMapper;
import com.artkuznet.converter.maxed.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LVL {

    private static final int[] HEADER = new int[]{0x0000FFEF, 0x00000014};

    private static final byte[] UNKNOWN_PREFERENCES_DATA = {
            (byte) 0x82, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x14, (byte) 0x2C, (byte) 0x09, (byte) 0x00,
            (byte) 0x00, (byte) 0x80, (byte) 0x3F, (byte) 0x09, (byte) 0x00, (byte) 0x00, (byte) 0x7A, (byte) 0x44,
            (byte) 0x09, (byte) 0x0A, (byte) 0xD7, (byte) 0x23, (byte) 0x3C, (byte) 0x09, (byte) 0x00, (byte) 0x00,
            (byte) 0x7A, (byte) 0x44, (byte) 0x09, (byte) 0xCD, (byte) 0xCC, (byte) 0xCC, (byte) 0x3D, (byte) 0x09,
            (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x3F, (byte) 0x09, (byte) 0x6F, (byte) 0x12, (byte) 0x83,
            (byte) 0x3A, (byte) 0x14, (byte) 0x00, (byte) 0x14, (byte) 0x01, (byte) 0x14, (byte) 0x00, (byte) 0x14,
            (byte) 0x01, (byte) 0x14, (byte) 0x01, (byte) 0x09, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3E,
            (byte) 0x14, (byte) 0x01, (byte) 0x14, (byte) 0x00, (byte) 0x14, (byte) 0x00, (byte) 0x0A, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x40, (byte) 0x11,
            (byte) 0x80, (byte) 0x11, (byte) 0x03, (byte) 0x09, (byte) 0x00, (byte) 0x00, (byte) 0x16, (byte) 0x43,
            (byte) 0x09, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x42, (byte) 0x14, (byte) 0x00, (byte) 0x14,
            (byte) 0x00, (byte) 0x14, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x3F,
            (byte) 0x14, (byte) 0x01, (byte) 0x14, (byte) 0x01, (byte) 0x14, (byte) 0x01, (byte) 0x14, (byte) 0x00,
            (byte) 0x14, (byte) 0x01, (byte) 0x14, (byte) 0x00, (byte) 0x14, (byte) 0x02, (byte) 0x14, (byte) 0x01,
            (byte) 0x14, (byte) 0x01, (byte) 0x08, (byte) 0xFF, (byte) 0x08, (byte) 0xF3, (byte) 0x08, (byte) 0xCE,
            (byte) 0x08, (byte) 0xFF, (byte) 0x11, (byte) 0x01, (byte) 0x0E, (byte) 0x01, (byte) 0x0E, (byte) 0x01,
            (byte) 0x0E, (byte) 0x00, (byte) 0x11, (byte) 0x01, (byte) 0x0E, (byte) 0x01, (byte) 0xFF, (byte) 0x6D,
            (byte) 0x03, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0xF0, (byte) 0x3F, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0xF0, (byte) 0x3F, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0xF0, (byte) 0x3F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xEF, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xEF, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xEF, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xEF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xEF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xEF, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00
    };

    private static final String WORLD_GROUP = "World group";

    private static final byte[] UNKNOWN_VIEWPORT_DATA = {
            (byte) 0xa6, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x40, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0x3f,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x3f,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xbf,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x3f,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe0, (byte) 0x3f,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x40, (byte) 0x40,
            (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x00,
            (byte) 0x00, (byte) 0x00
    };

    public static final byte[] UNKNOWN_MATERIALS_DATA = new byte[]{
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -16, 63, 0, 0, 0, 0, 0, 0, 16, 64, 0, 0, 0
    };

    public LVL(MaxLDB ldb) {
        bitmaps = ldb.getTextures()
                .getList().stream()
                .map(texture -> new Bitmap(texture.getFilePath(), texture.getFileType(), texture.getData()))
                .toList();

        materials = new ArrayList<>();

        ldb.getMaterials().getList().stream()
                .collect(Collectors.groupingBy(Material::getCategoryName))
                .forEach((materialName, materialBitmapList) -> {

                    var materialBitmaps = materialBitmapList.stream().map(material -> Objects.nonNull(material.getAlphaTexture())
                            ? new LvlMaterial.BitmapLayer(material.getDiffuseTexture().getFilePath(), material.getMaterialName(), 1, material.getAlphaTexture().getFilePath())
                            : new LvlMaterial.MaterialBitmap(material.getDiffuseTexture().getFilePath(), material.getMaterialName(), 0)).toList();

                    materials.add(new LvlMaterial(materialName, materialBitmaps.toArray(LvlMaterial.MaterialBitmap[]::new)));
                });

        final short[] polygonsCounter = {-1};
        final int[] meshCounter = {-1};
        final int[] pointlightCounter = {-1};

        var rooms = ldb.getRooms().getList().stream().map(room -> {
            var staticMeshId = room.getStaticMeshes().stream().findFirst().orElseThrow();
            var roomMesh = ldb.getStaticMeshes().getById(staticMeshId);

            var roomMeshPolygonsCount = roomMesh.getPolygons().getList().size();

            var geometries = IntStream.range(0, roomMeshPolygonsCount)
                    .mapToObj(roomMesh::constructPolygon)
                    .collect(Collectors.toCollection(ArrayList::new));

            var lvlVertexList = new ArrayList<Vector3D>();
            geometries.forEach(geometry -> geometry.getVertices().forEach(vertex -> {
                var point3d = new Vector3D(vertex.getX(), vertex.getY(), vertex.getZ());
                if (!lvlVertexList.contains(point3d)) {
                    lvlVertexList.add(point3d);
                }
            }));

            final var lvlPolygons = new ArrayList<LvlPolygon>();
            geometries.forEach(geometry -> {
                var vertices = geometry.getVertices();
                var sizeUV = geometry.getUvSize();

                var geometryPolygon = roomMesh.getPolygons().getById(geometry.getPolygonId());
                var normal = geometryPolygon.getNormal();

                var edges = new LvlPolygon.Edge[vertices.size()];
                for (int i = 0; i < edges.length - 1; i++) {
                    var vFrom = vertices.get(i);
                    var p3dFrom = new Vector3D(vFrom.getX(), vFrom.getY(), vFrom.getZ());
                    var vertexIndexFrom = lvlVertexList.indexOf(p3dFrom);

                    var vTo = vertices.get(i + 1);
                    var p3dTo = new Vector3D(vTo.getX(), vTo.getY(), vTo.getZ());
                    var vertexIndexTo = lvlVertexList.indexOf(p3dTo);

                    edges[i] = new LvlPolygon.Edge(vertexIndexFrom, vertexIndexTo);
                }

                edges[edges.length - 1] = new LvlPolygon.Edge(edges[edges.length - 2].getTo(), edges[0].getFrom());

                var poly = new LvlPolygon(
                        edges,
                        geometry.getMaterial().getCategoryName(),
                        geometry.getMaterial().getMaterialName(),
                        new Vector3D(normal.getX(), normal.getY(), normal.getZ())
                );

                poly.textureOffset = geometry.getTextureOffset();
                poly.unkVertex1 = geometry.getFirstVertex();
                poly.unkVertex2 = geometry.getFirstVertex();
                poly.unkVector1 = poly.getDefaultUnk5();

                poly.sizeUV = new double[]{sizeUV.getU(), sizeUV.getV()};
                poly.UV = geometry.getUv().stream().map(Vector3D::new).toList();

                poly.unkTransform = poly.getDefaultTransform();
                poly.pointPolygonIndex = -1;

                poly.testVertices = vertices.stream().map(v -> new Vector3D(v.getX(), v.getY(), v.getZ())).toList();

                poly.index = ++polygonsCounter[0];

                poly.calculateScaleUV();

                poly.geometryPolyGroup = geometryPolygon.getSmoothingGroup();
                poly.maxEdgeLength = geometryPolygon.getMaxEdgeLength();
                poly.maxAngle = geometryPolygon.getMaxAngle();

                lvlPolygons.add(poly);
            });

            room.getExits().stream().map(exitName -> ldb.getExits().findByName(exitName)).forEach(exit -> {
                var vertices = exit.getVertices().getList();

                vertices.forEach(vertex -> {
                    var point3d = new Vector3D(vertex.getX(), vertex.getY(), vertex.getZ());
                    if (!lvlVertexList.contains(point3d)) {
                        lvlVertexList.add(point3d);
                    }
                });

                var edges = new LvlPolygon.Edge[vertices.size()];
                for (int i = 0; i < edges.length - 1; i++) {
                    var vFrom = vertices.get(i);
                    var p3dFrom = new Vector3D(vFrom.getX(), vFrom.getY(), vFrom.getZ());
                    var vertexIndexFrom = lvlVertexList.indexOf(p3dFrom);

                    var vTo = vertices.get(i + 1);
                    var p3dTo = new Vector3D(vTo.getX(), vTo.getY(), vTo.getZ());
                    var vertexIndexTo = lvlVertexList.indexOf(p3dTo);

                    edges[i] = new LvlPolygon.Edge(vertexIndexFrom, vertexIndexTo);
                }
                edges[edges.length - 1] = new LvlPolygon.Edge(edges[edges.length - 2].getTo(), edges[0].getFrom());

                var normal = new Vector3D(exit.getNormal().getX(), exit.getNormal().getY(), exit.getNormal().getZ());
                var lvlExit = new LvlExit(edges, exit.getShortName(), normal, exit.getExitName(), exit.getParentRoomName());

                lvlExit.unkVertex1 = new Vector3D(vertices.get(0).getX(), vertices.get(0).getY(), vertices.get(0).getZ());
                lvlExit.unkVertex2 = new Vector3D(vertices.get(0).getX(), vertices.get(0).getY(), vertices.get(0).getZ());

                lvlExit.unkVector1 = lvlExit.getDefaultUnk5();

                lvlExit.textureOffset = new double[]{0, 0};

                lvlExit.unkTransform = lvlExit.getDefaultTransform();

                lvlExit.testVertices = vertices.stream().map(v -> new Vector3D(v.getX(), v.getY(), v.getZ())).toList();

                lvlExit.index = ++polygonsCounter[0];

                var defaultScaleUV = lvlExit.getDefaultScaleUV();
                lvlExit.scaleU = defaultScaleUV[0];
                lvlExit.scaleV = defaultScaleUV[1];

                lvlPolygons.add(lvlExit);
            });

            var roomName = room.getName().replaceFirst("::", "");

            System.out.println("Room: " + roomName);


            var groups = MeshBuilder.groupPolygons(lvlPolygons, lvlVertexList, true, roomName);


            var rooommm = groups.stream().filter(polygons -> polygons.stream().anyMatch(p -> p instanceof LvlExit)).toList();
            var emptyRoom = new HashSet<LvlPolygon>();
            rooommm.forEach(emptyRoom::addAll);


            var meshes = groups.stream().filter(polygons -> polygons.stream().noneMatch(p -> p instanceof LvlExit)).toList();

            meshes = new ArrayList<>(MeshBuilder.groupPolygons(meshes.stream().flatMap(List::stream).toList(), lvlVertexList, false, roomName));


            var childs = new ArrayList<MaxObject>();

            for (var m : meshes) {
                var childMesh = new Mesh();

                childMesh.setName("Mesh_" + (++meshCounter[0]));
                childMesh.setFlipFaces(false);
                childMesh.setVertices(lvlVertexList.toArray(Vector3D[]::new));
                childMesh.setPolygons(m.toArray(LvlPolygon[]::new));
                childMesh.setPolyGroups(new PolyGroup[]{});

                childs.add(childMesh.optimize().joinPolygons().buildPolyGroups());
            }

            var dynamicMeshes = ldb.getDynamicMeshes().getList().stream()
                    .filter(d -> d.getRoomName().equals(room.getName()))
                    .map(new Function<LdbDynamicMesh, DynamicMesh>() {
                        @Override
                        public DynamicMesh apply(LdbDynamicMesh ldbDynamicMesh) {
                            var dynamic = new DynamicMesh();

                            var dynamicPolygonsCount = ldbDynamicMesh.getPolygons().getList().size();

                            var geometries = IntStream.range(0, dynamicPolygonsCount)
                                    .mapToObj(ldbDynamicMesh::constructPolygon)
                                    .collect(Collectors.toCollection(ArrayList::new));

                            var lvlVertexList = new ArrayList<Vector3D>();
                            geometries.forEach(geometry -> geometry.getVertices().forEach(vertex -> {
                                var point3d = new Vector3D(vertex.getX(), vertex.getY(), vertex.getZ());
                                if (!lvlVertexList.contains(point3d)) {
                                    lvlVertexList.add(point3d);
                                }
                            }));

                            var dynamicPolygons = new ArrayList<LvlPolygon>();


                            geometries.forEach(geometry -> {
                                var vertices = geometry.getVertices();
                                var sizeUV = geometry.getUvSize();

                                var geometryPolygon = ldbDynamicMesh.getPolygons().getById(geometry.getPolygonId());
                                var normal = geometryPolygon.getNormal();


                                var edges = new LvlPolygon.Edge[vertices.size()];
                                for (int i = 0; i < edges.length - 1; i++) {
                                    var vFrom = vertices.get(i);
                                    var p3dFrom = new Vector3D(vFrom.getX(), vFrom.getY(), vFrom.getZ());
                                    var vertexIndexFrom = lvlVertexList.indexOf(p3dFrom);

                                    var vTo = vertices.get(i + 1);
                                    var p3dTo = new Vector3D(vTo.getX(), vTo.getY(), vTo.getZ());
                                    var vertexIndexTo = lvlVertexList.indexOf(p3dTo);

                                    edges[i] = new LvlPolygon.Edge(vertexIndexFrom, vertexIndexTo);
                                }

                                edges[edges.length - 1] = new LvlPolygon.Edge(edges[edges.length - 2].getTo(), edges[0].getFrom());

                                var poly = new LvlPolygon(
                                        edges,
                                        geometry.getMaterial().getCategoryName(),
                                        geometry.getMaterial().getMaterialName(),
                                        new Vector3D(normal.getX(), normal.getY(), normal.getZ())
                                );

                                poly.textureOffset = geometry.getTextureOffset();
                                poly.unkVertex1 = geometry.getFirstVertex();
                                poly.unkVertex2 = geometry.getFirstVertex();
                                poly.unkVector1 = poly.getDefaultUnk5();

                                poly.sizeUV = new double[]{sizeUV.getU(), sizeUV.getV()};
                                poly.UV = geometry.getUv().stream().map(Vector3D::new).toList();

                                poly.unkTransform = poly.getDefaultTransform();
                                poly.pointPolygonIndex = -1;

                                poly.testVertices = vertices.stream()
                                        .map(v -> new Vector3D(v.getX(), v.getY(), v.getZ()))
                                        .toList();

                                poly.index = ++polygonsCounter[0];

                                poly.calculateScaleUV();

                                poly.geometryPolyGroup = geometryPolygon.getSmoothingGroup();
                                poly.maxEdgeLength = geometryPolygon.getMaxEdgeLength();
                                poly.maxAngle = geometryPolygon.getMaxAngle();

                                dynamicPolygons.add(poly);
                            });

                            dynamic.pointlightAffected = ldbDynamicMesh.getConfig().getPointlightAffected() == 1;
                            dynamic.blockExplosions = ldbDynamicMesh.getConfig().getBlockExplosions() == 1;
                            dynamic.bulletCollisions = ldbDynamicMesh.getConfig().getBulletCollisions() == 1;
                            dynamic.dynamicCollisions = ldbDynamicMesh.getConfig().getDynamicCollisions() == 1;
                            dynamic.lightMapped = ldbDynamicMesh.getConfig().getLightMapped() == 1;
                            dynamic.contUpdate = ldbDynamicMesh.getConfig().getContUpdate() == 1;

                            dynamic.flags = new byte[]{
                                    1,
                                    1,
                                    (byte) (dynamic.dynamicCollisions ? 1 : 0),
                                    1,
                                    (byte) (dynamic.pointlightAffected ? 1 : 0),
                                    (byte) (dynamic.contUpdate ? 1 : 0),
                                    0
                            };

                            dynamic.setName(ldbDynamicMesh.getShortName());

                            dynamic.setFlipFaces(false);

                            dynamic.setVertices(lvlVertexList.toArray(Vector3D[]::new));
                            dynamic.setPolygons(dynamicPolygons.toArray(LvlPolygon[]::new));
                            dynamic.setTransform(ldbDynamicMesh.getTransformDouble());

                            dynamic.parentName = ldbDynamicMesh.getProperties().getParentDynamicMeshName();
                            dynamic.fullName = ldbDynamicMesh.getSharedName();

                            dynamic.setTransform(ldbDynamicMesh.getProperties().getObjectToParentTransformDouble());

                            dynamic.setDynamicData(DynamicDataMapper.toDynamicData(
                                    dynamic.getTransform(),
                                    ldbDynamicMesh.getAnimations().getList()
                            ));

                            return dynamic.optimize().joinPolygons().buildPolyGroups();
                        }
                    }).toList();

            childs.addAll(dynamicMeshes);

            var characters = ldb.getCharacters().getList().stream()
                    .filter(e -> e.getRoomName().equals(room.getName()))
                    .map(character -> {
                        var enemy = new Enemy();
                        enemy.setType(character.getCharacterName());
                        enemy.setName(character.getShortName());

                        enemy.parentName = character.getProperties().getParentDynamicMeshName();
                        enemy.fullName = character.getSharedName();

                        enemy.setTransform(character.getProperties().getObjectToParentTransformDouble());

                        return enemy;
                    }).toList();

            childs.addAll(characters);

            var lights = ldb.getPointlights().getList().stream()
                    .filter(e -> e.getProperties().getRoomId() == room.getId())
                    .map(ldbPointLight -> {
                        var lvlLight = new LvlLight();

                        lvlLight.setR(ldbPointLight.getR());
                        lvlLight.setG(ldbPointLight.getG());
                        lvlLight.setB(ldbPointLight.getB());
                        lvlLight.setA(ldbPointLight.getA());

                        lvlLight.setIntensity(ldbPointLight.getIntensity());
                        lvlLight.setFalloff(ldbPointLight.getFalloff());

                        lvlLight.parentName = ldbPointLight.getProperties().getParentDynamicMeshName();
                        lvlLight.fullName = ldbPointLight.getProperties().getName();

                        lvlLight.setName("Pointlight_" + (++pointlightCounter[0]));

                        lvlLight.setTransform(ldbPointLight.getProperties().getObjectToParentTransformDouble());

                        return lvlLight;
                    }).toList();

            childs.addAll(lights);

            var triggers = ldb.getTriggers().getList().stream()
                    .filter(e -> e.getRoomName().equals(room.getName()))
                    .map(ldbTrigger -> {
                        var lvlTrigger = new LvlTrigger();

                        lvlTrigger.setRadius(ldbTrigger.getRadius());
                        lvlTrigger.setName(ldbTrigger.getShortName());
                        lvlTrigger.setType(ldbTrigger.getTypeString());

                        lvlTrigger.parentName = ldbTrigger.getProperties().getParentDynamicMeshName();
                        lvlTrigger.fullName = ldbTrigger.getSharedName();

                        lvlTrigger.setTransform(ldbTrigger.getProperties().getObjectToParentTransformDouble());

                        return lvlTrigger;
                    }).toList();

            childs.addAll(triggers);

            var items = ldb.getItems().getList().stream()
                    .filter(e -> e.getRoomName().equals(room.getName()))
                    .map(item -> {
                        var lvlItem = new LevelItem();
                        lvlItem.setItemType(item.getItemName());
                        lvlItem.setName(item.getShortName());

                        lvlItem.parentName = item.getProperties().getParentDynamicMeshName();
                        lvlItem.fullName = item.getSharedName();

                        lvlItem.setTransform(item.getProperties().getObjectToParentTransformDouble());

                        return lvlItem;
                    }).toList();

            childs.addAll(items);

            var waypoints = ldb.getWaypoints().getList().stream()
                    .filter(e -> e.getRoomName().equals(room.getName()))
                    .map(waypoint -> {
                        var lvlPoint = new LvlPoint();
                        lvlPoint.setType(waypoint.getType());
                        lvlPoint.setName(waypoint.getShortName());
                        lvlPoint.parentName = waypoint.getProperties().getParentDynamicMeshName();
                        lvlPoint.fullName = waypoint.getSharedName();
                        lvlPoint.setTransform(waypoint.getProperties().getObjectToParentTransformDouble());

                        return lvlPoint;
                    }).toList();

            childs.addAll(waypoints);

            var allChildNames = childs.stream()
                    .map(o -> o.fullName.replace(".DO", "").replace(".TRIGGER", ""))
                    .toList();

            var floatingFSMs = ldb.getFSMs().getList().stream()
                    .filter(e -> e.getRoomName().equals(room.getName()))
                    .filter(f -> !allChildNames.contains(f.getSharedName()))
                    .map(ldbFSM -> {
                        var fsm = new FloatingFSM();
                        fsm.setName(ldbFSM.getShortName());

                        fsm.parentName = ldbFSM.getProperties().getParentDynamicMeshName();
                        fsm.fullName = ldbFSM.getSharedName();

                        fsm.setTransform(
                                fsm.parentName.isEmpty()
                                        ? ldbFSM.getProperties().getObjectToRoomTransformDouble()
                                        : ldbFSM.getProperties().getObjectToParentTransformDouble()
                        );

                        return fsm;
                    }).toList();

            childs.addAll(floatingFSMs);

            var nestedObjects = childs.stream().filter(o -> !o.parentName.isEmpty()).toList();
            var parentObjects = childs.stream().filter(o -> o.parentName.isEmpty()).toList();

            var mesh = new Mesh();

            parentObjects.forEach(parent -> {
                parent.parentObject = mesh;
                parent.childObjects.addAll(collectChilds(nestedObjects, parent));
            });

            mesh.setName(roomName);
            mesh.setFlipFaces(true);
            mesh.setVertices(lvlVertexList.toArray(Vector3D[]::new));
            mesh.setPolygons(emptyRoom.toArray(LvlPolygon[]::new));
            mesh.setTransform(roomMesh.getTransformDouble());
            mesh.setPolyGroups(new PolyGroup[]{});

            mesh.childObjects.addAll(parentObjects);

            return mesh.optimize().joinPolygons().buildPolyGroups();
        }).toList();

        for (var room : rooms) {
            countPolygons(room);
        }

        Map<String, String> objNamesMap = new HashMap<>();

        List<MaxObject> objectFlatList = new ArrayList<>(getAllObjects(rooms));

        for (var obj : objectFlatList.stream().filter(
                o -> !(o instanceof Mesh && !(o instanceof Dynamic))
                        && (o.parentObject instanceof Mesh)
                        && !(o.parentObject instanceof Dynamic)
                        && !o.fullName.isEmpty() && o.fullName.substring(2).split("::").length > 2
        ).toList()
        ) {
            var newName = new ArrayList<>(Arrays.stream(obj.fullName.substring(2).split("::")).toList());
            newName.remove(0);
            newName.remove(newName.size() - 1);
            newName.add(obj.getName());
            obj.setName(String.join("_", newName));
        }

        rooms.forEach(room -> objectFlatList.forEach(object -> objNamesMap.putAll(getNewObjectNamesMap(object))));

        var fsmMapper = new FsmDataMapper(objNamesMap);

        List<FSM> fsmObjects = objectFlatList.stream().filter(o -> o instanceof FSM).map(o -> (FSM) o).toList();
        fsmObjects.forEach(o -> setObjectFSMData(fsmMapper, ldb, (MaxObject) o));

        rooms.forEach(room -> {
            for (var lvlPolygon : room.getPolygons()) {
                if (!(lvlPolygon instanceof LvlExit)) {
                    continue;
                }
                var exit = (LvlExit) lvlPolygon;

                var linkedExit = rooms.stream()
                        .map(Mesh::getPolygons)
                        .flatMap(Arrays::stream)
                        .filter(p -> p instanceof LvlExit)
                        .map(p -> (LvlExit) p)
                        .filter(e -> e.exitName.equals(exit.linkedExitName))
                        .findFirst().orElse(null);

                if (linkedExit == null) {
                    exit.pointPolygonIndex = 0;
                    continue;
                }

                exit.pointPolygonIndex = linkedExit.index;
            }
        });

        objects.addAll(rooms);
    }

    public static List<? extends MaxObject> getAllObjects(List<? extends MaxObject> objects) {
        var list = new ArrayList<MaxObject>();

        list.addAll(objects);

        for (var o : objects) {
            list.addAll(getAllObjects(o.childObjects));
        }

        return list;
    }

    private static void setObjectFSMData(FsmDataMapper fsmMapper, MaxLDB ldb, MaxObject object) {
        if (!(object instanceof FSM)) {
            throw new RuntimeException();
        }

        var objectName = object.fullName;
        if (object instanceof Dynamic) {
            objectName = objectName.replace(".DO", "");
        }
        if (object instanceof LvlTrigger) {
            objectName = objectName.replace(".TRIGGER", "");
        }

        if (object instanceof Enemy) {
            var ldbCharacter = ldb.getCharacters().findByName(object.fullName);

            ((FSM) object).setFsmData(fsmMapper.toFSMData(object, ldbCharacter));

            return;
        }

        LdbDynamicMesh ldbDynamicMesh = ldb.getDynamicMeshes().findByName(object.fullName);

        var ldbFsm = ldb.getFSMs().findByName(objectName);
        if (ldbFsm == null) {
            throw new RuntimeException("FSM not found: " + objectName);
        }

        ((FSM) object).setFsmData(fsmMapper.toFSMData(object, ldbFsm, ldbDynamicMesh));
    }

    private static Map<String, String> getNewObjectNamesMap(MaxObject object) {

        Map<String, String> objectNamesMap = new HashMap<>();

        var path = new ArrayList<String>();
        var obj = object;
        while (obj != null) {
            path.add(obj.getName());
            obj = obj.parentObject;
        }
        Collections.reverse(path);

        object.newFullName = "::" + String.join("::", path);

        if (object instanceof Dynamic) {
            object.newFullName += ".DO";
        }
        if (object instanceof LvlTrigger) {
            object.newFullName += ".TRIGGER";
        }

        if (!object.fullName.isEmpty()) {
            objectNamesMap.put(object.fullName, object.newFullName);
        }

        return objectNamesMap;
    }

    private static List<MaxObject> collectChilds(List<MaxObject> list, MaxObject parent) {
        var ch = list.stream().filter(object -> parent.fullName.equals(object.parentName)).toList();

        for (var c : ch) {
            c.parentObject = parent;
            c.childObjects.addAll(collectChilds(list, c));
        }

        return ch;
    }

    static short polygonCounter = -1;

    private static void countPolygons(Mesh mesh) {
        for (var polygon : mesh.getPolygons()) {
            polygon.index = ++polygonCounter;
        }
        for (var child : mesh.childObjects) {
            if (child instanceof Mesh) {
                countPolygons((Mesh) child);
            }
        }
    }

    private List<Bitmap> bitmaps;

    private List<LvlMaterial> materials;

    private List<MaxObject> objects = new ArrayList<>();

    public List<Byte> toBytes() {
        var data = new ArrayList<Byte>();

        data.addAll(toBytes(HEADER[0]));
        data.addAll(toBytes(HEADER[1]));

        data.addAll(toBytes(bitmaps.size()));

        for (Bitmap bitmap : bitmaps) {
            data.addAll(toBytes(bitmap.getType()));
            data.addAll(toBytes(bitmap.getData().length));
            data.add((byte) bitmap.getName().length());
            data.addAll(toBytes(bitmap.getName()));
            data.addAll(toBytes(bitmap.getData()));
        }

        data.addAll(toBytes(materials.size()));

        for (LvlMaterial material : materials) {

            data.addAll(toBytes(material.getBitmaps().length));
            data.add((byte) material.getName().length());
            data.addAll(toBytes(material.getName()));

            for (LvlMaterial.MaterialBitmap materialBitmap : material.getBitmaps()) {

                data.add((byte) materialBitmap.getShortName().length());
                data.addAll(toBytes(materialBitmap.getShortName()));

                data.add((byte) materialBitmap.getName().length());
                data.addAll(toBytes(materialBitmap.getName()));

                data.addAll(toBytes(materialBitmap.getLayerType()));

                if (materialBitmap instanceof LvlMaterial.BitmapLayer) {
                    data.add((byte) ((LvlMaterial.BitmapLayer) materialBitmap).getLayerBitmapName().length());
                    data.addAll(toBytes(((LvlMaterial.BitmapLayer) materialBitmap).getLayerBitmapName()));
                }

                data.addAll(toBytes(UNKNOWN_MATERIALS_DATA));
            }
        }

        data.addAll(toBytes(UNKNOWN_PREFERENCES_DATA));

        data.add((byte) WORLD_GROUP.length());
        data.addAll(toBytes(WORLD_GROUP));

        data.add((byte) 0);

        data.addAll(toBytes(objects.size()));

        for (MaxObject object : objects) {
            data.addAll(toBytes(object));
        }

        data.addAll(toBytes(UNKNOWN_VIEWPORT_DATA));

        return data;
    }

    private static List<Byte> toBytes(MaxObject object) {
        var data = new ArrayList<Byte>();

        data.addAll(toBytes(getObjectType(object)));

        if (object instanceof LvlPoint) {
            data.addAll(toBytes(((LvlPoint) object).getType()));
        }

        if (object instanceof PointObject) {
            data.add((byte) ((PointObject) object).getType().length());
            data.addAll(toBytes(((PointObject) object).getType()));
            data.addAll(toBytes(((PointObject) object).getRadius()));
        }

        data.addAll(transformToBytes(object.getTransform()));

        var position = object.getPosition();
        data.addAll(toBytes(position[0]));
        data.addAll(toBytes(position[1]));
        data.addAll(toBytes(position[2]));
        data.addAll(toBytes(position[3]));
        data.addAll(toBytes(position[4]));
        data.addAll(toBytes(position[5]));
        data.addAll(toBytes(position[6]));

        data.addAll(toBytes(object.isHidden() ? 0 : 1));

        data.add((byte) object.getName().length());
        data.addAll(toBytes(object.getName()));

        data.add((byte) (object instanceof FSM ? 1 : 0));
        if (object instanceof FSM) {
            var fsmDataBytes = toBytes(((FSM) object).getFsmData());
            data.addAll(toBytes(fsmDataBytes.size()));
            data.addAll(fsmDataBytes);
        }

        if (object instanceof Mesh) {
            data.add((byte) (((Mesh) object).getFlipFaces() ? 0 : 1));

            data.addAll(toBytes(new byte[]{0, 0, 0, 0, 0, 0, 0}));

            data.addAll(toBytes(((Mesh) object).getVertices().length));
            for (Vector3D vertex : ((Mesh) object).getVertices()) {
                data.addAll(toBytes(vertex));
            }

            data.addAll(toBytes(((Mesh) object).getPolygons().length));
            for (LvlPolygon polygon : ((Mesh) object).getPolygons()) {
                data.addAll(toBytes(polygon.getEdges().length));

                data.addAll(toBytes(polygon.normal));

                data.addAll(toBytes(polygon.unkVector1));

                data.addAll(transformPolygonToBytes(polygon.getDefaultTransform()));

                data.addAll(toBytes(polygon.unkVertex1));

                data.addAll(toBytes(polygon.scaleU));
                data.addAll(toBytes(polygon.scaleV));

                data.addAll(toBytes(polygon.normal));

                data.addAll(toBytes(polygon.textureOffset[0]));
                data.addAll(toBytes(polygon.textureOffset[1]));

                data.addAll(toBytes(polygon.unkVertex2));

                data.addAll(toBytes(0));

                // color RGBA
                byte rgba = "laser".equalsIgnoreCase(polygon.getMaterialName()) ? (byte) 255 : 0;
                data.add(rgba);
                data.add(rgba);
                data.add(rgba);
                data.add(rgba);

                data.addAll(toBytes(polygon.lightIntensity));
                data.addAll(toBytes(polygon.lightmapResolution));

                if (polygon instanceof LvlExit) {
                    data.addAll(toBytes(4));
                    data.addAll(toBytes(polygon.pointPolygonIndex));
                } else {
                    data.addAll(toBytes(0));
                    data.addAll(toBytes((short) -1));
                }

                if (polygon instanceof LvlExit) {
                    data.add((byte) 0);
                    data.add((byte) 0);
                } else {
                    data.add((byte) 255);
                    data.addAll(toBytes((short) 255));
                }

                data.add((byte) polygon.getMaterialName().length());
                data.addAll(toBytes(polygon.getMaterialName()));

                data.add((byte) polygon.getBitmapName().length());
                data.addAll(toBytes(polygon.getBitmapName()));

                if (polygon.getBitmapName().isEmpty()) {
                    data.add((byte) 0);
                }

                for (LvlPolygon.Edge edge : polygon.getEdges()) {
                    data.addAll(toBytes(edge.getFrom()));
                    data.addAll(toBytes(edge.getTo()));
                }

                var polygonTriangles = polygon.getTriangles();
                data.addAll(toBytes(polygonTriangles.size()));
                for (LvlPolygon.Triangle triangle : polygonTriangles) {
                    data.addAll(toBytes(triangle.normal));
                    data.addAll(toBytes(triangle.vertices.size()));
                    for (Integer vertexIndex : triangle.vertices) {
                        data.addAll(toBytes(vertexIndex));
                    }
                }


                data.addAll(toBytes(polygon.unkVector1));
                data.addAll(toBytes(new Vector3D(0, 0, 0)));

                var unkVect = polygon.getUnknownVectors();
                data.addAll(toBytes(unkVect[0]));
                data.addAll(toBytes(unkVect[1]));

                data.addAll(toBytes(polygon.unkVector1));

                data.addAll(toBytes(new Vector3D(0, 0, 0)));
                data.addAll(toBytes(new Vector3D(0, 0, 0)));

                data.addAll(toBytes(4));
                data.addAll(toBytes(4));

                data.addAll(toBytes(1.0));
                data.addAll(toBytes(1.0));

                data.addAll(toBytes(66));

                data.addAll(toBytes((short) 0));
                data.addAll(toBytes((short) 2));
                data.addAll(toBytes((short) 0));
                data.addAll(toBytes((short) 0));
                data.addAll(toBytes((short) 0));
                data.addAll(toBytes((short) 0));

                data.addAll(toBytes((short) 4));
                data.addAll(toBytes((short) 4));

                data.add((byte) 24);
                data.add((byte) 32);

                var lightMapBytes = new byte[66 - 18];
                Arrays.fill(lightMapBytes, (byte) 127);
                data.addAll(toBytes(lightMapBytes));

                data.addAll(toBytes(1));

                data.addAll(toBytes(120.0f));
                data.addAll(toBytes(179.0f));
                data.addAll(toBytes(0.0f));
                data.addAll(toBytes(0.0f));
            }

            data.add((byte) (object instanceof Dynamic ? 1 : 0));
            if (object instanceof Dynamic) {
                var dynamicDataBytes = toBytes(((Dynamic) object).getDynamicData());

                data.addAll(toBytes(dynamicDataBytes.size()));
                data.addAll(dynamicDataBytes);

                data.add((byte) ((Dynamic) object).getDefaultKeyframe().length());
                data.addAll(toBytes(((Dynamic) object).getDefaultKeyframe()));

                data.add((byte) ((Dynamic) object).getUnknownKeyframe().length());
                data.addAll(toBytes(((Dynamic) object).getUnknownKeyframe()));
            }

            data.addAll(toBytes(((Mesh) object).flags));

            data.add((byte) (((Mesh) object).bulletCollisions ? 1 : 0));
            data.add((byte) 0);

            if (object instanceof Dynamic) {
                data.addAll(toBytes(new byte[]{0, -128}));
            } else {
                data.addAll(toBytes(new byte[]{20, -1}));
            }

            data.addAll(toBytes(
                    new byte[]{
                            (byte) 63,
                            (byte) (object instanceof Dynamic ? 1 : 0), // cast no shadows
                            (byte) 1,
                            (byte) (((Mesh) object).blockExplosions ? 1 : 0)
                    }
            ));
        }

        data.addAll(toBytes(object.childObjects.size()));
        for (MaxObject childObject : object.childObjects) {
            data.addAll(toBytes(childObject));
        }

        if (object instanceof Mesh) {
            data.addAll(toBytes(((Mesh) object).getPolyGroups().length));
            for (PolyGroup polyGroup : ((Mesh) object).getPolyGroups()) {
                data.add((byte) polyGroup.getName().length());
                data.addAll(toBytes(polyGroup.getName()));
                data.add((byte) (polyGroup.isSmoothLightMaps() ? 1 : 0));
                data.addAll(toBytes(polyGroup.getPolygons().length));
                for (int polygon : polyGroup.getPolygons()) {
                    data.addAll(toBytes(polygon));
                }
                data.add((byte) (polyGroup.isSmoothGeometry() ? 1 : 0));
                data.addAll(toBytes(polyGroup.getMaxEdgeLength()));
                data.addAll(toBytes(polyGroup.getMaxAngle()));
                data.add((byte) (polyGroup.isFreezeLightMaps() ? 1 : 0));
            }
        }

        if (object instanceof LvlLight) {
            data.addAll(toBytes(((LvlLight) object).getR()));
            data.addAll(toBytes(((LvlLight) object).getG()));
            data.addAll(toBytes(((LvlLight) object).getB()));
            data.addAll(toBytes(((LvlLight) object).getA()));
            data.addAll(toBytes(((LvlLight) object).getIntensity()));
            data.addAll(toBytes(((LvlLight) object).getFalloff()));
        }

        return data;
    }

    private static int getObjectType(MaxObject object) {
        if (object instanceof Mesh) {
            return Mesh.getObjectType();
        }
        if (object instanceof LvlLight) {
            return LvlLight.getObjectType();
        }
        if (object instanceof LvlTrigger) {
            return LvlTrigger.getObjectType();
        }
        if (object instanceof LvlPoint) {
            return LvlPoint.getObjectType();
        }
        if (object instanceof LevelItem) {
            return LevelItem.getObjectType();
        }
        if (object instanceof Enemy) {
            return Enemy.getObjectType();
        }
        if (object instanceof FloatingFSM) {
            return FloatingFSM.getObjectType();
        }

        throw new RuntimeException("Unknown object type");
    }

    private static List<Byte> transformToBytes(double[][] transform) {
        var data = new ArrayList<Byte>();

        data.addAll(toBytes(transform[3][0]));
        data.addAll(toBytes(transform[3][1]));
        data.addAll(toBytes(transform[3][2]));

        data.addAll(toBytes(transform[0][0]));
        data.addAll(toBytes(transform[0][1]));
        data.addAll(toBytes(transform[0][2]));

        data.addAll(toBytes(transform[1][0]));
        data.addAll(toBytes(transform[1][1]));
        data.addAll(toBytes(transform[1][2]));

        data.addAll(toBytes(transform[2][0]));
        data.addAll(toBytes(transform[2][1]));
        data.addAll(toBytes(transform[2][2]));

        return data;
    }

    private static List<Byte> transformPolygonToBytes(double[][] transform) {
        var data = new ArrayList<Byte>();

        data.addAll(toBytes(transform[0][0]));
        data.addAll(toBytes(transform[0][1]));
        data.addAll(toBytes(transform[0][2]));

        data.addAll(toBytes(transform[1][0]));
        data.addAll(toBytes(transform[1][1]));
        data.addAll(toBytes(transform[1][2]));

        data.addAll(toBytes(transform[2][0]));
        data.addAll(toBytes(transform[2][1]));
        data.addAll(toBytes(transform[2][2]));

        data.addAll(toBytes(transform[3][0]));
        data.addAll(toBytes(transform[3][1]));
        data.addAll(toBytes(transform[3][2]));

        return data;
    }

    private static List<Byte> toBytes(String value) {
        return toBytes(value.getBytes());
    }

    private static List<Byte> toBytes(byte[] value) {
        var data = new ArrayList<Byte>();
        for (byte b : value) {
            data.add(b);
        }

        return data;
    }

    private static List<Byte> toBytes(double value) {
        var data = new ArrayList<Byte>();

        long lng = Double.doubleToLongBits(value);
        for (int i = 7; i >= 0; i--) {
            data.add((byte) ((lng >> ((7 - i) * 8)) & 0xff));
        }

        return data;
    }

    private static List<Byte> toBytes(float value) {
        var data = new ArrayList<Byte>();

        int _int = Float.floatToIntBits(value);
        for (int i = 3; i >= 0; i--) {
            data.add((byte) ((_int >> ((3 - i) * 8)) & 0xff));
        }

        return data;
    }

    private static List<Byte> toBytes(int value) {
        var data = new ArrayList<Byte>();
        data.add((byte) value);
        data.add((byte) (value >> 8));
        data.add((byte) (value >> 16));
        data.add((byte) (value >> 24));

        return data;
    }

    private static List<Byte> toBytes(short value) {
        var data = new ArrayList<Byte>();
        data.add((byte) value);
        data.add((byte) (value >> 8));

        return data;
    }

    private static List<Byte> toBytes(Vector3D point) {
        var data = new ArrayList<Byte>();
        data.addAll(toBytes(point.getX()));
        data.addAll(toBytes(point.getY()));
        data.addAll(toBytes(point.getZ()));

        return data;
    }

    private static List<Byte> toBytes(FloatingFSM.FSMData fsmData) {
        var data = new ArrayList<Byte>();

        data.add((byte) 0x1D);
        data.addAll(toDynamicBytes((byte) fsmData.states.size()));

        fsmData.states.forEach(state -> data.addAll(toDynamicBytes(state)));

        data.addAll(toDynamicBytes(fsmData.defaultState));

        data.add((byte) 0x1D);
        data.addAll(toDynamicBytes((byte) fsmData.customStrings.size()));

        fsmData.customStrings.forEach(customString -> data.addAll(toDynamicBytes(customString)));

        data.add((byte) 0x1F);
        data.addAll(toDynamicBytes((byte) fsmData.messageHandlers.size()));

        fsmData.messageHandlers.forEach(msgHandler -> {
            data.addAll(toDynamicBytes(msgHandler.name));

            data.add((byte) 0x1C);
            data.addAll(toDynamicBytes((byte) msgHandler.sendBefore.size()));

            msgHandler.sendBefore.forEach(msg -> data.addAll(toBytes(msg)));

            data.add((byte) 0x1F);
            data.addAll(toDynamicBytes((byte) msgHandler.stateSpecific.size()));

            msgHandler.stateSpecific.forEach((specificName, specificMessages) -> {
                data.addAll(toDynamicBytes(specificName));

                data.add((byte) 0x1C);
                data.addAll(toDynamicBytes((byte) specificMessages.size()));

                specificMessages.forEach(msg -> data.addAll(toBytes(msg)));

            });

            data.add((byte) 0x1C);
            data.addAll(toDynamicBytes((byte) msgHandler.sendAfter.size()));
            msgHandler.sendAfter.forEach(msg -> data.addAll(toBytes(msg)));

        });

        byte enemyByte = (byte) (fsmData.enemyData ? 0x00 : 0x01);
        data.addAll(toBytes(new byte[]{0x0E, enemyByte, 0x0E, enemyByte}));

        return data;
    }

    private static List<Byte> toBytes(FloatingFSM.FSMData.Message msg) {
        var data = new ArrayList<Byte>();

        data.addAll(toDynamicBytes(msg.message));
        data.addAll(toDynamicBytes(msg.functionName));
        data.addAll(toDynamicBytes(msg.targetObjectName));
        data.add((byte) 0x1C);
        data.addAll(toDynamicBytes((byte) msg.params.size()));
        msg.params.forEach(param -> data.addAll(toDynamicBytes(param)));
        data.addAll(toBytes(new byte[]{0x0E, (byte) (msg.targetObjectName.isEmpty() ? 0x01 : 0x00)}));

        return data;
    }


    private static List<Byte> toBytes(DynamicMesh.DynamicData dynamicData) {
        var data = new ArrayList<Byte>();

        data.addAll(toDynamicBytes((byte) dynamicData.keyframeTransforms.size()));

        dynamicData.keyframeTransforms.forEach((keyframe, transform) -> {
            data.addAll(toDynamicBytes(keyframe));
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    data.addAll(toDynamicBytes(transform[i][j]));
                }
            }
        });

        data.addAll(toDynamicBytes((byte) dynamicData.animations.size()));

        dynamicData.animations.forEach(animation -> {
            data.addAll(toDynamicBytes(animation.name));
            data.addAll(toDynamicBytes(animation.length));
            data.addAll(toDynamicBytes(animation.startTransform));
            data.addAll(toDynamicBytes(animation.endTransform));

            data.addAll(toBytes(new byte[]{0x1C, 0x14, 0x00, 0x1C, 0x14, 0x00, 0x1C, 0x14, 0x00}));

            for (byte b : new byte[]{0x71, 0x03, 0x01}) {
                data.addAll(toDynamicBytes(b));
            }

            data.addAll(toDynamicBytes(animation.position.sampleRate));
            for (int i = 0; i < animation.position.points.size(); i++) {
                data.addAll(toDynamicBytes(animation.position.points.get(i)));
            }

            for (byte b : new byte[]{0x71, 0x03, 0x01}) {
                data.addAll(toDynamicBytes(b));
            }

            data.addAll(toDynamicBytes(animation.rotation.sampleRate));
            for (int i = 0; i < animation.rotation.points.size(); i++) {
                data.addAll(toDynamicBytes(animation.rotation.points.get(i)));
            }

            data.addAll(toDynamicBytes((double) Math.round(animation.length * 1000000d) / 1000000d));

            data.addAll(toDynamicBytes(animation.startKeyframe));
            data.addAll(toDynamicBytes(animation.endKeyframe));

            var rMin = animation.rotation.points.stream().min(Float::compareTo).orElseThrow();
            var rMax = animation.rotation.points.stream().max(Float::compareTo).orElseThrow();
            var pMin = animation.position.points.stream().min(Float::compareTo).orElseThrow();
            var pMax = animation.position.points.stream().max(Float::compareTo).orElseThrow();

            data.addAll(toDynamicAnimation("Position", animation.position, rMin, rMax, pMin, pMax, animation.unkByte1, animation.unkByte2));
            data.addAll(toDynamicAnimation("Rotation", animation.rotation, 0f, 1f, 0f, 1f, animation.unkByte3, animation.unkByte4));
        });

        return data;
    }

    private static List<Byte> toDynamicAnimation(
            String type,
            DynamicMesh.DynamicData.DynamicAnimation.AnimationGraph graph,
            Float min1,
            Float max1,
            Float min2,
            Float max2,
            byte unkByte1,
            byte unkByte2
    ) {
        var data = new ArrayList<Byte>();

        for (byte b : new byte[]{0x71, 0x03}) {
            data.addAll(toDynamicBytes(b));
        }

        data.addAll(toDynamicBytes("t"));
        data.addAll(toDynamicBytes(type));

        for (byte b : new byte[]{0x01, 0x00, 0x00}) {
            data.addAll(toDynamicBytes(b));
        }

        data.addAll(toDynamicBytes(unkByte1));

        for (byte b : new byte[]{0x01, 0x01, 0x02}) {
            data.addAll(toDynamicBytes(b));
        }

        data.addAll(toDynamicBytes(min1));
        data.addAll(toDynamicBytes(max1));
        data.addAll(toDynamicBytes(min2));
        data.addAll(toDynamicBytes(max2));

        data.addAll(toDynamicBytes(255));
        data.addAll(toDynamicBytes(0));
        data.addAll(toDynamicBytes(0));

        data.addAll(toDynamicBytes(unkByte2));

        for (byte b : new byte[]{0x00, 0x00}) {
            data.addAll(toDynamicBytes(b));
        }

        data.addAll(toDynamicBytes(graph.sampleRate));

        data.addAll(dynamicFloat4List(graph.interpolation));

        data.addAll(toBytes(new byte[]{0x11, 0x00}));

        return data;
    }

    private static List<Byte> toDynamicBytes(Object value) {
        if (value instanceof Byte) {
            return dynamicByte((Byte) value);
        }
        if (value instanceof String) {
            return dynamicString((String) value);
        }
        if (value instanceof Double) {
            return dynamicDouble((Double) value);
        }
        if (value instanceof Integer) {
            return dynamicInteger((Integer) value);
        }
        if (value instanceof Float) {
            return dynamicFloat((Float) value);
        }
        if (value instanceof float[][]) {
            return dynamicTransform((float[][]) value);
        }
        if (value instanceof Short) {
            return ((Short) value) >= 128 ? dynamicShort((Short) value) : dynamicByte(((Short) value).byteValue());
        }

        throw new RuntimeException("unknown type");
    }

    private static List<Byte> dynamicFloat4List(List<float[]> value) {
        var data = new ArrayList<Byte>();

        data.addAll(toBytes(new byte[]{0x11, (byte) value.size()}));
        for (int i = 0; i < value.size(); i++) {
            data.addAll(toDynamicBytes((byte) 0x70));
            data.addAll(toDynamicBytes((byte) 0x01));
            data.addAll(toDynamicBytes(value.get(i)[0]));
            data.addAll(toDynamicBytes(value.get(i)[1]));
            data.addAll(toDynamicBytes(value.get(i)[2]));
            data.addAll(toDynamicBytes(value.get(i)[3]));
        }

        return data;
    }

    private static List<Byte> dynamicByte(Byte value) {
        return new ArrayList<>(toBytes(new byte[]{0x14, value}));
    }

    private static List<Byte> dynamicDouble(Double value) {
        var data = new ArrayList<Byte>();

        data.add((byte) 0x0A);
        data.addAll(toBytes(value));

        return data;
    }

    private static List<Byte> dynamicInteger(Integer value) {
        var data = new ArrayList<Byte>();

        data.add((byte) 0x01);
        data.addAll(toBytes(value));

        return data;
    }

    private static List<Byte> dynamicShort(Short value) {
        var data = new ArrayList<Byte>();

        data.add((byte) 0x13);
        data.addAll(toBytes(value));

        return data;
    }

    private static List<Byte> dynamicFloat(Float value) {
        var data = new ArrayList<Byte>();

        data.add((byte) 0x09);
        data.addAll(toBytes(value));

        return data;
    }

    private static List<Byte> dynamicTransform(float[][] value) {
        var data = new ArrayList<Byte>();

        data.add((byte) 0x1A);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                data.addAll(toBytes(value[i][j]));
            }
        }

        return data;
    }

    private static List<Byte> dynamicString(String value) {
        var data = new ArrayList<Byte>();

        data.addAll(toBytes(new byte[]{0x0D, 0x14, (byte) value.length()}));
        data.addAll(toBytes(value));

        return data;
    }
}
