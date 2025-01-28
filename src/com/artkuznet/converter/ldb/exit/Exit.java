package com.artkuznet.converter.ldb.exit;

import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexContainer;

public class Exit {
    private String exitName;
    private VertexContainer vertices;
    private Vertex normal;
    private float[][] transform;
    private int roomId;
    private int parentRoomId;
    private String parentRoomName;

    public Exit(
            String exitName,
            VertexContainer vertices,
            Vertex normal,
            float[][] transform,
            int roomId,
            int parentRoomId,
            String parentRoomName
    ) {
        this.exitName = exitName;
        this.vertices = vertices;
        this.normal = normal;
        this.transform = transform;
        this.roomId = roomId;
        this.parentRoomId = parentRoomId;
        this.parentRoomName = parentRoomName;
    }

    public String getExitName() {
        return exitName;
    }

    public String getShortName() {
        return exitName.substring(exitName.lastIndexOf("::") + 2);
    }

    public VertexContainer getVertices() {
        return vertices;
    }

    public Vertex getNormal() {
        return normal;
    }

    public String getParentRoomName() {
        return parentRoomName;
    }
}
