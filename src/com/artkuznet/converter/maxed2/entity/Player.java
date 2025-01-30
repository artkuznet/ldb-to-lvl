package com.artkuznet.converter.maxed2.entity;

import com.artkuznet.converter.maxed2.entity.fsm.EntityFSM;
import com.artkuznet.converter.maxed2.entity.fsm.FsmData;
import com.artkuznet.converter.maxed2.entity.fsm.Handler;

import java.util.Arrays;

public class Player extends EntityFSM {

    public Player() {
        this.name = "player";
        FsmData fsmData = new FsmData();
        fsmData.setHandlers(Arrays.asList(
                new Handler("OnActivate"),
                new Handler("OnDeath"),
                new Handler("OnLowHealth"),
                new Handler("Startup")
        ));
        this.setFsmData(fsmData);
    }
}
