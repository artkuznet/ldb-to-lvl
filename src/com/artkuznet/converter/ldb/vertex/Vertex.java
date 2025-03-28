package com.artkuznet.converter.ldb.vertex;

import com.artkuznet.converter.Vector3D;

import java.util.Objects;

public class Vertex {

    private float x;
    private float y;
    private float z;

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(Vector3D point) {
        this.x = (float) point.getX();
        this.y = (float) point.getY();
        this.z = (float) point.getZ();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }


    @Override
    public int hashCode() {
        return Objects.hash(Math.round(x * 1e6), Math.round(y * 1e6), Math.round(z * 1e6));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex that = (Vertex) o;
        return Math.abs(that.x - x) < 1e-6 &&
                Math.abs(that.y - y) < 1e-6 &&
                Math.abs(that.z - z) < 1e-6;
    }
}
