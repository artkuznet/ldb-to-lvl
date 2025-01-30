package com.artkuznet.converter.maxed2.entity;

public class DynamicPointlight extends Entity {

    public String getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }

    public float getFalloff() {
        return falloff;
    }

    public static class Color {
        private float R;
        private float G;
        private float B;
        private float A;

        public Color(float r, float g, float b, float a) {
            R = r;
            G = g;
            B = b;
            A = a;
        }

        public float getR() {
            return R;
        }

        public float getG() {
            return G;
        }

        public float getB() {
            return B;
        }

        public float getA() {
            return A;
        }
    }

    private String type = "Pointlight";

    private Color color;

    private float intensity = 1.0f;

    private float falloff;

    public DynamicPointlight() {
        this.radius = 0.5;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public void setFalloff(float falloff) {
        this.falloff = falloff;
    }

}
