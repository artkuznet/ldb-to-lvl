package com.artkuznet.converter;

import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexUV;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Vector3D {

    private double x;
    private double y;
    private double z;

    public Vector3D(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(VertexUV vertexUV) {
        this.x = vertexUV.getU();
        this.y = vertexUV.getV();
        this.z = 0;
    }

    public Vector3D(Vertex vertex) {
        this.x = vertex.getX();
        this.y = vertex.getY();
        this.z = vertex.getZ();
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Vector3D hardSmooth() {
        this.x = (double) Math.round(this.x * 100d) / 100d;
        this.y = (double) Math.round(this.y * 100d) / 100d;
        this.z = (double) Math.round(this.z * 100d) / 100d;

        return this;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vector3D multiply(double k) {
        x *= k;
        y *= k;
        z *= k;

        return this;
    }

    public Vector3D multiply(final Vector3D p) {
        double i = this.y * p.z - this.z * p.y;
        double j = this.x * p.z - this.z * p.x;
        double k = this.x * p.y - this.y * p.x;

        return new Vector3D(i, -j, k);
    }

    public Vector3D cross(Vector3D o) {
        return new Vector3D(
                y * o.z - z * o.y,
                z * o.x - x * o.z,
                x * o.y - y * o.x
        );
    }

    public double dot(Vector3D o) {
        return x * o.x + y * o.y + z * o.z;
    }

    public double angle(final Vector3D point) {
        double ab = x * point.x + y * point.y + z * point.z;

        return Math.acos(ab / (this.magnitude() * point.magnitude())) * 180.0 / Math.PI;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double length() {
        return Math.sqrt(dot(this));
    }

    public Vector3D minus(final Vector3D point) {
        x -= point.x;
        y -= point.y;
        z -= point.z;

        return this;
    }

    @Override
    public Vector3D clone() {
        return new Vector3D(x, y, z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(x * 1e6), Math.round(y * 1e6), Math.round(z * 1e6));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3D that = (Vector3D) o;
        return Math.abs(that.x - x) < 1e-6 &&
                Math.abs(that.y - y) < 1e-6 &&
                Math.abs(that.z - z) < 1e-6;
    }

    public Vector3D rotate(double[][] rotateMatrix) {
        double[][] a = transformMatrix();
        double[][] b = rotateMatrix;

        x = a[3][0] * b[0][0] + a[3][1] * b[1][0] + a[3][2] * b[2][0];
        y = a[3][0] * b[0][1] + a[3][1] * b[1][1] + a[3][2] * b[2][1];
        z = a[3][0] * b[0][2] + a[3][1] * b[1][2] + a[3][2] * b[2][2];

        return this;
    }

    private double[][] transformMatrix() {
        return new double[][]{
                new double[]{1, 0, 0},
                new double[]{0, 1, 0},
                new double[]{0, 0, 1},
                new double[]{x, y, z},
        };
    }

    public Vector3D normalize() {
        double u = magnitude();

        return new Vector3D(this.x / u, this.y / u, this.z / u);
    }

    public static Vector3D calculateNormal(Vector3D p1, Vector3D p2, Vector3D p3) {
        Vector3D v1 = p2.clone().minus(p1.clone());
        Vector3D v2 = p3.clone().minus(p1.clone());

        return v1.multiply(v2).normalize();
    }

    public static Vector3D calculateNormal(List<Vector3D> points) {
        if (points.size() != 3) {
            throw new RuntimeException("wrong triangle size");
        }

        return calculateNormal(points.get(0), points.get(1), points.get(2));
    }

    public static List<Vector3D> findTriangle(List<Vector3D> points) {
        for (int i = 2; i < points.size(); i++) {
            List<Vector3D> triangle = Arrays.asList(points.get(0).clone(), points.get(1).clone(), points.get(i).clone());
            Vector3D testNormal = calculateNormal(triangle);
            if (!Double.isNaN(testNormal.x) && !Double.isNaN(testNormal.y) && !Double.isNaN(testNormal.z)) {
                return triangle;
            }
        }

        return points;
    }

    public static double size(Vector3D vMin, Vector3D vMax) {
        return vMax.clone().minus(vMin).magnitude();
    }

    public static int compare(Vector3D v1, Vector3D v2) {
        if (v1.getX() != v2.getX()) return Double.compare(v1.getX(), v2.getX());
        if (v1.getY() != v2.getY()) return Double.compare(v1.getY(), v2.getY());
        return Double.compare(v1.getZ(), v2.getZ());
    }

    public double dotProduct(Vector3D other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public double get(int coord) {
        switch (coord) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
            default:
                return 0;
        }
    }
}
