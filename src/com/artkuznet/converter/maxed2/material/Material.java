package com.artkuznet.converter.maxed2.material;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Material {

    private String categoryName = MaterialType.DEFAULT.toString().toLowerCase();

    private String name = "";
    private List<String> diffuseTextureNames = Collections.singletonList("");
    private String lightLayerTextureName = "";
    private String detailTextureName = "";
    private boolean dualsided = false;
    private int referenceValue = 240;
    private boolean adultContent = false;
    private int blendMode = 0; // 0 - normal, 1 - alpha compare, 2 - alpha blend, 3 - addictive, 4 - with detail texture
    private boolean edgeBlend = false;
    private boolean reflection = false;
    private boolean gloss = false;
    private String reflectionTextureName = "";
    private String glossTextureName = "";
    private int uiVisibleFrame = 0;
    private int framerate = 1;

    private List<float[]> unkFloats = Arrays.asList(
            new float[]{0, 1.875f},
            new float[]{0, 1.875f},
            new float[]{0, 1.875f},
            new float[]{0, 1.875f},
            new float[]{0, 1.875f}
    );

    @Override
    public int hashCode() {
        return name.hashCode() + categoryName.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Material)) {
            return false;
        }

        Material that = (Material) object;

        if (!that.name.equals(this.name) || !that.categoryName.equals(this.categoryName)) {
            return false;
        }

        return true;
    }

    public Material() {

    }

    public Material(String diffuseTextureName) {
        this.name = diffuseTextureName;
        this.diffuseTextureNames = Collections.singletonList(diffuseTextureName);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDiffuseTextureNames(List<String> diffuseTextureNames) {
        this.diffuseTextureNames = diffuseTextureNames;
    }

    public void setLightLayerTextureName(String lightLayerTextureName) {
        this.lightLayerTextureName = lightLayerTextureName;
    }

    public void setDetailTextureName(String detailTextureName) {
        this.detailTextureName = detailTextureName;
    }

    public void setDualsided(boolean dualsided) {
        this.dualsided = dualsided;
    }

    public void setReferenceValue(int referenceValue) {
        this.referenceValue = referenceValue;
    }

    public void setAdultContent(boolean adultContent) {
        this.adultContent = adultContent;
    }

    public void setBlendMode(int blendMode) {
        this.blendMode = blendMode;
    }

    public void setEdgeBlend(boolean edgeBlend) {
        this.edgeBlend = edgeBlend;
    }

    public void setReflection(boolean reflection) {
        this.reflection = reflection;
    }

    public void setGloss(boolean gloss) {
        this.gloss = gloss;
    }

    public void setReflectionTextureName(String reflectionTextureName) {
        this.reflectionTextureName = reflectionTextureName;
    }

    public void setGlossTextureName(String glossTextureName) {
        this.glossTextureName = glossTextureName;
    }

    public void setUiVisibleFrame(int uiVisibleFrame) {
        this.uiVisibleFrame = uiVisibleFrame;
    }

    public void setFramerate(int framerate) {
        this.framerate = framerate;
    }

    public String getName() {
        return name;
    }

    public List<String> getDiffuseTextureNames() {
        return diffuseTextureNames;
    }

    public String getLightLayerTextureName() {
        return lightLayerTextureName;
    }

    public String getDetailTextureName() {
        return detailTextureName;
    }

    public boolean isDualsided() {
        return dualsided;
    }

    public int getReferenceValue() {
        return referenceValue;
    }

    public boolean isAdultContent() {
        return adultContent;
    }

    public int getBlendMode() {
        return blendMode;
    }

    public boolean isEdgeBlend() {
        return edgeBlend;
    }

    public boolean isReflection() {
        return reflection;
    }

    public boolean isGloss() {
        return gloss;
    }

    public String getReflectionTextureName() {
        return reflectionTextureName;
    }

    public String getGlossTextureName() {
        return glossTextureName;
    }

    public int getUiVisibleFrame() {
        return uiVisibleFrame;
    }

    public int getFramerate() {
        return framerate;
    }

    public List<float[]> getUnkFloats() {
        return unkFloats;
    }

    public void setUnkFloats(List<float[]> unkFloats) {
        this.unkFloats = unkFloats;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
