package com.artkuznet.converter.maxed2.entity.prefab;

import com.artkuznet.converter.maxed2.entity.fsm.EntityFSM;

public class PrefabParent extends EntityFSM {

    private String type = "";

    private boolean opened = false;

    public PrefabParent() {
        this.radius = 0.25; // todo dynamic size
    }

    public String getType() {
        return type;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}
