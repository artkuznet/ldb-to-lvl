package com.artkuznet.converter.lv2;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.lv2.reader.*;
import com.artkuznet.converter.lv2.writer.EntityWriter;
import com.artkuznet.converter.maxed2.entity.*;
import com.artkuznet.converter.maxed2.entity.fsm.FSM;
import com.artkuznet.converter.maxed2.entity.fsm.FloatingFSM;
import com.artkuznet.converter.maxed2.entity.mesh.*;
import com.artkuznet.converter.maxed2.entity.point.AIN;
import com.artkuznet.converter.maxed2.entity.point.JumpPoint;
import com.artkuznet.converter.maxed2.entity.point.WayPoint;
import com.artkuznet.converter.maxed2.entity.prefab.Prefab;
import com.artkuznet.converter.maxed2.entity.prefab.PrefabParent;
import com.artkuznet.converter.maxed2.material.MaterialCategory;

import java.util.ArrayList;
import java.util.List;

public class MaxLV2Reader {

    MaxTypeReader reader;

    LV2 lv2 = new LV2();

    public LV2 getLV2() {
        return lv2;
    }

    public MaxLV2Reader(String filename) {
        reader = new MaxTypeReader(filename);

        readHeader();
        lv2.setTextures(TextureReader.read(reader));
        lv2.setMaterialCategories(readMaterialCategories());
        readLightmapData();
        lv2.setPreferences(DocumentPreferencesReader.read(reader));
        lv2.setWorldGroup(readEntityRecursive(null));

        if (2 != (int) reader.readObject()) {
            throw new RuntimeException();
        }

        readUnkData2();

        lv2.setCameraPosition(new Vector3D(
                (double) reader.readObject(),
                (double) reader.readObject(),
                (double) reader.readObject())
        );
        lv2.setCameraRotation((Vertex) reader.readObject());
        lv2.setUnkCam((double) reader.readObject());

        readGroups();
    }

    private Entity readEntityRecursive(Entity parent) {
        Entity entity = readBaseEntityAttributes();
        entity = readEntity(entity);
        entity.setParentEntity(parent);
        int childCount = (int) reader.readObject();
        for (int i = 0; i < childCount; i++) {
            Entity child = readEntityRecursive(entity);
            entity.addChildEntity(child);
        }
        return entity;
    }

    private Entity readBaseEntityAttributes() {

        int type = (int) reader.readObject();

        reader.rememberOffset();

        ReaderHelper.read400(reader);

        int dataSize = (int) reader.readObject();

        int unk1 = (int) reader.readObject(); // todo ?

        double[][] localMatrix = ReaderHelper.readLocalMatrix(reader);
        Vector3D minPoint = new Vector3D((double) reader.readObject(), (double) reader.readObject(), (double) reader.readObject());
        Vector3D maxPoint = new Vector3D((double) reader.readObject(), (double) reader.readObject(), (double) reader.readObject());
        double radius = (double) reader.readObject();

        int[] generalProperties = new int[]{
                (int) reader.readObject(), // is hidden (0 - hidden, 1 - not)
                (int) reader.readObject(), // exclude from game (1 - exclude, 0 - not)
                (int) reader.readObject(), // exclude from lighting (1 exclude)
                (int) reader.readObject(), // enable export regrouping (1 enable)
                (int) reader.readObject(), // gameplay critical (1 critical)
        };

        String name = (String) reader.readObject();
        boolean hasFsm = 1 == (int) reader.readObject();

        Entity o = getMaxObject(type, hasFsm);

        o.setName(name);
        o.setUnk1(unk1);
        o.setHidden(0 == generalProperties[0]);
        o.setExcludeFromGame(1 == generalProperties[1]);
        o.setExcludeFromLighting(1 == generalProperties[2]);
        o.setEnableExportRegrouping(1 == generalProperties[3]);
        o.setGameplayCritical(1 == generalProperties[4]);
        o.setLocalMatrix(localMatrix);
        o.setMinPoint(minPoint);
        o.setMaxPoint(maxPoint);
        o.setRadius(radius);

        if (o instanceof FSM) {
            FSMReader.read(o, reader);
            dataSize += 13;
        }

        reader.validateDataSize(dataSize);

        return o;
    }

