package com.artkuznet.converter.lv2.converter.helper;

import com.artkuznet.converter.maxed2.material.MaterialType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialCategoryMapper {

    private final static Map<MaterialType, List<String>> categoryBitmaps;

    static {
        categoryBitmaps = new HashMap<>();

        categoryBitmaps.put(MaterialType.GLASS_BULLETPROOF, Arrays.asList(
                "X:\\Textures\\Window\\window03_256x512.dds"
        ));

        categoryBitmaps.put(MaterialType.TILE, Arrays.asList(
                "X:\\Textures\\Floor\\Tilefloor03G_512x512.dds",
                "X:\\Textures\\Tile\\Tile15_B_256x256.dds"
        ));

        categoryBitmaps.put(MaterialType.WOOD, Arrays.asList(
                "X:\\Textures\\Wood\\Vodka_woodpanel1_512x512.dds",
                "X:\\Textures\\Door\\WoodenDoor06_256x512.dds",
                "X:\\Textures\\Wood\\skirtboard01_512x32.dds",
                "X:\\Textures\\Wood\\Temp_Bradel_Lautaseina_512x512.dds",
                "X:\\Textures\\Wood\\Woodwall01_512x512.dds"
        ));

        categoryBitmaps.put(MaterialType.PLASTIC, Arrays.asList(
                "X:\\Textures\\Maxshouse\\Maxhouse_Furniture_TrashBin_256x512.dds"
        ));

        categoryBitmaps.put(MaterialType.METAL_SOLID, Arrays.asList(
                "X:\\TEXTURES\\MACHINES\\HEATER01_256X256.DDS"
        ));

        categoryBitmaps.put(MaterialType.ELECTRICPANEL, Arrays.asList(
                "X:\\Textures\\Lamps\\lamp_ceiling01_256x64.dds"
        ));

        categoryBitmaps.put(MaterialType.GRAFFITI, Arrays.asList(
                "X:\\Textures\\Misc\\Trash01_B_512x512.dds",
                "X:\\Textures\\Graffiti\\Graffiti_generic02_dds_256x256.dds",
                "X:\\TEXTURES\\MISC\\TRASHONTHEFLOOR03_512X512.DDS",
                "X:\\Textures\\Markings\\oil_stain01_256x256.dds",
                "X:\\Textures\\Misc\\Floor_numbers02_512x64.dds",
                "X:\\Textures\\Maxshouse\\Maxhouse_Object_CD_DeadLeaves_256x256.dds"
        ));

        categoryBitmaps.put(MaterialType.EXTERNALWALL, Arrays.asList(
                "X:\\Textures\\Maxshouse\\ExternalWall01_512x512.dds"
        ));

        categoryBitmaps.put(MaterialType.METAL_HOLLOW, Arrays.asList(
                "X:\\Textures\\Metal\\VentilationPipe03_1024x512.dds"
        ));

        categoryBitmaps.put(MaterialType.LEAVES, Arrays.asList(
                "X:\\Textures\\Maxshouse\\Maxhouse_Plant_Hanging01_256x512.dds",
                "X:\\Textures\\Maxshouse\\Maxhouse_Plant_Hanging02_256x512.dds",
                "X:\\Textures\\Plants\\Plant04_256x512.dds"
        ));

        categoryBitmaps.put(MaterialType.CARDBOARD, Arrays.asList(
                "X:\\Textures\\Box\\CardboardBox04Side_256x64.dds"
        ));
    }

    public static MaterialType getCategoryByBitmapName(String bitmapName) {

        for (Map.Entry<MaterialType, List<String>> e : categoryBitmaps.entrySet()) {
            if (e.getValue().contains(bitmapName)) {
                return e.getKey();
            }
        }

        return MaterialType.DEFAULT;
    }
}
