package com.artkuznet.converter.ldb2.collistionshape;

import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.ldb2.Shape;

import java.util.Collections;
import java.util.List;

public class CollisionShape implements Shape {
    private List<Vertex> vertices;
    private List<Integer> indices;
    private List<Integer> materialIndices;
    private int isConvex;
    private List<Integer> collisionMask;
    private CollisionShapeMoppData havokMopp;

    public CollisionShape(
            List<Vertex> vertices,
            List<Integer> indices,
            List<Integer> materialIndices,
            int isConvex,
            List<Integer> collisionMask,
            CollisionShapeMoppData havokMopp
    ) {
        if (indices.size() / 3 != materialIndices.size()) {
            throw new RuntimeException();
        }

        this.vertices = vertices;
        this.indices = indices;
        this.materialIndices = materialIndices;
        this.isConvex = isConvex;
        this.collisionMask = collisionMask;
        this.havokMopp = havokMopp;
    }

    @Override
    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    @Override
    public List<Integer> getIndices() {
        return indices;
    }

    @Override
    public int getMaterialId() {
        return 0;
    }

    @Override
    public List<VertexUV> getUvs() {
        return Collections.emptyList();
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public List<Integer> getMaterialIndices() {
        return materialIndices;
    }

    public void setMaterialIndices(List<Integer> materialIndices) {
        this.materialIndices = materialIndices;
    }

    public int getIsConvex() {
        return isConvex;
    }

    public void setIsConvex(int isConvex) {
        this.isConvex = isConvex;
    }

    public List<Integer> getCollisionMask() {
        return collisionMask;
    }

    public void setCollisionMask(List<Integer> collisionMask) {
        this.collisionMask = collisionMask;
    }

    public CollisionShapeMoppData getHavokMopp() {
        return havokMopp;
    }

    public void setHavokMopp(CollisionShapeMoppData havokMopp) {
        this.havokMopp = havokMopp;
    }
}
