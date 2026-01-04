package com.artkuznet.converter.maxed2.entity;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.maxed2.entity.fsm.FSM;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Entity {

    protected Entity parentEntity = null;
    protected List<Entity> childEntities = new ArrayList<>();

    protected String name;
    protected String ldb2FsmName;

    protected int unk1 = 0;
    protected boolean hidden = false;
    protected boolean excludeFromGame = false;
    protected boolean excludeFromLighting = false;
    protected boolean enableExportRegrouping = false;

    public boolean isGameplayCritical() {
        return gameplayCritical;
    }

    protected boolean gameplayCritical = true;

    protected double[][] localMatrix = new double[][]{
            new double[]{1, 0, 0},
            new double[]{0, 1, 0},
            new double[]{0, 0, 1},
            new double[]{0, 0, 0},
    }; // transform

    protected Vector3D minPoint = new Vector3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

    protected Vector3D maxPoint = new Vector3D(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);

    protected double radius = 0;

    public void setName(String name) {
        this.name = name;
    }

    public void setUnk1(int unk1) {
        this.unk1 = unk1;
    }

    public void setParentEntity(Entity entity) {
        this.parentEntity = entity;
    }

    public void addChildEntity(Entity entity) {
        this.childEntities.add(entity);
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setExcludeFromGame(boolean excludeFromGame) {
        this.excludeFromGame = excludeFromGame;
    }

    public void setExcludeFromLighting(boolean excludeFromLighting) {
        this.excludeFromLighting = excludeFromLighting;
    }

    public void setEnableExportRegrouping(boolean enableExportRegrouping) {
        this.enableExportRegrouping = enableExportRegrouping;
    }

    public void setGameplayCritical(boolean gameplayCritical) {
        this.gameplayCritical = gameplayCritical;
    }

    public void setLocalMatrix(double[][] localMatrix) {
        this.localMatrix = localMatrix;
    }

    public void setMinPoint(Vector3D minPoint) {
        this.minPoint = minPoint;
    }

    public void setMaxPoint(Vector3D maxPoint) {
        this.maxPoint = maxPoint;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Entity getParentEntity() {
        return parentEntity;
    }

    public List<Entity> getChildEntities() {
        return childEntities;
    }

    public List<Entity> getAllFsmChildEntities() {
        List<Entity> childs = new ArrayList<>();

        for (Entity child : childEntities) {
            if (child instanceof FSM) {
                childs.add(child);
            }
            childs.addAll(child.getAllFsmChildEntities().stream()
                    .filter(e -> e instanceof FSM)
                    .collect(Collectors.toList())
            );
        }

        return childs;
    }

    public String getName() {
        return name;
    }

    public String getLdb2FsmName() {
        return ldb2FsmName;
    }

    public void setLdb2FsmName(String ldb2FsmName) {
        this.ldb2FsmName = ldb2FsmName;
    }

    public String getFullName() {

        StringBuilder name = new StringBuilder(this.name);

        Entity parent = parentEntity;
        while (parent != null) {

            name.insert(0, parent.name + "::");

            parent = parent.parentEntity;
        }

        name.insert(0, "::");

        return name.toString();
    }

    public int getUnk1() {
        return unk1;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isExcludeFromGame() {
        return excludeFromGame;
    }

    public boolean isExcludeFromLighting() {
        return excludeFromLighting;
    }

    public boolean isEnableExportRegrouping() {
        return enableExportRegrouping;
    }

    public double[][] getLocalMatrix() {
        return localMatrix;
    }

    public Vector3D getMinPoint() {
        return minPoint;
    }

    public Vector3D getMaxPoint() {
        return maxPoint;
    }

    public double getRadius() {
        return radius;
    }
}
