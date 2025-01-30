package com.artkuznet.converter.ldb2.material;

import com.artkuznet.converter.ldb2.lightmap.LightMapTexture;
import com.artkuznet.converter.ldb2.texture.LdbTexture;

import java.util.Collections;
import java.util.Objects;

public class LdbMaterial {
    private int id;
    private LdbTexture diffuseTexture;
    private LdbTexture detailTexture;
    private LdbTexture reflectionTexture;
    private LdbTexture glossTexture;
    private LightMapTexture lightmapTexture;
    private MaterialProperties properties;

    public LdbMaterial(int id) {
        this.id = id;
    }

    public void setDiffuseTexture(LdbTexture texture) {
        this.diffuseTexture = texture;
    }

    public void setDetailTexture(LdbTexture texture) {
        this.detailTexture = texture;
    }

    public void setReflectionTexture(LdbTexture texture) {
        this.reflectionTexture = texture;
    }

    public void setGlossTexture(LdbTexture texture) {
        this.glossTexture = texture;
    }

    public void setLightmapTexture(LightMapTexture texture) {
        this.lightmapTexture = texture;
    }

    public void setProperties(MaterialProperties properties) {
        this.properties = properties;
    }

    public int getId() {
        return id;
    }

    public LdbTexture getDiffuseTexture() {
        return diffuseTexture;
    }

    public LdbTexture getDetailTexture() {
        return detailTexture;
    }

    public LdbTexture getReflectionTexture() {
        return reflectionTexture;
    }

    public LdbTexture getGlossTexture() {
        return glossTexture;
    }

    public LightMapTexture getLightmapTexture() {
        return lightmapTexture;
    }

    public MaterialProperties getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LdbMaterial that = (LdbMaterial) o;

        if (!Objects.equals(this.diffuseTexture, that.diffuseTexture)) {
            return false;
        }

        // todo ?

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(detailTexture); // todo ?
    }

    public static LdbMaterial fromTexture(LdbTexture texture) {
        LdbMaterial material = new LdbMaterial(-1);

        material.setDiffuseTexture(texture);
        material.setProperties(new MaterialProperties(
                240,
                0,
                0,
                0,
                1,
                1,
                0,
                Collections.singletonList(texture),
                0
        ));

        return material;
    }
}
