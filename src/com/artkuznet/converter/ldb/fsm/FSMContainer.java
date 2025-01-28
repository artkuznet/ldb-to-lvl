package com.artkuznet.converter.ldb.fsm;

import java.util.ArrayList;
import java.util.List;

public class FSMContainer {

    private List<LdbFSM> fsms = new ArrayList<>();

    public void add(LdbFSM fsm) {
        fsms.add(fsm);
    }

    public List<LdbFSM> getList() {
        return fsms;
    }

    public LdbFSM findByName(String name) {
        return fsms.stream().filter(fsm -> fsm.getSharedName().equals(name)).findFirst().orElse(null);
    }
}
