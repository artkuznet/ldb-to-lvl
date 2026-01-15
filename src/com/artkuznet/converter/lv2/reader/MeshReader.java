package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.TriggerData;
import com.artkuznet.converter.maxed2.entity.mesh.*;

import java.util.ArrayList;
import java.util.List;

public class MeshReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof Mesh)) {
            throw new RuntimeException();
        }

        if (!(entity instanceof DynamicMesh)) {
            reader.rememberOffset();
            ReaderHelper.read10C(reader);
            int dataSize = (int) reader.readObject();
            readMesh(entity, reader);
            reader.validateDataSize(dataSize);
        } else {
            readMesh(entity, reader);
        }

        return entity;
    }

    private static Entity readMesh(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof Mesh)) {
            throw new RuntimeException();
        }

        int offset1 = reader.getOffset();

        ReaderHelper.read900(reader);

        int dataSize1 = (int) reader.readObject();

        ((Mesh) entity).setFlipNormals(1 == (int) reader.readObject());

        ((Mesh) entity).setVertices(MeshReader.readVertices(reader));

        ((Mesh) entity).setPolygons(MeshReader.readPolygons(reader, (Mesh) entity));

        ((Mesh) entity).setHasDynamic(1 == (int) reader.readObject());
        if (((Mesh) entity).getHasDynamic()) {
            if (!(entity instanceof DynamicMesh)) {
                throw new RuntimeException();
            }

            ((DynamicMesh) entity).setDynamicData(ReaderHelper.readDynamicData(reader));
        }

        ((Mesh) entity).setProperties(MeshReader.readMeshProperties(reader));

        ((Mesh) entity).setTrigger(1 == (int) reader.readObject());
        if (((Mesh) entity).isTrigger()) {

            if (!(entity instanceof DynamicMesh)) { // todo TriggerMesh ?
                throw new RuntimeException();
            }

            int offset2 = reader.getOffset();

            ReaderHelper.read300(reader);

            int dataSize2 = (int) reader.readObject();

            TriggerData triggerData = new TriggerData();

            triggerData.setPlayer(1 == (int) reader.readObject());
            triggerData.setUse(1 == (int) reader.readObject());
            triggerData.setEnemy(1 == (int) reader.readObject());
            triggerData.setBullet(1 == (int) reader.readObject());
            triggerData.setLookAt(1 == (int) reader.readObject());
            triggerData.setVisibility(1 == (int) reader.readObject());
            triggerData.setActivatorsUseAnimation((String) reader.readObject());

            if (reader.getOffset() - offset2 != dataSize2) {
                throw new RuntimeException("Invalid data size");
            }

            ((DynamicMesh) entity).setTriggerData(triggerData);
        }

        if (reader.getOffset() - offset1 != dataSize1) {
            throw new RuntimeException("Invalid data size");
        }

        ((Mesh) entity).setMeshUnk4(1 == (int) reader.readObject());
        if (((Mesh) entity).isMeshUnk4()) {
            MeshReader.readUnknownMeshData2(reader, (Mesh) entity);
        }

        return entity;
    }

    public static MeshProperties readMeshProperties(MaxTypeReader reader) {

        int offset = reader.getOffset();

        ReaderHelper.read600(reader);

        int dataSize = (int) reader.readObject();

        MeshProperties meshProperties = new MeshProperties();

        meshProperties.setCharacterCollisions(1 == (int) reader.readObject());
        meshProperties.setUseLightmaps(1 == (int) reader.readObject());
        meshProperties.setPointlightsAffect(1 == (int) reader.readObject());
        meshProperties.setFsmContinuousUpdate(1 == (int) reader.readObject());
        meshProperties.setBulletCollisions(1 == (int) reader.readObject());
        meshProperties.setCastNoShadows(1 == (int) reader.readObject());
        meshProperties.setCollisions(1 == (int) reader.readObject());
        meshProperties.setBlockExplosions(1 == (int) reader.readObject());
        meshProperties.setDoNotRender(1 == (int) reader.readObject());
        meshProperties.setNoDecals(1 == (int) reader.readObject());
        meshProperties.setSoundEnvironment((String) reader.readObject());
        meshProperties.setGenerateConvexHull(1 == (int) reader.readObject());
        meshProperties.setRayTracing(1 == (int) reader.readObject());
        meshProperties.setPhysicalMaterial((String) reader.readObject());
        meshProperties.setElevator(1 == (int) reader.readObject());
        meshProperties.setGenerateBoundingBoxHull(1 == (int) reader.readObject());

        if (reader.getOffset() - dataSize != offset) {
            throw new RuntimeException();
        }

        return meshProperties;
    }

    private static void readUnknownMeshData2(MaxTypeReader reader, Mesh mesh) {

        int offset = reader.getOffset();

        ReaderHelper.read100(reader);

        int unkDataLength = (int) reader.readObject();

        mesh.setUnkInt(new int[]{
                (int) reader.readObject(), // 0x11
                (int) reader.readObject(), // 0x14
                (int) reader.readObject(), // 0x14
                (int) reader.readObject(), // 0x14
        });

        mesh.setUnkVertex(new Vertex[]{
                (Vertex) reader.readObject(),
                (Vertex) reader.readObject(),
        });

        mesh.setUnkData(reader.readBytes(unkDataLength - (reader.getOffset() - offset))); // lightmap ?

        if (reader.getOffset() - offset != unkDataLength) {
            throw new RuntimeException("Invalid data size");
        }
    }


    private static List<Vector3D> readVertices(MaxTypeReader reader) {
        int verticesCount = (int) reader.readObject(); // 0x11
        List<Vector3D> list = new ArrayList<>();
        for (int i = 0; i < verticesCount; i++) {
            int offset = reader.getOffset();
            ReaderHelper.read100(reader);
            int dataSize = (int) reader.readObject();
            list.add(readVector3D(reader));
            if (reader.getOffset() - dataSize != offset) {
                throw new RuntimeException();
            }
        }
        return list;
    }

    private static List<Polygon> readPolygons(MaxTypeReader reader, Mesh parentMesh) {

        List<Polygon> polygons = new ArrayList<>();

        int polygonsCount = (int) reader.readObject(); // 0x11
        for (int i = 0; i < polygonsCount; i++) {
            Polygon polygon = new Polygon();

            int offset = reader.getOffset();
            ReaderHelper.read300(reader);
            int dataSize = (int) reader.readObject();

            polygon.setIndex((int) reader.readObject());

            int edgesCount = (int) reader.readObject();

            polygon.setNorm1(readVector3D(reader)); // normal
            polygon.setNorm2(readVector3D(reader)); // normal

            polygon.setMatr1(readMatr3x3(reader));

            polygon.setNorm3(readVector3D(reader)); // normal

            polygon.setVertexXYZ(readVector3D(reader)); // (0, 0, 0)
            polygon.setScaleU(readVector3D(reader));
            polygon.setScaleV(readVector3D(reader));

            polygon.setNorm4(readVector3D(reader));

            polygon.setTextureVertexUV(new double[]{
                    (double) reader.readObject(),
                    (double) reader.readObject(),
            });
            polygon.setUnkVertexXYZ(readVector3D(reader));

            Polygon.Unk1 unk3 = new Polygon.Unk1();

            unk3.unkInt = (int) reader.readObject();
            unk3.norm1 = readVector3D(reader);
            unk3.matr = readMatr3x3(reader);
            unk3.norm2 = readVector3D(reader);
            unk3.unkD = new double[]{
                    (double) reader.readObject(),
                    (double) reader.readObject()
            };

            polygon.setUnk3(unk3);

            polygon.setTexelsPerMeter((float) reader.readObject());

            // lightmap index + portal index ?
            polygon.setUnk4(new int[]{
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
            });

            polygon.setMaterialCategory((String) reader.readObject());
            polygon.setMaterialName((String) reader.readObject());

            List<Polygon.Edge> edges = new ArrayList<>();
            for (int j = 0; j < edgesCount; j++) {
                int from = (int) reader.readObject();
                int to = (int) reader.readObject();
                edges.add(new Polygon.Edge(from, to));
            }
            polygon.setEdges(edges);

            List<Polygon.Triangle> triangles = new ArrayList<>();
            int trianglesCount = (int) reader.readObject();
            for (int j = 0; j < trianglesCount; j++) {
                Polygon.Triangle triangle = new Polygon.Triangle();
                triangle.setNormal(readVector3D(reader));
                int triangleVerticesCount = (int) reader.readObject();
                List<Integer> triangleVertices = new ArrayList<>();
                for (int k = 0; k < triangleVerticesCount; k++) {
                    triangleVertices.add((int) reader.readObject());
                }
                triangle.setVertexIndices(triangleVertices);
                triangles.add(triangle);
            }
            polygon.setTriangles(triangles);

            List<Polygon.Unk1> unk5 = new ArrayList<>();

            int unkSize = (int) reader.readObject();
            for (int k = 0; k < unkSize - 1; k++) {
                Polygon.Unk1 unkTmp = new Polygon.Unk1();

                unkTmp.unkInt = (int) reader.readObject();
                unkTmp.norm1 = readVector3D(reader);
                unkTmp.matr = readMatr3x3(reader);
                unkTmp.norm2 = readVector3D(reader);
                unkTmp.unkD = new double[]{
                        (double) reader.readObject(),
                        (double) reader.readObject()
                };

                unk5.add(unkTmp);
            }

            polygon.setUnk5(unk5);

            if (reader.getOffset() - dataSize != offset) {
                throw new RuntimeException();
            }

            polygons.add(polygon);
        }

        return polygons;
    }

    private static Vector3D readVector3D(MaxTypeReader reader) {
        return new Vector3D((double) reader.readObject(), (double) reader.readObject(), (double) reader.readObject());
    }

    private static double[][] readMatr3x3(MaxTypeReader reader) {
        return new double[][]{
                new double[]{
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                },
                new double[]{
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                },
                new double[]{
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                },
        };
    }
}
