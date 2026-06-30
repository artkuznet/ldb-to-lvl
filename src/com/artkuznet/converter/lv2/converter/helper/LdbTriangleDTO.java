package com.artkuznet.converter.lv2.converter.helper;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.maxed2.material.MaterialType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// todo refactor
public class LdbTriangleDTO {

    private final List<Vector3D> vertices;

    private Vector3D normal;

    private int materialId = 0;

    private int materialTypeId = MaterialType.DEFAULT.ordinal();

    //    protected List<VertexUV> uv = new ArrayList<>();
    protected List<VertexUV> uv;// = Arrays.asList(new VertexUV(0, 0), new VertexUV(1, 0), new VertexUV(0, 1));


    private final double area;

    public double getArea() {
        return area;
    }

    public LdbTriangleDTO(List<Vector3D> vertices) {
        if (vertices.size() != 3) {
            throw new RuntimeException();
        }
        this.vertices = vertices.stream().map(Vector3D::clone).collect(Collectors.toList());
        this.normal = Vector3D.calculateNormal(this.vertices);

        computeWorldAlignedUV();

        Vector3D v1 = this.vertices.get(0).clone();
        Vector3D v2 = this.vertices.get(1).clone();
        Vector3D v3 = this.vertices.get(2).clone();

        double a = v2.clone().minus(v1).magnitude();
        double b = v3.clone().minus(v2).magnitude();
        double c = v1.clone().minus(v3).magnitude();

        double p = (a + b + c) / 2.0;

        this.area = Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    public LdbTriangleDTO rotate(double[][] matrix) {
        vertices.forEach(v -> v.rotate(matrix));
        normal = Vector3D.calculateNormal(vertices);

        return this;
    }

    public LdbTriangleDTO minus(Vector3D vector) {
        vertices.forEach(v -> v.minus(vector));
        return this;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public void setUv(List<VertexUV> uv) {
        if (uv.size() != 3) {
            throw new RuntimeException();
        }
        this.uv = uv;
    }

    private void computeWorldAlignedUV() {
        Vector3D v0 = vertices.get(0);
        Vector3D v1 = vertices.get(1);
        Vector3D v2 = vertices.get(2);

        Vector3D e1 = v1.clone().minus(v0);
        Vector3D e2 = v2.clone().minus(v0);

        Vector3D normal = e1.cross(e2).normalize();

        double ax = Math.abs(normal.getX());
        double ay = Math.abs(normal.getY());
        double az = Math.abs(normal.getZ());

        uv = new ArrayList<>(3);

        if (ay >= ax && ay >= az) {
            for (Vector3D v : vertices) {
                uv.add(new VertexUV(
                        (float) v.getX(),
                        (float) v.getZ()
                ));
            }
        } else if (ax >= ay && ax >= az) {
            for (Vector3D v : vertices) {
                uv.add(new VertexUV(
                        (float) v.getZ(),
                        (float) v.getY()
                ));
            }
        } else {
            for (Vector3D v : vertices) {
                uv.add(new VertexUV(
                        (float) v.getX(),
                        (float) v.getY()
                ));
            }
        }
    }

    public int getMaterialId() {
        return materialId;
    }

    public List<Vector3D> getVertices() {
        return vertices;
    }

    public Vector3D getNormal() {
        return normal;
    }

    public List<VertexUV> getUv() {
        return uv;
    }

    public int getMaterialTypeId() {
        return materialTypeId;
    }

    public void setMaterialTypeId(int materialTypeId) {
        this.materialTypeId = materialTypeId;
    }
}
