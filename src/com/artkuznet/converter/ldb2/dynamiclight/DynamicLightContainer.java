package com.artkuznet.converter.ldb2.dynamiclight;

import java.util.ArrayList;
import java.util.List;

public class DynamicLightContainer {
    private List<DynamicLight> dynamicLights;

    public DynamicLightContainer() {
        this.dynamicLights = new ArrayList<>();
    }

    public DynamicLight get(int key) {
        return dynamicLights.get(key);
    }

    public void set(int key, DynamicLight value) {
        dynamicLights.set(key, value);
    }

    public int size() {
        return dynamicLights.size();
    }

    public void add(DynamicLight dynamicLight) {
        dynamicLights.add(dynamicLight);
    }

    public List<DynamicLight> getList() {
        return dynamicLights;
    }
}
