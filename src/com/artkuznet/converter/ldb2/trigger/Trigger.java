package com.artkuznet.converter.ldb2.trigger;

import com.artkuznet.converter.ldb2.collistionshape.CollisionShape;

import java.util.List;

public class Trigger {

    private final int fsmId;

    private final float radius;

    private final int activationPlayer;

    private final int activationUse;

    private final int activationEnemy;

    private final int activationBullet;

    private final int activationLookAt;

    private final int activationVisibility;

    private final String activatorsUseAnimation;

    private final int hasCollisionShape;

    private final int parent;

    private final List<CollisionShape> collisionShapes;

    public Trigger(
            int fsmId,
            float radius,
            int activationPlayer,
            int activationUse,
            int activationEnemy,
            int activationBullet,
            int activationLookAt,
            int activationVisibility,
            String activatorsUseAnimation,
            int hasCollisionShape,
            int parent,
            List<CollisionShape> collisionShapes
    ) {
        this.fsmId = fsmId;
        this.radius = radius;
        this.activationPlayer = activationPlayer;
        this.activationUse = activationUse;
        this.activationEnemy = activationEnemy;
        this.activationBullet = activationBullet;
        this.activationLookAt = activationLookAt;
        this.activationVisibility = activationVisibility;
        this.activatorsUseAnimation = activatorsUseAnimation;
        this.hasCollisionShape = hasCollisionShape;
        this.parent = parent;
        this.collisionShapes = collisionShapes;
    }

    public int getFsmId() {
        return fsmId;
    }

    public float getRadius() {
        return radius;
    }

    public int getActivationPlayer() {
        return activationPlayer;
    }

    public int getActivationUse() {
        return activationUse;
    }

    public int getActivationEnemy() {
        return activationEnemy;
    }

    public int getActivationBullet() {
        return activationBullet;
    }

    public int getActivationLookAt() {
        return activationLookAt;
    }

    public int getActivationVisibility() {
        return activationVisibility;
    }

    public String getActivatorsUseAnimation() {
        return activatorsUseAnimation;
    }

    public int getHasCollisionShape() {
        return hasCollisionShape;
    }

    public int getParent() {
        return parent;
    }

    public List<CollisionShape> getCollisionShapes() {
        return collisionShapes;
    }
}
