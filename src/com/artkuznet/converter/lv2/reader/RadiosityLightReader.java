package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.RadiosityLight;

public class RadiosityLightReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof RadiosityLight)) {
            throw new RuntimeException();
        }

        reader.rememberOffset();

        ReaderHelper.read100(reader);

        int dataSize = (int) reader.readObject();

        ((RadiosityLight) entity).setColor(
                new RadiosityLight.Color(
                        (int) reader.readObject(),
                        (int) reader.readObject(),
                        (int) reader.readObject(),
                        (int) reader.readObject()
                )
        );

        ((RadiosityLight) entity).setIntensity((float) reader.readObject());
        ((RadiosityLight) entity).setHotspotAngle((float) reader.readObject());
        ((RadiosityLight) entity).setFalloffAngle((float) reader.readObject());

        ((RadiosityLight) entity).setRadiosityLightIndex((int) reader.readObject());

        reader.validateDataSize(dataSize);

        return entity;
    }
}
