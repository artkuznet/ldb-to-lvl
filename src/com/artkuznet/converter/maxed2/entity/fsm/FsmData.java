package com.artkuznet.converter.maxed2.entity.fsm;

import java.util.ArrayList;
import java.util.List;

public class FsmData {

    private List<String> stateNames = new ArrayList<>();

    private String defaultStateName = "";

    private List<String> customEventNames = new ArrayList<>();

    private List<Handler> handlers = new ArrayList<>();

    private boolean unk1 = true;

    private boolean unk2 = true;

    private List<Timer> timers = new ArrayList<>();

    public List<String> getStateNames() {
        return stateNames;
    }

    public void setStateNames(List<String> stateNames) {
        this.stateNames = stateNames;
    }

    public String getDefaultStateName() {
        return defaultStateName;
    }

    public void setDefaultStateName(String defaultStateName) {
        this.defaultStateName = defaultStateName;
    }

    public List<String> getCustomEventNames() {
        return customEventNames;
    }

    public void setCustomEventNames(List<String> customEventNames) {
        this.customEventNames = customEventNames;
    }

    public List<Handler> getHandlers() {
        return handlers;
    }

    public void addHandler(Handler handler) {
        handlers.add(handler);
    }

    public void addTimer(Timer timer) {
        timers.add(timer);
    }

    public void setHandlers(List<Handler> handlers) {
        this.handlers = handlers;
    }

    public boolean getUnk1() {
        return unk1;
    }

    public void setUnk1(boolean unk1) {
        this.unk1 = unk1;
    }

    public boolean getUnk2() {
        return unk2;
    }

    public void setUnk2(boolean unk2) {
        this.unk2 = unk2;
    }

    public List<Timer> getTimers() {
        return timers;
    }

    public void setTimers(List<Timer> timers) {
        this.timers = timers;
    }
}
