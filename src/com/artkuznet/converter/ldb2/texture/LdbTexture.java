package com.artkuznet.converter.ldb2.texture;

import com.artkuznet.converter.util.ResourceUtils;

public class LdbTexture {
    private String filePath;
    private int fileType;
    private byte[] data;
    private int groupId;

    public LdbTexture(int groupId, String filePath, int fileType, byte[] data) {
        this.groupId = groupId;
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

    public int getGroupId() {
        return groupId;
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

    public static LdbTexture fromFile(String name) {
        try {
            return new LdbTexture(
                    -1,
                    String.format("C:\\MaxPayne2Dev\\Textures\\Indicators\\%s.dds", name),
                    5,
                    ResourceUtils.readDDS(name)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
