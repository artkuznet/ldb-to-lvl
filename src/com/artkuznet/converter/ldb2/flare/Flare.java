package com.artkuznet.converter.ldb2.flare;

import com.artkuznet.converter.ldb2.EntityLdb2;

public class Flare implements EntityLdb2 {
    private String flareName;
    private float[][] transform;
    private int roomId;

    public Flare(String flareName, float[][] transform, int roomId) {
        this.flareName = flareName;
        this.transform = transform;
        this.roomId = roomId;
    }

    @Override
    public String getName() {
        return "Flare";
    }

    @Override
    public float[][] getLocalTransform() {
        return transform;
    }

    public String getFlareName() {
        return flareName;
    }

    public void setFlareName(String flareName) {
        this.flareName = flareName;
    }

    public float[][] getTransform() {
        return transform;
    }

    public void setTransform(float[][] transform) {
        this.transform = transform;
    }

    @Override
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
