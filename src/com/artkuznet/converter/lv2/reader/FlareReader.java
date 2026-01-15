package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.Flare;

public class FlareReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof Flare)) {
            throw new RuntimeException();
        }

        reader.rememberOffset();

        ReaderHelper.read100(reader);

        int dataSize = (int) reader.readObject();

        ((Flare) entity).setType((String) reader.readObject());

        double radius = (double) reader.readObject();
        if (Double.compare(radius, entity.getRadius()) != 0) {
            throw new RuntimeException();
        }

        reader.validateDataSize(dataSize);

        reader.rememberOffset();
        ReaderHelper.read100(reader);
        reader.validateDataSize((int) reader.readObject());

        return entity;
    }
}
