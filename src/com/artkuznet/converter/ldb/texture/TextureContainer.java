package com.artkuznet.converter.ldb.texture;

import java.util.ArrayList;
import java.util.List;

public class TextureContainer {
    private List<Texture> textures = new ArrayList<>();

    public void add(Texture texture) {
        textures.add(texture);
    }

    public List<Texture> getList() {
        return textures;
    }

    public Texture findTextureByFileName(String filePath) {
        return textures.stream()
                .filter(texture -> texture.getFilePath().equals(filePath))
                .findFirst()
                .orElse(null);
    }

}
