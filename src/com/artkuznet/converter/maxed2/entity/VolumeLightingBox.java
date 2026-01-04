package com.artkuznet.converter.maxed2.entity;

public class VolumeLightingBox extends Entity {

    private double width;
    private double height;
    private double depth;
    private int resolution = 1;

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDepth() {
        return depth;
    }

    public int getResolution() {
        return resolution;
    }
}
