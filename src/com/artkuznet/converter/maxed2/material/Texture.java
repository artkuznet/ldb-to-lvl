package com.artkuznet.converter.maxed2.material;

public class Texture {

    private String filePath;
    private int fileType;
    private byte[] data;

    public Texture(String filePath, byte[] data) {
        this.filePath = filePath;
        this.fileType = getType(data[0]);
        this.data = data;
    }

    public Texture(String filePath, int fileType, byte[] data) {
        this.filePath = filePath;
        this.fileType = fileType;
        this.data = data;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getFileType() {
        return fileType;
    }

    public byte[] getData() {
        return data;
    }

    private int getType(byte firstByte) {
        switch (firstByte) {
            case 0x00:
                return 0;
            case 0x0A:
                return 3;
            case (byte) 0xFF:
                return 4;
            default:
                throw new RuntimeException("Unsupported bitmap type");
        }
    }
}
