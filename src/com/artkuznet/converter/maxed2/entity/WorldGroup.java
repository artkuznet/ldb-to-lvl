package com.artkuznet.converter.maxed2.entity;

import com.artkuznet.converter.ldb2.fsm.FSM;
import com.artkuznet.converter.lv2.converter.FsmDataConverter;

public class WorldGroup extends Entity {

    public WorldGroup() {
        this.name = "World group";
    }

    public static WorldGroup getEmpty() {
        WorldGroup worldGroup = new WorldGroup();

        Player player = new Player();

        player.setParentEntity(worldGroup);
        worldGroup.addChildEntity(player);

        return worldGroup;
    }

    public static WorldGroup getEmpty(FSM playerFsm) {
        WorldGroup worldGroup = new WorldGroup();

        Player player = new Player();

        player.setFsmData(FsmDataConverter.convert(playerFsm));

        player.setParentEntity(worldGroup);
        worldGroup.addChildEntity(player);

        return worldGroup;
    }
}
