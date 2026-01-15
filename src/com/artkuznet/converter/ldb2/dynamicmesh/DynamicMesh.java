package com.artkuznet.converter.ldb2.dynamicmesh;

import com.artkuznet.converter.ldb2.aabb.AABB;
import com.artkuznet.converter.ldb2.collistionshape.CollisionShape;
import com.artkuznet.converter.ldb2.staticmesh.StaticMeshContainer;

import java.util.List;

public class DynamicMesh {
    private int fsmId;
    private boolean useLightMaps;
    private boolean pointLightsAffect;
    private boolean continuousUpdate;
    private boolean bulletCollision;
    private boolean characterCollision;
    private boolean blockExplosions;
    private boolean noDecals;
    private boolean elevator;
    private int physicalMaterial;
    private int prefabId;
    private boolean shareCollision;
    private AABB aabb;
    private StaticMeshContainer staticMeshContainer;
    private List<CollisionShape> collision;
    private List<DynamicMeshAnimation> animations;

    public DynamicMesh(
            int fsmId,
            boolean useLightMaps,
            boolean pointLightsAffect,
            boolean continuousUpdate,
            boolean bulletCollision,
            boolean characterCollision,
            boolean blockExplosions,
            boolean noDecals,
            boolean elevator,
            int physicalMaterial,
            int prefabId,
            boolean shareCollision,
            AABB aabb,
            StaticMeshContainer mesh,
            List<CollisionShape> collision,
            List<DynamicMeshAnimation> animations
    ) {
        this.fsmId = fsmId;
        this.useLightMaps = useLightMaps;
        this.pointLightsAffect = pointLightsAffect;
        this.continuousUpdate = continuousUpdate;
        this.bulletCollision = bulletCollision;
        this.characterCollision = characterCollision;
        this.blockExplosions = blockExplosions;
        this.noDecals = noDecals;
        this.elevator = elevator;
        this.physicalMaterial = physicalMaterial;
        this.prefabId = prefabId;
        this.shareCollision = shareCollision;
        this.aabb = aabb;
        this.staticMeshContainer = mesh;
        this.collision = collision;
        this.animations = animations;
    }

    public int getFsmId() {
        return fsmId;
    }

    public void setFsmId(int fsmId) {
        this.fsmId = fsmId;
    }

    public boolean isUseLightMaps() {
        return useLightMaps;
    }

    public void setUseLightMaps(boolean useLightMaps) {
        this.useLightMaps = useLightMaps;
    }

    public boolean isPointLightsAffect() {
        return pointLightsAffect;
    }

    public void setPointLightsAffect(boolean pointLightsAffect) {
        this.pointLightsAffect = pointLightsAffect;
    }

    public boolean isContinuousUpdate() {
        return continuousUpdate;
    }

    public void setContinuousUpdate(boolean continuousUpdate) {
        this.continuousUpdate = continuousUpdate;
    }

    public boolean isBulletCollision() {
        return bulletCollision;
    }

    public void setBulletCollision(boolean bulletCollision) {
        this.bulletCollision = bulletCollision;
    }

    public boolean isCharacterCollision() {
        return characterCollision;
    }

    public void setCharacterCollision(boolean characterCollision) {
        this.characterCollision = characterCollision;
    }

    public boolean isBlockExplosions() {
        return blockExplosions;
    }

    public void setBlockExplosions(boolean blockExplosions) {
        this.blockExplosions = blockExplosions;
    }

    public boolean isNoDecals() {
        return noDecals;
    }

    public void setNoDecals(boolean noDecals) {
        this.noDecals = noDecals;
    }

    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    public int getPhysicalMaterial() {
        return physicalMaterial;
    }

    public void setPhysicalMaterial(int physicalMaterial) {
        this.physicalMaterial = physicalMaterial;
    }

    public int getPrefabId() {
        return prefabId;
    }

    public void setPrefabId(int prefabId) {
        this.prefabId = prefabId;
    }

    public boolean isShareCollision() {
        return shareCollision;
    }

    public void setShareCollision(boolean shareCollision) {
        this.shareCollision = shareCollision;
    }

    public AABB getAabb() {
        return aabb;
    }

    public void setAabb(AABB aabb) {
        this.aabb = aabb;
    }

    public StaticMeshContainer getStaticMeshContainer() {
        return staticMeshContainer;
    }

    public void setStaticMeshContainer(StaticMeshContainer staticMeshContainer) {
        this.staticMeshContainer = staticMeshContainer;
    }

    public List<CollisionShape> getCollision() {
        return collision;
    }

    public void setCollision(List<CollisionShape> collision) {
        this.collision = collision;
    }

    public List<DynamicMeshAnimation> getAnimations() {
        return animations;
    }

    public void setAnimations(List<DynamicMeshAnimation> animations) {
        this.animations = animations;
    }
}
