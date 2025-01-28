package com.artkuznet.converter.ldb.lightmap;

import java.util.ArrayList;
import java.util.List;

public class LightmapTextureContainer {

    private List<LightmapTexture> textures = new ArrayList<>();

    public void add(LightmapTexture texture) {
        textures.add(texture);
    }

    public LightmapTexture getTextureById(int id) {

        if (-1 == id) {
            return null;
        }

        return textures.stream()
                .filter(texture -> texture.getId() == id).findFirst()
                .orElseThrow(() -> new RuntimeException("LightmapTexture not found with id " + id));
    }
}
