package com.artkuznet.converter.lv2.converter.helper.radiosity;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.maxed2.entity.RadiosityLight;
import com.artkuznet.converter.maxed2.entity.mesh.Mesh;
import com.artkuznet.converter.maxed2.entity.mesh.Polygon;
import com.artkuznet.converter.maxed2.material.Material;

import java.util.Comparator;
import java.util.List;

public class LightMesh6 implements LightProcessor {
    @Override
    public void process(Mesh mesh, List<Vector3D> vertices, List<Material> materials, List<LdbMaterial> ldbMaterials, int materialId) {

        Polygon p = mesh.getPolygons().stream().max(Comparator.comparingDouble(Polygon::getArea)).orElseThrow(RuntimeException::new);

        int id = mesh.getPolygons().indexOf(p);

        RadiosityLight light = new RadiosityLight(255, 255, 200, 700, 180, 180);
        light.setRadiosityLightIndex(id);
        mesh.addChildEntity(light);
        light.setParentEntity(mesh);
    }
}
