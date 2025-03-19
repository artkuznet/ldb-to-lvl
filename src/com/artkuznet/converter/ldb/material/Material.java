package com.artkuznet.converter.ldb.material;

import com.artkuznet.converter.ldb.texture.Texture;

public class Material {

    private int idx;

    private int id;

    private String categoryName;

    private String materialName;

    private Texture diffuseTexture;

    private Texture alphaTexture;

    private MaterialProperties properties = new MaterialProperties();

    public Material(int idx, int id, String categoryName, String materialName) {
        this.idx = idx;
        this.id = id;
        this.categoryName = categoryName;
        this.materialName = materialName;
    }

    public int getIdx() {
        return idx;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public Texture getAlphaTexture() {
        return alphaTexture;
    }

    public Texture getDiffuseTexture() {
        return diffuseTexture;
    }

    public void setDiffuseTexture(Texture texture) {
        diffuseTexture = texture;
    }

    public void setAlphaTexture(Texture texture) {
        alphaTexture = texture;
    }

    public void setProperties(int hasAlphaTest, int hasAdultContent) {
        properties.setHasAlphaTest(hasAlphaTest);
        properties.setHasAdultContent(hasAdultContent);
    }

    public MaterialProperties getProperties() {
        return properties;
    }
}
