package com.artkuznet.converter.ldb2.character;

import com.artkuznet.converter.ldb2.EntityLdb2;

public class Character implements EntityLdb2 {

    private String entityName;
    private String characterName;
    private float[][] transform;
    private int roomId;
    private int enemyGroupId;
    private String activatorUseAnimation;

    public Character(
            String entityName,
            String characterName,
            float[][] transform,
            int roomId,
            int enemyGroupId,
            String activatorUseAnimation
    ) {
        this.entityName = entityName;
        this.characterName = characterName;
        this.transform = transform;
        this.roomId = roomId;
        this.enemyGroupId = enemyGroupId;
        this.activatorUseAnimation = activatorUseAnimation;
    }

    @Override
    public String getName() {
        return entityName;
    }

    @Override
    public float[][] getLocalTransform() {
        return transform;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public float[][] getTransform() {
        return transform;
    }

    public void setTransform(float[][] transform) {
        this.transform = transform;
    }

    @Override
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getEnemyGroupId() {
        return enemyGroupId;
    }

    public void setEnemyGroupId(int enemyGroupId) {
        this.enemyGroupId = enemyGroupId;
    }

    public String getActivatorUseAnimation() {
        return activatorUseAnimation;
    }

    public void setActivatorUseAnimation(String activatorUseAnimation) {
        this.activatorUseAnimation = activatorUseAnimation;
    }
}
