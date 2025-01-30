package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.maxed.LvlExit;
import com.artkuznet.converter.maxed.LvlPolygon;
import com.artkuznet.converter.maxed2.entity.mesh.Polygon;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PolygonConverter {

    public static Polygon convert(LvlPolygon lvlPolygon) {

        Polygon polygon = new Polygon();

        polygon.setIndex(lvlPolygon.getIndex());
        polygon.setNorm1(lvlPolygon.getNormal());
        polygon.setNorm2(lvlPolygon.getNormal());
        polygon.setNorm3(lvlPolygon.getNormal());
        polygon.setNorm4(lvlPolygon.getNormal());

        polygon.setMaterialCategory(lvlPolygon.getMaterialName());
        polygon.setMaterialName(lvlPolygon.getBitmapName());

        polygon.setEdges(Arrays.stream(lvlPolygon.getEdges()).map(e -> new Polygon.Edge(e.getFrom(), e.getTo())).collect(Collectors.toList()));

        polygon.setTriangles(lvlPolygon.getTriangles().stream().map(triangle -> {
            Polygon.Triangle t = new Polygon.Triangle();
            t.setNormal(triangle.normal);
            t.setVertexIndices(triangle.vertices);
            return t;
        }).collect(Collectors.toList()));

        polygon.setScaleU(lvlPolygon.getScaleU());
        polygon.setScaleV(lvlPolygon.getScaleV());

        polygon.setUnk3(new Polygon.Unk1());
        boolean isExit = lvlPolygon instanceof LvlExit;
        polygon.setUnk4(new int[]{0, -1, isExit ? 4 : 0, isExit ? 0 : -1});

        if (isExit) {
            polygon.linkedPortalName = ((LvlExit) lvlPolygon).linkedExitName;
        }

        polygon.setVertexXYZ(lvlPolygon.getUnkVertex());
        polygon.setUnkVertexXYZ(lvlPolygon.getUnkVertex());

        polygon.setTextureVertexUV(lvlPolygon.textureOffset);

        polygon.setArea(lvlPolygon.getArea());

        return polygon;
    }
}
