package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.material.Texture;

import java.util.ArrayList;
import java.util.List;

public class TextureReader {

    public static List<Texture> read(MaxTypeReader reader) {
        reader.rememberOffset();

        ReaderHelper.read100(reader);

        int dataSize = (int) reader.readObject();

        int texturesCount = (int) reader.readObject();

        List<Texture> textures = new ArrayList<>();

        for (int i = 0; i < texturesCount; i++) {
            int offset = reader.getOffset();

            ReaderHelper.read100(reader);

            int textureDataSize = (int) reader.readObject();

            int fileType = (int) reader.readObject();

            int dataLength = (int) reader.readObject();
            String filePath = (String) reader.readObject();
            byte[] data = reader.readBytes(dataLength);

            if (reader.getOffset() - offset != textureDataSize) {
                throw new RuntimeException();
            }

            textures.add(new Texture(filePath, fileType, data));
        }

        reader.validateDataSize(dataSize);

        return textures;
    }
}
