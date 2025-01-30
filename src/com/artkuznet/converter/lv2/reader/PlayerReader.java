package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.Player;

public class PlayerReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof Player)) {
            throw new RuntimeException();
        }

        String playerGroup = (String) reader.readObject();
        if (!"PLAYER_GROUP".equals(playerGroup)) {
            throw new RuntimeException();
        }

        return entity;
    }
}
