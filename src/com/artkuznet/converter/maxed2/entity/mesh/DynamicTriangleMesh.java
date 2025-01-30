package com.artkuznet.converter.maxed2.entity.mesh;

import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.maxed2.entity.fsm.FSM;
import com.artkuznet.converter.maxed2.entity.fsm.FsmData;

import java.util.ArrayList;
import java.util.List;

public class DynamicTriangleMesh extends TriangleMesh implements FSM {

    public static class UnkBlock1 {
        public double[] d1;
        public int[] i1;
        public double[] d2;
    }

    public static class UnkBlock2 {
        public int[] vIndices;

        public VertexUV[] uv;

        public String unkS1;

        public String unkS2;

        public int unk11;
    }

    public List<UnkBlock1> unkBlock1List = new ArrayList<>();
    public List<UnkBlock2> unkBlock2List = new ArrayList<>();

    public int unk4;

    protected FsmData fsmData;

    private MeshProperties properties;

    private String triangleMeshSourceFile;
    private String triangleMeshSourceName;

    private boolean hasDynamic;

    private Dynamic dynamicData;

    @Override
    public void setFsmData(FsmData data) {
        this.fsmData = data;
    }

    @Override
    public FsmData getFsmData() {
        return fsmData;
    }

    public MeshProperties getProperties() {
        return properties;
    }

    public void setProperties(MeshProperties properties) {
        this.properties = properties;
    }

    public String getTriangleMeshSourceFile() {
        return triangleMeshSourceFile;
    }

    public void setTriangleMeshSourceFile(String triangleMeshSourceFile) {
        this.triangleMeshSourceFile = triangleMeshSourceFile;
    }

    public String getTriangleMeshSourceName() {
        return triangleMeshSourceName;
    }

    public void setTriangleMeshSourceName(String triangleMeshSourceName) {
        this.triangleMeshSourceName = triangleMeshSourceName;
    }

    public boolean isHasDynamic() {
        return hasDynamic;
    }

    public void setHasDynamic(boolean hasDynamic) {
        if (!hasDynamic) {
            throw new RuntimeException();
        }

        this.hasDynamic = hasDynamic;
    }

    public Dynamic getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(Dynamic dynamicData) {
        this.dynamicData = dynamicData;
    }
}
