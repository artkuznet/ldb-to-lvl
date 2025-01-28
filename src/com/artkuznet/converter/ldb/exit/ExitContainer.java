package com.artkuznet.converter.ldb.exit;

import java.util.ArrayList;
import java.util.List;

public class ExitContainer {

    private List<Exit> exits = new ArrayList<>();

    public void add(Exit exit) {
        exits.add(exit);
    }

    public Exit findByName(String exitName) {
        return exits.stream().filter(e -> e.getExitName().equals(exitName)).findFirst().orElseThrow();
    }
}
