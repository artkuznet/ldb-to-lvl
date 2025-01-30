package com.artkuznet.converter.lv2.converter.helper.radiosity;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.lv2.converter.MeshConverter;
import com.artkuznet.converter.lv2.converter.helper.LdbTriangleDTO;
import com.artkuznet.converter.lv2.converter.helper.MeshCounter;
import com.artkuznet.converter.lv2.converter.helper.MeshDummy;
import com.artkuznet.converter.maxed2.entity.RadiosityLight;
import com.artkuznet.converter.maxed2.entity.mesh.Mesh;
import com.artkuznet.converter.maxed2.entity.mesh.Polygon;
import com.artkuznet.converter.maxed2.material.Material;

import java.util.Comparator;
import java.util.List;

import static com.artkuznet.converter.lv2.converter.RoomConverter.getTriangles;

public class CorridorWallLamp implements LightProcessor {
    @Override
    public void process(Mesh mesh, List<Vector3D> vertices, List<Material> materials, List<LdbMaterial> ldbMaterials, int materialId) {

        Polygon p = mesh.getPolygons().stream()
                .max(Comparator.comparingInt(o -> o.getEdges().size()))
                .orElseThrow(RuntimeException::new);

        List<LdbTriangleDTO> lampTriangles = getTriangles(
                MeshDummy.generate(0.15f),
                ldbMaterials,
                false,
                mesh.getCenter().minus(p.getNorm1().clone().multiply(-0.35)).multiply(-1)
        );
        lampTriangles.forEach(t -> t.setMaterialId(materialId));

        Mesh lightMesh = MeshConverter.convert(
                vertices,
                lampTriangles,
                "light_" + MeshCounter.getInstance().next(),
                null,
                materials,
                false,
                false
        );

        lightMesh.setExcludeFromGame(true);

        int x = (int) p.getNorm1().clone().hardSmooth().getX();
        int z = (int) p.getNorm1().clone().hardSmooth().getZ();

        int d = 0;

        if (x == 0 && z < 0) {
            d = 1;
        }
        if (x == 0 && z > 0) {
            d = 2;
        }
        if (x < 0 && z == 0) {
            d = 3;
        }
        if (x > 0 && z == 0) {
            d = 4;
        }

        if (d == 0) {
            throw new RuntimeException();
        }

        for (int i = 0; i < lightMesh.getPolygons().size(); i++) {

            float intensity = 80;

            if (d == 1) {
                if (i == 4) {
                    intensity = 40f;
                }
                if (i == 5) {
                    intensity = 250f;
                }
            }
            if (d == 2) {
                if (i == 5) {
                    intensity = 40f;
                }
                if (i == 4) {
                    intensity = 250f;
                }
            }
            if (d == 3) {
                if (i == 0) {
                    intensity = 40f;
                }
                if (i == 1) {
                    intensity = 250f;
                }
            }
            if (d == 4) {
                if (i == 1) {
                    intensity = 40f;
                }
                if (i == 0) {
                    intensity = 250f;
                }
            }

            RadiosityLight light = new RadiosityLight(255, 255, 200, intensity, 180, 180f);
            light.setRadiosityLightIndex(i);
            lightMesh.addChildEntity(light);
            light.setParentEntity(lightMesh);
        }

        mesh.addChildEntity(lightMesh);
        lightMesh.setParentEntity(mesh);
    }
}
