package com.artkuznet.converter.ldb.pointlight;

import java.util.ArrayList;
import java.util.List;

public class PointLightContainer {

    private List<PointLight> pointlights = new ArrayList<>();

    public void add(PointLight pointLight) {
        pointlights.add(pointLight);
    }

    public List<PointLight> getList() {
        return pointlights;
    }
}
