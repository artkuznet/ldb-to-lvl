package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.lv2.converter.helper.LdbTriangleDTO;
import com.artkuznet.converter.lv2.converter.helper.LdbTrianglePortalDTO;
import com.artkuznet.converter.lv2.converter.helper.PolygonIndexCounter;
import com.artkuznet.converter.maxed.LvlExit;
import com.artkuznet.converter.maxed.LvlPolygon;
import com.artkuznet.converter.maxed2.entity.mesh.Polygon;
import com.artkuznet.converter.maxed2.material.Material;
import com.artkuznet.converter.util.VectorCalculator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static LvlPolygon convert(LdbTriangleDTO triangle, List<Vector3D> vertices, List<Material> materials) {

        LvlPolygon.Edge[] edges = new LvlPolygon.Edge[]{
                new LvlPolygon.Edge(vertices.indexOf(triangle.getVertices().get(0)), vertices.indexOf(triangle.getVertices().get(1))),
                new LvlPolygon.Edge(vertices.indexOf(triangle.getVertices().get(1)), vertices.indexOf(triangle.getVertices().get(2))),
                new LvlPolygon.Edge(vertices.indexOf(triangle.getVertices().get(2)), vertices.indexOf(triangle.getVertices().get(0)))
        };

        int materialId = triangle.getMaterialId();
        String materialName = materials.get(Math.max(materialId, 0)).getName();
        String materialCategory = materials.get(Math.max(materialId, 0)).getCategoryName();

        LvlPolygon polygon = triangle instanceof LdbTrianglePortalDTO
                ? new LvlExit(edges, "", triangle.getNormal(), triangle.portalName, triangle.linkedPortalName)
                : new LvlPolygon(edges, materialCategory, materialName, triangle.getNormal());

        polygon.index = PolygonIndexCounter.getInstance().next();

        polygon.setTriangles(Stream.of(triangle).map(ldbTriangleDTO -> {
            LvlPolygon.Triangle t = new LvlPolygon.Triangle();
            t.normal = (ldbTriangleDTO.getNormal());
            t.vertices = (ldbTriangleDTO.getVertices().stream().map(vertices::indexOf).collect(Collectors.toList()));
            return t;
        }).collect(Collectors.toList()));

        Vector3D[] pUV = VectorCalculator.calculateUVVectors(
                triangle.getVertices(),
                triangle.getUv()
        );

        polygon.setScaleU(pUV[0]);
        polygon.setScaleV(pUV[1]);


        polygon.setUnkVertex(triangle.getVertices().get(0).clone());

        VertexUV uv = triangle.getMaterialId() < 0
                ? new VertexUV(0, 0)
                : triangle.getUv().get(0);

        polygon.textureOffset = (new double[]{uv.getU(), uv.getV()});

        polygon.UV = triangle.getUv();

        polygon.setArea(triangle.getArea());

        if (triangle instanceof LdbTrianglePortalDTO) {
            Vector3D[] textureSpace = polygon.getDefaultScaleUV();
            polygon.setScaleU(textureSpace[0]);
            polygon.setScaleV(textureSpace[1]);
            polygon.textureOffset = (new double[]{0, 0});
        }

        return polygon;
    }
}
