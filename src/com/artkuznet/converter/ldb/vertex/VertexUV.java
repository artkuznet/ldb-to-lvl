package com.artkuznet.converter.ldb.vertex;

import com.artkuznet.converter.Vector3D;

public class VertexUV {

    private float u;
    private float v;

    public VertexUV(float u, float v) {
        this.u = u;
        this.v = v;
    }

    public VertexUV(Vector3D v) {
        this.u = (float) v.getX();
        this.v = (float) v.getY();
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }
}
