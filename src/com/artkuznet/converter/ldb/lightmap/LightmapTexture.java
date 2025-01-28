package com.artkuznet.converter.ldb.lightmap;

public class LightmapTexture {
    private int id;
    private int fileType;
    private byte[] data;

    public LightmapTexture(int id, int fileType, byte[] data) {
        this.id = id;
        this.fileType = fileType;
        this.data = data;
    }

    public String getFileTypeName() {
        return switch (fileType) {
            case 0 -> "tga";
            case 2 -> "scx";
            case 3 -> "pcx";
            case 4 -> "jpg";
            case 5 -> "dds";
            default -> throw new RuntimeException("Unknown texture file type " + fileType);
        };
    }

    public int getId() {
        return id;
    }
}
