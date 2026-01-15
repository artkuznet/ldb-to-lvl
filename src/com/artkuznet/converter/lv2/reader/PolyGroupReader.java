package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.mesh.PolyGroup;

public class PolyGroupReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof PolyGroup)) {
            throw new RuntimeException();
        }

        reader.rememberOffset();

        ReaderHelper.read200(reader);

        int dataSize = (int) reader.readObject();

        int polygonIndexCount = (int) reader.readObject();
        for (int i = 0; i < polygonIndexCount; i++) {
            ((PolyGroup) entity).addPolygonIndex((int) reader.readObject());
        }

        ((PolyGroup) entity).setSmoothLightmaps(1 == (int) reader.readObject());
        ((PolyGroup) entity).setSmoothGeometry(1 == (int) reader.readObject());
        ((PolyGroup) entity).setMaxAngle((float) reader.readObject());
        ((PolyGroup) entity).setMaxEdge((float) reader.readObject());
        ((PolyGroup) entity).setFreezeLightmaps(1 == (int) reader.readObject());
        ((PolyGroup) entity).setRayTracing(1 == (int) reader.readObject());

        reader.validateDataSize(dataSize);

        return entity;
    }
}
