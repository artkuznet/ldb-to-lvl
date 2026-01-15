package com.artkuznet.converter.ldb2.levelitem;

import com.artkuznet.converter.ldb2.EntityLdb2;

public class LevelItem implements EntityLdb2 {
    private String entityName;
    private String itemName;
    private float[][] transform;
    private int roomId;

    public LevelItem(String entityName, String itemName, float[][] transform, int roomId) {
        this.entityName = entityName;
        this.itemName = itemName;
        this.transform = transform;
        this.roomId = roomId;
    }

    @Override
    public String getName() {
        return entityName;
    }

    @Override
    public float[][] getLocalTransform() {
        return transform;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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