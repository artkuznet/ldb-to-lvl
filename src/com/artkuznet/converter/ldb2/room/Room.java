package com.artkuznet.converter.ldb2.room;

import com.artkuznet.converter.ldb2.aabb.AABB;
import com.artkuznet.converter.ldb2.collistionshape.CollisionShape;
import com.artkuznet.converter.ldb2.dynamicmesh.DynamicMesh;
import com.artkuznet.converter.ldb2.staticmesh.StaticMeshContainer;
import com.artkuznet.converter.ldb2.volumelight.VolumeLight;

import java.util.List;

public class Room {
    private int id;
    private String name;
    private float[][] transform;
    private AABB aabb;
    private StaticMeshContainer staticMesh;
    private List<CollisionShape> collisions;
    private List<VolumeLight> volumeLights;
//    private List<DynamicMesh> dynamicMeshes;

    public Room(
            int id,
            String name,
            float[][] transform,
            AABB aabb,
            StaticMeshContainer staticMesh,
            List<CollisionShape> collisions,
            List<VolumeLight> volumeLights//,
//            List<DynamicMesh> dynamicMeshes
    ) {
        this.id = id;
        this.name = name;
        this.transform = transform;
        this.aabb = aabb;
        this.staticMesh = staticMesh;
        this.collisions = collisions;
        this.volumeLights = volumeLights;
//        this.dynamicMeshes = dynamicMeshes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float[][] getTransform() {
        return transform;
    }

    public void setTransform(float[][] transform) {
        this.transform = transform;
    }

    public AABB getAabb() {
        return aabb;
    }

    public void setAabb(AABB aabb) {
        this.aabb = aabb;
    }

    public StaticMeshContainer getStaticMeshes() {
        return staticMesh;
    }

    public void setStaticMesh(StaticMeshContainer staticMesh) {
        this.staticMesh = staticMesh;
    }

    public List<CollisionShape> getCollisions() {
        return collisions;
    }

    public void setCollisions(List<CollisionShape> collisions) {
        this.collisions = collisions;
    }

    public List<VolumeLight> getVolumeLights() {
        return volumeLights;
    }

    public void setVolumeLights(List<VolumeLight> volumeLights) {
        this.volumeLights = volumeLights;
    }

//    public List<DynamicMesh> getDynamicMeshes() {
//        return dynamicMeshes;
//    }

//    public void setDynamicMeshes(List<DynamicMesh> dynamicMeshes) {
//        this.dynamicMeshes = dynamicMeshes;
//    }

    public int getId() {
        return id;
    }
}