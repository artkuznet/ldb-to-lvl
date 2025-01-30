package com.artkuznet.converter.lv2.converter.helper.radiosity;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.lv2.converter.MeshConverter;
import com.artkuznet.converter.lv2.converter.helper.LdbTriangleDTO;
import com.artkuznet.converter.lv2.converter.helper.MeshCounter;
import com.artkuznet.converter.lv2.converter.helper.MeshDummy;
import com.artkuznet.converter.maxed2.entity.RadiosityLight;
import com.artkuznet.converter.maxed2.entity.mesh.Mesh;
import com.artkuznet.converter.maxed2.material.Material;

import java.util.List;

import static com.artkuznet.converter.lv2.converter.RoomConverter.getTriangles;

public class Bulb implements LightProcessor {

    @Override
    public void process(Mesh mesh, List<Vector3D> vertices, List<Material> materials, List<LdbMaterial> ldbMaterials, int materialId) {
//        mesh.getChildEntities().clear();

        List<LdbTriangleDTO> lampTriangles = getTriangles(
                MeshDummy.generate(0.20f),
                ldbMaterials,
                false,
                mesh.getCenter().minus(new Vector3D(0, 0.2, 0)).multiply(-1)
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

        for (int i = 0; i < lightMesh.getPolygons().size(); i++) {

            float intensity = 100;

            if (i == 2) {
                intensity = 30f;
            }
            if (i == 3) {
                intensity = 250f;
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
