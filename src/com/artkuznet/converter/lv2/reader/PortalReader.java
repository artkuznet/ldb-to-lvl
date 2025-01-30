package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.Portal;

public class PortalReader {
    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof Portal)) {
            throw new RuntimeException();
        }

        reader.rememberOffset();

        ReaderHelper.read300(reader);

        int dataSize = (int) reader.readObject();

        ((Portal) entity).setPolygonIndex((int) reader.readObject());
        ((Portal) entity).setIgnoreInGISLighting(1 == (int) reader.readObject());
        ((Portal) entity).setAlwaysClosed(1 == (int) reader.readObject());

        reader.validateDataSize(dataSize);

        return entity;
    }
}
