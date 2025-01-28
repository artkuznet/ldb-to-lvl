package com.artkuznet.converter.ldb.texture;

public class Texture {

    private String filePath;
    private int fileType;
    private byte[] data;

    public Texture(final String filePath, final int fileType, final byte[] data) {
        this.filePath = filePath;
        this.fileType = fileType;
        this.data = data;
    }

    public int getFileType() {
        return fileType;
    }

    public byte[] getData() {
        return data;
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

    public String getFilePath() {
        return filePath;
    }
}
