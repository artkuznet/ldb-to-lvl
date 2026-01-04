package com.artkuznet.converter.maxed2.entity.fsm;

import com.artkuznet.converter.maxed2.entity.Entity;

public abstract class EntityFSM extends Entity implements FSM {

    protected FsmData fsmData = new FsmData();

    @Override
    public void setFsmData(FsmData data) {
        this.fsmData = data;
    }

    @Override
    public FsmData getFsmData() {
        return fsmData;
    }
}
