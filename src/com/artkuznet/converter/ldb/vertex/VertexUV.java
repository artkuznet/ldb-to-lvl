package com.artkuznet.converter.ldb.vertex;

import com.artkuznet.converter.Vector3D;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexUV that = (VertexUV) o;
        return Math.abs(that.u - u) < 1e-6 &&
                Math.abs(that.v - v) < 1e-6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(u * 1e6), Math.round(v * 1e6));
    }
}
