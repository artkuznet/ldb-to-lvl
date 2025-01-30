package com.artkuznet.converter.ldb2.lightmap;

import java.util.ArrayList;
import java.util.List;

public class LightMapTextureContainer {
    private List<LightMapTexture> textures;

    public LightMapTextureContainer() {
        this.textures = new ArrayList<>();
    }

    public void add(LightMapTexture texture) {
        textures.add(texture);
    }

    public LightMapTexture get(int index) {
        return textures.get(index);
    }

    public int size() {
        return textures.size();
    }

    public LightMapTexture findLightMapById(int id) {
        for (LightMapTexture texture : textures) {
            if (texture.getId() == id) {
                return texture;
            }
        }
        return null;
    }
}