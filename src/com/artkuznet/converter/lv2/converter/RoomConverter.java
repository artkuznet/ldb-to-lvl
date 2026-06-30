package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.ldb2.EntityLdb2;
import com.artkuznet.converter.ldb2.Shape;
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
//import com.artkuznet.converter.lv2.converter.helper.radiosity.RadiosityLightGenerator;
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
import com.artkuznet.converter.obj.converter.Ldb2Converter;
import com.artkuznet.converter.util.*;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RoomConverter {

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


        Pair<List<LdbTriangleDTO>, List<List<LdbTriangleDTO>>> roomWithObjects = Ldb2Converter.extractTriangles(room, ldbPortals, new HashMap<>());



        List<FSM> nonFloatingFsms = new ArrayList<>();


        List<Vector3D> vertices = roomWithObjects.getValue().stream()
                .flatMap(List::stream)
                .map(LdbTriangleDTO::getVertices)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
//
        vertices.addAll(roomWithObjects.getKey().stream()
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
                    List<LdbTriangleDTO> dynamicTriangles = Ldb2Converter.getTriangles(
                            dynamicMesh.getStaticMeshContainer().isEmpty()
                                    ? MeshDummy.generate()
                                    : dynamicMesh.getStaticMeshContainer().getList().stream().map(s -> (Shape) s).collect(Collectors.toList()),
                            pivot
                    );

//                    dynamicTriangles.stream()
//                            .filter(t -> t.getMaterialTypeId() == MaterialType.DEFAULT.ordinal())
//                            .filter(t -> t.getMaterialId() >= 0)
//                            .forEach(t -> {
//                                Material material = materials.get(t.getMaterialId());
//                                material.setCategoryName(MaterialCategoryMapper.getCategoryByBitmapName(material.getName()).toString().toLowerCase());
//                            });

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
                            .map(s -> Ldb2Converter.getTriangles(Collections.singletonList(s), pivot))
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

        Set<LdbTriangleDTO> finalRoom1 = new HashSet<>(roomWithObjects.getKey());

        com.artkuznet.converter.maxed.Mesh lvlRoomMesh = new com.artkuznet.converter.maxed.Mesh();
        lvlRoomMesh.setName(roomName);
        lvlRoomMesh.setIsRoom(true);
//        lvlRoomMesh.setTransform(roomMatrix);
        lvlRoomMesh.setIsRoom(finalRoom1.stream().anyMatch(t -> t instanceof LdbTrianglePortalDTO));
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

        entities.addAll(roomWithObjects.getValue().stream().distinct()
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

//                    RadiosityLightGenerator.appendLight(m1, vertices, materials, ldbMaterials, materialTypeIds.get(MaterialType.LIGHTS));

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

                                List<LdbTriangleDTO> triangleDTOS = Ldb2Converter.getTriangles(Collections.singletonList(collisionShape), new Vector3D(0, 0, 0)).stream()
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

      /*  EntityTreeBuilder.buildEntityTree(
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
*/
        /*entities.stream()
                .filter(e -> e instanceof Enemy)
                .forEach(e -> {
                    e.setParentEntity(roomMesh);
                    roomMesh.addChildEntity(e);
                });
        */
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
