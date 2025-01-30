package com.artkuznet.converter.ldb2.aabb;

import com.artkuznet.converter.ldb.vertex.Vertex;

public class AABB {
    private Vertex minPoint;
    private Vertex maxPoint;
    private Vertex pivotPoint;

    public AABB(Vertex minPoint, Vertex maxPoint, Vertex pivotPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        this.pivotPoint = pivotPoint;
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

    public Vertex getPivotPoint() {
        return pivotPoint;
    }

    public void setPivotPoint(Vertex pivotPoint) {
        this.pivotPoint = pivotPoint;
    }
}
