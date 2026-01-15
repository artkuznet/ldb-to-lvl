package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.Trigger;
import com.artkuznet.converter.maxed2.entity.TriggerData;

public class TriggerReader {
    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof Trigger)) {
            throw new RuntimeException();
        }

        ((Trigger) entity).setType((String) reader.readObject());

        double radius = (double) reader.readObject();
        if (Double.compare(radius, entity.getRadius()) != 0) {
            throw new RuntimeException();
        }

        reader.rememberOffset();

        ReaderHelper.read300(reader);

        int dataSize = (int) reader.readObject();

        TriggerData triggerData = new TriggerData();

        triggerData.setPlayer(1 == (int) reader.readObject());
        triggerData.setUse(1 == (int) reader.readObject());
        triggerData.setEnemy(1 == (int) reader.readObject());
        triggerData.setBullet(1 == (int) reader.readObject());
        triggerData.setLookAt(1 == (int) reader.readObject());
        triggerData.setVisibility(1 == (int) reader.readObject());

        triggerData.setActivatorsUseAnimation((String) reader.readObject());

        ((Trigger) entity).setData(triggerData);

        reader.validateDataSize(dataSize);

        return entity;
    }
}
