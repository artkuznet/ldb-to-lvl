package com.artkuznet.converter.maxed2.entity;

public class LevelItem extends Entity {

    private String type;

    public LevelItem() {
        this.radius = 0.05;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
