package com.artkuznet.converter.ldb.waypoint;

import com.artkuznet.converter.ldb.property.EntityProperties;

public class Waypoint {

    public String getSharedName() {
        return sharedName;
    }

    public EntityProperties getProperties() {
        return objectProperties;
    }

    public int getType() {
        return type;
    }

    public String getShortName() {
        return sharedName.substring(sharedName.lastIndexOf("::") + 2);
    }

    public String getRoomName() {
        return sharedName.substring(0, sharedName.substring(2).indexOf("::") + 2);
    }

    private String sharedName;
    private EntityProperties objectProperties;
    private int type;

    public Waypoint(
            String sharedName,
            EntityProperties entityProperties,
            int type
    ) {
        this.sharedName = sharedName;
        this.objectProperties = entityProperties;
        this.type = type;
    }
}
