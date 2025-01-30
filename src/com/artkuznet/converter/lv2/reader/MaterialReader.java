package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.material.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialReader {

    public static Material read(MaxTypeReader reader) {
        reader.rememberOffset();

        ReaderHelper.read500(reader);

        int dataSize = (int) reader.readObject();

        Material material = new Material();

        material.setName((String) reader.readObject());

        List<String> diffuseTextureNames = new ArrayList<>();
        int framesCount = (int) reader.readObject();
        for (int k = 0; k < framesCount; k++) {
            diffuseTextureNames.add((String) reader.readObject());
        }

        material.setDiffuseTextureNames(diffuseTextureNames);
        material.setLightLayerTextureName((String) reader.readObject());
        material.setDetailTextureName((String) reader.readObject());

        List<float[]> unkFloats = new ArrayList<>();

        for (int k = 0; k < 5; k++) {
            if (10 != reader.readByte()) {
                throw new RuntimeException();
            }
            // 128x128
            unkFloats.add(new float[]{reader.readFloat(), reader.readFloat()});
        }
        material.setUnkFloats(unkFloats);

        material.setDualsided(1 == (int) reader.readObject());
        material.setReferenceValue((int) reader.readObject());
        material.setAdultContent(1 == (int) reader.readObject());
        material.setBlendMode((int) reader.readObject());
        material.setEdgeBlend(1 == (int) reader.readObject());
        material.setReflection(1 == (int) reader.readObject());
        material.setGloss(1 == (int) reader.readObject());
        material.setReflectionTextureName((String) reader.readObject());
        material.setGlossTextureName((String) reader.readObject());
        material.setUiVisibleFrame((int) reader.readObject());
        material.setFramerate((int) reader.readObject());

        reader.validateDataSize(dataSize);

        if (material.getUiVisibleFrame() > framesCount - 1) {
            throw new RuntimeException();
        }

        return material;
    }
}
