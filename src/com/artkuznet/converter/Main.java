package com.artkuznet.converter;

import com.artkuznet.converter.ldb.MaxLDBReader;
import com.artkuznet.converter.ldb2.EntityLdb2;
import com.artkuznet.converter.ldb2.MaxLDB2;
import com.artkuznet.converter.ldb2.MaxLDBReader2;
import com.artkuznet.converter.ldb2.character.CharacterEnemyGroup;
import com.artkuznet.converter.lv2.LV2;
import com.artkuznet.converter.lv2.MaxLV2Reader;
import com.artkuznet.converter.lv2.MaxLV2Writer;
import com.artkuznet.converter.lv2.converter.MaterialConverter;
import com.artkuznet.converter.lv2.converter.RoomConverter;
import com.artkuznet.converter.lv2.converter.TextureConverter;
import com.artkuznet.converter.lvl.LVL;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.Player;
import com.artkuznet.converter.maxed2.entity.Portal;
import com.artkuznet.converter.maxed2.entity.WorldGroup;
import com.artkuznet.converter.maxed2.entity.mesh.Mesh;
import com.artkuznet.converter.maxed2.material.Material;
import com.artkuznet.converter.maxed2.material.MaterialCategory;
import com.artkuznet.converter.obj.OBJ;
import com.artkuznet.converter.util.FsmMessagesFixer;
import com.artkuznet.converter.util.MaterialLoader;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(final String[] args) {

//        MaxLV2Reader lvlReader2 = new MaxLV2Reader(String.format("C:\\MaxPayne2Dev\\Levels\\%s.lv2", "Manor_Frontyard_GG"));
        MaxLV2Reader lvlReader2 = new MaxLV2Reader(String.format("C:\\MaxPayne2Dev\\Levels\\%s.lv2", "ExampleLevel"));


//        String levelName = "05_First_Vodka";
//        String levelName = "04_Warehouse";
        String levelName = "07_Maxs_Apartment_B";
//        String levelName = "21_The_Manor_B";
//        String levelName = "ExampleLevel8520";
        MaxLDBReader2 ldbReader2 = new MaxLDBReader2(String.format("C:\\MaxPayne2Dev\\Game\\data\\database\\levels\\Game\\%s.ldb", levelName));
//        MaxLDBReader2 ldbReader2 = new MaxLDBReader2(String.format("C:\\MaxPayne2Dev\\Game\\data\\database\\levels\\work\\%s.ldb", levelName));

        LV2 lv2 = new LV2();

        MaxLDB2 ldb = ldbReader2.getLdb();

        lv2.setTextures(ldb.getTextures().getList()
                .stream().map(TextureConverter::convert)
                .collect(Collectors.toList())
        );

        List<Material> materials = ldbReader2.getLdb().getMaterials().getList().stream()
                .map(MaterialConverter::convert)
                .collect(Collectors.toList());

        lv2.addTextures(MaterialLoader.getDefaultTextures());

        materials.addAll(MaterialLoader.getDefaultMaterials());

        List<Mesh> rooms = ldb.getRooms().getList().stream()
                .map(room -> {

                    List<EntityLdb2> roomEntities = new ArrayList<>();

                    roomEntities.addAll(ldb.getJumpPoints().getList().stream().filter(e -> e.getRoomId() == room.getId()).collect(Collectors.toList()));
                    roomEntities.addAll(ldb.getWayPoints().getList().stream().filter(e -> e.getRoomId() == room.getId()).collect(Collectors.toList()));
                    roomEntities.addAll(ldb.getCharacters().getList().stream().filter(e -> e.getRoomId() == room.getId()).collect(Collectors.toList()));
                    roomEntities.addAll(ldb.getLevelItems().getList().stream().filter(e -> e.getRoomId() == room.getId()).collect(Collectors.toList()));
                    roomEntities.addAll(ldb.getFlares().getList().stream().filter(e -> e.getRoomId() == room.getId()).collect(Collectors.toList()));
                    roomEntities.addAll(ldb.getDynamicLights().getList().stream().filter(e -> e.getRoomId() == room.getId()).collect(Collectors.toList()));

                    return RoomConverter.convert(
                            room,
                            roomEntities, // todo optimize filter
                            ldb.getPortals().getList(),
                            ldb.getTriggers().getList(),
                            ldb.getDynamicMeshes().getList(),
                            ldb.getFSMS().getList(),
                            materials,
                            ldb.getMaterials().getList(),
                            ldb.getCharacterEnemyGroups().getList()
                    );
                })
                .collect(Collectors.toList());

        List<MaterialCategory> materialCategories = new ArrayList<>();

        Set<String> categoryNames = materials.stream().map(Material::getCategoryName).collect(Collectors.toSet());
        categoryNames.forEach(
                categoryName -> materialCategories.add(
                        new MaterialCategory(
                                categoryName,
                                materials.stream()
                                        .filter(material -> material.getCategoryName().equalsIgnoreCase(categoryName))
                                        .distinct() // todo ?
                                        .collect(Collectors.toList())
                        )
                )
        );

        lv2.setMaterialCategories(materialCategories);

        rooms.forEach(room -> room.getPolygons().forEach(polygon -> {
            if (polygon.getUnk4()[2] == 4) {
                Portal linkedPortal = rooms.stream()
                        .filter(m -> ("::" + m.getName()).equalsIgnoreCase(polygon.linkedPortalName.substring(0, polygon.linkedPortalName.lastIndexOf("::"))))
                        .map(Entity::getChildEntities)
                        .flatMap(List::stream)
                        .filter(e -> e instanceof Portal)
                        .map(e -> (Portal) e)
                        .filter(p -> ("::" + p.getParentEntity().getName() + "::" + p.getName()).equalsIgnoreCase(polygon.linkedPortalName))
                        .findFirst()
                        .orElseThrow(RuntimeException::new);

                polygon.getUnk4()[3] = ((Mesh) linkedPortal.getParentEntity()).getPolygons()
                        .get(linkedPortal.getPolygonIndex())
                        .getIndex();
            }
        }));

        WorldGroup worldGroup = WorldGroup.getEmpty(ldb.getFSMS().getList().stream()
                .filter(fsm -> fsm.getName().equals("FSMPlayer"))
                .findFirst()
                .orElseThrow(RuntimeException::new)
        );

        FsmMessagesFixer.updateFsmMessages(rooms, (Player) worldGroup.getChildEntities().stream()
                .filter(e -> e instanceof Player)
                .findFirst()
                .orElseThrow(RuntimeException::new)
        );

        for (Mesh room : rooms) {
            room.setParentEntity(worldGroup);
            worldGroup.addChildEntity(room);
        }

        lv2.setWorldGroup(worldGroup);

        lv2.setGroups(ldb.getCharacterEnemyGroups().getList().stream().map(CharacterEnemyGroup::getName).collect(Collectors.toList()));

        MaxLV2Writer lv2Writer = new MaxLV2Writer(lv2, String.format("C:\\MaxPayne2Dev\\Levels\\%s.lv2", levelName));
//        MaxLV2Writer lv2Writer = new MaxLV2Writer(lvlReader2.getLV2(), String.format("C:\\MaxPayne2Dev\\Levels\\%s.lv2", levelName));
        lv2Writer.write();

        if (levelName != null) {
            return; // todo remove
        }

        List<String> filenames = Arrays.stream(args)
                .filter(a -> a.toLowerCase().endsWith(".ldb") || a.toLowerCase().endsWith(".obj"))
                .collect(Collectors.toList());

        if (filenames.isEmpty()) {
            throw new RuntimeException("Filename(s) missing");
        }

        Options options = Options.getInstance();

        if (Arrays.stream(args).anyMatch("--skip-join-polygons"::equalsIgnoreCase)) {
            options.skipJoinPolygons = true;
            System.out.println("Skip polygon joining");
        }

        if (Arrays.stream(args).anyMatch("--lv2"::equalsIgnoreCase)) {
            options.saveAsLv2 = true;
            System.out.println("Save as lv2");
        }

        for (String filename : filenames) {
            if (filename.toLowerCase().endsWith(".ldb")) {
                System.out.printf("Read LDB file: \"%s\"%n", filename);
                if (options.saveAsLv2) {
                    throw new RuntimeException("Not implemented yet");
                } else {
                    saveLvl(filename.replace(".ldb", ".lvl"), new LVL(new MaxLDBReader(filename).getLdb()));
                }
            }
            if (filename.toLowerCase().endsWith(".obj")) {
                System.out.printf("Read OBJ file: \"%s\"%n", filename);
                if (options.saveAsLv2) {
                    saveLv2(filename.replace(".obj", ".lv2"), new LV2(new OBJ(filename)));
                } else {
                    saveLvl(filename.replace(".obj", ".lvl"), new LVL(new OBJ(filename)));
                }
            }
        }

        System.out.println("Done");
    }

    private static void saveLvl(String filename, LVL lvl) {
        Writer writer = new Writer(filename);

        List<Byte> bytesList = lvl.toBytes();
        byte[] bytesArray = new byte[bytesList.size()];
        for (int i = 0; i < bytesArray.length; i++) {
            bytesArray[i] = bytesList.get(i);
        }
        writer.writeBytes(bytesArray);

        try {
            writer.save();
            System.out.printf("Saved as: \"%s\"%n%n", filename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveLv2(String filename, LV2 lv2) {
        new MaxLV2Writer(lv2, filename).write();
        System.out.printf("Saved as: \"%s\"%n%n", filename);
    }
}
