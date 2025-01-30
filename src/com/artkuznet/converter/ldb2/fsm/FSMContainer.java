package com.artkuznet.converter.ldb2.fsm;

import java.util.ArrayList;
import java.util.List;

public class FSMContainer {
    private List<FSM> fsms;

    public FSMContainer() {
        this.fsms = new ArrayList<>();
    }

    public FSM get(int key) {
        return fsms.get(key);
    }

    public void set(int key, FSM value) {
        fsms.set(key, value);
    }

    public int size() {
        return fsms.size();
    }

    public void add(FSM fsm) {
        fsms.add(fsm);
    }

    public List<FSM> getList() {
        return fsms;
    }
}
