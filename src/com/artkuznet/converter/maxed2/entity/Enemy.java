package com.artkuznet.converter.maxed2.entity;

import com.artkuznet.converter.maxed2.entity.fsm.EntityFSM;

public class Enemy extends EntityFSM {

    private String type; // Skin

    public String group = "";

    private String activatorsUseAnimation = "Default";

    public Enemy() {
        this.radius = 0.5;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setActivatorsUseAnimation(String activatorsUseAnimation) {
        this.activatorsUseAnimation = activatorsUseAnimation;
    }

    public String getType() {
        return type;
    }

    public String getGroup() {
        return group;
    }

    public String getActivatorsUseAnimation() {
        return activatorsUseAnimation;
    }
}
