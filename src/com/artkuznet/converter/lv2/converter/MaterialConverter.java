package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.ldb2.material.MaterialProperties;
import com.artkuznet.converter.ldb2.texture.LdbTexture;
import com.artkuznet.converter.maxed2.material.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialConverter {

    public static Material convert(LdbMaterial ldbMaterial) {
        Material material = new Material();

        MaterialProperties properties = ldbMaterial.getProperties();

        List<String> names = properties.getFrames().stream()
                .map(LdbTexture::getFilePath)
                .collect(Collectors.toList());

        String glossTextureName = ldbMaterial.getGlossTexture() != null
                ? ldbMaterial.getGlossTexture().getFilePath()
                : "";

        String reflectionTextureName = ldbMaterial.getReflectionTexture() != null
                ? ldbMaterial.getReflectionTexture().getFilePath()
                : "";

        material.setName(names.get(0));
        material.setDiffuseTextureNames(names);
        material.setLightLayerTextureName("");
        material.setDetailTextureName("");
        material.setDualsided(1 == properties.getDualSided());
        material.setReferenceValue(properties.getAlphaCompareReferenceValue() & 0xFF);
        material.setAdultContent(false);
        material.setBlendMode(mapBlendMode(properties.getBlendMode()));
        material.setEdgeBlend(1 == material.getBlendMode());
        material.setReflection(!reflectionTextureName.isEmpty());
        material.setGloss(!glossTextureName.isEmpty());
        material.setReflectionTextureName(reflectionTextureName);
        material.setGlossTextureName(glossTextureName);
        material.setUiVisibleFrame(properties.getVisibleFrame());
        material.setFramerate(properties.getFramerate());

        List<float[]> unkFloats = new ArrayList<>();
        unkFloats.add(new float[]{0, 1.875f});
        unkFloats.add(new float[]{0, 1.875f});
        unkFloats.add(new float[]{0, 1.875f});
        unkFloats.add(new float[]{0, 1.875f});
        unkFloats.add(new float[]{0, 1.875f});
        material.setUnkFloats(unkFloats);

        return material;
    }

    private static int mapBlendMode(int mode) {
        return mode != 0 ? 1 : 0; // todo ?
    }
}
