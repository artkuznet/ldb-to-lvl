package com.artkuznet.converter.ldb.staticmesh;

import com.artkuznet.converter.ldb.texture.TextureVertexContainer;

import java.util.ArrayList;
import java.util.List;

public class StaticMeshContainer {

    private TextureVertexContainer textureVertices = new TextureVertexContainer();
    private List<StaticMesh> staticMeshes = new ArrayList<>();

    public void add(StaticMesh staticMesh) {
        staticMesh.setTextureVertices(this.textureVertices);
        staticMeshes.add(staticMesh);
    }

    public List<StaticMesh> getStaticMeshList() {
        return staticMeshes;
    }

    public StaticMesh getById(int id) {
        return staticMeshes.stream().filter(staticMesh -> staticMesh.getId() == id).findFirst().orElseThrow(RuntimeException::new);
    }

    public TextureVertexContainer getTextureVertices() {
        return textureVertices;
    }
}
