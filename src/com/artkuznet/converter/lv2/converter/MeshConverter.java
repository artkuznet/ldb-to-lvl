package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.lv2.converter.helper.LdbTriangleDTO;
import com.artkuznet.converter.maxed.LvlExit;
import com.artkuznet.converter.maxed.LvlPolygon;
import com.artkuznet.converter.maxed.PolyGroup;
import com.artkuznet.converter.maxed2.entity.mesh.Mesh;
import com.artkuznet.converter.maxed2.material.Material;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MeshConverter {

    public static Mesh convert(com.artkuznet.converter.maxed.Mesh lvlMesh) {
        return convert(lvlMesh, false);
    }

    public static Mesh convert(com.artkuznet.converter.maxed.Mesh lvlMesh, boolean isDynamic) {

        Mesh mesh = isDynamic ? new com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh() : new Mesh();

        mesh.setName(lvlMesh.getName());
        mesh.setFlipNormals(!lvlMesh.isRoom()); // todo check
        mesh.setLocalMatrix(lvlMesh.getTransform());

        if (mesh instanceof com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh) {
            mesh.getProperties().setCollisions(false); // todo fix
        }

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

    public static Mesh convert(
            List<Vector3D> vertices,
            List<LdbTriangleDTO> triangles,
            String name,
            double[][] transform,
            List<Material> materials,
            boolean isDynamic,
            boolean buildPolygroups
    ) {
        vertices.addAll(
                triangles.stream()
                        .map(LdbTriangleDTO::getVertices)
                        .flatMap(List::stream)
                        .distinct()
                        .collect(Collectors.toList())
        );

        com.artkuznet.converter.maxed.Mesh m = new com.artkuznet.converter.maxed.Mesh();
        m.setName(name);
        m.setIsRoom(false);
        if (transform != null) {
            m.setTransform(transform);
        }
        m.setVertices(vertices.toArray(new Vector3D[0]));
        m.setPolygons(triangles.stream()
                .map(t -> PolygonConverter.convert(t, vertices, materials))
                .peek(p -> p.parentMesh = m).toArray(LvlPolygon[]::new)
        );


        com.artkuznet.converter.maxed.Mesh mesh = m.optimize().joinPolygons();

        if (buildPolygroups) {
            mesh.buildPolyGroups();
        }

        return isDynamic
                ? (com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh) MeshConverter.convert(mesh, true)
                : MeshConverter.convert(mesh, false);

    }
}
