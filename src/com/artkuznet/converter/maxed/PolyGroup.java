package com.artkuznet.converter.maxed;

public class PolyGroup {

    private String name;
    private int[] polygons;
    private boolean smoothLightMaps;
    private boolean smoothGeometry;
    private boolean freezeLightMaps;
    private float maxEdgeLength;
    private float maxAngle;

    public void setName(final String name) {
        this.name = name;
    }

    public void setPolygons(final int[] polygons) {
        this.polygons = polygons;
    }

    public void setSmoothLightMaps(final boolean smoothLightMaps) {
        this.smoothLightMaps = smoothLightMaps;
    }

    public void setSmoothGeometry(final boolean smoothGeometry) {
        this.smoothGeometry = smoothGeometry;
    }

    public void setFreezeLightMaps(final boolean freezeLightMaps) {
        this.freezeLightMaps = freezeLightMaps;
    }

    public void setMaxEdgeLength(final float maxEdgeLength) {
        this.maxEdgeLength = maxEdgeLength;
    }

    public void setMaxAngle(final float maxAngle) {
        this.maxAngle = maxAngle;
    }

    public String getName() {
        return name;
    }

    public int[] getPolygons() {
        return polygons;
    }

    public boolean isSmoothLightMaps() {
        return smoothLightMaps;
    }

    public boolean isSmoothGeometry() {
        return smoothGeometry;
    }

    public boolean isFreezeLightMaps() {
        return freezeLightMaps;
    }

    public float getMaxEdgeLength() {
        return maxEdgeLength;
    }

    public float getMaxAngle() {
        return maxAngle;
    }
}
