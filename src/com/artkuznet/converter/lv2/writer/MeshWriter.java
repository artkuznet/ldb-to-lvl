package com.artkuznet.converter.lv2.writer;

import com.artkuznet.converter.*;
import com.artkuznet.converter.Number;
import com.artkuznet.converter.lv2.Block;
import com.artkuznet.converter.maxed2.entity.TriggerData;
import com.artkuznet.converter.maxed2.entity.mesh.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MeshWriter {

    public static void write(MaxTypeWriter writer, Mesh mesh) {
        byte[] meshBytes = WriterHelper.toBytes(Block.X900, Arrays.asList(
                mesh.isFlipNormals(),
                verticesToBytes(mesh.getVertices()),
                polygonsToBytes(mesh.getPolygons()),
                mesh.getHasDynamic(),
                mesh.getHasDynamic() ? dynamicToBytes(((DynamicMesh) mesh).getDynamicData()) : new byte[]{},
                meshPropertiesToBytes(mesh.getProperties()),
                mesh.isTrigger(),
                mesh.isTrigger() ? meshTriggerToBytes(((DynamicMesh) mesh).getTriggerData()) : new byte[]{},
                mesh.isMeshUnk4()
        ));

        if (!(mesh instanceof DynamicMesh)) {
            writer.writeBytes(WriterHelper.toBytes(Block.X10C, Arrays.asList(
                            meshBytes,
                            mesh.isMeshUnk4() ? unk2ToBytes(mesh) : new byte[]{}
                    )
            ));
        } else {
            writer.writeBytes(meshBytes);
        }
    }

    public static byte[] meshPropertiesToBytes(MeshProperties properties) {
        return WriterHelper.toBytes(Block.X600, Arrays.asList(
                properties.isCharacterCollisions(),
                properties.isUseLightmaps(),
                properties.isPointlightsAffect(),
                properties.isFsmContinuousUpdate(),
                properties.isBulletCollisions(),
                properties.isCastNoShadows(),
                properties.isCollisions(),
                properties.isBlockExplosions(),
                properties.isDoNotRender(),
                properties.isNoDecals(),
                properties.getSoundEnvironment(),
                properties.isGenerateConvexHull(),
                properties.isRayTracing(),
                properties.getPhysicalMaterial(),
                properties.isElevator(),
                properties.isGenerateBoundingBoxHull()
        ));
    }

    public static byte[] dynamicToBytes(Dynamic dynamic) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        byte[] keyframeBytes = keyframesToBytes(dynamic.getKeyframes());
        byte[] animationBytes = animationsToBytes(dynamic.getAnimations());

        MaxTypeWriter buffer1 = new MaxTypeWriter();
        buffer1.write(dynamic.getGotoKeyframe());
        buffer1.write(dynamic.getDefaultKeyframe());
        buffer1.write(dynamic.getLightingKeyframe());
        byte[] bytes1 = buffer1.toBytes();

        int dataSize2 = keyframeBytes.length + animationBytes.length;
        int dataSize1 = dataSize2 + bytes1.length + 8 + 5 + new UNumber(dataSize2).toBytes().length;

        buffer.write(Block.X100);
        buffer.write(dataSize1);
        buffer.write(new UNumber(dataSize2));

        buffer.writeBytes(keyframeBytes);
        buffer.writeBytes(animationBytes);
        buffer.writeBytes(bytes1);

        return buffer.toBytes();
    }

    private static byte[] animationsToBytes(List<Dynamic.Animation> animations) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(new Number(animations.size()));
        for (Dynamic.Animation animation : animations) {
            buffer.write(animation.getName());
            buffer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(
                    matrix4x3ToBytes(animation.getStartTransform()),
                    matrix4x3ToBytes(animation.getEndTransform()),
                    animDataToBytes(animation.getData1()),
                    animDataToBytes(animation.getData2()),
                    animation.getLength(),
                    animation.getStartKeyframe(),
                    animation.getEndKeyframe(),
                    animGraphToBytes(animation.getPosition(), "Position"),
                    animGraphToBytes(animation.getRotation(), "Rotation")
            )));
        }

        return buffer.toBytes();
    }

    private static byte[] animGraphToBytes(Dynamic.Animation.Graph graph, String type) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write((byte) 0x71);
        buffer.write((byte) 0x03);
        buffer.write("t");
        buffer.write(type);
        buffer.write((byte) 0x01);
        buffer.write((byte) 0x00);
        buffer.write((byte) 0x00);
        buffer.write(new Number(graph.getUnk1()));
        buffer.write((byte) 0x01);
        buffer.write((byte) 0x01);
        buffer.write((byte) 0x02);
        buffer.write(graph.getUnk2()[0]);
        buffer.write(graph.getUnk2()[1]);
        buffer.write(graph.getUnk2()[2]);
        buffer.write(graph.getUnk2()[3]);
        buffer.write(new UInt(255));
        buffer.write(new UInt(0));
        buffer.write(new UInt(0));
        buffer.write((byte) 0x01);
        buffer.write((byte) 0x00);
        buffer.write((byte) 0x00);
        buffer.write(new Number(graph.getSampleRate()));
        buffer.write(new UNumber(graph.getValues().size()));
        for (float[] values : graph.getValues()) {
            buffer.write((byte) 0x70);
            buffer.write((byte) 0x01);
            buffer.write(values[0]);
            buffer.write(values[1]);
            buffer.write(values[2]);
            buffer.write(values[3]);
        }
        buffer.write(new UNumber(0));

        return buffer.toBytes();
    }

    private static byte[] animDataToBytes(Dynamic.Animation.Data data) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write((byte) 0x72);
        buffer.write((byte) 0x01);
        buffer.write((byte) 0x01);
        buffer.write(new Number(data.getValues().length));
        buffer.write((byte) 0x73);
        buffer.write((byte) 0x01);
        buffer.write(new Number(data.getValues().length));
        buffer.write(data.getMin());
        buffer.write(data.getMax());
        buffer.writeBytes(data.getValues());

        return buffer.toBytes();
    }

    private static byte[] keyframesToBytes(List<Dynamic.Keyframe> keyframes) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(new Number(keyframes.size()));
        for (Dynamic.Keyframe keyframe : keyframes) {
            buffer.write(keyframe.getName());
            buffer.writeBytes(EntityWriter.localMatrixToBytes(keyframe.getMatrix()));
        }

        return buffer.toBytes();
    }

    private static byte[] meshTriggerToBytes(TriggerData trigger) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(WriterHelper.toBytes(Block.X300, Arrays.asList(
                trigger.isPlayer(),
                trigger.isUse(),
                trigger.isEnemy(),
                trigger.isBullet(),
                trigger.isLookAt(),
                trigger.isVisibility(),
                trigger.getActivatorsUseAnimation()
        )));

        return buffer.toBytes();
    }

    private static byte[] verticesToBytes(List<Vector3D> vertices) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(new UNumber(vertices.size()));
        for (Vector3D v : vertices) {
            buffer.write(WriterHelper.toBytes(Block.X100, Collections.singletonList(v)));
        }

        return buffer.toBytes();
    }

    private static byte[] polygonsToBytes(List<Polygon> polygons) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(new UNumber(polygons.size()));
        for (Polygon p : polygons) {
            buffer.write(WriterHelper.toBytes(Block.X300, Arrays.asList(
                    new UNumber(p.getIndex()),
                    new Number(p.getEdges().size()),
                    p.getNorm1(),
                    p.getNorm2(),
                    matrix3x3ToBytes(p.getMatr1()),
                    p.getNorm3(),
                    p.getVertexXYZ(),
                    p.getScaleU(),
                    p.getScaleV(),
                    p.getNorm4(),
                    p.getTextureVertexUV()[0],
                    p.getTextureVertexUV()[1],
                    p.getUnkVertexXYZ(),
                    unkToBytes(Collections.singletonList(p.getUnk3())),
                    p.getTexelsPerMeter(),
                    1 == p.getUnk4()[0],
                    new UInt(p.getUnk4()[1]),
                    new UNumber(p.getUnk4()[2]),
                    new Number(p.getUnk4()[3]),
                    p.getMaterialCategory(),
                    p.getMaterialName(),
                    edgesToBytes(p.getEdges()),
                    new Number(p.getTriangles().size()),
                    trianglesToBytes(p.getTriangles()),
                    new UNumber(p.getUnk5().size() + 1),
                    unkToBytes(p.getUnk5())
            )));
        }
        return buffer.toBytes();
    }

    private static byte[] edgesToBytes(List<Polygon.Edge> edges) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        for (Polygon.Edge e : edges) {
            buffer.write(new Number(e.getFrom()));
            buffer.write(new Number(e.getTo()));
        }

        return buffer.toBytes();
    }

    private static byte[] trianglesToBytes(List<Polygon.Triangle> triangles) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        for (Polygon.Triangle t : triangles) {
            buffer.write(t.getNormal());
            buffer.write(new Number(t.getVertexIndices().size()));
            for (Integer v : t.getVertexIndices()) {
                buffer.write(new Number(v));
            }
        }

        return buffer.toBytes();
    }

    private static byte[] unkToBytes(List<Polygon.Unk1> list) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        for (Polygon.Unk1 unk : list) {
            buffer.write(new Number(unk.unkInt));
            buffer.write(unk.norm1);
            buffer.write(MeshWriter.matrix3x3ToBytes(unk.matr));
            buffer.write(unk.norm2);
            buffer.write(unk.unkD[0]);
            buffer.write(unk.unkD[1]);
        }

        return buffer.toBytes();
    }

    private static byte[] matrix3x3ToBytes(double[][] m) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(m[0][0]);
        buffer.write(m[0][1]);
        buffer.write(m[0][2]);

        buffer.write(m[1][0]);
        buffer.write(m[1][1]);
        buffer.write(m[1][2]);

        buffer.write(m[2][0]);
        buffer.write(m[2][1]);
        buffer.write(m[2][2]);

        return buffer.toBytes();
    }

    private static byte[] unk2ToBytes(Mesh mesh) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(
                new UNumber(mesh.getUnkInt()[0]),
                (byte) mesh.getUnkInt()[1],
                (byte) mesh.getUnkInt()[2],
                (byte) mesh.getUnkInt()[3],
                mesh.getUnkVertex()[0],
                mesh.getUnkVertex()[1],
                mesh.getUnkData()
        )));

        return buffer.toBytes();
    }

    private static byte[] matrix4x3ToBytes(float[][] m) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.writeByte((byte) 0x1A);

        buffer.writeFloat(m[0][0]);
        buffer.writeFloat(m[0][1]);
        buffer.writeFloat(m[0][2]);

        buffer.writeFloat(m[1][0]);
        buffer.writeFloat(m[1][1]);
        buffer.writeFloat(m[1][2]);

        buffer.writeFloat(m[2][0]);
        buffer.writeFloat(m[2][1]);
        buffer.writeFloat(m[2][2]);

        buffer.writeFloat(m[3][0]);
        buffer.writeFloat(m[3][1]);
        buffer.writeFloat(m[3][2]);

        return buffer.toBytes();
    }
}
