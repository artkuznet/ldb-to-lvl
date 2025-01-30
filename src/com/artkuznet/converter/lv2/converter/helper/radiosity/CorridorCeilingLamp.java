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

public class CorridorCeilingLamp implements LightProcessor {
    @Override
    public void process(Mesh mesh, List<Vector3D> vertices, List<Material> materials, List<LdbMaterial> ldbMaterials, int materialId) {

        Vector3D size = mesh.getSize();

        List<LdbTriangleDTO> lampTriangles = getTriangles(
                size.getX() > size.getZ() ? MeshDummy.generate(1.45f, 0.25f)
                        : MeshDummy.generate(0.25f, 1.45f),
                ldbMaterials,
                false,
                mesh.getCenter().minus(new Vector3D(0, 0.15, 0)).multiply(-1)
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
            RadiosityLight light = new RadiosityLight(255, 255, 200, 120f, 1, 150f);
            light.setRadiosityLightIndex(i);
            lightMesh.addChildEntity(light);
            light.setParentEntity(lightMesh);
        }

        mesh.addChildEntity(lightMesh);
        lightMesh.setParentEntity(mesh);
    }
}
