package com.artkuznet.converter.maxed2.entity.mesh;

public class MeshProperties {

    private boolean characterCollisions = true;
    private boolean useLightmaps = true;
    private boolean pointlightsAffect;
    private boolean fsmContinuousUpdate;
    private boolean bulletCollisions = true;
    private boolean castNoShadows;
    private boolean collisions = true;
    private boolean blockExplosions = true;
    private boolean doNotRender;
    private boolean noDecals;
    private String soundEnvironment = "";
    private boolean generateConvexHull;
    private boolean rayTracing = true;
    private String physicalMaterial = "";
    private boolean elevator;
    private boolean generateBoundingBoxHull;

    public boolean isCharacterCollisions() {
        return characterCollisions;
    }

    public void setCharacterCollisions(boolean characterCollisions) {
        this.characterCollisions = characterCollisions;
    }

    public boolean isUseLightmaps() {
        return useLightmaps;
    }

    public void setUseLightmaps(boolean useLightmaps) {
        this.useLightmaps = useLightmaps;
    }

    public boolean isPointlightsAffect() {
        return pointlightsAffect;
    }

    public void setPointlightsAffect(boolean pointlightsAffect) {
        this.pointlightsAffect = pointlightsAffect;
    }

    public boolean isFsmContinuousUpdate() {
        return fsmContinuousUpdate;
    }

    public void setFsmContinuousUpdate(boolean fsmContinuousUpdate) {
        this.fsmContinuousUpdate = fsmContinuousUpdate;
    }

    public boolean isBulletCollisions() {
        return bulletCollisions;
    }

    public void setBulletCollisions(boolean bulletCollisions) {
        this.bulletCollisions = bulletCollisions;
    }

    public boolean isCastNoShadows() {
        return castNoShadows;
    }

    public void setCastNoShadows(boolean castNoShadows) {
        this.castNoShadows = castNoShadows;
    }

    public boolean isCollisions() {
        return collisions;
    }

    public void setCollisions(boolean collisions) {
        this.collisions = collisions;
    }

    public boolean isBlockExplosions() {
        return blockExplosions;
    }

    public void setBlockExplosions(boolean blockExplosions) {
        this.blockExplosions = blockExplosions;
    }

    public boolean isDoNotRender() {
        return doNotRender;
    }

    public void setDoNotRender(boolean doNotRender) {
        this.doNotRender = doNotRender;
    }

    public boolean isNoDecals() {
        return noDecals;
    }

    public void setNoDecals(boolean noDecals) {
        this.noDecals = noDecals;
    }

    public String getSoundEnvironment() {
        return soundEnvironment;
    }

    public void setSoundEnvironment(String soundEnvironment) {
        this.soundEnvironment = soundEnvironment;
    }

    public boolean isGenerateConvexHull() {
        return generateConvexHull;
    }

    public void setGenerateConvexHull(boolean generateConvexHull) {
        this.generateConvexHull = generateConvexHull;
    }

    public boolean isRayTracing() {
        return rayTracing;
    }

    public void setRayTracing(boolean rayTracing) {
        this.rayTracing = rayTracing;
    }

    public String getPhysicalMaterial() {
        return physicalMaterial;
    }

    public void setPhysicalMaterial(String physicalMaterial) {
        this.physicalMaterial = physicalMaterial;
    }

    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    public boolean isGenerateBoundingBoxHull() {
        return generateBoundingBoxHull;
    }

    public void setGenerateBoundingBoxHull(boolean generateBoundingBoxHull) {
        this.generateBoundingBoxHull = generateBoundingBoxHull;
    }
}
