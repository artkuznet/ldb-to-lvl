package com.artkuznet.converter.ldb2.material;

import com.artkuznet.converter.ldb2.texture.LdbTexture;

import java.util.List;

public class MaterialProperties {
    // Normal - 0, AlphaBlend - 4, AlphaCompare - 1, AlphaCompareEdgeBlend - 2, Additive - 3, WithDetailTexture - 5,
    // WithReflectionTexture - 6, WithGlossTexture - 9, AlphaCompareReflectionGloss - 10,
    // AlphaCompareEdgeBlendReflectionGloss - 11 AlphaCompareEdgeBlendReflection - 8 AlphaCompareReflection - 7
    private int alphaCompareReferenceValue;
    // For Z-fighting (for instance: graffity textures)
    private int sortPriority;
    // For Z-fighting (for instance: graffity textures)
    private int detailOffset;
    private int dualSided;
    // For Z-fighting (for instance: graffity textures)
    private int writesZBuffer;
    private int framerate;
    private int visibleFrame;
    private List<LdbTexture> frames;
    private int blendMode;

    public MaterialProperties(
            int alphaCompareReferenceValue,
            int sortPriority,
            int detailOffset,
            int dualSided,
            int writesZBuffer,
            int framerate,
            int visibleFrame,
            List<LdbTexture> frames,
            int blendMode
    ) {
        this.alphaCompareReferenceValue = alphaCompareReferenceValue;
        this.sortPriority = sortPriority;
        this.detailOffset = detailOffset;
        this.dualSided = dualSided;
        this.writesZBuffer = writesZBuffer;
        this.framerate = framerate;
        this.visibleFrame = visibleFrame;
        this.frames = frames;
        this.blendMode = blendMode;
    }

    public int getAlphaCompareReferenceValue() {
        return alphaCompareReferenceValue;
    }

    public void setAlphaCompareReferenceValue(int alphaCompareReferenceValue) {
        this.alphaCompareReferenceValue = alphaCompareReferenceValue;
    }

    public int getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(int sortPriority) {
        this.sortPriority = sortPriority;
    }

    public int getDetailOffset() {
        return detailOffset;
    }

    public void setDetailOffset(int detailOffset) {
        this.detailOffset = detailOffset;
    }

    public int getDualSided() {
        return dualSided;
    }

    public void setDualSided(int dualSided) {
        this.dualSided = dualSided;
    }

    public int getWritesZBuffer() {
        return writesZBuffer;
    }

    public void setWritesZBuffer(int writesZBuffer) {
        this.writesZBuffer = writesZBuffer;
    }

    public int getFramerate() {
        return framerate;
    }

    public void setFramerate(int framerate) {
        this.framerate = framerate;
    }

    public int getVisibleFrame() {
        return visibleFrame;
    }

    public void setVisibleFrame(int visibleFrame) {
        this.visibleFrame = visibleFrame;
    }

    public List<LdbTexture> getFrames() {
        return frames;
    }

    public void setFrames(List<LdbTexture> frames) {
        this.frames = frames;
    }

    public int getBlendMode() {
        return blendMode;
    }

    public void setBlendMode(int blendMode) {
        this.blendMode = blendMode;
    }
}
