package com.artkuznet.converter.ldb2.portal;

import java.util.ArrayList;
import java.util.List;

public class PortalContainer {
    private List<Portal> portals;

    public PortalContainer() {
        this.portals = new ArrayList<>();
    }

    public Portal get(int key) {
        return portals.get(key);
    }

    public void set(int key, Portal value) {
        portals.set(key, value);
    }

    public int size() {
        return portals.size();
    }

    public void add(Portal portal) {
        portals.add(portal);
    }

    public List<Portal> getList() {
        return portals;
    }
}
