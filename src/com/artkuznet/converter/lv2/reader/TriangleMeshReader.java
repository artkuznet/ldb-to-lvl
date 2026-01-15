package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.mesh.TriangleMesh;

public class TriangleMeshReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof TriangleMesh)) {
            throw new RuntimeException();
        }

        ReaderHelper.read300(reader);

        int dataSize = (int) reader.readObject();

        byte[] unkTriangleMeshData = reader.readBytes(dataSize - 8 - 5);

        ((TriangleMesh) entity).setUnkTriangleMeshData(unkTriangleMeshData);

        return entity;
    }
}
