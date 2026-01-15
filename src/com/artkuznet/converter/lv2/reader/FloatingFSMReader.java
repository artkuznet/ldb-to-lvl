package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.fsm.FloatingFSM;

public class FloatingFSMReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof FloatingFSM)) {
            throw new RuntimeException();
        }

        double radius = (double) reader.readObject();
        if (Math.abs(radius - entity.getRadius()) > 0.0000001) {
            throw new RuntimeException();
        }

        return entity;
    }
}
