package com.artkuznet.converter;

import com.artkuznet.converter.ldb.vertex.VertexUV;

import java.util.List;

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

    public Vector3D softSmooth() {
        this.x = (double) Math.round(this.x * 10000000d) / 10000000d;
        this.y = (double) Math.round(this.y * 10000000d) / 10000000d;
        this.z = (double) Math.round(this.z * 10000000d) / 10000000d;

        return this;
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

    public Vector3D rotateAxis(double angle, Vector3D axis) {
        var u = axis.clone().normalize();
        var o = angle * Math.PI / 180.;

        var m00 = Math.cos(o) + u.x * u.x * (1. - Math.cos(o));
        var m01 = u.x * u.y * (1. - Math.cos(o)) - u.z * Math.sin(o);
        var m02 = u.x * u.z * (1. - Math.cos(o)) + u.y * Math.sin(o);

        var m10 = u.y * u.x * (1. - Math.cos(o)) + u.z * Math.sin(o);
        var m11 = Math.cos(o) + u.y * u.y * (1. - Math.cos(o));
        var m12 = u.y * u.z * (1. - Math.cos(o)) - u.x * Math.sin(o);

        var m20 = u.z * u.x * (1. - Math.cos(o)) - u.y * Math.sin(o);
        var m21 = u.z * u.y * (1. - Math.cos(o)) + u.x * Math.sin(o);
        var m22 = Math.cos(o) + u.z * u.z * (1. - Math.cos(o));

        return rotate(new double[][]{
                new double[]{m00, m01, m02},
                new double[]{m10, m11, m12},
                new double[]{m20, m21, m22},
        });
    }

    public Vector3D rotateX(double angle) {
        var rad = angle * Math.PI / 180.0;

        return rotate(new double[][]{
                new double[]{1, 0, 0},
                new double[]{0, Math.cos(rad), -Math.sin(rad)},
                new double[]{0, Math.sin(rad), Math.cos(rad)},
        });
    }

    public Vector3D rotateY(double angle) {
        var rad = angle * Math.PI / 180.0;

        return rotate(new double[][]{
                new double[]{Math.cos(rad), 0, Math.sin(rad)},
                new double[]{0, 1, 0},
                new double[]{-Math.sin(rad), 0, Math.cos(rad)},
        });
    }

    public Vector3D rotateZ(double angle) {
        var rad = angle * Math.PI / 180.0;

        return rotate(new double[][]{
                new double[]{Math.cos(rad), -Math.sin(rad), 0},
                new double[]{Math.sin(rad), Math.cos(rad), 0},
                new double[]{0, 0, 1},
        });
    }

    public Vector3D multiply(double k) {
        x *= k;
        y *= k;
        z *= k;

        return this;
    }

    public Vector3D multiply(Vector3D p) {
        var i = this.y * p.z - this.z * p.y;
        var j = this.x * p.z - this.z * p.x;
        var k = this.x * p.y - this.y * p.x;

        return new Vector3D(i, -j, k);
    }

    public double angle(Vector3D point) {
        var ab = x * point.x + y * point.y + z * point.z;

        return Math.acos(ab / (this.magnitude() * point.magnitude())) * 180.0 / Math.PI;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3D minus(Vector3D point) {
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
        final long prime = 31;
        long result = 1;

        result = prime * result + Double.doubleToLongBits(x);
        result = prime * result + Double.doubleToLongBits(y);
        result = prime * result + Double.doubleToLongBits(z);

        return (int) result;
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

        Vector3D other = (Vector3D) obj;

        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
            return false;
        }

        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
            return false;
        }

        if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z)) {
            return false;
        }

        return true;
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
        var u = magnitude();

        return new Vector3D(this.x / u, this.y / u, this.z / u);
    }

    public static Vector3D P(Vector3D n1, Vector3D n2) {
        var p = new Vector3D(n1.y * n2.z - n1.z * n2.y, n1.z * n2.x - n1.x * n2.z, n1.x * n2.y - n1.y * n2.x);

        return p.clone().softSmooth().equals(new Vector3D(0, 0, 0)) ? new Vector3D(-1, 0, 0) : p;
    }

    public static Vector3D center(List<Vector3D> points) {
        var x = points.stream().map(Vector3D::getX).reduce(Double::sum).orElseThrow();
        var y = points.stream().map(Vector3D::getY).reduce(Double::sum).orElseThrow();
        var z = points.stream().map(Vector3D::getZ).reduce(Double::sum).orElseThrow();

        return new Vector3D(x / points.size(), y / points.size(), z / points.size());
    }

    public static Vector3D calculateNormal(Vector3D p1, Vector3D p2, Vector3D p3) {
        var v1 = p2.clone().minus(p1.clone());
        var v2 = p3.clone().minus(p1.clone());

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
            var triangle = List.of(points.get(0).clone(), points.get(1).clone(), points.get(i).clone());
            var testNormal = calculateNormal(triangle);
            if (!Double.isNaN(testNormal.x) && !Double.isNaN(testNormal.y) && !Double.isNaN(testNormal.z)) {
                return triangle;
            }
        }

        return points;
    }

    public static List<Vector3D> moveCenter(List<Vector3D> points) {
        var center = center(points);

        return points.stream().map(p -> p.clone().minus(center)).toList();
    }

    public static double sizeX(List<Vector3D> points) {
        var min = points.stream().map(Vector3D::getX).min(Double::compareTo).orElseThrow();
        var max = points.stream().map(Vector3D::getX).max(Double::compareTo).orElseThrow();

        return max - min;
    }

    public static double sizeY(List<Vector3D> points) {
        var min = points.stream().map(Vector3D::getY).min(Double::compareTo).orElseThrow();
        var max = points.stream().map(Vector3D::getY).max(Double::compareTo).orElseThrow();

        return max - min;
    }

    public static boolean roughEquals(Vector3D p1, Vector3D p2) {
        var dx = Math.abs(p1.x - p2.x);
        var dy = Math.abs(p1.y - p2.y);
        var dz = Math.abs(p1.z - p2.z);

        return dx < 0.001 && dy < 0.001 && dz < 0.001;
    }
}
