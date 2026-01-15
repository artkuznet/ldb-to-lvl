package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.LevelItem;

public class LevelItemReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof LevelItem)) {
            throw new RuntimeException();
        }

        reader.rememberOffset();

        ReaderHelper.read100(reader);

        int dataSize = (int) reader.readObject();

        ((LevelItem) entity).setType((String) reader.readObject());

        double radius = (double) reader.readObject();
        if (Math.abs(radius - entity.getRadius()) > 0.0000001) {
            throw new RuntimeException();
        }

        reader.validateDataSize(dataSize);

        return entity;
    }
}
