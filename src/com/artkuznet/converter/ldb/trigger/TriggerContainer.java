package com.artkuznet.converter.ldb.trigger;

import java.util.ArrayList;
import java.util.List;

public class TriggerContainer {

    private List<Trigger> triggers = new ArrayList<>();

    public void add(Trigger trigger) {
        triggers.add(trigger);
    }

    public List<Trigger> getList() {
        return triggers;
    }
}
