package com.artkuznet.converter.lv2.converter.helper.radiosity;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.maxed2.entity.mesh.Mesh;
import com.artkuznet.converter.maxed2.material.Material;

import java.util.List;

public interface LightProcessor {

    void process(Mesh mesh,
                 List<Vector3D> vertices,
                 List<Material> materials, // todo remove
                 List<LdbMaterial> ldbMaterials, // todo remove
                 int materialId

    );
}
