package com.artkuznet.converter.maxed;

import java.util.*;

public class DynamicMesh extends Mesh implements Dynamic, FSM {

    public static class DynamicData {

        public static class DynamicAnimation {

            public static class AnimationGraph {
                public short sampleRate;

                public List<Float> points;

                public List<float[]> interpolation = new ArrayList<>();
            }

            public String name;

            public float length;

            public float[][] startTransform;
            public float[][] endTransform;

            public String startKeyframe;
            public String endKeyframe;

            public AnimationGraph position;
            public AnimationGraph rotation;

            public byte unkByte1 = -1;
            public byte unkByte2 = 0;

            public byte unkByte3 = -1;
            public byte unkByte4 = 0;
        }

        public LinkedHashMap<String, double[][]> keyframeTransforms = new LinkedHashMap<>();

        public List<DynamicAnimation> animations = new ArrayList<>();
    }

    private DynamicData dynamicData;

    private FloatingFSM.FSMData fsmData = new FloatingFSM.FSMData();

    private String defaultKeyframe = Dynamic.KEYFRAME_0;
    private String unknownKeyframe = Dynamic.KEYFRAME_0;

    @Override
    public DynamicData getDynamicData() {
        return this.dynamicData;
    }

    public void setDynamicData(DynamicData dynamicData) {
        this.dynamicData = dynamicData;
    }

    @Override
    public FloatingFSM.FSMData getFsmData() {
        return this.fsmData;
    }

    @Override
    public void setFsmData(FloatingFSM.FSMData fsmData) {
        this.fsmData = fsmData;
    }

    @Override
    public String getDefaultKeyframe() {
        return this.defaultKeyframe;
    }

    @Override
    public String getUnknownKeyframe() {
        return this.unknownKeyframe;
    }

    @Override
    public DynamicMesh optimize() {
        return (DynamicMesh) super.optimize();
    }

    @Override
    public DynamicMesh joinPolygons() {
        return (DynamicMesh) super.joinPolygons();
    }

    @Override
    public DynamicMesh buildPolyGroups() {
        return (DynamicMesh) super.buildPolyGroups();
    }
}
