package com.artkuznet.converter.ldb.trigger;

import com.artkuznet.converter.ldb.property.EntityProperties;

public class Trigger {
    private String sharedName;
    private EntityProperties properties;
    private float radius;
    private int type; // 0 - action_button 3 - character_collide 4 - look_at_trigger 1 - player_collide 2 - projectile_collide

    public String getTypeString() {
        switch (type) {
            case 0:
                return "Action button";
            case 1:
                return "Player collision";
            case 2:
                return "Projectile collision";
            case 3:
                return "Character collision";
            case 4:
                return "Look-at";
            default:
                throw new RuntimeException("Unknown trigger type");
        }
    }

    public Trigger(
            String sharedName,
            EntityProperties properties,
            float radius,
            int type
    ) {
        this.sharedName = sharedName;
        this.properties = properties;
        this.radius = radius;
        this.type = type;
    }

    public String getSharedName() {
        return sharedName;
    }

    public String getShortName() {
        String name = sharedName.substring(sharedName.lastIndexOf("::") + 2);
        name = name.substring(0, name.length() - 8);

        if (name.length() > Byte.MAX_VALUE) {
            throw new RuntimeException(name);
        }

        return name;
    }

    public String getRoomName() {
        return sharedName.substring(0, sharedName.substring(2).indexOf("::") + 2);
    }

    public EntityProperties getProperties() {
        return properties;
    }

    public float getRadius() {
        return radius;
    }

    public int getType() {
        return type;
    }
}
