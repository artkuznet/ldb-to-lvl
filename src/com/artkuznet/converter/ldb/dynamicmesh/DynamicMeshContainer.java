package com.artkuznet.converter.ldb.dynamicmesh;

import com.artkuznet.converter.ldb.texture.TextureVertexContainer;

import java.util.ArrayList;
import java.util.List;

public class DynamicMeshContainer {
    private TextureVertexContainer textureVertices = new TextureVertexContainer();
    private List<LdbDynamicMesh> dynamicMeshes = new ArrayList<>();

    public List<LdbDynamicMesh> getList() {
        return dynamicMeshes;
    }

    public TextureVertexContainer getTextureVertices() {
        return textureVertices;
    }

    public void addDynamicMesh(LdbDynamicMesh dynamicMesh) {
        dynamicMesh.setTextureVertices(textureVertices);
        dynamicMeshes.add(dynamicMesh);
    }

    public LdbDynamicMesh findByName(String name) {
        return dynamicMeshes.stream().filter(d->d.getSharedName().equals(name)).findFirst().orElse(null);
    }
}
