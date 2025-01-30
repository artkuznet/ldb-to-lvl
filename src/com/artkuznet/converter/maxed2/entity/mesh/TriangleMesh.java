package com.artkuznet.converter.maxed2.entity.mesh;

import com.artkuznet.converter.maxed2.entity.Entity;

public class TriangleMesh extends Entity {

    private byte[] unkTriangleMeshData;

    public byte[] getUnkTriangleMeshData() {
        return unkTriangleMeshData;
    }

    public void setUnkTriangleMeshData(byte[] unkTriangleMeshData) {
        this.unkTriangleMeshData = unkTriangleMeshData;
    }
}
