package com.artkuznet.converter.obj.converter;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.ldb2.MaxLDB2;
import com.artkuznet.converter.ldb2.Shape;
import com.artkuznet.converter.ldb2.collistionshape.CollisionShape;
import com.artkuznet.converter.ldb2.fsm.FSM;
import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.ldb2.portal.Portal;
import com.artkuznet.converter.ldb2.room.Room;
import com.artkuznet.converter.lv2.converter.helper.LdbTriangleDTO;
import com.artkuznet.converter.lv2.converter.helper.LdbTrianglePortalDTO;
import com.artkuznet.converter.lv2.converter.helper.MatrixUtil;
import com.artkuznet.converter.lv2.converter.helper.MeshCounter;
import com.artkuznet.converter.maxed2.material.MaterialType;
import com.artkuznet.converter.obj.MTL;
import com.artkuznet.converter.obj.OBJ;
import com.artkuznet.converter.util.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Ldb2Converter {

    public static OBJ convert(String ldbFileName, MaxLDB2 ldb2) {

        String filename = ldbFileName.substring(ldbFileName.lastIndexOf("\\") + 1);
        filename = filename.substring(0, filename.length() - 4);

        String path = ldbFileName.substring(0, ldbFileName.lastIndexOf("\\")) + "\\" + filename;

        File directory = new File(path + "\\textures");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("Failed to create directory");
            }
        }

        AtomicInteger materialId = new AtomicInteger(-1);

        Map<String, String> materialTextures = new LinkedHashMap<>();

        ldb2.getMaterials().getList().stream()
                .map(LdbMaterial::getDiffuseTexture)
                .forEach(texture -> {

                    if (texture.getFileType() != 5) {
                        throw new RuntimeException("Non dds texture found");
                    }

                    String name = texture.getFilePath().replace("\\", "_");
                    name = name.substring(12);
                    name = name.substring(0, name.length() - 4);
                    name += ".jpg";

                    try {
                        Files.write(Paths.get(directory.getAbsolutePath() + "\\" + name), DdsToJpgConverter.convert(texture.getData()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    materialTextures.put(String.format("material_%s", materialId.incrementAndGet()), name);
                });


        Map<String, Integer> materialTypeMap = new HashMap<>();

        MaterialLoader.getDefaultTextures().stream()
                .filter(texture -> texture.getFilePath().contains("skybox") || texture.getFilePath().contains("collision"))
                .forEach(texture -> {

                    String name = texture.getFilePath().substring(25).replace("\\", "_");
                    name = name.substring(0, name.length() - 4);
                    name += ".jpg";

                    try {
                        Files.write(Paths.get(directory.getAbsolutePath() + "\\" + name), DdsToJpgConverter.convert(texture.getData()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    materialTypeMap.put(name.replace("Indicators_", "").replace(".jpg", ""), materialId.incrementAndGet());
                    materialTextures.put(String.format("material_%s", materialId.get()), name);
                });

        Map<Integer, FSM> fsmMap = ldb2.getFSMS().getList().stream()
                .collect(Collectors.toMap(FSM::getIndex, Function.identity()));

        List<OBJ.Object3D> dynamicMeshes = ldb2.getDynamicMeshes().getList().stream()
                .filter(dynamicMesh -> !dynamicMesh.getStaticMeshContainer().isEmpty())
                .map(
                        dynamicMesh -> {

                            FSM fsm = fsmMap.get(dynamicMesh.getFsmId());

                            if (fsm == null) {
                                throw new RuntimeException();
                            }

                            double[][] matrix = MatrixUtil.matrixFloatToDouble(fsm.getTransform());
                            Vector3D position = MatrixUtil.getPosition(fsm.getTransform()).multiply(-1);

                            return toObject3D(
                                    fsm.getName(),
                                    getTriangles(
                                            dynamicMesh.getStaticMeshContainer().getList().stream().map(s -> (Shape) s).collect(Collectors.toList()),
                                            new Vector3D(dynamicMesh.getAabb().getPivotPoint()).multiply(-1)
                                    ).stream()
                                            .peek(t -> t.rotate(matrix).minus(position))
                                            .collect(Collectors.toList())
                            );
                        }
                ).collect(Collectors.toList());


        List<OBJ.Object3D> staticMeshes = ldb2.getRooms().getList().stream()
                .map(room -> {
                    Pair<List<LdbTriangleDTO>, List<List<LdbTriangleDTO>>> m = extractTriangles(room, ldb2.getPortals().getList(), materialTypeMap);
                    List<List<LdbTriangleDTO>> result = new ArrayList<>(m.getValue());
                    result.add(m.getKey());

                    return result.stream()
                            .map(t -> toObject3D(room.getName() + "::mesh_" + MeshCounter.getInstance().next(), t))
                            .collect(Collectors.toList());

                })
                .flatMap(List::stream)
                .collect(Collectors.toList());


        staticMeshes.addAll(dynamicMeshes);

        List<String> text = new ArrayList<>();


        text.add(String.format("mtllib %s.mtl\r\n", filename));

        OBJ obj = new OBJ();
        staticMeshes.forEach(obj::addObject3D);
        text.add(obj.asText());

        byte[] objBytes = text.stream()
                .reduce((a, b) -> a + b)
                .map(String::getBytes)
                .orElseThrow(RuntimeException::new);

        byte[] mtlBytes = toMtl(materialTextures).asText().getBytes();

        try {
            Files.write(Paths.get(path + "\\" + filename + ".obj"), objBytes);
            Files.write(Paths.get(path + "\\" + filename + ".mtl"), mtlBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }

    private static MTL toMtl(Map<String, String> materialTextures) {
        MTL mtl = new MTL();

        materialTextures.entrySet().stream()
                .map(e -> new MTL.Material(e.getKey(), "textures/" + e.getValue()))
                .forEach(mtl::addMaterial);

        return mtl;
    }

    private static OBJ.Object3D toObject3D(String meshName, List<LdbTriangleDTO> triangles) {
        OBJ.Object3D object3D = new OBJ.Object3D();

        object3D.setName(meshName);

        List<Vector3D> vertices = triangles.stream()
                .map(LdbTriangleDTO::getVertices)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        List<VertexUV> uvs = triangles.stream()
                .map(LdbTriangleDTO::getUv)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        vertices.forEach(object3D::addVertex);

        uvs.stream().map(uv -> new OBJ.UV(uv.getU(), uv.getV())).forEach(object3D::addUV);

        triangles.forEach(t -> {

            List<OBJ.Face.Vertex> objVertices = new ArrayList<>();

            for (int i = 0; i < t.getVertices().size(); i++) {
                objVertices.add(new OBJ.Face.Vertex(1 + vertices.indexOf(t.getVertices().get(i)), t.getUv().isEmpty() ? -1 : 1 + uvs.indexOf(t.getUv().get(i)), -1));
            }

            object3D.addFace(new OBJ.Face(objVertices, t.getMaterialId() > -1 ? "material_" + t.getMaterialId() : null, object3D));

        });

        return object3D;
    }

    private static List<LdbTriangleDTO> getTriangles(List<Shape> shapes, Vector3D position) {
        List<LdbTriangleDTO> meshTriangles = new ArrayList<>();
        for (Shape shape : shapes) {
            for (int i = 0; i < shape.getIndices().size() / 3; i++) {
                List<Integer> indices = shape.getIndices().subList(i * 3, (i + 1) * 3);
                List<Vector3D> vertices = indices.stream()
                        .map(index -> shape.getVertices().get(index))
                        .map(Vector3D::new)
                        .map(v -> v.clone().minus(position))
                        .collect(Collectors.toList());

                LdbTriangleDTO triangle = new LdbTriangleDTO(vertices);

                if (shape instanceof CollisionShape) {
                    triangle.setMaterialTypeId(((CollisionShape) shape).getMaterialIndices().get(i));
                }

                triangle.setMaterialId(shape.getMaterialId());

                if (!shape.getUvs().isEmpty()) {
                    triangle.setUv(indices.stream().map(index -> shape.getUvs().get(index)).collect(Collectors.toList()));
                }

                meshTriangles.add(triangle);
            }
        }

        return meshTriangles;
    }

    // todo refactor
    private static Pair<List<LdbTriangleDTO>, List<List<LdbTriangleDTO>>> extractTriangles(Room room, List<Portal> portals, Map<String, Integer> materialTypeMap) {
        double[][] roomMatrix = MatrixUtil.matrixFloatToDouble(room.getTransform());

        Vector3D positionOffset = new Vector3D(roomMatrix[3][0], roomMatrix[3][1], roomMatrix[3][2]).multiply(-1);

        List<LdbTrianglePortalDTO> portalTriangles = portals.stream()
                .filter(portal -> portal.getName().toLowerCase().startsWith(room.getName().toLowerCase() + "::"))
                .map(portal -> Triangulator.triangulate(portal.getPoints().stream()
                                .map(Vector3D::new)
                                .collect(Collectors.toList())
                        ).stream()
                        .map(LdbTrianglePortalDTO::new)
                        .peek(t -> t.setMaterialId(materialTypeMap.get("skybox"))) // todo "portal"
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<LdbTriangleDTO> collisionTriangles = getTriangles(
                room.getCollisions().stream().map(s -> (Shape) s).collect(Collectors.toList()),
                positionOffset
        );

        List<LdbTriangleDTO> triangles = getTriangles(
                room.getStaticMeshes().getList().stream().map(s -> (Shape) s).collect(Collectors.toList()),
                positionOffset
        );

        triangles.addAll(portalTriangles);

        MaterialLoader.getMaterialTypeMap()
                .forEach((name, materialType) -> {
                    if (MaterialType.COLLISION_NODRAW.equals(materialType) || MaterialType.SKYBOX.equals(materialType)) {
                        triangles.addAll(
                                collisionTriangles.stream()
                                        .filter(t -> materialType.ordinal() == t.getMaterialTypeId())
                                        .peek(t -> t.setMaterialId(materialTypeMap.get(name.toLowerCase())))
                                        .collect(Collectors.toList())
                        );
                    }
                });

        List<List<LdbTriangleDTO>> groups = TriangleGrouper.groupTriangles(triangles);

        List<LdbTriangleDTO> largest = groups.stream()
                .max(Comparator.comparingDouble(o -> o.stream().map(LdbTriangleDTO::getArea).reduce(Double::sum).orElseThrow(RuntimeException::new)))
                .orElseThrow(RuntimeException::new);

        List<LdbTriangleDTO> withPortals = groups.stream()
                .filter(triangleDTOS -> triangleDTOS.stream().anyMatch(t -> t instanceof LdbTrianglePortalDTO))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<LdbTriangleDTO> finalRoom = new ArrayList<>(largest);
        finalRoom.addAll(withPortals);
        finalRoom = finalRoom.stream().distinct().collect(Collectors.toList());

        List<LdbTriangleDTO> finalRoom1 = finalRoom;
        List<List<LdbTriangleDTO>> groups1 = TriangleGrouper.groupTriangles(
                triangles.stream()
                        .filter(t -> t.getMaterialTypeId() != MaterialType.COLLISION_NODRAW.ordinal())
                        .filter(t -> !finalRoom1.contains(t))
                        .collect(Collectors.toList())
        );

        return new Pair<>(finalRoom, groups1);
    }
}
