package com.artkuznet.converter.ldb.vertex;

import com.artkuznet.converter.Vector3D;

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
        final int prime = 31;
        int result = 1;

        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        result = prime * result + Float.floatToIntBits(z);

        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Vertex other = (Vertex) obj;

        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
            return false;
        }

        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
            return false;
        }

        if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) {
            return false;
        }

        return true;
    }
}
