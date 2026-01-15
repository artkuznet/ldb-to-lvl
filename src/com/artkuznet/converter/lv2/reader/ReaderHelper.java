package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.mesh.Dynamic;

import java.util.ArrayList;
import java.util.List;

public class ReaderHelper {

    public static void read100(MaxTypeReader reader) {
        if (0xC != reader.readInt() || 0x100 != reader.readInt()) {
            throw new RuntimeException();
        }
    }

    public static void read200(MaxTypeReader reader) {
        if (0xC != reader.readInt() || 0x200 != reader.readInt()) {
            throw new RuntimeException();
        }
    }

    public static void read300(MaxTypeReader reader) {
        if (0xC != reader.readInt() || 0x300 != reader.readInt()) {
            throw new RuntimeException();
        }
    }

    public static void read400(MaxTypeReader reader) {
        if (0xC != reader.readInt() || 0x400 != reader.readInt()) {
            throw new RuntimeException();
        }
    }

    public static void read500(MaxTypeReader reader) {
        if (0xC != reader.readInt() || 0x500 != reader.readInt()) {
            throw new RuntimeException();
        }
    }

    public static void read600(MaxTypeReader reader) {
        if (0xC != reader.readInt() || 0x600 != reader.readInt()) {
            throw new RuntimeException();
        }
    }

    public static void read900(MaxTypeReader reader) {
        if (0xC != reader.readInt() || 0x900 != reader.readInt()) {
            throw new RuntimeException();
        }
    }

    public static void read10C(MaxTypeReader reader) {
        if (0x10C != reader.readInt() || 0 != reader.readInt()) {
            throw new RuntimeException();
        }
    }

    public static double[][] readLocalMatrix(MaxTypeReader reader) {
        double[] translation = new double[]{
                (double) reader.readObject(),
                (double) reader.readObject(),
                (double) reader.readObject(),
        };
        double[][] rotation = new double[][]{
                new double[]{
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                },
                new double[]{
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                },
                new double[]{
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                        (double) reader.readObject(),
                },
        };

        return new double[][]{
                rotation[0],
                rotation[1],
                rotation[2],
                translation
        };
    }

    private static Dynamic.Animation.Data readAnimData(MaxTypeReader reader) {

        Dynamic.Animation.Data data = new Dynamic.Animation.Data();

        if (0x72 != (int) reader.readObject()) {
            throw new RuntimeException();
        }
        if (0x01 != (int) reader.readObject()) {
            throw new RuntimeException();
        }
        if (0x01 != (int) reader.readObject()) {
            throw new RuntimeException();
        }
        int sampleRate1 = (int) reader.readObject();

        if (0x73 != (int) reader.readObject()) {
            throw new RuntimeException();
        }
        if (0x01 != (int) reader.readObject()) {
            throw new RuntimeException();
        }
        int sampleRate2 = (int) reader.readObject();

        if (sampleRate1 != sampleRate2) {
            throw new RuntimeException();
        }

        data.setMin((float) reader.readObject());
        data.setMax((float) reader.readObject());
        data.setValues(reader.readBytes(sampleRate2));

        return data;
    }

    public static Dynamic.Animation.Graph readAnimationGraph(MaxTypeReader reader, String type) {

        Dynamic.Animation.Graph graph = new Dynamic.Animation.Graph();

        if (0x71 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }
        if (0x03 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }
        if (!"t".equals(reader.readObject())) {
            throw new RuntimeException();
        }
        if (!type.equals(reader.readObject())) {
            throw new RuntimeException();
        }

        if (0x01 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }
        if (0x00 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }
        if (0x00 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }

        graph.setUnk1((int) reader.readObject()); // Number

        if (0x01 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }
        if (0x01 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }
        if (0x02 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }

        graph.setUnk2(new float[]{
                (float) reader.readObject(),
                (float) reader.readObject(),
                (float) reader.readObject(),
                (float) reader.readObject(),
        });

        // red color // UInt 0x01
        if (255 != (int) reader.readObject() || 0 != (int) reader.readObject() || 0 != (int) reader.readObject()) {
            throw new RuntimeException();
        }

        if (0x01 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }
        if (0x00 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }
        if (0x00 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }

        graph.setSampleRate((int) reader.readObject()); // number

        int count = (int) reader.readObject(); // UNumber
        if (count < 2) {
            throw new RuntimeException();
        }

        List<float[]> values = new ArrayList<>();
        values.add(readFloats4(reader));
        for (int i = 0; i < count - 2; i++) {
            values.add(readFloats4(reader));
        }
        values.add(readFloats4(reader));

        graph.setValues(values);

        if ((int) reader.readObject() != 0) {// 0x11 - UNumber
            throw new RuntimeException();
        }

        return graph;
    }

    private static float[] readFloats4(MaxTypeReader reader) {
        if (0x70 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }
        if (0x01 != (int) reader.readObject()) { // byte
            throw new RuntimeException();
        }
        return new float[]{
                (float) reader.readObject(),
                (float) reader.readObject(),
                (float) reader.readObject(),
                (float) reader.readObject(),
        };
    }

    public static Dynamic readDynamicData(MaxTypeReader reader) {

        Dynamic dynamic = new Dynamic();

        int offset1 = reader.getOffset();

        ReaderHelper.read100(reader);

        int dataSize1 = (int) reader.readObject();
        int dataSize2 = (int) reader.readObject();

        int offset2 = reader.getOffset();

        int keyframesCount = (int) reader.readObject();
        for (int i = 0; i < keyframesCount; i++) {
            dynamic.addKeyframe(new Dynamic.Keyframe(
                    (String) reader.readObject(),
                    ReaderHelper.readLocalMatrix(reader)
            ));
        }

        int animationCount = (int) reader.readObject();
        for (int i = 0; i < animationCount; i++) {

            Dynamic.Animation animation = new Dynamic.Animation();

            animation.setName((String) reader.readObject());

            // data start
            reader.rememberOffset();

            ReaderHelper.read100(reader);

            int dataLength1 = (int) reader.readObject();

            animation.setStartTransform((float[][]) reader.readObject());
            animation.setEndTransform((float[][]) reader.readObject());

            animation.setData1(readAnimData(reader));
            animation.setData2(readAnimData(reader));

            animation.setLength((double) reader.readObject());

            animation.setStartKeyframe((String) reader.readObject());
            animation.setEndKeyframe((String) reader.readObject());

            animation.setPosition(ReaderHelper.readAnimationGraph(reader, "Position"));
            animation.setRotation(ReaderHelper.readAnimationGraph(reader, "Rotation"));

            // data end
            reader.validateDataSize(dataLength1);

            dynamic.addAnimation(animation);
        }

        if (reader.getOffset() - offset2 != dataSize2) {
            throw new RuntimeException("Invalid data size");
        }

        dynamic.setGotoKeyframe((String) reader.readObject());
        dynamic.setDefaultKeyframe((String) reader.readObject());
        dynamic.setLightingKeyframe((String) reader.readObject());

        if (reader.getOffset() - offset1 != dataSize1) {
            throw new RuntimeException("Invalid data size");
        }

        return dynamic;
    }
}
