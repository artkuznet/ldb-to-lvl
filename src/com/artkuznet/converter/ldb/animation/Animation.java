package com.artkuznet.converter.ldb.animation;

import com.artkuznet.converter.ldb.fsm.FSMMessageContainer;

public class Animation {
    private String animationName;
    private float lengthInSecs;
    private float[][] startTransform;
    private float[][] endTransform;
    private FSMMessageContainer leavingFirstFrame;
    private FSMMessageContainer returningFirstFrame;
    private FSMMessageContainer reachingSecondFrame;
    private Graph translationGraph;
    private Graph rotationGraph;

    public Animation(
            String animationName,
            float lengthInSecs,
            float[][] startTransform,
            float[][] endTransform,
            FSMMessageContainer leavingFirstFrame,
            FSMMessageContainer returningFirstFrame,
            FSMMessageContainer reachingSecondFrame,
            Graph translationGraph,
            Graph rotationGraph
    ) {
        this.animationName = animationName;
        this.lengthInSecs = lengthInSecs;
        this.startTransform = startTransform;
        this.endTransform = endTransform;
        this.leavingFirstFrame = leavingFirstFrame;
        this.returningFirstFrame = returningFirstFrame;
        this.reachingSecondFrame = reachingSecondFrame;
        this.translationGraph = translationGraph;
        this.rotationGraph = rotationGraph;
    }

    public String getAnimationName() {
        return animationName;
    }

    public float getLengthInSecs() {
        return lengthInSecs;
    }

    public float[][] getStartTransform() {
        return startTransform;
    }

    public double[][] getStartTransformDouble() {
        return new double[][]{
                new double[]{startTransform[0][0], startTransform[0][1], startTransform[0][2]},
                new double[]{startTransform[1][0], startTransform[1][1], startTransform[1][2]},
                new double[]{startTransform[2][0], startTransform[2][1], startTransform[2][2]},
                new double[]{startTransform[3][0], startTransform[3][1], startTransform[3][2]},
        };
    }

    public float[][] getEndTransform() {
        return endTransform;
    }

    public double[][] getEndTransformDouble() {
        return new double[][]{
                new double[]{endTransform[0][0], endTransform[0][1], endTransform[0][2]},
                new double[]{endTransform[1][0], endTransform[1][1], endTransform[1][2]},
                new double[]{endTransform[2][0], endTransform[2][1], endTransform[2][2]},
                new double[]{endTransform[3][0], endTransform[3][1], endTransform[3][2]},
        };
    }

    public FSMMessageContainer getLeavingFirstFrame() {
        return leavingFirstFrame;
    }

    public FSMMessageContainer getReturningFirstFrame() {
        return returningFirstFrame;
    }

    public FSMMessageContainer getReachingSecondFrame() {
        return reachingSecondFrame;
    }

    public Graph getTranslationGraph() {
        return translationGraph;
    }

    public Graph getRotationGraph() {
        return rotationGraph;
    }
}
