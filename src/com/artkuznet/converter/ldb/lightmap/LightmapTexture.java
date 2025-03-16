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

    public byte[] getData() {
        return data;
    }

    public String getFileTypeName() {
        switch (fileType) {
            case 0:
                return "tga";
            case 2:
                return "scx";
            case 3:
                return "pcx";
            case 4:
                return "jpg";
            case 5:
                return "dds";
            default:
                throw new RuntimeException("Unknown texture file type " + fileType);
        }
    }

    public int getId() {
        return id;
    }
}
