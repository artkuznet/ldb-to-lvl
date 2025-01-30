package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.prefab.PrefabParent;

public class PrefabParentReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof PrefabParent)) {
            throw new RuntimeException();
        }

        ((PrefabParent) entity).setType((String) reader.readObject());

        ((PrefabParent) entity).setOpened(1 == (int) reader.readObject());

        double radius = (double) reader.readObject();
        if (Double.compare(radius, entity.getRadius()) != 0) {
            throw new RuntimeException();
        }

        return entity;
    }
}
