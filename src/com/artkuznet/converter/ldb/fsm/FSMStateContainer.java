package com.artkuznet.converter.ldb.fsm;

import java.util.ArrayList;
import java.util.List;

public class FSMStateContainer {

    private List<String> states = new ArrayList<>();
    private String defaultState = "";

    public void add(String name) {
        states.add(name);
    }

    public void setDefault(String name) {
        defaultState = name;
    }

    public List<String> getList() {
        return this.states;
    }

    public String getDefaultState() {
        return this.defaultState;
    }
}
