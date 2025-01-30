package com.artkuznet.converter.lv2.converter.helper.radiosity;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.maxed2.entity.RadiosityLight;
import com.artkuznet.converter.maxed2.entity.mesh.Mesh;
import com.artkuznet.converter.maxed2.material.Material;

import java.util.Arrays;
import java.util.List;

public class LightMesh12 implements LightProcessor {
    @Override
    public void process(Mesh mesh, List<Vector3D> vertices, List<Material> materials, List<LdbMaterial> ldbMaterials, int materialId) {

        for (int i = 0; i < mesh.getPolygons().size(); i++) {

            if (Arrays.asList(1, 3, 6, 8, 9, 11).contains(i)) {
                continue;
            }

            RadiosityLight light = new RadiosityLight(255, 255, 255, 5, 180, 180);
            light.setRadiosityLightIndex(i);
            mesh.addChildEntity(light);
            light.setParentEntity(mesh);
        }
    }
}
