package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.VolumeLightingBox;

public class VolumeLightingBoxReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof VolumeLightingBox)) {
            throw new RuntimeException();
        }

        reader.rememberOffset();

        ReaderHelper.read100(reader);

        int dataSize = (int) reader.readObject();

        ((VolumeLightingBox) entity).setWidth((double) reader.readObject());
        ((VolumeLightingBox) entity).setHeight((double) reader.readObject());
        ((VolumeLightingBox) entity).setDepth((double) reader.readObject());
        ((VolumeLightingBox) entity).setResolution((int) reader.readObject());

        reader.validateDataSize(dataSize);

        return entity;
    }
}
