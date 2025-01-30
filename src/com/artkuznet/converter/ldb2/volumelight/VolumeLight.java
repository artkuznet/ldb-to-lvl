package com.artkuznet.converter.ldb2.volumelight;

import java.util.List;

public class VolumeLight {
    private int gridHeight;
    private int gridWidth;
    private int gridDepth;
    private VolumeLightAABB aabb;
    private List<VolumeLightRGB> rgb;

    public VolumeLight(int gridHeight, int gridWidth, int gridDepth, VolumeLightAABB aabb, List<VolumeLightRGB> rgb) {
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.gridDepth = gridDepth;
        this.aabb = aabb;
        this.rgb = rgb;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public int getGridDepth() {
        return gridDepth;
    }

    public void setGridDepth(int gridDepth) {
        this.gridDepth = gridDepth;
    }

    public VolumeLightAABB getAabb() {
        return aabb;
    }

    public void setAabb(VolumeLightAABB aabb) {
        this.aabb = aabb;
    }

    public List<VolumeLightRGB> getRgb() {
        return rgb;
    }

    public void setRgb(List<VolumeLightRGB> rgb) {
        this.rgb = rgb;
    }
}
