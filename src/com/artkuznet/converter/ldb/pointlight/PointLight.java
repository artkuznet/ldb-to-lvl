package com.artkuznet.converter.ldb.pointlight;

import com.artkuznet.converter.ldb.property.EntityProperties;

public class PointLight {

    private int id;
    private EntityProperties entityProperties;
    private float r;
    private float g;
    private float b;
    private float a;
    private float falloff;
    private float intensity;

    public PointLight(
            int id,
            EntityProperties objectProperties,
            float r,
            float g,
            float b,
            float a,
            float falloff,
            float intensity
    ) {
        this.id = id;
        this.entityProperties = objectProperties;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.falloff = falloff;
        this.intensity = intensity;
    }

    public int getId() {
        return id;
    }

    public EntityProperties getProperties() {
        return entityProperties;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public float getA() {
        return a;
    }

    public float getFalloff() {
        return falloff;
    }

    public float getIntensity() {
        return intensity;
    }
}