    private Entity getMaxObject(int type, boolean hasFsm) {
        switch (type) {
            case 120:
                return new VolumeLightingBox();
            case 104:
                return new PrefabParent();
            case 115:
                return new Prefab();
            case 105:
                return new FloatingFSM();
            case 103:
                return new WorldGroup();
            case 118:
                return new AIN();
            case 107:
                return new WayPoint();
            case 108:
                return new JumpPoint();
            case 109:
                return new LevelItem();
            case 110:
                return new Enemy();
            case 100:
                return hasFsm ? new DynamicMesh() : new Mesh();
            case 117:
                return hasFsm ? new DynamicTriangleMesh() : new TriangleMesh();
            case 111:
                return new DynamicPointlight();
            case 112:
                return new PolyGroup();
            case 113:
                return new RadiosityLight();
            case 114:
                return new Portal();
            case 106:
                return new Trigger();
            case 116:
                return new Player();
            case 119:
                return new Flare();
            default:
                throw new RuntimeException();
        }
    }

    private Entity readEntity(Entity o) {
        int type = EntityWriter.getEntityType(o);

        switch (type) {
            case 120:
                return VolumeLightingBoxReader.read(o, reader);
            case 104:
                return PrefabParentReader.read(o, reader);
            case 115: // prefab
            case 107: // waypoint
            case 108: // jumppoint
            case 118: // AIN
            case 103: // worldgroup
                return o;
            case 105:
                return FloatingFSMReader.read(o, reader);
            case 100:
                return MeshReader.read(o, reader);
            case 117:
                return (o instanceof FSM) ? DynamicTriangleMeshReader.read(o, reader) : TriangleMeshReader.read(o, reader);
            case 114:
                return PortalReader.read(o, reader);
            case 112:
                return PolyGroupReader.read(o, reader);
            case 106:
                return TriggerReader.read(o, reader);
            case 113:
                return RadiosityLightReader.read(o, reader);
            case 116:
                return PlayerReader.read(o, reader);
            case 111:
                return DynamicPointlightReader.read(o, reader);
            case 110:
                return EnemyReader.read(o, reader);
            case 109:
                return LevelItemReader.read(o, reader);
            case 119:
                return FlareReader.read(o, reader);
            default:
                throw new RuntimeException("wrong object type");
        }
    }

    private void readHeader() {
        reader.rememberOffset();

        ReaderHelper.read100(reader);

        int dataSize = (int) reader.readObject();

        lv2.setMaxEdVersion((String) reader.readObject());

        if (3 != (int) reader.readObject()) {
            throw new RuntimeException();
        }

        lv2.setMaxEdBuild((String) reader.readObject());

        reader.validateDataSize(dataSize);
    }

    private List<MaterialCategory> readMaterialCategories() {

        List<MaterialCategory> materialCategories = new ArrayList<>();

        int materialCategoriesCount = (int) reader.readObject();
        for (int i = 0; i < materialCategoriesCount; i++) {

            reader.rememberOffset();

            ReaderHelper.read100(reader);

            int dataSize = (int) reader.readObject();
            int categoryMaterialsCount = (int) reader.readObject();

            MaterialCategory category = new MaterialCategory();

            category.setName((String) reader.readObject());

            reader.validateDataSize(dataSize);

            for (int j = 0; j < categoryMaterialsCount; j++) {
                category.addMaterial(MaterialReader.read(reader));
            }

            materialCategories.add(category);
        }

        return materialCategories;
    }

    private void readLightmapData() {
        ReaderHelper.read100(reader);
        int dataSize = (int) reader.readObject();
        lv2.setLightmapData(reader.readBytes(dataSize - 8 - 5));
    }

    private void readUnkData2() {
        reader.rememberOffset();
        ReaderHelper.read100(reader);
        int dataSize = (int) reader.readObject();
        lv2.setUnkData2(reader.readBytes(dataSize - 8 - 5));
        reader.validateDataSize(dataSize);
    }

    private void readGroups() {
        reader.rememberOffset();

        ReaderHelper.read100(reader);

        int dataSize = (int) reader.readObject();

        int groupCount = (int) reader.readObject();
        List<String> groupNames = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) {
            groupNames.add((String) reader.readObject());
        }

        lv2.setGroups(groupNames);

        reader.validateDataSize(dataSize);
    }
}
