package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.DynamicPointlight;
import com.artkuznet.converter.maxed2.entity.Entity;

public class DynamicPointlightReader {
    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof DynamicPointlight)) {
            throw new RuntimeException();
        }

        reader.rememberOffset();

        ReaderHelper.read100(reader);

        int dataSize1 = (int) reader.readObject();

        ((DynamicPointlight) entity).setType((String) reader.readObject());

        double radius = (double) reader.readObject();
        if (Double.compare(radius, entity.getRadius()) != 0) {
            throw new RuntimeException();
        }

        reader.validateDataSize(dataSize1);

        reader.rememberOffset();

        ReaderHelper.read100(reader);

        int dataSize2 = (int) reader.readObject();

        ((DynamicPointlight) entity).setColor(
                new DynamicPointlight.Color(
                        (float) reader.readObject(),
                        (float) reader.readObject(),
                        (float) reader.readObject(),
                        (float) reader.readObject()
                )
        );

        ((DynamicPointlight) entity).setIntensity((float) reader.readObject());
        ((DynamicPointlight) entity).setFalloff((float) reader.readObject());

        reader.validateDataSize(dataSize2);

        return entity;
    }
}
