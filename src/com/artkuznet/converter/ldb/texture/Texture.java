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

    public String getFilePath() {
        return filePath;
    }
}
