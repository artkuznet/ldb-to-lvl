package com.artkuznet.converter.ldb2.collistionshape;

public class CollisionShapeMoppData {
    private float originX;
    private float originY;
    private float originZ;
    private byte[] moppCode;

    public CollisionShapeMoppData(float originX, float originY, float originZ, byte[] moppCode) {
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
        this.moppCode = moppCode;
    }

    public float getOriginX() {
        return originX;
    }

    public void setOriginX(float originX) {
        this.originX = originX;
    }

    public float getOriginY() {
        return originY;
    }

    public void setOriginY(float originY) {
        this.originY = originY;
    }

    public float getOriginZ() {
        return originZ;
    }

    public void setOriginZ(float originZ) {
        this.originZ = originZ;
    }

    public byte[] getMoppCode() {
        return moppCode;
    }

    public void setMoppCode(byte[] moppCode) {
        this.moppCode = moppCode;
    }
}
