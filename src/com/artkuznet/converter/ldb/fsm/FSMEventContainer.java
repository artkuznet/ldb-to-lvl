package com.artkuznet.converter.ldb.fsm;

import java.util.ArrayList;
import java.util.List;

public class FSMEventContainer {

    private List<FSMEvent> events = new ArrayList<>();

    public void add(FSMEvent event) {
        events.add(event);
    }

    public List<FSMEvent> getList() {
        return events;
    }
}
