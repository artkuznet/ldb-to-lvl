package com.artkuznet.converter.maxed2.entity;

import com.artkuznet.converter.maxed2.entity.fsm.EntityFSM;

public class Trigger extends EntityFSM {

    private String type = "Trigger";

    private TriggerData data;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public TriggerData getData() {
        return data;
    }

    public void setData(TriggerData data) {
        this.data = data;
    }
}
