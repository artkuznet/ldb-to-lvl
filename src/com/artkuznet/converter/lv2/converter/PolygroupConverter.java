package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.maxed.PolyGroup;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PolygroupConverter {

    public static com.artkuznet.converter.maxed2.entity.mesh.PolyGroup convert(PolyGroup polyGroup) {

        com.artkuznet.converter.maxed2.entity.mesh.PolyGroup polyGroup2 = new com.artkuznet.converter.maxed2.entity.mesh.PolyGroup();

        polyGroup2.setName(polyGroup.getName());
        polyGroup2.setPolygonIndices(IntStream.of(polyGroup.getPolygons()).boxed().collect(Collectors.toList()));
        polyGroup2.setSmoothLightmaps(polyGroup.isSmoothLightMaps());
        polyGroup2.setSmoothGeometry(polyGroup.isSmoothGeometry());
        polyGroup2.setMaxAngle(polyGroup.getMaxAngle());
        polyGroup2.setMaxEdge(polyGroup.getMaxEdgeLength());
        polyGroup2.setFreezeLightmaps(polyGroup.isFreezeLightMaps());
        polyGroup2.setRayTracing(false);

        return polyGroup2;
    }
}
