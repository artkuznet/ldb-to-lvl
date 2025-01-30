package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb2.dynamicmesh.DynamicMeshAnimation;
import com.artkuznet.converter.lv2.converter.helper.MatrixUtil;
import com.artkuznet.converter.maxed2.entity.mesh.Dynamic;
import com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DynamicDataConverter {

    private static final String KEYFRAME = "Keyframe_";

    // TODO refactor

    public static Dynamic convert(DynamicMesh mesh, Vector3D roomPosition, List<DynamicMeshAnimation> dynamicMeshAnimations/*, FSM fsm*/) {
        Dynamic dynamic = new Dynamic();

        getAllKeyframes(mesh.getLocalMatrix(), roomPosition, dynamicMeshAnimations).forEach(dynamic::addKeyframe);

//        String firstKeyframeName = dynamic.getKeyframes().get(0).getName();
//        dynamic.setGotoKeyframe(firstKeyframeName);
//        dynamic.setDefaultKeyframe(firstKeyframeName); // todo ?
//        dynamic.setLightingKeyframe(firstKeyframeName);

        dynamicMeshAnimations.stream().map(a -> convert(a, dynamic.getKeyframes(), roomPosition)).forEach(dynamic::addAnimation);

        List<double[][]> transforms = dynamic.getKeyframes().stream()
                .map(Dynamic.Keyframe::getMatrix)
                .map(m -> revertKeyframeMatrix(m, new Vector3D(0, 0, 0)))
                .collect(Collectors.toList());

        // todo absolute position
        double[][] d = mesh.getLocalMatrix();

        int i1 = indexOf(transforms, d);
        int i2 = indexOf2(transforms, d);

        int id = i1 > 0 ? i1 : Math.max(i2, 0);

        String defaultKeyframeName = dynamic.getKeyframes().get(id).getName();
        dynamic.setGotoKeyframe(defaultKeyframeName);
        dynamic.setDefaultKeyframe(defaultKeyframeName);
        dynamic.setLightingKeyframe(defaultKeyframeName);

        return dynamic;
    }

    private static Dynamic.Animation convert(DynamicMeshAnimation ldb2Anim, List<Dynamic.Keyframe> keyframes, Vector3D roomPosition) {
        Dynamic.Animation animation = new Dynamic.Animation();

        animation.setName(ldb2Anim.getName());
        animation.setLength(ldb2Anim.getLength());

        animation.setStartTransform(ldb2Anim.getStartTransform());
        animation.setEndTransform(ldb2Anim.getEndTransform());

        List<double[][]> transforms = keyframes.stream()
                .map(Dynamic.Keyframe::getMatrix)
                .map(m -> revertKeyframeMatrix(m, roomPosition))
                .collect(Collectors.toList());

        int startKeyframeIndex = indexOf(transforms, MatrixUtil.matrixFloatToDouble(ldb2Anim.getStartTransform()));
        int endKeyframeIndex = indexOf(transforms, MatrixUtil.matrixFloatToDouble(ldb2Anim.getEndTransform()));

        animation.setStartKeyframe(keyframes.get(startKeyframeIndex).getName());
        animation.setEndKeyframe(keyframes.get(endKeyframeIndex).getName());

        animation.setRotation(getGraph(ldb2Anim.getrTime(), ldb2Anim.getrValue()));
        animation.setPosition(getGraph(ldb2Anim.gettTime(), ldb2Anim.gettValue()));

        // todo interpolation ?
        Dynamic.Animation.Data defaultData = new Dynamic.Animation.Data();

        defaultData.setMin(0);
        defaultData.setMax(0);
        defaultData.setValues(new byte[]{0, 17, 34, 51, 68, 85, 102, 119, -120, -103, -86, -69, -52, -35, -18, -1});

        // todo rotation ?
        animation.setData1(defaultData);

        // todo position ?
        animation.setData2(defaultData);

        return animation;
    }

    private static Dynamic.Animation.Graph getGraph(List<Float> time, List<Float> value) {
        if (time.size() != value.size()) {
            throw new RuntimeException();
        }

        Dynamic.Animation.Graph graph = new Dynamic.Animation.Graph();
        graph.setUnk1(-1); // todo ?
        graph.setUnk2(new float[]{
                time.stream().min(Float::compareTo).get(),
                time.stream().max(Float::compareTo).get(),
                value.stream().min(Float::compareTo).get(),
                value.stream().max(Float::compareTo).get(),
        });
        graph.setSampleRate(16);

        List<float[]> values = new ArrayList<>();
        for (int i = 0; i < time.size(); i++) {
            values.add(new float[]{time.get(i), value.get(i), 0, 0});
        }

        graph.setValues(values);

        return graph;
    }

    private static List<Dynamic.Keyframe> getAllKeyframes(double[][] meshLocalMatrix, Vector3D roomPosition, List<DynamicMeshAnimation> dynamicMeshAnimations) {

        if (dynamicMeshAnimations.isEmpty()) {
            return Collections.singletonList(new Dynamic.Keyframe(KEYFRAME + 0, keyframeMatrix(meshLocalMatrix)));
        }

        List<Dynamic.Keyframe> keyframes = new ArrayList<>();

        AtomicInteger keyframeIndex = new AtomicInteger(-1);
        List<String> keyframeNames = new ArrayList<>();
        List<double[][]> keyframeTransforms = new ArrayList<>();

        // todo refactor

        dynamicMeshAnimations.forEach(
                animation -> {

                    double[][] t1 = keyframeMatrix(MatrixUtil.matrixFloatToDouble(animation.getStartTransform(), roomPosition));
                    double[][] t2 = keyframeMatrix(MatrixUtil.matrixFloatToDouble(animation.getEndTransform(), roomPosition));

                    if (indexOf(keyframeTransforms, t1) < 0) {
                        keyframeNames.add(KEYFRAME + (keyframeIndex.incrementAndGet()));
                        keyframeTransforms.add(t1);
                    }
                    if (indexOf(keyframeTransforms, t2) < 0) {
                        keyframeNames.add(KEYFRAME + (keyframeIndex.incrementAndGet()));
                        keyframeTransforms.add(t2);
                    }
                }
        );

        for (int i = 0; i < keyframeNames.size(); i++) {
            keyframes.add(new Dynamic.Keyframe(keyframeNames.get(i), keyframeTransforms.get(i)));
        }


        return keyframes;
    }

    // todo Keyframes Util

    private static double[][] keyframeMatrix(double[][] matrix) {
        return new double[][]{
                matrix[1],
                matrix[2],
                matrix[3],
                matrix[0],
        };
    }

    private static double[][] revertKeyframeMatrix(double[][] matrix, Vector3D p) {
        return new double[][]{
                matrix[3],
                matrix[0],
                matrix[1],
                new double[]{matrix[2][0] + p.getX(), matrix[2][1] + p.getY(), matrix[2][2] + p.getZ()},
        };
    }

    private static int indexOf(List<double[][]> list, double[][] t) {
        return IntStream.range(0, list.size()).filter(i -> transformEquals(list.get(i), t)).findFirst().orElse(-1);
    }

    private static int indexOf2(List<double[][]> list, double[][] t) {
        return IntStream.range(0, list.size()).filter(i -> transformEquals2(list.get(i), t)).findFirst().orElse(-1);
    }

    private static boolean transformEquals(double[][] t1, double[][] t2) {
        return IntStream.range(0, 4).noneMatch(i -> IntStream.range(0, 3).anyMatch(j -> Math.abs(t1[i][j] - t2[i][j]) >= 0.000001));
    }

    private static boolean transformEquals2(double[][] t1, double[][] t2) {
        return IntStream.range(0, 3).noneMatch(i -> IntStream.range(0, 3).anyMatch(j -> Math.abs(t1[i][j] - t2[i][j]) >= 0.000001));
    }
}
