package com.artkuznet.converter.ldb2.texture;

import java.util.ArrayList;
import java.util.List;

public class TextureContainer {
    private List<LdbTexture> textures;

    public TextureContainer() {
        this.textures = new ArrayList<>();
    }

    public int size() {
        return textures.size();
    }

    public LdbTexture get(int key) {
        return textures.get(key);
    }

    public void add(LdbTexture texture) {
        textures.add(texture);
    }

    public LdbTexture findTextureByGroupAndID(int groupId, int id) {
        int index = -1;
        for (LdbTexture texture : textures) {
            if (texture.getGroupId() == groupId) {
                index++;
                if (index == id) {
                    return texture;
                }
            }
        }
        return null;
    }

    public List<LdbTexture> getList() {
        return textures;
    }
}
