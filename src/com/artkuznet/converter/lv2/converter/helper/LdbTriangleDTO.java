package com.artkuznet.converter.lv2.converter.helper;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.maxed2.material.MaterialType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// todo refactor
public class LdbTriangleDTO {

//    public boolean isMesh() {
//        return mesh;
//    }

    public boolean isConvex() {
        return convex;
    }

//    private boolean mesh = false;

    private boolean convex = false;

//    private boolean dynamicMesh = false;

    private List<Vector3D> vertices;

    private Vector3D normal;

    private int materialId = 0;

    private int materialTypeId = MaterialType.DEFAULT.ordinal();

    public List<VertexUV> uv;

//    private Integer meshId;

//    private Integer collisionId;

    private String materialCategory = "default"; // todo?

    private boolean portal = false;

    public String portalName;
    public String linkedPortalName;

    public String getPortalName() {
        return portalName;
    }

    private double area;

    public double getArea() {
        return area;
    }

//    public boolean isDynamicMesh() {
//        return dynamicMesh;
//    }

//    public void setDynamicMesh() {
//        dynamicMesh = true;
//    }

    public void setConvex() {
        convex = true;
    }

    public void setPortal(String portalName, String linkedPortalName) {
        this.portal = true;
        this.portalName = portalName;
        this.linkedPortalName = linkedPortalName;
        this.uv = Arrays.asList(new VertexUV(0, 0), new VertexUV(1, 0), new VertexUV(0, 1));
    }

    public boolean isPortal() {
        return portal;
    }

    public LdbTriangleDTO(List<Vector3D> vertices) {
//        this.mesh = isMesh;

        if (vertices.size() != 3) {
            throw new RuntimeException();
        }
        this.vertices = vertices.stream()
                .map(Vector3D::clone)
//                    .map(Vector3D::hardSmooth)
//                    .map(Vector3D::softSmooth)
                .collect(Collectors.toList());
        this.normal = Vector3D.calculateNormal(this.vertices)/*.softSmooth()*/;


        Vector3D v1 = this.vertices.get(0).clone();
        Vector3D v2 = this.vertices.get(1).clone();
        Vector3D v3 = this.vertices.get(2).clone();

        double a = v2.clone().minus(v1).magnitude();
        double b = v3.clone().minus(v2).magnitude();
        double c = v1.clone().minus(v3).magnitude();

        double p = (a + b + c) / 2.0;

        this.area = Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

//    public void setIsMesh() {
//        this.mesh = true;
//    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public void setUv(List<VertexUV> uv) {
        if (uv.size() != 3) {
            throw new RuntimeException();
        }
        this.uv = uv
                .stream()
//                    .map(Vector3Dtest::new)
//                    .map(Vector3Dtest::hardSmooth) // todo ??
//                    .map(v -> new VertexUV((float) v.getX(), (float) v.getY()))
                .collect(Collectors.toList())
        ;
    }

//    public void setMeshId(Integer meshId) {
//        this.meshId = meshId;
//    }

//    public void setCollisionId(Integer collisionId) {
//        this.collisionId = collisionId;
//    }

    public int getMaterialId() {
        return materialId;
    }

    public int getMaterialTypeId() {
        return materialTypeId;
    }

    public void setMaterialTypeId(Integer materialTypeId) {
        this.materialCategory = materialTypeId < MaterialType.values().length
                ? MaterialType.values()[materialTypeId].toString().toLowerCase()
                : "unknown";

        this.materialTypeId = materialTypeId;
    }

//    public Integer getCollisionId() {
//        return collisionId;
//    }

    public List<Vector3D> getVertices() {
        return vertices;
    }

//    public Integer getMeshId() {
//        return meshId;
//    }

    public Vector3D getNormal() {
        return normal;
    }

    public List<VertexUV> getUv() {
        return uv;
    }

}
