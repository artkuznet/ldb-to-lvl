package com.artkuznet.converter.ldb.texture;

import java.util.ArrayList;
import java.util.List;

public class TextureVertexContainer {

    private List<TextureVertex> textureVertices = new ArrayList<>();

    public void add(TextureVertex textureVertex) {
        textureVertices.add(textureVertex);
    }

    public List<TextureVertex> getList() {
        return textureVertices;
    }
}
