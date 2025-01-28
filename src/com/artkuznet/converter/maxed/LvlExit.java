package com.artkuznet.converter.maxed;

import com.artkuznet.converter.Vector3D;

public class LvlExit extends LvlPolygon {

    public String exitName;
    public String linkedExitName;

    public LvlExit(
            Edge[] edges,
            String materialName,
            Vector3D normal,
            String exitName,
            String linkedExitName
    ) {
        super(edges, materialName, "", normal);
        this.exitName = exitName;
        this.linkedExitName = linkedExitName;
    }
}
