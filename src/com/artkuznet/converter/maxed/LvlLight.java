package com.artkuznet.converter.maxed;

public class LvlLight extends MaxObject implements PointObject {

    private float r;

    private float g;

    private float b;

    private float a;

    private float intensity;

    private float falloff;

    @Override
    public double[] getPosition() {
        return new double[]{0, 0, 0, 0, 0, 0, getRadius()};
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

    public void setIntensity(final float intensity) {
        this.intensity = intensity;
    }

    public void setFalloff(final float falloff) {
        this.falloff = falloff;
    }

    public float getIntensity() {
        return intensity;
    }

    public float getFalloff() {
        return falloff;
    }

    public static int getObjectType() {
        return 10;
    }

    public void setR(final float r) {
        this.r = r;
    }

    public void setG(final float g) {
        this.g = g;
    }

    public void setB(final float b) {
        this.b = b;
    }

    public void setA(final float a) {
        this.a = a;
    }

    @Override
    public String getType() {
        return "DynamicPointlight";
    }
}
