package com.artkuznet.converter.mapper;

import com.artkuznet.converter.ldb.animation.Animation;
import com.artkuznet.converter.maxed.DynamicMesh;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DynamicDataMapper {

    private static final String KEYFRAME = "Keyframe ";

    private static final double ERROR_SUM = 0.01;

    public static DynamicMesh.DynamicData toDynamicData(double[][] transform, List<Animation> animations) {
        DynamicMesh.DynamicData dynamicData = new DynamicMesh.DynamicData();

        AtomicInteger keyframeIndex = new AtomicInteger(-1);

        List<String> keyframeNames = new ArrayList<>();
        keyframeNames.add(KEYFRAME + (keyframeIndex.incrementAndGet()));

        List<double[][]> keyframeTransforms = new ArrayList<>();
        keyframeTransforms.add(transform);

        animations.forEach(
                animation -> {
                    double[][] t1 = animation.getStartTransformDouble();
                    double[][] t2 = animation.getEndTransformDouble();

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
            dynamicData.keyframeTransforms.put(keyframeNames.get(i), keyframeTransforms.get(i));
        }

        dynamicData.animations = animations.stream().map(animation -> {
            DynamicMesh.DynamicData.DynamicAnimation dynamicAnimation = new DynamicMesh.DynamicData.DynamicAnimation();

            dynamicAnimation.name = animation.getAnimationName();
            dynamicAnimation.length = animation.getLengthInSecs();

            int startKeyframeIndex = indexOf(keyframeTransforms, animation.getStartTransformDouble());
            if (startKeyframeIndex < 0) {
                throw new RuntimeException();
            }
            dynamicAnimation.startKeyframe = KEYFRAME + startKeyframeIndex;
            int endKeyframeIndex = indexOf(keyframeTransforms, animation.getEndTransformDouble());
            if (endKeyframeIndex < 0) {
                throw new RuntimeException();
            }
            dynamicAnimation.endKeyframe = KEYFRAME + endKeyframeIndex;
            dynamicAnimation.startTransform = animation.getStartTransform();
            dynamicAnimation.endTransform = animation.getEndTransform();

            dynamicAnimation.position = new DynamicMesh.DynamicData.DynamicAnimation.AnimationGraph();
            dynamicAnimation.position.sampleRate = (short) animation.getTranslationGraph().getSampleRate();
            dynamicAnimation.position.points = animation.getTranslationGraph().getPoints();

            dynamicAnimation.rotation = new DynamicMesh.DynamicData.DynamicAnimation.AnimationGraph();
            dynamicAnimation.rotation.sampleRate = (short) animation.getRotationGraph().getSampleRate();
            dynamicAnimation.rotation.points = animation.getRotationGraph().getPoints();

            dynamicAnimation.position.interpolation = getInterpolation(dynamicAnimation.position.points.toArray(new Float[0]));
            dynamicAnimation.rotation.interpolation = getInterpolation(dynamicAnimation.rotation.points.toArray(new Float[0]));

            return dynamicAnimation;

        }).collect(Collectors.toList());

        return dynamicData;
    }

    private static List<float[]> getInterpolation(Float[] points) {
        LinkedHashMap<Integer, Float> map = new LinkedHashMap<>();
        map.put(0, points[0]);
        map.put(points.length - 1, points[points.length - 1]);

        while (true) {
            List<Double> errors = getErrors(points, map);

            Double errorsSum = errors.stream().reduce(Double::sum).orElseThrow(RuntimeException::new);

            if (errorsSum < ERROR_SUM) {
                break;
            }

            Double maxError = errors.stream().max(Double::compareTo).orElseThrow(RuntimeException::new);
            int maxErrIndex = errors.indexOf(maxError);

            map.put(maxErrIndex, points[maxErrIndex]);
        }

        return map.keySet().stream()
                .map(key -> new float[]{key / (float) (points.length - 1), map.get(key), 0, 0})
                .sorted((o1, o2) -> Float.compare(o1[0], o2[0]))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<Double> getErrors(Float[] points, LinkedHashMap<Integer, Float> values) {
        Float[] arr = new Float[points.length];
        values.forEach((index, value) -> arr[index] = value);

        Float[] interpolated = interpolate(arr);

        return IntStream.range(0, interpolated.length)
                .mapToDouble(i -> Math.pow(interpolated[i] - points[i], 2))
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static boolean transformEquals(double[][] t1, double[][] t2) {
        return IntStream.range(0, 4).noneMatch(i -> IntStream.range(0, 3).anyMatch(j -> Math.abs(t1[i][j] - t2[i][j]) >= 0.000001));
    }

    private static int indexOf(List<double[][]> list, double[][] t) {
        return IntStream.range(0, list.size()).filter(i -> transformEquals(list.get(i), t)).findFirst().orElse(-1);
    }

    private static Float linearInterpolate(Float y1, Float y2, Float mu) {
        return (y1 * (1 - mu) + y2 * mu);
    }

    private static Float[] interpolate(Float[] a) {
        if (!(a[0] != null && a[a.length - 1] != null)) {
            return null;
        }

        ArrayList<Integer> nonNullIdx = new ArrayList<>();
        ArrayList<Integer> steps = new ArrayList<>();

        int stepCnt = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != null) {
                nonNullIdx.add(i);
                if (stepCnt != 0) {
                    steps.add(stepCnt);
                }
                stepCnt = 0;
            } else {
                stepCnt++;
            }
        }

        Float fStart = null;
        Float fEnd = null;
        Float fStep = null;
        Float fMu = null;

        int i = 0;

        while (i < a.length - 1) {
            if (a[i] != null && nonNullIdx.size() > 1 && !steps.isEmpty()) {
                fStart = a[nonNullIdx.get(0)];
                fEnd = a[nonNullIdx.get(1)];
                fStep = 1.0f / (steps.get(0) + 1.0f);
                fMu = fStep;
                nonNullIdx.remove(0);
                steps.remove(0);
            } else if (a[i] == null) {
                a[i] = linearInterpolate(fStart, fEnd, fMu);
                fMu += fStep;
            }
            i++;
        }

        return a;
    }
}
