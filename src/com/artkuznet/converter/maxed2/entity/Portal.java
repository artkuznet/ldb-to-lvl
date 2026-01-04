package com.artkuznet.converter.maxed2.entity;

public class Portal extends Entity {

    private int polygonIndex;

    private boolean ignoreInGISLighting;

    private boolean alwaysClosed;

    public void setPolygonIndex(int polygonIndex) {
        this.polygonIndex = polygonIndex;
    }

    public void setIgnoreInGISLighting(boolean ignoreInGISLighting) {
        this.ignoreInGISLighting = ignoreInGISLighting;
    }

    public void setAlwaysClosed(boolean alwaysClosed) {
        this.alwaysClosed = alwaysClosed;
    }

    public int getPolygonIndex() {
        return polygonIndex;
    }

    public boolean isIgnoreInGISLighting() {
        return ignoreInGISLighting;
    }

    public boolean isAlwaysClosed() {
        return alwaysClosed;
    }
}
