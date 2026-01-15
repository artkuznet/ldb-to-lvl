package com.artkuznet.converter.ldb2.jumppoint;

import com.artkuznet.converter.ldb2.EntityLdb2;

public class JumpPoint implements EntityLdb2 {
    private String name;
    private float[][] transform;
    private int roomId;

    public JumpPoint(String name, float[][] transform, int roomId) {
        this.name = name;
        this.transform = transform;
        this.roomId = roomId;
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
}
