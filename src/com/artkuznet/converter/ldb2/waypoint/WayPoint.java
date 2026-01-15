package com.artkuznet.converter.ldb2.waypoint;

import com.artkuznet.converter.ldb2.EntityLdb2;

public class WayPoint implements EntityLdb2 {
    private String name;
    private float[][] transform;
    private int roomId;
    private int unk;

    public WayPoint(String name, float[][] transform, int roomId, int unk) {
        this.name = name;
        this.transform = transform;
        this.roomId = roomId;
        this.unk = unk;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float[][] getLocalTransform() {
        return transform;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getUnk() {
        return unk;
    }

    public void setUnk(int unk) {
        this.unk = unk;
    }
}
