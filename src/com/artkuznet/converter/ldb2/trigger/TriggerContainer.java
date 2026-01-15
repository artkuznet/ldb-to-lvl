package com.artkuznet.converter.ldb2.trigger;

import java.util.ArrayList;
import java.util.List;

public class TriggerContainer {

    private List<Trigger> triggers;

    public TriggerContainer() {
        this.triggers = new ArrayList<>();
    }

    public Trigger get(int key) {
        return triggers.get(key);
    }

    public void set(int key, Trigger value) {
        triggers.set(key, value);
    }

    public void add(Trigger trigger) {
        triggers.add(trigger);
    }

    public List<Trigger> getList() {
        return triggers;
    }
}
