package com.artkuznet.converter.obj.converter;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.ldb2.MaxLDB2;
import com.artkuznet.converter.ldb2.fsm.FSM;
import com.artkuznet.converter.ldb2.material.LdbMaterial;
import com.artkuznet.converter.ldb2.staticmesh.StaticMesh;
import com.artkuznet.converter.lv2.converter.helper.LdbTriangleDTO;
import com.artkuznet.converter.lv2.converter.helper.MatrixUtil;
import com.artkuznet.converter.lv2.converter.helper.MeshCounter;
import com.artkuznet.converter.obj.MTL;
import com.artkuznet.converter.obj.OBJ;
import com.artkuznet.converter.util.DdsToJpgConverter;

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

        Map<Integer, FSM> fsmMap = ldb2.getFSMS().getList().stream()
                .collect(Collectors.toMap(FSM::getIndex, Function.identity()));

        List<OBJ.Object3D> objs = ldb2.getDynamicMeshes().getList().stream()
                .filter(dynamicMesh -> !dynamicMesh.getStaticMeshContainer().isEmpty())
                .map(
                        dynamicMesh -> {

                            FSM fsm = fsmMap.get(dynamicMesh.getFsmId());

                            if (fsm == null) {
                                throw new RuntimeException();
                            }

                            double[][] matr = MatrixUtil.matrixFloatToDouble(fsm.getTransform());

                            Vector3D pos = new Vector3D(fsm.getTransform()[3][0], fsm.getTransform()[3][1], fsm.getTransform()[3][2]).multiply(-1);

                            return toObject3D(fsm.getName(), getTriangles(
                                            dynamicMesh.getStaticMeshContainer().getList(),
                                            new Vector3D(dynamicMesh.getAabb().getPivotPoint()).multiply(-1)
                                    ).stream()
                                            .peek(t -> t.getVertices().stream()
                                                    .peek(v -> v.rotate(matr).minus(pos))
                                                    .collect(Collectors.toList())
                                            )
                                            .collect(Collectors.toList())
                            );
                        }
                ).collect(Collectors.toList());


        List<List<LdbTriangleDTO>> triangles2 = ldb2.getRooms().getList().stream()
                .map(room -> room.getStaticMeshes().getList().stream()
                        .map(staticMesh -> {

                            double[][] roomMatrix = MatrixUtil.matrixFloatToDouble(room.getTransform());

                            Vector3D roomPosition = new Vector3D(roomMatrix[3][0], roomMatrix[3][1], roomMatrix[3][2]);

                            return getTriangles(
                                    Collections.singletonList(staticMesh),
                                    roomPosition.clone().multiply(-1)
                            );
                        }).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());


//        triangles.addAll(triangles2);

        List<OBJ.Object3D> object3DList = triangles2.stream()
                .map(t -> toObject3D("mesh_" + MeshCounter.getInstance().next(), t))
                .collect(Collectors.toList());

        object3DList.addAll(objs);

//        List<OBJ.Object3D> object3DList = triangles2.stream()
//                .map(GeometryProcessor::splitIntoRoomAndObjects)
//                .map(t -> t.stream().map(tt->toObject3D("mesh_" + MeshCounter.getInstance().next(), tt)).collect(Collectors.toList()))
//                .flatMap(List::stream)
//                .collect(Collectors.toList());

        List<String> text = new ArrayList<>();


        text.add(String.format("mtllib %s.mtl\r\n", filename));

        OBJ obj = new OBJ();
        object3DList.forEach(obj::addObject3D);
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

        return new OBJ();
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
                objVertices.add(new OBJ.Face.Vertex(1 + vertices.indexOf(t.getVertices().get(i)), 1 + uvs.indexOf(t.getUv().get(i)), -1));
            }

            object3D.addFace(new OBJ.Face(objVertices, "material_" + t.getMaterialId(), object3D));

        });

        return object3D;
    }

    private static List<LdbTriangleDTO> getTriangles(List<StaticMesh> staticMeshes, Vector3D position) {
        List<LdbTriangleDTO> meshTriangles = new ArrayList<>();
        for (StaticMesh staticMesh : staticMeshes) {
            for (int i = 0; i < staticMesh.getIndices().size() / 3; i++) {
                List<Integer> indices = staticMesh.getIndices().subList(i * 3, (i + 1) * 3);
                List<Vector3D> vertices = indices.stream()
                        .map(index -> staticMesh.getVertices().get(index))
                        .map(Vector3D::new)
                        .map(v -> v.clone().minus(position))
                        .collect(Collectors.toList());

                LdbTriangleDTO triangle = new LdbTriangleDTO(vertices);

                triangle.setMaterialId(staticMesh.getMaterialId());

                triangle.setUv(indices.stream().map(index -> staticMesh.getUvs().get(index)).collect(Collectors.toList()));

                meshTriangles.add(triangle);
            }
        }

        return meshTriangles.stream()
                .filter(t -> t.getArea() > 1e-6) // fix zero area triangles
                .collect(Collectors.toList());
    }
}
