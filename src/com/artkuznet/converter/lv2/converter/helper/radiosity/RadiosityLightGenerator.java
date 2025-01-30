package com.artkuznet.converter.lv2.converter.helper.radiosity;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.maxed2.entity.mesh.Mesh;
import com.artkuznet.converter.maxed2.material.Material;

import java.util.List;

public class RadiosityLightGenerator {

    public static void appendLight(Mesh mesh,
                                   List<Vector3D> vertices,
                                   List<Material> materials, // todo remove
                                   List<LdbMaterial> ldbMaterials, // todo remove
                                   int materialId
    ) {
        long checksum = mesh.getChecksum();

        LightMeshEnum lightMeshEnum = LightMeshEnum.fromChecksum(checksum);

        if (null == lightMeshEnum) {
            return;
        }

        getProcessor(lightMeshEnum).process(mesh, vertices, materials, ldbMaterials, materialId);
    }

    private static LightProcessor getProcessor(LightMeshEnum lightMeshEnum) {

        switch (lightMeshEnum) {
            case CORRIDOR_CEILING_LAMP:
                return new CorridorCeilingLamp();
            case CORRIDOR_WALL_LAMP:
                return new CorridorWallLamp();
            case CORRIDOR_WINDOW:
            case CORRIDOR_WINDOW_1:
                return new CorridorWindow();
            case ROOM_WALL_LAMP:
                return new LightMesh10(50);
            case STREET_LAMP:
            case STREET_LAMP_1:
            case STREET_LAMP_2:
                return new StreetLamp();
            case BULB:
                return new Bulb();
            case LIGHT_4:
                return new LightMesh4();
            case LIGHT_5:
                return new LightMesh5();
            case LIGHT_6:
                return new LightMesh6();
            case LIGHT_7:
                return new LightMesh7();
            case LIGHT_8:
                return new LightMesh8();
            case LIGHT_9:
            case LIGHT_9_1:
                return new LightMesh9(1000);

            case LIGHT_10:
                return new LightMesh10(300);

            case LIGHT_11:
                return new LightMesh11(5);

            case LIGHT_12:
                return new LightMesh10(200);

            case LIGHT_13:
                return new LightMesh9(200);
            case LIGHT_14:
//                return new LightMesh12();
                return new LightMesh11(10);

            case LIGHT_VODKA_1:
            case LIGHT_VODKA_2:
            case LIGHT_VODKA_3:
            case LIGHT_VODKA_4:
            case LIGHT_VODKA_5:
            case LIGHT_VODKA_6:
            case LIGHT_VODKA_7:
            case LIGHT_VODKA_8:
            case LIGHT_VODKA_9:
            case LIGHT_VODKA_10:
            case LIGHT_VODKA_11:

            case LIGHT_15:
                return new LightMesh11(1);
        }

        throw new RuntimeException();
    }
}
