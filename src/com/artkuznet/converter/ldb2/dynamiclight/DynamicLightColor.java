package com.artkuznet.converter.ldb2.dynamiclight;

public class DynamicLightColor {
    private float R;
    private float G;
    private float B;
    private float A;

    public DynamicLightColor(float R, float G, float B, float A) {
        this.R = R;
        this.G = G;
        this.B = B;
        this.A = A;
    }

    public float getR() {
        return R;
    }

    public void setR(float R) {
        this.R = R;
    }

    public float getG() {
        return G;
    }

    public void setG(float G) {
        this.G = G;
    }

    public float getB() {
        return B;
    }

    public void setB(float B) {
        this.B = B;
    }

    public float getA() {
        return A;
    }

    public void setA(float A) {
        this.A = A;
    }
}
