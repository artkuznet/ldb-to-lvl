package com.artkuznet.converter.maxed2.entity.mesh;

import com.artkuznet.converter.maxed2.entity.TriggerData;
import com.artkuznet.converter.maxed2.entity.fsm.FSM;
import com.artkuznet.converter.maxed2.entity.fsm.FsmData;
import com.artkuznet.converter.maxed2.entity.fsm.Handler;

import java.util.ArrayList;
import java.util.Arrays;

public class DynamicMesh extends Mesh implements FSM {

    private TriggerData triggerData;

    public DynamicMesh() {
        this.hasDynamic = true;
        FsmData fsmData = new FsmData();
        fsmData.setHandlers(new ArrayList<>(Arrays.asList(
                new Handler("DO_BulletCollides"),
                new Handler("DO_MovedToInvalidPosition"),
                new Handler("DO_MovedToInvalidPositionEnds"),
                new Handler("DO_OnDeath"),
                new Handler("Startup")
        )));
        this.getProperties().setPhysicalMaterial("default");
        this.fsmData = fsmData;
    }

    public Dynamic getDynamicData() {
        return dynamicData;
    }

    protected FsmData fsmData;

    public void setDynamicData(Dynamic dynamicData) {
        this.dynamicData = dynamicData;
    }

    protected Dynamic dynamicData;

    @Override
    public void setFsmData(FsmData data) {
        this.fsmData = data;
    }

    @Override
    public FsmData getFsmData() {
        return fsmData;
    }

    public TriggerData getTriggerData() {
        return triggerData;
    }

    public void setTriggerData(TriggerData triggerData) {
        this.triggerData = triggerData;
    }
}
