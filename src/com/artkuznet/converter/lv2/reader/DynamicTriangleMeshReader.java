package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.maxed2.entity.mesh.DynamicTriangleMesh;
import com.artkuznet.converter.maxed2.entity.Entity;

public class DynamicTriangleMeshReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof DynamicTriangleMesh)) {
            throw new RuntimeException();
        }

        ((DynamicTriangleMesh) entity).setProperties(MeshReader.readMeshProperties(reader));

        ((DynamicTriangleMesh) entity).setTriangleMeshSourceFile((String) reader.readObject());
        ((DynamicTriangleMesh) entity).setTriangleMeshSourceName((String) reader.readObject());

        ((DynamicTriangleMesh) entity).setHasDynamic(1 == (int) reader.readObject());

        ((DynamicTriangleMesh) entity).setDynamicData(ReaderHelper.readDynamicData(reader));

        int unk2 = (int) reader.readObject(); // UNumber
        for (int i = 0; i < unk2; i++) {

            DynamicTriangleMesh.UnkBlock1 block = new DynamicTriangleMesh.UnkBlock1();

            reader.rememberOffset();
            ReaderHelper.read100(reader);
            int dsize = (int) reader.readObject();
            block.d1 = new double[]{
                    (double) reader.readObject(),
                    (double) reader.readObject(),
                    (double) reader.readObject(),
                    (double) reader.readObject(),
                    (double) reader.readObject(),
                    (double) reader.readObject(),
            };
            block.i1 = new int[]{
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
            };
            block.d2 = new double[]{
                    (double) reader.readObject(),
                    (double) reader.readObject(),
            };

            reader.validateDataSize(dsize);

            ((DynamicTriangleMesh) entity).unkBlock1List.add(block);
        }


        int unk3 = (int) reader.readObject(); // Unumber
        for (int i = 0; i < unk3; i++) {
            DynamicTriangleMesh.UnkBlock2 block = new DynamicTriangleMesh.UnkBlock2();

            reader.rememberOffset();
            ReaderHelper.read200(reader);
            int dsize = (int) reader.readObject();

            block.vIndices = new int[]{
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
            };

            block.uv = new VertexUV[]{
                    (VertexUV) reader.readObject(),
                    (VertexUV) reader.readObject(),
                    (VertexUV) reader.readObject(),
            };

            block.unkS1 = (String) reader.readObject();
            block.unkS2 = (String) reader.readObject();

            block.unk11 = (int) reader.readObject(); // Number

            reader.validateDataSize(dsize);

            ((DynamicTriangleMesh) entity).unkBlock2List.add(block);
        }

        ((DynamicTriangleMesh) entity).unk4 = (int) reader.readObject(); // Uint

        return entity;
    }
}
