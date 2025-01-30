package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.ldb2.texture.LdbTexture;
import com.artkuznet.converter.maxed2.material.Texture;

public class TextureConverter {

    public static Texture convert(LdbTexture ldbTexture) {
        return new Texture(ldbTexture.getFilePath(), ldbTexture.getFileType(), ldbTexture.getData());
    }
}
