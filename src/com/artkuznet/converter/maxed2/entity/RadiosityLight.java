package com.artkuznet.converter.maxed2.entity;

public class RadiosityLight extends Entity {

    public RadiosityLight() {
        this.name = "RadiosityLight";
        this.excludeFromGame = true;
    }

    public RadiosityLight(int r, int g, int b, float intensity, float hotspotAngle, float falloffAngle) {
        this();

        this.color = new Color(r, g, b, 255);
        this.intensity = intensity;
        this.hotspotAngle = hotspotAngle;
        this.falloffAngle = falloffAngle;
    }

    public Color getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }

    public float getHotspotAngle() {
        return hotspotAngle;
    }

    public float getFalloffAngle() {
        return falloffAngle;
    }

    public int getRadiosityLightIndex() {
        return radiosityLightIndex;
    }

    public static class Color {

        private int R;
        private int G;
        private int B;
        private int A;

        public Color(int r, int g, int b, int a) {
            R = r;
            G = g;
            B = b;
            A = a;
        }

        public int getR() {
            return R;
        }

        public int getG() {
            return G;
        }

        public int getB() {
            return B;
        }

        public int getA() {
            return A;
        }
    }

    private Color color;
    private float intensity;
    private float hotspotAngle;
    private float falloffAngle;

    private int radiosityLightIndex = 0;

    public void setColor(Color color) {
        this.color = color;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public void setHotspotAngle(float hotspotAngle) {
        this.hotspotAngle = hotspotAngle;
    }

    public void setFalloffAngle(float falloffAngle) {
        this.falloffAngle = falloffAngle;
    }

    public void setRadiosityLightIndex(int radiosityLightIndex) {
        this.radiosityLightIndex = radiosityLightIndex;
    }
}
