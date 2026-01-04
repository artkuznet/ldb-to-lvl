package com.artkuznet.converter.maxed2.entity.fsm;

public class Timer {

    private String name;

    private boolean typeToggle;

    private float length;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getTypeToggle() {
        return typeToggle;
    }

    public void setTypeToggle(boolean typeToggle) {
        this.typeToggle = typeToggle;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }
}
