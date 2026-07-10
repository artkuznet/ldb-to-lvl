package com.artkuznet.converter.maxed.helper;

import com.artkuznet.converter.maxed.LvlPolygon;
import com.artkuznet.converter.maxed.Mesh;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MeshLightingProcessor {

    public static void process(Mesh mesh) {

        // TODO refactor
        long checksum = mesh.getChecksum();
        List<Double> areas = Arrays.stream(mesh.getPolygons()).map(LvlPolygon::getArea).collect(Collectors.toList());

        LightType.fromChecksum(checksum).ifPresent(lightType -> lightType.processMesh(mesh));
    }
}
