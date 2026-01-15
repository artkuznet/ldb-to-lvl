package com.artkuznet.converter.ldb2.dynamiclight;

import com.artkuznet.converter.ldb2.EntityLdb2;

public class DynamicLight implements EntityLdb2 {
    private float[][] transform;
    private int roomId;
    private DynamicLightColor color;
    private float falloff;

    public DynamicLight(float[][] transform, int roomId, DynamicLightColor color, float falloff) {
        this.transform = transform;
        this.roomId = roomId;
        this.color = color;
        this.falloff = falloff;
    }

    @Override
    public float[][] getLocalTransform() {
        return transform;
    }

    public void setTransform(float[][] transform) {
        this.transform = transform;
    }

    @Override
    public int getRoomId() {
        return roomId;
    }

    @Override
    public String getName() {
        throw new RuntimeException("no value");
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public DynamicLightColor getColor() {
        return color;
    }

    public void setColor(DynamicLightColor color) {
        this.color = color;
    }

    public float getFalloff() {
        return falloff;
    }

    public void setFalloff(float falloff) {
        this.falloff = falloff;
    }
}
