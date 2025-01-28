package com.artkuznet.converter.ldb.property;

public class EntityProperties {

    private String name;
    private float[][] objectToRoomTransform;
    private float[][] objectToParentTransform;
    private int roomId;
    private String parentDynamicMeshName;

    public EntityProperties(
            String name,
            float[][] objectToRoomTransform,
            float[][] objectToParentTransform,
            int roomId,
            String parentDynamicMeshName
    ) {
        this.name = name;
        this.objectToRoomTransform = objectToRoomTransform;
        this.objectToParentTransform = objectToParentTransform;
        this.roomId = roomId;
        this.parentDynamicMeshName = parentDynamicMeshName;
    }

    public String getName() {
        return name;
    }

    public float[][] getObjectToRoomTransform() {
        return objectToRoomTransform;
    }

    public double[][] getObjectToRoomTransformDouble() {
        double[][] t = new double[objectToRoomTransform.length][];

        for (int i = 0; i < objectToRoomTransform.length; i++) {
            t[i] = new double[objectToRoomTransform[i].length];
            for (int j = 0; j < objectToRoomTransform[i].length; j++) {
                t[i][j] = objectToRoomTransform[i][j];
            }
        }

        return t;
    }

    public float[][] getObjectToParentTransform() {
        return objectToParentTransform;
    }

    public double[][] getObjectToParentTransformDouble() {
        double[][] t = new double[objectToParentTransform.length][];

        for (int i = 0; i < objectToParentTransform.length; i++) {
            t[i] = new double[objectToParentTransform[i].length];
            for (int j = 0; j < objectToParentTransform[i].length; j++) {
                t[i][j] = objectToParentTransform[i][j];
            }
        }

        return t;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getParentDynamicMeshName() {
        return parentDynamicMeshName;
    }
}
