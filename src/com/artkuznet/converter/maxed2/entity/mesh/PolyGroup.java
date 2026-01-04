package com.artkuznet.converter.maxed2.entity.mesh;

import com.artkuznet.converter.maxed2.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class PolyGroup extends Entity {

    private List<Integer> polygonIndices = new ArrayList<>();
    private boolean smoothLightmaps;
    private boolean smoothGeometry;
    private float maxAngle;
    private float maxEdge;
    private boolean freezeLightmaps;
    private boolean rayTracing;

    public void addPolygonIndex(int index) {
        this.polygonIndices.add(index);
    }

    public void setPolygonIndices(List<Integer> indices) {
        this.polygonIndices = indices;
    }

    public void setSmoothLightmaps(boolean smoothLightmaps) {
        this.smoothLightmaps = smoothLightmaps;
    }

    public void setSmoothGeometry(boolean smoothGeometry) {
        this.smoothGeometry = smoothGeometry;
    }

    public void setMaxAngle(float maxAngle) {
        this.maxAngle = maxAngle;
    }

    public void setMaxEdge(float maxEdge) {
        this.maxEdge = maxEdge;
    }

    public void setFreezeLightmaps(boolean freezeLightmaps) {
        this.freezeLightmaps = freezeLightmaps;
    }

    public void setRayTracing(boolean rayTracing) {
        this.rayTracing = rayTracing;
    }

    public List<Integer> getPolygonIndices() {
        return polygonIndices;
    }

    public boolean isSmoothLightmaps() {
        return smoothLightmaps;
    }

    public boolean isSmoothGeometry() {
        return smoothGeometry;
    }

    public float getMaxAngle() {
        return maxAngle;
    }

    public float getMaxEdge() {
        return maxEdge;
    }

    public boolean isFreezeLightmaps() {
        return freezeLightmaps;
    }

    public boolean isRayTracing() {
        return rayTracing;
    }
}
