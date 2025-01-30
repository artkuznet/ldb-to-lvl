package com.artkuznet.converter.ldb2.dynamicmesh;

import java.util.ArrayList;
import java.util.List;

public class DynamicMeshContainer {
    private List<DynamicMesh> dynamicMeshes;

    public DynamicMeshContainer() {
        this.dynamicMeshes = new ArrayList<>();
    }

    public DynamicMesh get(int key) {
        return dynamicMeshes.get(key);
    }

    public void set(int key, DynamicMesh value) {
        dynamicMeshes.set(key, value);
    }

    public int size() {
        return dynamicMeshes.size();
    }

    public void add(DynamicMesh dynamicMesh) {
        dynamicMeshes.add(dynamicMesh);
    }

    public List<DynamicMesh> getList() {
        return dynamicMeshes;
    }
}
