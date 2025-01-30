package com.artkuznet.converter.ldb2.fsm;

import java.util.List;

public class FSMCode {
    private String eventName;
    private List<String> onBefore;
    private List<String> onAfter;

    public FSMCode(String eventName, List<String> onBefore, List<String> onAfter) {
        this.eventName = eventName;
        this.onBefore = onBefore;
        this.onAfter = onAfter;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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
}
