package com.artkuznet.converter.lv2.converter.helper;

import com.artkuznet.converter.Vector3D;

import java.util.List;

public class LdbTrianglePortalDTO extends LdbTriangleDTO {
    public String getPortalName() {
        return portalName;
    }

    public String getLinkedPortalName() {
        return linkedPortalName;
    }

    private String portalName;
    private String linkedPortalName;

    public LdbTrianglePortalDTO(List<Vector3D> vertices, String portalName, String linkedPortalName) {
        super(vertices);
        this.portalName = portalName;
        this.linkedPortalName = linkedPortalName;
//        this.uv = Arrays.asList(new VertexUV(0, 0), new VertexUV(1, 0), new VertexUV(0, 1));
    }
}
