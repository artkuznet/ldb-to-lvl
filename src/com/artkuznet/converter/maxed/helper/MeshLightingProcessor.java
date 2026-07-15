package com.artkuznet.converter.maxed.helper;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.maxed.LvlPolygon;
import com.artkuznet.converter.maxed.Mesh;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MeshLightingProcessor {

    public static void process(Mesh mesh) {

        if (mesh.isRoom()) {
            Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getBitmapName().contains("AS_WINDOW3_128X128"))
                    .filter(p -> p.getNormal().getY() == 0)
                    .forEach(p -> {
//                        p.setColor(new LvlPolygon.Color(166, 166, 234));
                        p.setColor(LvlPolygon.Color.NIGHT);
//                        p.setLightIntensity(0.025f);
                        p.setLightIntensity(p.getNormal().getY() == 0 ? 0.1f : 0.0273);
                    });

            Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getBitmapName().contains("ASGARD_LIGHTSTRIPE"))
                    .forEach(p -> p.setColor(LvlPolygon.Color.WHITE));

            Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getBitmapName().contains("SECURITYPAD"))
                    .filter(p -> p.getArea() > 0.04 && p.getArea() < 0.05)
                    .forEach(p -> p.setColor(LvlPolygon.Color.WHITE));

            Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getBitmapName().contains("EXIT02CD"))
                    .filter(p -> p.getArea() > 0.12)
                    .forEach(p -> p.setColor(LvlPolygon.Color.WHITE));

            Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getBitmapName().contains("LAMP27_32X64"))
                    .filter(p -> p.getArea() > 0.03 || p.getEdges().length == 5 && p.getNormal().getY() < 0)
                    .forEach(p -> p.setColor(LvlPolygon.Color.WARM));

            List<Double> areas = Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getBitmapName().contains("EXIT02CD"))
                    .map(LvlPolygon::getArea)
                    .collect(Collectors.toList());

            int x = 2;


            return;
        }

        // TODO remove
        List<Double> areas = Arrays.stream(mesh.getPolygons()).map(LvlPolygon::getArea).sorted().collect(Collectors.toList());
        List<Vector3D> normals = Arrays.stream(mesh.getPolygons()).map(LvlPolygon::getNormal).collect(Collectors.toList());

        long sum = mesh.getChecksum();

        LightType.fromChecksum(mesh.getChecksum()).ifPresent(lightType -> lightType.processMesh(mesh));
    }
}
