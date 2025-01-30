package com.artkuznet.converter.ldb2.dynamicmesh;

import java.util.List;

public class DynamicMeshAnimation {

    private String name;

    private float length;

    private float[][] startTransform;

    private float[][] endTransform;

    private int tSampleRate;

    private List<Float> tTime;

    private List<Float> tValue;

    private int rSampleRate;

    private List<Float> rTime;

    private List<Float> rValue;

    private List<String> leavingFirstFrameMessages;

    private List<String> returningToFirstFrameMessages;

    private List<String> reachingSecondFrameMessages;

    public DynamicMeshAnimation(
            String name,
            float length,
            float[][] startTransform,
            float[][] endTransform,
            int tSampleRate,
            List<Float> tTime,
            List<Float> tValue,
            int rSampleRate,
            List<Float> rTime,
            List<Float> rValue,
            List<String> leavingFirstFrameMessages,
            List<String> returningToFirstFrameMessages,
            List<String> reachingSecondFrameMessages
    ) {
        this.name = name;
        this.length = length;
        this.startTransform = startTransform;
        this.endTransform = endTransform;
        this.tSampleRate = tSampleRate;
        this.tTime = tTime;
        this.tValue = tValue;
        this.rSampleRate = rSampleRate;
        this.rTime = rTime;
        this.rValue = rValue;
        this.leavingFirstFrameMessages = leavingFirstFrameMessages;
        this.returningToFirstFrameMessages = returningToFirstFrameMessages;
        this.reachingSecondFrameMessages = reachingSecondFrameMessages;
    }

    public String getName() {
        return name;
    }

    public float getLength() {
        return length;
    }

    public float[][] getStartTransform() {
        return startTransform;
    }

    public float[][] getEndTransform() {
        return endTransform;
    }

    public int gettSampleRate() {
        return tSampleRate;
    }

    public List<Float> gettTime() {
        return tTime;
    }

    public List<Float> gettValue() {
        return tValue;
    }

    public int getrSampleRate() {
        return rSampleRate;
    }

    public List<Float> getrTime() {
        return rTime;
    }

    public List<Float> getrValue() {
        return rValue;
    }

    public List<String> getLeavingFirstFrameMessages() {
        return leavingFirstFrameMessages;
    }

    public List<String> getReturningToFirstFrameMessages() {
        return returningToFirstFrameMessages;
    }

    public List<String> getReachingSecondFrameMessages() {
        return reachingSecondFrameMessages;
    }
}
