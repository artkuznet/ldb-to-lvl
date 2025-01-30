package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Enemy;
import com.artkuznet.converter.maxed2.entity.Entity;

public class EnemyReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof Enemy)) {
            throw new RuntimeException();
        }

        ((Enemy) entity).setType((String) reader.readObject());

        double radius = (double) reader.readObject();
        if (Double.compare(radius, entity.getRadius()) != 0) {
            throw new RuntimeException();
        }

        reader.rememberOffset();

        ReaderHelper.read200(reader);

        int dataSize = (int) reader.readObject();

        ((Enemy) entity).setGroup((String) reader.readObject());
        ((Enemy) entity).setActivatorsUseAnimation((String) reader.readObject());

        reader.validateDataSize(dataSize);

        return entity;
    }
}
