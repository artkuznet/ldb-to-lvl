package com.artkuznet.converter.ldb2.volumelight;

import com.artkuznet.converter.ldb.vertex.Vertex;

public class VolumeLightAABB {
    private Vertex minPoint;
    private Vertex maxPoint;

    public VolumeLightAABB(Vertex minPoint, Vertex maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
    }

    public Vertex getMinPoint() {
        return minPoint;
    }

    public void setMinPoint(Vertex minPoint) {
        this.minPoint = minPoint;
    }

    public Vertex getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(Vertex maxPoint) {
        this.maxPoint = maxPoint;
    }
}
