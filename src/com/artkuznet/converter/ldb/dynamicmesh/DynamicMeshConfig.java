package com.artkuznet.converter.ldb.dynamicmesh;

public class DynamicMeshConfig {
    private int dynamicCollisions;
    private int bulletCollisions;
    private int lightMapped;
    private int contUpdate;
    private int pointlightAffected;
    private int blockExplosions;

    public DynamicMeshConfig(
            int dynamicCollisions,
            int bulletCollisions,
            int lightMapped,
            int contUpdate,
            int pointlightAffected,
            int blockExplosions
    ) {
        this.dynamicCollisions = dynamicCollisions;
        this.bulletCollisions = bulletCollisions;
        this.lightMapped = lightMapped;
        this.contUpdate = contUpdate;
        this.pointlightAffected = pointlightAffected;
        this.blockExplosions = blockExplosions;
    }

    public int getDynamicCollisions() {
        return dynamicCollisions;
    }

    public int getBulletCollisions() {
        return bulletCollisions;
    }

    public int getLightMapped() {
        return lightMapped;
    }

    public int getContUpdate() {
        return contUpdate;
    }

    public int getPointlightAffected() {
        return pointlightAffected;
    }

    public int getBlockExplosions() {
        return blockExplosions;
    }
}
