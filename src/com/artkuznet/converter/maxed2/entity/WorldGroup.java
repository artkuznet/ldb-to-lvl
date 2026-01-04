package com.artkuznet.converter.maxed2.entity;

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
}
