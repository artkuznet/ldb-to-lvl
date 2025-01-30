package com.artkuznet.converter.ldb2;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.ldb2.aabb.AABB;
import com.artkuznet.converter.ldb2.character.Character;
import com.artkuznet.converter.ldb2.character.CharacterEnemyGroup;
import com.artkuznet.converter.ldb2.collistionshape.CollisionShape;
import com.artkuznet.converter.ldb2.collistionshape.CollisionShapeMoppData;
import com.artkuznet.converter.ldb2.dynamiclight.DynamicLight;
import com.artkuznet.converter.ldb2.dynamiclight.DynamicLightColor;
import com.artkuznet.converter.ldb2.dynamicmesh.DynamicMesh;
import com.artkuznet.converter.ldb2.dynamicmesh.DynamicMeshAnimation;
import com.artkuznet.converter.ldb2.flare.Flare;
import com.artkuznet.converter.ldb2.fsm.*;
import com.artkuznet.converter.ldb2.jumppoint.JumpPoint;
import com.artkuznet.converter.ldb2.levelitem.LevelItem;
import com.artkuznet.converter.ldb2.lightmap.LightMapTexture;
import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.ldb2.material.MaterialProperties;
import com.artkuznet.converter.ldb2.portal.Portal;
import com.artkuznet.converter.ldb2.room.Room;
import com.artkuznet.converter.ldb2.staticmesh.StaticMesh;
import com.artkuznet.converter.ldb2.staticmesh.StaticMeshContainer;
import com.artkuznet.converter.ldb2.texture.LdbTexture;
import com.artkuznet.converter.ldb2.trigger.Trigger;
import com.artkuznet.converter.ldb2.volumelight.VolumeLight;
import com.artkuznet.converter.ldb2.volumelight.VolumeLightAABB;
import com.artkuznet.converter.ldb2.volumelight.VolumeLightRGB;
import com.artkuznet.converter.ldb2.waypoint.WayPoint;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class MaxLDBReader2 {

    private byte[] stringTable;
    private float physicalWorldSize;
    private MaxLDB2 ldb;

    private MaxTypeReader reader;

    private void parseStringTable() {
        int size = (int) reader.readObject();
        stringTable = reader.readBytes(size);
    }

    private String getStringFromStringTable(int pos) {
        int endPos = pos;
        while (stringTable[endPos] != 0) {
            endPos++;
        }
        return new String(stringTable, pos, endPos - pos);
    }

    private Map<String, FSMCode2> parseFSMHandlers(int pos) {

        int[] p = {pos};

        int count = stringTable[p[0]];
        p[0] += 2;

        Map<String, FSMCode2> handlers = new HashMap<>();

        for (int i = 0; i < count; i++) {

            String handlerName = getStringFromStringTable(p[0]);

            p[0] += handlerName.length() + 1;

            FSMCode2 fsmCode2 = parseFSMCode2(p);

            handlers.put(handlerName, fsmCode2);
        }

        return handlers;
    }

    private FSMCode2 parseFSMCode2(int pos) {
        return parseFSMCode2(new int[]{pos});
    }

    private FSMCode2 parseFSMCode2(int[] pos) {

        List<String> onBefore = parseFsmMessages(pos);
        List<String> onAfter = parseFsmMessages(pos);

        int customStateMessageCount = stringTable[pos[0]];
        pos[0] += 2;

        List<FSMState> fsmStates = new ArrayList<>();

        for (int i = 0; i < customStateMessageCount; i++) {
            int stateIndex = stringTable[pos[0]];
            pos[0] += 2;

            List<String> stateMessages = parseFsmMessages(pos);

            fsmStates.add(new FSMState(stateIndex, stateMessages));
        }

        return new FSMCode2(onBefore, onAfter, fsmStates);
    }

    private List<String> parseFsmMessages(int pos) {
        return parseFsmMessages(new int[]{pos});
    }

    private List<String> parseFsmMessages(int[] pos) {
        if (pos.length != 1) {
            throw new RuntimeException();
        }
        List<String> messages = new ArrayList<>();
        int count = stringTable[pos[0]];
        pos[0] += 2;
        for (int i = 0; i < count; i++) {
            String line = getStringFromStringTable(pos[0]);
            messages.add(line);
            pos[0] += line.length() + 1;
        }

        return messages;
    }

    private LdbTexture parseTexture(int groupId) {
        int fileType = (int) reader.readObject();
        int size = (int) reader.readObject();
        String filePath = getStringFromStringTable((Integer) reader.readObject());
        byte[] data = reader.readBytes(size);
        return new LdbTexture(groupId, filePath, fileType, data);
    }

    private void parseLightMaps() {
        int isDds = (int) reader.readObject();
        int fileType = isDds == 1 ? 5 : 0;
        int count = (int) reader.readObject();
        for (int i = 0; i < count; i++) {
            int size = (int) reader.readObject();
            byte[] data = reader.readBytes(size);
            ldb.getLightMaps().add(new LightMapTexture(i, fileType, data));
        }
    }

    private void parseTextures() {
        // Diffuse textures
        int diffuseCount = (int) reader.readObject();
        for (int i = 0; i < diffuseCount; i++) {
            ldb.getTextures().add(parseTexture(0));
        }

        // Lightmaps
        parseLightMaps();

        // Detail, reflection, and gloss textures
        for (int groupId = 1; groupId <= 3; groupId++) {
            int count = (int) reader.readObject();
            for (int i = 0; i < count; i++) {
                ldb.getTextures().add(parseTexture(groupId));
            }
        }
    }

    private void parseMaterials() {
        int materialCount = (int) reader.readObject();
        for (int i = 0; i < materialCount; i++) {
            LdbMaterial material = new LdbMaterial(i);
            int blendMode = (int) reader.readObject();
            int frameStart = (int) reader.readObject();
            int frameEnd = (int) reader.readObject();
            List<LdbTexture> frames = new ArrayList<>();
            for (int j = frameStart; j <= frameEnd; j++) {
                frames.add(ldb.getTextures().findTextureByGroupAndID(0, j));
            }
            material.setLightmapTexture(ldb.getLightMaps().findLightMapById((Integer) reader.readObject()));
            material.setDetailTexture(ldb.getTextures().findTextureByGroupAndID(1, (Integer) reader.readObject()));
            material.setReflectionTexture(ldb.getTextures().findTextureByGroupAndID(2, (Integer) reader.readObject()));
            material.setGlossTexture(ldb.getTextures().findTextureByGroupAndID(3, (Integer) reader.readObject()));
            material.setProperties(new MaterialProperties(
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    frames,
                    blendMode
            ));
            material.setDiffuseTexture(frames.get(material.getProperties().getVisibleFrame()));
            ldb.getMaterials().add(material);
        }
    }

    private void parseHeader() {
        byte[] header = reader.readBytes(4);
        if ((int) reader.readObject() != 0x22) {
            throw new RuntimeException("Unsupported file version");
        }
    }

    private void parseRooms() {
        int roomCount = (int) reader.readObject();
        for (int i = 0; i < roomCount; i++) {
            String roomName = (String) reader.readObject();
            float[][] transform = (float[][]) reader.readObject();

            int unk1 = (int) reader.readObject(); // Unknown value

            ldb.getRooms().add(new Room(
                    i,
                    roomName,
                    transform,
                    new AABB((Vertex) reader.readObject(), (Vertex) reader.readObject(), (Vertex) reader.readObject()),
                    parseRoomStaticMesh(),
                    parseRoomCollisions(),
                    parseRoomVolumeLights()
            ));
        }
    }

    private StaticMeshContainer parseRoomStaticMesh() {
        StaticMeshContainer staticMeshes = new StaticMeshContainer();
        int meshCount = (int) reader.readObject();
        for (int i = 0; i < meshCount; i++) {
            int materialId = (int) reader.readObject();

            int hasLightmapsUVs = (int) reader.readObject();
            int hasDetailTexture = (int) reader.readObject();

            int polygonsCount = (int) reader.readObject();
            int verticesCount = (int) reader.readObject();

            List<Vertex> vertices = new ArrayList<>();
            List<Vertex> normals = new ArrayList<>();
            List<VertexUV> uvs = new ArrayList<>();
            List<VertexUV> lightmapUVs = new ArrayList<>();
            List<VertexUV> detailTextureUVs = new ArrayList<>();

            for (int j = 0; j < verticesCount; j++) {
                vertices.add(new Vertex(parseFloat(), parseFloat(), parseFloat()));
            }

            for (int j = 0; j < verticesCount; j++) {
                normals.add(new Vertex(parseFloat(), parseFloat(), parseFloat()));
            }

            for (int j = 0; j < verticesCount; j++) {
                uvs.add(new VertexUV(parseFloat(), parseFloat()));
            }

            if (hasLightmapsUVs == 1) {
                for (int j = 0; j < verticesCount; j++) {
                    lightmapUVs.add(new VertexUV(parseFloat(), parseFloat()));
                }
            }

            if (hasDetailTexture == 1) {
                for (int j = 0; j < verticesCount; j++) {
                    detailTextureUVs.add(new VertexUV(parseFloat(), parseFloat()));
                }
            }

            List<Integer> indices = new ArrayList<>();
            for (int j = 0; j < polygonsCount * 3; j++) {
                indices.add(parseInt(2, false));
            }

            staticMeshes.add(new StaticMesh(vertices, normals, indices, materialId, uvs, lightmapUVs, detailTextureUVs));
        }
        return staticMeshes;
    }

    private List<CollisionShape> parseRoomCollisions() {
        List<CollisionShape> collisionShapes = new ArrayList<>();
        int collisionCount = (int) reader.readObject();
        for (int i = 0; i < collisionCount; i++) {
            int verticesCount = (int) reader.readObject();
            int polygonsCount = (int) reader.readObject();
            List<Vertex> vertices = new ArrayList<>();
            for (int j = 0; j < verticesCount; j++) {
                vertices.add(new Vertex(parseFloat(), parseFloat(), parseFloat()));
            }
            List<Integer> indices = new ArrayList<>();
            for (int j = 0; j < polygonsCount * 3; j++) {
                indices.add(parseInt(2, false));
            }
            List<Integer> materialIndices = new ArrayList<>();
            for (int j = 0; j < polygonsCount; j++) {
                materialIndices.add(parseInt(1, false));
            }
            int isConvex = (int) reader.readObject();
            int collisionMask = (int) reader.readObject();
            float originX = parseFloat();
            float originY = parseFloat();
            float originZ = parseFloat();
            reader.readBytes(4); // Unknown value
            int moppCodeSize = parseInt(4, true);
            byte[] moppCode = reader.readBytes(moppCodeSize);
            collisionShapes.add(
                    new CollisionShape(
                            vertices,
                            indices,
                            materialIndices,
                            isConvex,
                            new ArrayList<>() /*collisionMask*/,
                            new CollisionShapeMoppData(originX, originY, originZ, moppCode)
                    )
            );
        }
        return collisionShapes;
    }

    private List<VolumeLight> parseRoomVolumeLights() {
        List<VolumeLight> volumeLights = new ArrayList<>();
        int volumeLightCount = (int) reader.readObject();
        for (int i = 0; i < volumeLightCount; i++) {
            int gridWidth = (int) reader.readObject();
            int gridHeight = (int) reader.readObject();
            int gridDepth = (int) reader.readObject();
            Vertex minPoint = (Vertex) reader.readObject();
            Vertex maxPoint = (Vertex) reader.readObject();
            List<VolumeLightRGB> colors = new ArrayList<>();
            for (int j = 0; j < gridWidth * gridHeight * gridDepth; j++) {
                colors.add(new VolumeLightRGB(parseFloat(), parseFloat(), parseFloat()));
            }
            volumeLights.add(new VolumeLight(gridWidth, gridHeight, gridDepth, new VolumeLightAABB(minPoint, maxPoint), colors));
        }
        return volumeLights;
    }

    private void parseDynamicLights() {
        int lightCount = (int) reader.readObject();
        for (int i = 0; i < lightCount; i++) {
            float[][] transform = (float[][]) reader.readObject();
            int roomId = (int) reader.readObject();
            float R = (float) reader.readObject();
            float G = (float) reader.readObject();
            float B = (float) reader.readObject();
            float A = (float) reader.readObject();
            float falloff = (float) reader.readObject();

            ldb.getDynamicLights().add(new DynamicLight(
                    transform,
                    roomId,
                    new DynamicLightColor(R, G, B, A),
                    falloff
            ));
        }
    }

    private void parseFlares() {
        int flareCount = (int) reader.readObject();
        for (int i = 0; i < flareCount; i++) {
            ldb.getFlares().add(new Flare(
                    getStringFromStringTable((int) reader.readObject()),
                    (float[][]) reader.readObject(),
                    (int) reader.readObject()
            ));
        }
    }

    private void parseLevelItems() {
        int itemCount = (int) reader.readObject();
        for (int i = 0; i < itemCount; i++) {
            ldb.getLevelItems().add(new LevelItem(
                    getStringFromStringTable((int) reader.readObject()),
                    getStringFromStringTable((int) reader.readObject()),
                    (float[][]) reader.readObject(),
                    (int) reader.readObject()
            ));
        }
    }

    private void parsePortals() {
        int portalCount = (int) reader.readObject();
        for (int i = 0; i < portalCount; i++) {
            String name = (String) reader.readObject();
            Vertex normal = (Vertex) reader.readObject();
            int unk1 = (int) reader.readObject();
            int unk2 = (int) reader.readObject();

            int cnt = (int) reader.readObject();
            List<Vertex> points = new ArrayList<>();
            for (int j = 0; j < cnt; j++) {
                points.add((Vertex) reader.readObject());
            }

            ldb.getPortals().add(new Portal(name, normal, unk1, unk2, points));
        }
    }

    private void parseJumpPoints() {
        int jumpPointCount = (int) reader.readObject();
        for (int i = 0; i < jumpPointCount; i++) {
            ldb.getJumpPoints().add(new JumpPoint(
                    (String) reader.readObject(),
                    (float[][]) reader.readObject(),
                    (int) reader.readObject()
            ));
        }
    }

    private void parseWayPoints() {
        int wayPointCount = (int) reader.readObject();
        for (int i = 0; i < wayPointCount; i++) {
            ldb.getWayPoints().add(new WayPoint(
                    (String) reader.readObject(),
                    (float[][]) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject()
            ));
        }
    }

    private void parseCharacters() {
        reader.readByte(); // Unknown value
        int enemyGroupCount = (int) reader.readObject();
        for (int i = 0; i < enemyGroupCount; i++) {
            ldb.getCharacterEnemyGroups().add(new CharacterEnemyGroup(
                    (String) reader.readObject(), (Integer) reader.readObject()
            ));
        }
        int characterCount = (int) reader.readObject();
        for (int i = 0; i < characterCount; i++) {
            ldb.getCharacters().add(new Character(
                    getStringFromStringTable((int) reader.readObject()),
                    getStringFromStringTable((int) reader.readObject()),
                    (float[][]) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (String) reader.readObject()
            ));
        }
    }

    private void parseFSMs() {
        int fsmCount = (int) reader.readObject();
        for (int i = 0; i < fsmCount; i++) {
            String name = getStringFromStringTable((int) reader.readObject());
            float[][] transform = (float[][]) reader.readObject();
            int parentId = (int) reader.readObject();
            float[][] localTransform = (float[][]) reader.readObject();
            int roomId = (int) reader.readObject();

            String defaultState = getStringFromStringTable((int) reader.readObject());
            reader.readByte(); // Unknown value
            int customStateCount = (int) reader.readObject();
            List<String> customStates = new ArrayList<>();
            for (int j = 0; j < customStateCount; j++) {
                customStates.add((String) reader.readObject());
            }
            int startupCodeOffset = (int) reader.readObject();
            int customEventOffset = (int) reader.readObject();
            int customStatesOffset = (int) reader.readObject();
            int entityFSMsOffset = (int) reader.readObject();

            FSMCode2 onStartup = parseFSMCode2(startupCodeOffset);
            Map<String, FSMCode2> handlersCustomEvent = parseFSMHandlers(customEventOffset);
            Map<String, FSMCode2> handlersCustomStates = parseFSMHandlers(customStatesOffset);
            Map<String, FSMCode2> handlersEntityFSM = parseFSMHandlers(entityFSMsOffset);

            List<FSMTimer> fsmTimers = new ArrayList<>();

            int timerCount = (int) reader.readObject();
            for (int j = 0; j < timerCount; j++) {
                String timerName = getStringFromStringTable((int) reader.readObject());
                int isRealTime = (int) reader.readObject();
                float length = (float) reader.readObject();
                int startTimerFSMOffset = (int) reader.readObject();
                int endTimerFSMOffset = (int) reader.readObject();

                FSMCode2 onStart = parseFSMCode2(startTimerFSMOffset);
                FSMCode2 onEnd = parseFSMCode2(endTimerFSMOffset);

                fsmTimers.add(new FSMTimer(
                        timerName,
                        isRealTime,
                        length,
                        onStart,
                        onEnd
                ));
            }

            ldb.getFSMS().add(new FSM(
                    i,
                    name,
                    transform,
                    parentId,
                    localTransform,
                    roomId,
                    customStates,
                    defaultState,
                    onStartup,
                    handlersEntityFSM,
                    handlersCustomEvent,
                    fsmTimers
            ));
        }
    }

    private void parseTriggers() {
        int triggerCount = (int) reader.readObject();
        for (int i = 0; i < triggerCount; i++) {
            int fmsId = (int) reader.readObject();
            float radius = (float) reader.readObject();
            int activationPlayer = (int) reader.readObject();
            int activationUse = (int) reader.readObject();
            int activationEnemy = (int) reader.readObject();
            int activationBullet = (int) reader.readObject();
            int activationLookAt = (int) reader.readObject();
            int activationVisibility = (int) reader.readObject();
            String activatorsUseAnimation = (String) reader.readObject();
            int hasCollisionShape = (int) reader.readObject();

            int parent = -1;
            List<CollisionShape> collisionShapes = new ArrayList<>();

            if (hasCollisionShape == 1) {
                parent = (int) reader.readObject();
                if (parent == -1) {
                    collisionShapes = parseRoomCollisions();
                }
            }

            Trigger trigger = new Trigger(
                    fmsId,
                    radius,
                    activationPlayer,
                    activationUse,
                    activationEnemy,
                    activationBullet,
                    activationLookAt,
                    activationVisibility,
                    activatorsUseAnimation,
                    hasCollisionShape,
                    parent,
                    collisionShapes
            );

            ldb.getTriggers().add(trigger);
        }
    }

    private List<DynamicMeshAnimation> parseDynamicMeshAnimation() {
        List<DynamicMeshAnimation> animations = new ArrayList<>();
        int animationCount = (int) reader.readObject();
        for (int i = 0; i < animationCount; i++) {
            String animName = getStringFromStringTable((int) reader.readObject());
            float animationLength = (float) reader.readObject();
            float[][] startTransform = (float[][]) reader.readObject();
            float[][] endTransform = (float[][]) reader.readObject();
            // Translation
            reader.readObject();
            reader.readObject();
            reader.readObject();
            int tSampleRate = (int) reader.readObject();
            int tNumPoints = (int) reader.readObject();
            List<Float> tTime = new ArrayList<>();
            List<Float> tValue = new ArrayList<>();
            for (int j = 0; j < tNumPoints; j++) {
                tTime.add(parseFloat());
            }
            for (int j = 0; j < tNumPoints; j++) {
                tValue.add(parseFloat());
            }
            // Rotation
            reader.readObject();
            reader.readObject();
            reader.readObject();
            int rSampleRate = (int) reader.readObject();
            int rNumPoints = (int) reader.readObject();
            List<Float> rTime = new ArrayList<>();
            List<Float> rValue = new ArrayList<>();
            for (int j = 0; j < rNumPoints; j++) {
                rTime.add(parseFloat());
            }
            for (int j = 0; j < rNumPoints; j++) {
                rValue.add(parseFloat());
            }
            int leavingFirstFrameFSMStart = (int) reader.readObject();
            int returningToFirstFrameFSMStart = (int) reader.readObject();
            int reachingSecondFrameFSMStart = (int) reader.readObject();

            List<String> leavingFirstFrameMessages = parseFsmMessages(leavingFirstFrameFSMStart);
            List<String> returningToFirstFrameMessages = parseFsmMessages(returningToFirstFrameFSMStart);
            List<String> reachingSecondFrameMessages = parseFsmMessages(reachingSecondFrameFSMStart);

            animations.add(new DynamicMeshAnimation(
                    animName,
                    animationLength,
                    startTransform,
                    endTransform,
                    tSampleRate,
                    tTime,
                    tValue,
                    rSampleRate,
                    rTime,
                    rValue,
                    leavingFirstFrameMessages,
                    returningToFirstFrameMessages,
                    reachingSecondFrameMessages
            ));
        }

        return animations;
    }

    private void parseDynamicMeshes() {
        List<Integer> groups = new ArrayList<>();
        List<Object[]> prefabMaster = new ArrayList<>();
        int dynamicMeshCount = (int) reader.readObject();
        for (int i = 0; i < dynamicMeshCount; i++) {
            int fsmId = (int) reader.readObject();
            int useLightMaps = (int) reader.readObject();
            int pointLightsAffect = (int) reader.readObject();
            int continuousUpdate = (int) reader.readObject();
            int bulletCollision = (int) reader.readObject();
            int characterCollision = (int) reader.readObject();
            int blockExplosions = (int) reader.readObject();
            int noDecals = (int) reader.readObject();
            int elevator = (int) reader.readObject();
            int physicalMaterial = (int) reader.readObject();
            int prefabId = (int) reader.readObject();
            int shareCollision = (int) reader.readObject();

            AABB aabb = new AABB((Vertex) reader.readObject(), (Vertex) reader.readObject(), (Vertex) reader.readObject());

            StaticMeshContainer mesh = null;
            List<CollisionShape> collisionShapes = new ArrayList<>();
            if (prefabId == -1) {
                mesh = parseRoomStaticMesh();
                collisionShapes = parseRoomCollisions();
            } else if (prefabId >= 0) {
                if (!groups.contains(prefabId)) {
                    mesh = parseRoomStaticMesh();
                    collisionShapes = parseRoomCollisions();
                    groups.add(prefabId);
                    prefabMaster.add(new Object[]{mesh, collisionShapes, aabb});
                } else {
                    if (useLightMaps != 0) {
                        mesh = parseRoomStaticMesh();
                        if (shareCollision == 0) {
                            collisionShapes = parseRoomCollisions();
                        } else {
                            collisionShapes = (List<CollisionShape>) prefabMaster.get(prefabId)[1];
                        }
                    } else {
                        collisionShapes = (List<CollisionShape>) prefabMaster.get(prefabId)[1];
                        mesh = (StaticMeshContainer) prefabMaster.get(prefabId)[0];
                        aabb = (AABB) prefabMaster.get(prefabId)[2];
                    }
                }
            }
            ldb.getDynamicMeshes().add(new DynamicMesh(
                    fsmId,
                    useLightMaps == 1,
                    pointLightsAffect == 1,
                    continuousUpdate == 1,
                    bulletCollision == 1,
                    characterCollision == 1,
                    blockExplosions == 1,
                    noDecals == 1,
                    elevator == 1,
                    physicalMaterial,
                    prefabId,
                    shareCollision == 1,
                    aabb,
                    mesh,
                    collisionShapes,
                    parseDynamicMeshAnimation()
            ));
        }
    }

    private void parseMirrors() {
        int mirrorCount = (int) reader.readObject();
        for (int i = 0; i < mirrorCount; i++) {
            Vertex planeNormal = (Vertex) reader.readObject();
            Vertex planePoint = (Vertex) reader.readObject();
            float boundingSphereRadius = (float) reader.readObject();
            Vertex boundingBoxMin = (Vertex) reader.readObject();
            Vertex boundingBoxMax = (Vertex) reader.readObject();
            int unk = (int) reader.readObject();
            int numTriangles = (int) reader.readObject();
            for (int j = 0; j < numTriangles; j++) {
                int materialID = (int) reader.readObject();
                Vertex triangleVertex1 = (Vertex) reader.readObject();
                Vertex triangleVertex2 = (Vertex) reader.readObject();
                Vertex triangleVertex3 = (Vertex) reader.readObject();
                VertexUV uvForVertex1 = (VertexUV) reader.readObject();
                VertexUV uvForVertex2 = (VertexUV) reader.readObject();
                VertexUV uvForVertex3 = (VertexUV) reader.readObject();
            }
            int roomID = (int) reader.readObject();
        }
    }

    public MaxLDBReader2(String filePath) {
        this.ldb = new MaxLDB2();
        this.reader = new MaxTypeReader(filePath);

        parseHeader();
        parseStringTable();
        physicalWorldSize = (float) reader.readObject();
        parseTextures();
        parseMaterials();
        parseRooms();
        parseDynamicLights();
        parseFlares();
        parseLevelItems();
        parsePortals();
        parseJumpPoints();
        parseWayPoints();
        parseCharacters();
        parseFSMs();
        parseTriggers();
        parseDynamicMeshes();
        parseMirrors();
    }

    private float parseFloat() {
        byte[] buffer = reader.readBytes(4);
        return ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    private int parseInt(int size, boolean signed) {
        byte[] buffer = reader.readBytes(size);
        ByteBuffer bb = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
        switch (size) {
            case 1:
                return signed ? bb.get() : bb.get() & 0xFF;
            case 2:
                return signed ? bb.getShort() : bb.getShort() & 0xFFFF;
            case 4:
                return bb.getInt();
            default:
                throw new RuntimeException("Unsupported size: " + size);
        }
    }

    public MaxLDB2 getLdb() {
        return ldb;
    }
}
