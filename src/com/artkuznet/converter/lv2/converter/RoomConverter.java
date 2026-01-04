package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.ldb2.EntityLdb2;
import com.artkuznet.converter.ldb2.character.CharacterEnemyGroup;
import com.artkuznet.converter.ldb2.collistionshape.CollisionShape;
import com.artkuznet.converter.ldb2.dynamicmesh.DynamicMesh;
import com.artkuznet.converter.ldb2.fsm.FSM;
import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.ldb2.portal.Portal;
import com.artkuznet.converter.ldb2.room.Room;
import com.artkuznet.converter.ldb2.staticmesh.StaticMesh;
import com.artkuznet.converter.ldb2.trigger.Trigger;
import com.artkuznet.converter.lv2.converter.helper.*;
import com.artkuznet.converter.lv2.converter.helper.radiosity.RadiosityLightGenerator;
import com.artkuznet.converter.maxed.LvlExit;
import com.artkuznet.converter.maxed.LvlPolygon;
import com.artkuznet.converter.maxed2.entity.Enemy;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.TriggerData;
import com.artkuznet.converter.maxed2.entity.fsm.FloatingFSM;
import com.artkuznet.converter.maxed2.entity.mesh.Mesh;
import com.artkuznet.converter.maxed2.entity.mesh.PhysicalMaterial;
import com.artkuznet.converter.maxed2.material.Material;
import com.artkuznet.converter.maxed2.material.MaterialType;
import com.artkuznet.converter.util.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RoomConverter {

    public static List<LdbTriangleDTO> getTriangles(
            List<StaticMesh> staticMeshes,
            List<LdbMaterial> ldbMaterials,
            boolean isDynamic,
            Vector3D pivot
    ) {
        List<LdbTriangleDTO> meshTriangles = new ArrayList<>();
        for (int meshId = 0; meshId < staticMeshes.size(); meshId++) {
            StaticMesh staticMesh = staticMeshes.get(meshId);
            for (int i = 0; i < staticMesh.getIndices().size() / 3; i++) {
                List<Integer> indices = staticMesh.getIndices().subList(i * 3, (i + 1) * 3);
                List<Vector3D> vertices = indices.stream()
                        .map(index -> staticMesh.getVertices().get(index))
                        .map(Vector3D::new)
                        .map(v -> v.clone().minus(pivot))
                        .collect(Collectors.toList());

                LdbTriangleDTO triangle = new LdbTriangleDTO(vertices);

                if (isDynamic) {
                    triangle.setDynamicMesh();
                }

                int id = staticMesh.getMaterialId();
                int materialId = ldbMaterials.indexOf(ldbMaterials.get(id));

                if (materialId >= 0) {
                    triangle.setMaterialId(materialId);
                }

                triangle.setUv(indices.stream().map(index -> staticMesh.getUvs().get(index)).collect(Collectors.toList()));

                triangle.setMeshId(meshId);

                meshTriangles.add(triangle);
            }
        }

        return meshTriangles.stream()
                .filter(t -> t.getArea() > 1e-6) // fix zero area triangles
                .collect(Collectors.toList());
    }

    private static List<LdbTriangleDTO> getCollisionTriangles(CollisionShape collisionShape, Vector3D pivot) {
        List<LdbTriangleDTO> collisionTriangles = new ArrayList<>();

        for (int i = 0; i < collisionShape.getIndices().size() / 3; i++) {
            List<Integer> indices = collisionShape.getIndices().subList(i * 3, (i + 1) * 3);
            List<Vector3D> vertices = indices.stream()
                    .map(index -> collisionShape.getVertices().get(index))
                    .map(Vector3D::new)
                    .map(v -> v.clone().minus(pivot))
                    .collect(Collectors.toList());

            LdbTriangleDTO triangle = new LdbTriangleDTO(vertices);

            triangle.setMaterialTypeId(collisionShape.getMaterialIndices().get(i));

//            triangle.setCollisionId(-1);

            triangle.setMaterialId(-20);

            triangle.setUv(Arrays.asList(new VertexUV(0, 0), new VertexUV(1, 0), new VertexUV(0, 1)));

            if (collisionShape.getIsConvex() == 1) {
                triangle.setConvex();
            }

            collisionTriangles.add(triangle);
        }

        return collisionTriangles;
    }

    public static Mesh convert(
            Room room,
            List<EntityLdb2> roomEntities,
            List<Portal> ldbPortals,
            List<Trigger> ldbTriggers,
            List<DynamicMesh> dynamicMeshes,
            List<FSM> fsms,
            List<Material> materials,
            List<LdbMaterial> ldbMaterials,
            List<CharacterEnemyGroup> enemyGroups
    ) {
        double[][] roomMatrix = MatrixUtil.matrixFloatToDouble(room.getTransform());

        // todo refactor

        Map<MaterialType, Integer> materialTypeIds = new HashMap<>();
        Arrays.stream(MaterialType.values())
                .forEach(materialType -> materials.stream()
                        .filter(material -> material.getCategoryName().equalsIgnoreCase(materialType.toString()))
                        .findFirst()
                        .ifPresent(material -> materialTypeIds.put(materialType, materials.indexOf(material)))
                );


        Vector3D roomPosition = new Vector3D(roomMatrix[3][0], roomMatrix[3][1], roomMatrix[3][2]);

        List<StaticMesh> staticMeshes = room.getStaticMeshes().getList();

        List<LdbTriangleDTO> meshTriangles = getTriangles(staticMeshes, ldbMaterials, false, new Vector3D(0, 0, 0));


        List<FSM> nonFloatingFsms = new ArrayList<>();


        List<LdbTriangleDTO> collisionTriangles = new ArrayList<>();

        List<CollisionShape> collisionShapes = room.getCollisions();
        for (int collisionId = 0; collisionId < collisionShapes.size(); collisionId++) {

            CollisionShape collisionShape = collisionShapes.get(collisionId);

            for (int i = 0; i < collisionShape.getIndices().size() / 3; i++) {
                List<Integer> indices = collisionShape.getIndices().subList(i * 3, (i + 1) * 3);
                List<Vector3D> vertices = indices.stream()
                        .map(index -> collisionShape.getVertices().get(index))
                        .map(Vector3D::new)
                        .collect(Collectors.toList());

                LdbTriangleDTO triangle = new LdbTriangleDTO(vertices);

                triangle.setMaterialTypeId(collisionShape.getMaterialIndices().get(i));

//                triangle.setCollisionId(collisionId);

                triangle.setMaterialId(materialTypeIds.get(MaterialType.DUMMY));

                triangle.setUv(Arrays.asList(new VertexUV(0, 0), new VertexUV(1, 0), new VertexUV(0, 1)));

                collisionTriangles.add(triangle);
            }
        }


        // todo refactor

        List<LdbTriangleDTO> portalTriangles = ldbPortals.stream()
                .filter(p -> p.getName().toLowerCase().startsWith(room.getName().toLowerCase() + "::"))
                .map(portal -> {

                    List<LdbTriangleDTO> triangleDTOS = new ArrayList<>();

                    List<Vector3D> pVertices = portal.getPoints().stream()
                            .map(Vector3D::new)
                            .map(v -> v.minus(roomPosition))
                            .collect(Collectors.toList());

                    Set<Portal> linkedPortals = ldbPortals.stream()
                            .filter(p -> !p.equals(portal))
                            .filter(p -> new HashSet<>(p.getPoints().stream()
                                    .map(Vector3D::new).map(Vector3D::hardSmooth).collect(Collectors.toSet())).containsAll(
                                    portal.getPoints().stream().map(Vector3D::new).map(Vector3D::hardSmooth).collect(Collectors.toSet())
                            ))
                            .collect(Collectors.toSet());

                    Portal linkedPortal = linkedPortals.size() == 1 ? linkedPortals.stream().findFirst().orElseThrow(RuntimeException::new) : null;

                    if (linkedPortals.size() != 1) {
                        throw new RuntimeException();
                    }

                    List<List<Vector3D>> triangulated = Triangulator.triangulate(pVertices);
                    for (List<Vector3D> triangles : triangulated) {
                        LdbTriangleDTO t = new LdbTriangleDTO(triangles);
                        t.setPortal(portal.getName(), linkedPortal.getName());
                        t.setMaterialId(-10); // todo ?
                        triangleDTOS.add(t);
                    }

                    return triangleDTOS;
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());

        meshTriangles.addAll(portalTriangles);


        List<List<LdbTriangleDTO>> extractedObjects = GeometryProcessor.splitIntoRoomAndObjects(meshTriangles);

        collisionTriangles.addAll(portalTriangles);
        List<List<LdbTriangleDTO>> extractedObjects2 = GeometryProcessor.splitIntoRoomAndObjects(
                collisionTriangles.stream()
                        .filter(o -> !Arrays.asList(
                                MaterialType.CAMERACOLLISION.ordinal(),
                                MaterialType.GRAFFITI.ordinal()
                        ).contains(o.getMaterialTypeId()))
                        .collect(Collectors.toList())
        );

        List<LdbTriangleDTO> largestMesh = extractedObjects.stream()
                .max(Comparator.comparingDouble(o -> o.stream()
                        .map(LdbTriangleDTO::getArea)
                        .reduce(Double::sum)
                        .orElseThrow(RuntimeException::new))
                ).orElseThrow(RuntimeException::new);

        List<LdbTriangleDTO> skyboxTriangles = collisionTriangles.stream()
                .filter(t -> t.getMaterialTypeId() == MaterialType.SKYBOX.ordinal())
                .peek(t -> t.setMaterialId(materialTypeIds.get(MaterialType.SKYBOX)))
                .collect(Collectors.toList());

        largestMesh.addAll(skyboxTriangles);

        // todo AI collisions

        // todo refactor

        List<LdbTriangleDTO> aiColTriangles = collisionTriangles.stream()
                .filter(t -> t.getMaterialTypeId() == MaterialType.AI_NODE_COLLISION_NODRAW.ordinal())
                .peek(t -> t.setMaterialId(materialTypeIds.get(MaterialType.AI_NODE_COLLISION_NODRAW)))
                .collect(Collectors.toList());

        List<LdbTriangleDTO> dummy = collisionTriangles.stream()
                .filter(t -> t.getMaterialTypeId() == MaterialType.DUMMY.ordinal())
                .peek(t -> t.setMaterialId(materialTypeIds.get(MaterialType.DUMMY)))
                .collect(Collectors.toList());

        List<LdbTriangleDTO> npcCol = collisionTriangles.stream()
                .filter(t -> t.getMaterialTypeId() == MaterialType.NPC_COLLISION_NODRAW.ordinal())
                .peek(t -> t.setMaterialId(materialTypeIds.get(MaterialType.NPC_COLLISION_NODRAW)))
                .collect(Collectors.toList());

        List<LdbTriangleDTO> charaterCol = collisionTriangles.stream()
                .filter(t -> t.getMaterialTypeId() == MaterialType.CHARACTERCOLLISION_NODRAW.ordinal())
                .peek(t -> t.setMaterialId(materialTypeIds.get(MaterialType.CHARACTERCOLLISION_NODRAW)))
                .collect(Collectors.toList());

        List<LdbTriangleDTO> colNodraw = collisionTriangles.stream()
                .filter(t -> t.getMaterialTypeId() == MaterialType.COLLISION_NODRAW.ordinal())
                .peek(t -> t.setMaterialId(materialTypeIds.get(MaterialType.COLLISION_NODRAW)))
                .collect(Collectors.toList());

        List<LdbTriangleDTO> playerColTriangles = collisionTriangles.stream()
                .filter(t -> t.getMaterialTypeId() == MaterialType.PLAYERCOLLISION_NODRAW.ordinal())
                .peek(t -> t.setMaterialId(materialTypeIds.get(MaterialType.PLAYERCOLLISION_NODRAW)))
                .collect(Collectors.toList());

        List<LdbTriangleDTO> cameraTriangles = collisionTriangles.stream()
                .filter(t -> t.getMaterialTypeId() == MaterialType.CAMERACOLLISION.ordinal())
                .peek(t -> t.setMaterialId(materialTypeIds.get(MaterialType.CAMERACOLLISION)))
                .collect(Collectors.toList());


        List<LdbTriangleDTO> largestMesh2 = extractedObjects2.stream()
                .max(Comparator.comparingDouble(o -> o.stream()
                        .map(LdbTriangleDTO::getArea)
                        .reduce(Double::sum)
                        .orElseThrow(RuntimeException::new))
                ).orElseThrow(RuntimeException::new);

        largestMesh2.addAll(skyboxTriangles);


        Map<LdbTriangleDTO, LdbTriangleDTO> map = TriangleMapper.mapTriangles(largestMesh, largestMesh2);
        Map<LdbTriangleDTO, LdbTriangleDTO> map2 = TriangleMapper.mapTriangles(largestMesh2, largestMesh);

        int mId = materials.indexOf(materials.stream()
                .filter(m -> m.getName().equals("X:\\Textures\\Maxshouse\\Maxhouse_Wood02_Burned.dds"))
                .findFirst().orElse(null)
        );

        Set<LdbTriangleDTO> finalRoom = new HashSet<>();
        finalRoom.addAll(map.keySet().stream()
                .filter(t -> !(room.getName().contains("3rd_floor_corridor") && t.getMaterialId() == mId))
                .collect(Collectors.toSet()));
        finalRoom.addAll(largestMesh2.stream().filter(cT -> !map2.containsKey(cT)).collect(Collectors.toSet()));

        finalRoom.addAll(portalTriangles);

        // todo do not generate polygroups!

        meshTriangles.addAll(playerColTriangles);
        meshTriangles.addAll(cameraTriangles);

        meshTriangles.addAll(aiColTriangles);
        meshTriangles.addAll(dummy);
        meshTriangles.addAll(colNodraw);
        meshTriangles.addAll(charaterCol);
        meshTriangles.addAll(npcCol);

        meshTriangles.stream()
                .filter(t -> t.getMaterialTypeId() == MaterialType.DEFAULT.ordinal())
                .filter(t -> t.getMaterialId() >= 0)
                .forEach(t -> {
                    Material material = materials.get(t.getMaterialId());
                    material.setCategoryName(MaterialCategoryMapper.getCategoryByBitmapName(material.getName()).toString().toLowerCase());
                });

        extractedObjects = GeometryProcessor.splitIntoRoomAndObjects(meshTriangles.stream()
                .filter(t -> !finalRoom.contains(t))
                .collect(Collectors.toList()));

        List<Vector3D> vertices = extractedObjects.stream()
                .flatMap(List::stream)
                .map(LdbTriangleDTO::getVertices)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        vertices.addAll(finalRoom.stream()
                .map(LdbTriangleDTO::getVertices)
                .flatMap(List::stream).distinct()
                .collect(Collectors.toList())
        );

        Map<Integer, DynamicMesh> dynamicMeshMap = dynamicMeshes.stream()
                .collect(Collectors.toMap(DynamicMesh::getFsmId, Function.identity()));

        List<Integer> dynamicMeshFsmIndices = dynamicMeshes.stream()
                .map(DynamicMesh::getFsmId)
                .collect(Collectors.toList());

        List<FSM> dynamicMeshFsms = fsms.stream()
                .filter(fsm -> fsm.getRoomId() == room.getId())
                .filter(fsm -> dynamicMeshFsmIndices.contains(fsm.getIndex()))
                .collect(Collectors.toList());

        // todo refactor

        Map<FSM, Entity> fsmEntityMap = new HashMap<>();

        List<com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh> dMeshes = dynamicMeshFsms.stream()
                .map(fsm -> {

                    nonFloatingFsms.add(fsm);

                    DynamicMesh dynamicMesh = dynamicMeshMap.get(fsm.getIndex());

                    if (dynamicMesh == null) {
                        throw new RuntimeException();
                    }

                    Vector3D pivot = new Vector3D(dynamicMesh.getAabb().getPivotPoint()).multiply(-1);
                    List<LdbTriangleDTO> dynamicTriangles = getTriangles(
                            dynamicMesh.getStaticMeshContainer().isEmpty()
                                    ? MeshDummy.generate()
                                    : dynamicMesh.getStaticMeshContainer().getList(),
                            ldbMaterials,
                            true,
                            pivot
                    );

                    dynamicTriangles.stream()
                            .filter(t -> t.getMaterialTypeId() == MaterialType.DEFAULT.ordinal())
                            .filter(t -> t.getMaterialId() >= 0)
                            .forEach(t -> {
                                Material material = materials.get(t.getMaterialId());
                                material.setCategoryName(MaterialCategoryMapper.getCategoryByBitmapName(material.getName()).toString().toLowerCase());
                            });

                    if (dynamicMesh.getStaticMeshContainer().isEmpty()) {
                        dynamicTriangles.forEach(t -> t.setMaterialId(materialTypeIds.get(MaterialType.DUMMY)));
                    }

                    com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh dMesh = (com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh) MeshConverter.convert(
                            vertices,
                            dynamicTriangles,
                            EntityNameBuilder.buildName(fsm.getName()),
                            MatrixUtil.matrixFloatToDouble(fsm.getLocalTransform(), roomPosition),
                            materials,
                            true,
                            true
                    );

                    // todo fix parent position
                    dMesh.setDynamicData(DynamicDataConverter.convert(dMesh, fsm.getParent() == -1 ? roomPosition : new Vector3D(0, 0, 0), dynamicMesh.getAnimations()/*, fsm*/));

                    dMesh.getProperties().setPhysicalMaterial(PhysicalMaterial.values()[dynamicMesh.getPhysicalMaterial()].toString().toLowerCase());

                    // todo properties
                    dMesh.getProperties().setUseLightmaps(dynamicMesh.isUseLightMaps());
                    dMesh.getProperties().setPointlightsAffect(dynamicMesh.isPointLightsAffect());
                    dMesh.getProperties().setFsmContinuousUpdate(dynamicMesh.isContinuousUpdate());
                    dMesh.getProperties().setBulletCollisions(dynamicMesh.isBulletCollision());
                    dMesh.getProperties().setCharacterCollisions(dynamicMesh.isCharacterCollision());
                    dMesh.getProperties().setBlockExplosions(dynamicMesh.isBlockExplosions());
                    dMesh.getProperties().setNoDecals(dynamicMesh.isNoDecals());
                    dMesh.getProperties().setElevator(dynamicMesh.isElevator());

                    dynamicMesh.getCollision().stream()
                            .map(s -> RoomConverter.getCollisionTriangles(s, pivot))
                            .map(tList -> tList.stream().peek(t -> t.setMaterialId(materialTypeIds.get(MaterialType.COLLISION_NODRAW))).collect(Collectors.toList()))
                            .map(triangleDTOS -> {
                                        Mesh lv2Mesh = MeshConverter.convert(
                                                vertices,
                                                triangleDTOS,
                                                "collision_" + MeshCounter.getInstance().next(),
                                                null,
                                                materials,
                                                false,
                                                false
                                        );

                                        lv2Mesh.getProperties().setGenerateConvexHull(true);

                                        return lv2Mesh;
                                    }
                            )
                            .filter(collisionMesh -> !dMesh.isClosed() || !new HashSet<>(collisionMesh.getVertices()).containsAll(dMesh.getVertices()))
                            .peek(collisionMesh -> {
                                        collisionMesh.getProperties().setCastNoShadows(true);
                                        collisionMesh.getProperties().setDoNotRender(true);
                                        collisionMesh.setGameplayCritical(false);
                                    }
                            ).forEach(collisionMesh -> {
                                collisionMesh.setParentEntity(dMesh);
                                dMesh.addChildEntity(collisionMesh);
                            });

                    if (dMesh.getChildEntities().stream().noneMatch(e -> e.getClass().equals(Mesh.class))
                            && !dynamicMesh.getCollision().isEmpty()
                    ) {
                        dMesh.getProperties().setGenerateConvexHull(true);

                        dMesh.getProperties().setCollisions(true);

                        dMesh.getProperties().setCharacterCollisions(dynamicMesh.isCharacterCollision());
                        dMesh.getProperties().setBulletCollisions(dynamicMesh.isBulletCollision());
                        dMesh.getProperties().setBlockExplosions(dynamicMesh.isBlockExplosions());
                    } else {
                        dMesh.getProperties().setCollisions(false);
                    }

                    dMesh.getProperties().setUseLightmaps(false);
                    dMesh.getProperties().setCastNoShadows(true);

                    dMesh.setFsmData(FsmDataConverter.convert(fsm, dynamicMesh.getAnimations()));

                    fsmEntityMap.put(fsm, dMesh);
                    dMesh.setLdb2FsmName(fsm.getName());

                    return dMesh;
                })
                .collect(Collectors.toList());

        MeshCounter meshCounter = MeshCounter.getInstance();

        String roomName = room.getName().substring(2);

        System.out.println(room.getName());

        Set<LdbTriangleDTO> finalRoom1 = finalRoom;

        com.artkuznet.converter.maxed.Mesh lvlRoomMesh = new com.artkuznet.converter.maxed.Mesh();
        lvlRoomMesh.setName(roomName);
        lvlRoomMesh.setIsRoom(true);
        lvlRoomMesh.setTransform(roomMatrix);
        lvlRoomMesh.setIsRoom(finalRoom1.stream().anyMatch(LdbTriangleDTO::isPortal));
        lvlRoomMesh.setVertices(vertices.toArray(new Vector3D[0]));

        Set<LvlPolygon> polygons = finalRoom1.stream()
                .map(t -> PolygonConverter.convert(t, vertices, materials))
                .peek(p -> p.parentMesh = lvlRoomMesh)
                .collect(Collectors.toSet());

        Set<LvlExit> exitPolygons = polygons.stream()
                .filter(p -> (p instanceof LvlExit))
                .map(p -> (LvlExit) p)
                .collect(Collectors.groupingBy(e -> e.exitName)).values()
                .stream()
                .map(exits -> {

                    List<LvlPolygon.Triangle> exitTriangles = exits.stream()
                            .map(LvlExit::getTriangles)
                            .flatMap(List::stream).collect(Collectors.toList());

                    LvlPolygon.Edge[] exitEdges = ContourFinder.findContours(exitTriangles.stream()
                                    .map(triangle -> Arrays.asList(
                                            new LvlPolygon.VertexEdge(vertices.get(triangle.vertices.get(0)), vertices.get(triangle.vertices.get(1))),
                                            new LvlPolygon.VertexEdge(vertices.get(triangle.vertices.get(1)), vertices.get(triangle.vertices.get(2))),
                                            new LvlPolygon.VertexEdge(vertices.get(triangle.vertices.get(2)), vertices.get(triangle.vertices.get(0)))
                                    ))
                                    .map(LvlPolygon.VertexPolygon::new)
                                    .map(PolygonProcessor::fixClockwise)
                                    .collect(Collectors.toList()))
                            .stream()
                            .flatMap(List::stream)
                            .map(e -> new LvlPolygon.Edge(vertices.indexOf(e.v1), vertices.indexOf(e.v2))).toArray(LvlPolygon.Edge[]::new);

                    LvlExit newExit = new LvlExit(exitEdges, "", exitTriangles.get(0).normal, exits.get(0).exitName, exits.get(0).linkedExitName);

                    newExit.setTriangles(exitTriangles);

                    newExit.index = exits.get(0).index;
                    newExit.setScaleU(exits.get(0).getScaleU());
                    newExit.setScaleV(exits.get(0).getScaleV());
                    newExit.setUnkVertex(exits.get(0).getUnkVertex().clone());
                    newExit.parentMesh = exits.get(0).parentMesh;

                    return newExit;
                }).collect(Collectors.toSet());

        polygons = polygons.stream().filter(p -> !(p instanceof LvlExit)).collect(Collectors.toSet());

        polygons.addAll(exitPolygons);

        lvlRoomMesh.setPolygons(polygons.toArray(new LvlPolygon[0]));

        Mesh roomMesh = MeshConverter.convert(lvlRoomMesh.optimize().joinPolygons().buildPolyGroups());

        List<Entity> entities = new ArrayList<>();

        entities.addAll(dMeshes);

        entities.addAll(extractedObjects.stream().distinct()
                .map(triangleDTOS -> {
                    Mesh m1 = MeshConverter.convert(
                            vertices,
                            triangleDTOS,
                            "mesh" + "_" + meshCounter.next(),
                            null,
                            materials,
                            false,
                            true
                    );

                    RadiosityLightGenerator.appendLight(m1, vertices, materials, ldbMaterials, materialTypeIds.get(MaterialType.LIGHTS));

                    return m1;
                })
                .collect(Collectors.toList())
        );

        fsms.stream().filter(fsm -> fsm.getRoomId() == room.getId())
                .forEach(fsm -> {
                    for (Trigger ldbTrigger : ldbTriggers) {
                        if (ldbTrigger.getFsmId() != fsm.getIndex()) {
                            continue;
                        }

                        nonFloatingFsms.add(fsm);

                        // todo refactor

                        if (!ldbTrigger.getCollisionShapes().isEmpty()) {

                            for (CollisionShape collisionShape : ldbTrigger.getCollisionShapes()) {

                                List<LdbTriangleDTO> triangleDTOS = getCollisionTriangles(collisionShape, new Vector3D(0, 0, 0)).stream()
                                        .peek(t -> t.setMaterialId(materialTypeIds.get(MaterialType.COLLISION_NODRAW))).collect(Collectors.toList());

                                vertices.addAll(triangleDTOS.stream()
                                        .map(LdbTriangleDTO::getVertices)
                                        .flatMap(List::stream).distinct()
                                        .collect(Collectors.toList())
                                );

                                com.artkuznet.converter.maxed.Mesh m = new com.artkuznet.converter.maxed.Mesh();
                                m.setName(EntityNameBuilder.buildName(fsm.getName()));
                                m.setTransform(MatrixUtil.matrixFloatToDouble(fsm.getLocalTransform(), roomPosition));

                                m.setIsRoom(false);

                                m.setVertices(vertices.toArray(new Vector3D[0]));
                                m.setPolygons(triangleDTOS.stream()
                                        .map(t -> PolygonConverter.convert(t, vertices, materials))
                                        .peek(p -> p.parentMesh = m).toArray(LvlPolygon[]::new)
                                );

                                com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh tr = (com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh) MeshConverter.convert(m.optimize().joinPolygons(), true);

                                TriggerData triggerData = new TriggerData();

                                triggerData.setPlayer(1 == ldbTrigger.getActivationPlayer());
                                triggerData.setUse(1 == ldbTrigger.getActivationUse());
                                triggerData.setEnemy(1 == ldbTrigger.getActivationEnemy());
                                triggerData.setBullet(1 == ldbTrigger.getActivationBullet());
                                triggerData.setLookAt(1 == ldbTrigger.getActivationLookAt());
                                triggerData.setVisibility(1 == ldbTrigger.getActivationVisibility());
                                triggerData.setActivatorsUseAnimation(ldbTrigger.getActivatorsUseAnimation());

                                tr.setTriggerData(triggerData);

                                tr.setFsmData(FsmDataConverter.convert(fsm));

                                tr.setTrigger(true);
                                tr.setHasDynamic(false);
                                tr.getProperties().setCollisions(true);
                                tr.getProperties().setCastNoShadows(true);

                                entities.add(tr);
                                fsmEntityMap.put(fsm, tr);
                                tr.setLdb2FsmName(fsm.getName());
                            }

                        } else {
                            com.artkuznet.converter.maxed2.entity.Trigger trigger = TriggerConverter.convert(ldbTrigger, fsm, /*fsm.getParent() == -1 ?*/ roomPosition/* : new Vector3D(0,0,0)*/);
                            entities.add(trigger);
                            fsmEntityMap.put(fsm, trigger);
                            trigger.setLdb2FsmName(fsm.getName());
                        }
                    }
                });

        Map<EntityLdb2, FSM> fsmMap = new HashMap<>();
        roomEntities.forEach(entityLdb2 -> fsms.stream()
                .filter(f -> f.getName().startsWith("FSM"))
                .filter(f -> f.getName().substring(3).equals(entityLdb2.getName()))
                .findFirst()
                .ifPresent(fsm -> fsmMap.put(entityLdb2, fsm))
        );

        nonFloatingFsms.addAll(fsmMap.values());

        Map<Integer, String> characterGroups = enemyGroups.stream()
                .collect(Collectors.toMap(CharacterEnemyGroup::getId, CharacterEnemyGroup::getName));

        entities.addAll(roomEntities.stream()
                .map(e -> {
                    FSM fsm = fsmMap.get(e);
                    Entity entity = EntityConverter.convert(e, fsm, characterGroups, roomPosition);
                    if (fsm != null) {
                        fsmEntityMap.put(fsm, entity);
                        entity.setLdb2FsmName(fsm.getName());
                    }
                    return entity;
                })
                .peek(e -> e.setLocalMatrix(new double[][]{
                        e.getLocalMatrix()[0],
                        e.getLocalMatrix()[1],
                        e.getLocalMatrix()[2],
                        new double[]{
                                e.getLocalMatrix()[3][0] - roomPosition.getX(),
                                e.getLocalMatrix()[3][1] - roomPosition.getY(),
                                e.getLocalMatrix()[3][2] - roomPosition.getZ()
                        },
                }))
                .collect(Collectors.toList())
        );

        entities.addAll(fsms.stream()
                .filter(fsm -> !fsm.getName().equals("FSMPlayer"))
                .filter(fsm -> fsm.getRoomId() == room.getId())
                .filter(fsm -> !nonFloatingFsms.contains(fsm))
                .map(fsm -> {
                    FloatingFSM floatingFSM = new FloatingFSM();
                    floatingFSM.setName(EntityNameBuilder.buildName(fsm.getName()));
                    floatingFSM.setLocalMatrix(MatrixUtil.matrixFloatToDouble(fsm.getLocalTransform(), roomPosition));

                    floatingFSM.setFsmData(FsmDataConverter.convert(fsm));

                    fsmEntityMap.put(fsm, floatingFSM);
                    floatingFSM.setLdb2FsmName(fsm.getName());

                    return floatingFSM;
                })
                .collect(Collectors.toList())
        );

        Map<Integer, FSM> ldb2FsmMap = fsms.stream()
                .filter(fsm -> fsm.getRoomId() == room.getId())
                .collect(Collectors.toMap(FSM::getIndex, Function.identity()));

        Map<FSM, FSM> ldb2FsmParentMap = fsms.stream()
                .filter(fsm -> fsm.getRoomId() == room.getId())
                .filter(fsm -> fsm.getParent() != -1)
                .filter(fsm -> ldb2FsmMap.get(fsm.getParent()) != null)
                .collect(Collectors.toMap(Function.identity(), fsm -> ldb2FsmMap.get(fsm.getParent())));

        Map<Entity, Entity> entityParentMap = ldb2FsmParentMap.entrySet().stream()
                .map((Function<Map.Entry<FSM, FSM>, Map.Entry<Entity, Entity>>) e -> new AbstractMap.SimpleEntry<>(
                        fsmEntityMap.get(e.getKey()), fsmEntityMap.get(e.getValue())
                ))
                .filter(e -> e.getKey() != null)
                .filter(e -> !(e.getKey() instanceof Enemy))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        EntityTreeBuilder.buildEntityTree(
                entities.stream()
                        .filter(e -> !(e instanceof Enemy))
                        .collect(Collectors.toList()),
                entityParentMap
        ).forEach(e -> {
            for (Entity child : e.getChildEntities()) {
                fixPosition(child, roomPosition);
            }
            e.setParentEntity(roomMesh);
            roomMesh.addChildEntity(e);
        });

        entities.stream()
                .filter(e -> e instanceof Enemy)
                .forEach(e -> {
                    e.setParentEntity(roomMesh);
                    roomMesh.addChildEntity(e);
                });

        return roomMesh;
    }

    private static void fixPosition(Entity entity, Vector3D pos) {

        if (entity instanceof Mesh) {
            if (!(entity instanceof com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh)) {
                return;
            }
        }

        for (Entity child : entity.getChildEntities()) {
            fixPosition(child, pos);
        }

        double[][] m = entity.getLocalMatrix();

        entity.setLocalMatrix(new double[][]{
                new double[]{m[0][0], m[0][1], m[0][2]},
                new double[]{m[1][0], m[1][1], m[1][2]},
                new double[]{m[2][0], m[2][1], m[2][2]},
                new double[]{m[3][0] + pos.getX(), m[3][1] + pos.getY(), m[3][2] + pos.getZ()},
        });
    }
}
