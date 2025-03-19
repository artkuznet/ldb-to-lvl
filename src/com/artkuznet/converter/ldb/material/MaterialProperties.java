package com.artkuznet.converter.ldb.material;

public class MaterialProperties {
    private int hasAlphaTest = 0;
    private int hasAdultContent = 0;

    public void setHasAlphaTest(int hasAlphaTest) {
        this.hasAlphaTest = hasAlphaTest;
    }

    public void setHasAdultContent(int hasAdultContent) {
        this.hasAdultContent = hasAdultContent;
    }

    public int getHasAlphaTest() {
        return hasAlphaTest;
    }

    public int getHasAdultContent() {
        return hasAdultContent;
    }
}
