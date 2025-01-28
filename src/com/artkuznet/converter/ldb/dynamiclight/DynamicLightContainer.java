package com.artkuznet.converter.ldb.dynamiclight;

import java.util.ArrayList;
import java.util.List;

public class DynamicLightContainer {
    private List<DynamicLight> lights = new ArrayList<>();

    public void add(DynamicLight light) {
        lights.add(light);
    }

    public List<DynamicLight> getList() {
        return lights;
    }
}
