package com.artkuznet.converter.ldb.texture;

import com.artkuznet.converter.ldb.vertex.VertexUV;

public class TextureVertex {
    private int vertexIdx;
    private VertexUV uv;
    private VertexUV lightmapUV;
    private int flags; // unsigned
    private int isHidden;

    public TextureVertex(
            int vertexIdx,
            VertexUV UV,
            VertexUV lightmapUV,
            int flags,
            int isHidden
    ) {
        this.vertexIdx = vertexIdx;
        this.uv = UV;
        this.lightmapUV = lightmapUV;
        this.flags = flags;
        this.isHidden = isHidden;
    }

    public int getVertexIdx() {
        return vertexIdx;
    }

    public VertexUV getUV() {
        return uv;
    }

    public VertexUV getLightmapUV() {
        return lightmapUV;
    }
}
