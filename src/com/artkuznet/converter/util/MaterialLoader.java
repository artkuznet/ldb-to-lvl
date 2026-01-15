package com.artkuznet.converter.util;

import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.ldb2.texture.LdbTexture;
import com.artkuznet.converter.lv2.converter.MaterialConverter;
import com.artkuznet.converter.lv2.converter.TextureConverter;
import com.artkuznet.converter.maxed2.material.Material;
import com.artkuznet.converter.maxed2.material.MaterialType;
import com.artkuznet.converter.maxed2.material.Texture;

import java.util.*;
import java.util.stream.Collectors;

public class MaterialLoader {

    private static final Map<String, MaterialType> materialTypeMap;

    static {
        materialTypeMap = new HashMap<>();
        materialTypeMap.put("ai", MaterialType.AI_NODE_COLLISION_NODRAW);
        materialTypeMap.put("dummy", MaterialType.DUMMY);
        materialTypeMap.put("skybox", MaterialType.SKYBOX);
        materialTypeMap.put("collision", MaterialType.COLLISION_NODRAW);
        materialTypeMap.put("character", MaterialType.CHARACTERCOLLISION_NODRAW);
        materialTypeMap.put("player", MaterialType.PLAYERCOLLISION_NODRAW);
        materialTypeMap.put("npc", MaterialType.NPC_COLLISION_NODRAW);
        materialTypeMap.put("camera", MaterialType.CAMERACOLLISION);
        materialTypeMap.put("white", MaterialType.LIGHTS);
    }

    public static Map<String, MaterialType> getMaterialTypeMap() {
        return materialTypeMap;
    }

    public static List<Texture> getDefaultTextures() {
        return getDefaultLdbTextureMap().values().stream()
                .map(TextureConverter::convert)
                .collect(Collectors.toList());
    }

    public static List<Material> getDefaultMaterials() {

        Map<MaterialType, LdbTexture> textureMap = getDefaultLdbTextureMap();

        List<Material> materials = new ArrayList<>();

        textureMap.forEach((materialType, texture) -> {
            Material material = MaterialConverter.convert(LdbMaterial.fromTexture(textureMap.get(materialType)));
            material.setCategoryName(materialType.toString().toLowerCase());
            materials.add(material);
        });

        return materials;
    }

    private static Map<MaterialType, LdbTexture> getDefaultLdbTextureMap() {
        Map<MaterialType, LdbTexture> textureMap = new HashMap<>();

        materialTypeMap.forEach((filename, materialType) -> textureMap.put(materialType, LdbTexture.fromFile(filename)));

        return textureMap;
    }
}
