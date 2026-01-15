package com.artkuznet.converter.ldb2.fsm;

import java.util.List;

public class FSMCode2 {
    private List<String> onBefore;
    private List<String> onAfter;
    private List<FSMState> states;

    public FSMCode2(List<String> onBefore, List<String> onAfter, List<FSMState> states) {
        this.onBefore = onBefore;
        this.onAfter = onAfter;
        this.states = states;
    }

    public List<String> getOnBefore() {
        return onBefore;
    }

    public void setOnBefore(List<String> onBefore) {
        this.onBefore = onBefore;
    }

    public List<String> getOnAfter() {
        return onAfter;
    }

    public void setOnAfter(List<String> onAfter) {
        this.onAfter = onAfter;
    }

    public List<FSMState> getStates() {
        return states;
    }

    public void setStates(List<FSMState> states) {
        this.states = states;
    }
}
