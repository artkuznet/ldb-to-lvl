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

    private List<VertexUV> uv = new ArrayList<>();

    public String portalName;
    public String linkedPortalName;

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
