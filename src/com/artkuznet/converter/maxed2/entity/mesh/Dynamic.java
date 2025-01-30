package com.artkuznet.converter.maxed2.entity.mesh;

import java.util.ArrayList;
import java.util.List;

public class Dynamic {

    public void setGotoKeyframe(String gotoKeyframe) {
        this.gotoKeyframe = gotoKeyframe;
    }

    public void setDefaultKeyframe(String defaultKeyframe) {
        this.defaultKeyframe = defaultKeyframe;
    }

    public void setLightingKeyframe(String lightingKeyframe) {
        this.lightingKeyframe = lightingKeyframe;
    }

    public List<Keyframe> getKeyframes() {
        return keyframes;
    }

    public List<Animation> getAnimations() {
        return animations;
    }

    public String getGotoKeyframe() {
        return gotoKeyframe;
    }

    public String getDefaultKeyframe() {
        return defaultKeyframe;
    }

    public String getLightingKeyframe() {
        return lightingKeyframe;
    }

    public static class Keyframe {
        private String name;
        private double[][] matrix;

        public Keyframe(String name, double[][] matrix) {
            this.name = name;
            this.matrix = matrix;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMatrix(double[][] matrix) {
            this.matrix = matrix;
        }

        public String getName() {
            return name;
        }

        public double[][] getMatrix() {
            return matrix;
        }
    }

    public static class Animation {

        public void setName(String name) {
            this.name = name;
        }

        public void setStartTransform(float[][] startTransform) {
            this.startTransform = startTransform;
        }

        public void setEndTransform(float[][] endTransform) {
            this.endTransform = endTransform;
        }

        public void setData1(Data data1) {
            this.data1 = data1;
        }

        public void setData2(Data data2) {
            this.data2 = data2;
        }

        public void setLength(double length) {
            this.length = length;
        }

        public void setStartKeyframe(String startKeyframe) {
            this.startKeyframe = startKeyframe;
        }

        public void setEndKeyframe(String endKeyframe) {
            this.endKeyframe = endKeyframe;
        }

        public void setPosition(Graph position) {
            this.position = position;
        }

        public void setRotation(Graph rotation) {
            this.rotation = rotation;
        }

        public String getName() {
            return name;
        }

        public float[][] getStartTransform() {
            return startTransform;
        }

        public float[][] getEndTransform() {
            return endTransform;
        }

        public Data getData1() {
            return data1;
        }

        public Data getData2() {
            return data2;
        }

        public double getLength() {
            return length;
        }

        public String getStartKeyframe() {
            return startKeyframe;
        }

        public String getEndKeyframe() {
            return endKeyframe;
        }

        public Graph getPosition() {
            return position;
        }

        public Graph getRotation() {
            return rotation;
        }

        public static class Data {
            private float min;
            private float max;
            private byte[] values;

            public void setMin(float min) {
                this.min = min;
            }

            public void setMax(float max) {
                this.max = max;
            }

            public void setValues(byte[] values) {
                this.values = values;
            }

            public float getMin() {
                return min;
            }

            public float getMax() {
                return max;
            }

            public byte[] getValues() {
                return values;
            }
        }

        public static class Graph {
            private int unk1;
            private float[] unk2;
            private int sampleRate;
            private List<float[]> values;

            public void setUnk1(int unk1) {
                this.unk1 = unk1;
            }

            public void setUnk2(float[] unk2) {
                this.unk2 = unk2;
            }

            public void setSampleRate(int sampleRate) {
                this.sampleRate = sampleRate;
            }

            public void setValues(List<float[]> values) {
                this.values = values;
            }

            public int getUnk1() {
                return unk1;
            }

            public float[] getUnk2() {
                return unk2;
            }

            public int getSampleRate() {
                return sampleRate;
            }

            public List<float[]> getValues() {
                return values;
            }
        }

        private String name;

        private float[][] startTransform;
        private float[][] endTransform;

        private Data data1;
        private Data data2;

        private double length;

        private String startKeyframe;

        private String endKeyframe;

        private Graph position;
        private Graph rotation;

    }

    private List<Keyframe> keyframes = new ArrayList<>();

    private List<Animation> animations = new ArrayList<>();

    private String gotoKeyframe;

    private String defaultKeyframe;

    private String lightingKeyframe;

    public void addKeyframe(Keyframe keyframe) {
        this.keyframes.add(keyframe);
    }

    public void addAnimation(Animation animation) {
        this.animations.add(animation);
    }
}
