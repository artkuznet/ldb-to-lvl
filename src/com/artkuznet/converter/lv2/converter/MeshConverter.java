package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.maxed.LvlExit;
import com.artkuznet.converter.maxed.PolyGroup;
import com.artkuznet.converter.maxed2.entity.mesh.Mesh;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MeshConverter {

    public static Mesh convert(com.artkuznet.converter.maxed.Mesh lvlMesh) {

        Mesh mesh = new Mesh();

        mesh.setName(lvlMesh.getName());
        mesh.setFlipNormals(!lvlMesh.isRoom()); // todo check
        mesh.setLocalMatrix(lvlMesh.getTransform());


        double[] pos = lvlMesh.getPosition();
        mesh.setMinPoint(new Vector3D(pos[0], pos[1], pos[2]));
        mesh.setMaxPoint(new Vector3D(pos[3], pos[4], pos[5]));
        mesh.setRadius(pos[6]);

        mesh.setVertices(Arrays.asList(lvlMesh.getVertices()));
        mesh.setPolygons(Arrays.stream(lvlMesh.getPolygons())
                .map(PolygonConverter::convert)
                .collect(Collectors.toList())
        );

        Arrays.stream(lvlMesh.getPolygons())
                .filter(p -> (p instanceof LvlExit))
                .map(p -> (LvlExit) p)
                .map(exit -> {
                    com.artkuznet.converter.maxed2.entity.Portal portal = new com.artkuznet.converter.maxed2.entity.Portal();
                    portal.setName(exit.exitName.substring(2 + exit.exitName.lastIndexOf("::")));
                    portal.setParentEntity(mesh);

                    portal.setPolygonIndex(mesh.getPolygons().indexOf(
                            mesh.getPolygons().stream()
                                    .filter(p -> p.getIndex() == exit.getIndex())
                                    .findFirst()
                                    .orElseThrow(RuntimeException::new))
                    );

                    return portal;
                })
                .forEach(mesh::addChildEntity);

        if (lvlMesh.isRoom()) {
            mesh.getProperties().setSoundEnvironment("default");
        }

        for (PolyGroup polyGroup : lvlMesh.getPolyGroups()) {
            com.artkuznet.converter.maxed2.entity.mesh.PolyGroup polyGroup2 = PolygroupConverter.convert(polyGroup);
            polyGroup2.setParentEntity(mesh);
            mesh.addChildEntity(polyGroup2);
        }

        return mesh;
    }
}
