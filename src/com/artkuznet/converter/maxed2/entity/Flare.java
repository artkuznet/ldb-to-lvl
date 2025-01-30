package com.artkuznet.converter.maxed2.entity;

public class Flare extends Entity {

    private String type; // Name

    public Flare() {
        this.radius = 0.25;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
