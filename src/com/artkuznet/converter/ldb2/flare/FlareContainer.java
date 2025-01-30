package com.artkuznet.converter.ldb2.flare;

import java.util.ArrayList;
import java.util.List;

public class FlareContainer {
    private List<Flare> flares;

    public FlareContainer() {
        this.flares = new ArrayList<>();
    }

    public Flare get(int key) {
        return flares.get(key);
    }

    public void set(int key, Flare value) {
        flares.set(key, value);
    }

    public int size() {
        return flares.size();
    }

    public void add(Flare flare) {
        flares.add(flare);
    }

    public List<Flare> getList() {
        return flares;
    }
}
