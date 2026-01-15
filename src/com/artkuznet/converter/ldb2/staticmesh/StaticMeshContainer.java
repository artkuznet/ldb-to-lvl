package com.artkuznet.converter.ldb2.staticmesh;

import java.util.ArrayList;
import java.util.List;

public class StaticMeshContainer {
    private List<StaticMesh> staticMeshes;

    public StaticMeshContainer() {
        this.staticMeshes = new ArrayList<>();
    }

    public StaticMesh get(int key) {
        return staticMeshes.get(key);
    }

    public int size() {
        return staticMeshes.size();
    }

    public void add(StaticMesh staticMesh) {
        staticMeshes.add(staticMesh);
    }

    public List<StaticMesh> getList() {
        return staticMeshes;
    }

    public boolean isEmpty() {
        return staticMeshes.isEmpty();
    }
}
