package com.artkuznet.converter.lv2.writer;

import com.artkuznet.converter.*;
import com.artkuznet.converter.Number;
import com.artkuznet.converter.lv2.Block;
import com.artkuznet.converter.maxed2.entity.mesh.DynamicTriangleMesh;
import com.artkuznet.converter.maxed2.entity.mesh.TriangleMesh;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TriangleMeshWriter {

    public static void write(MaxTypeWriter writer, TriangleMesh triangleMesh) {
        if (triangleMesh instanceof DynamicTriangleMesh) {
            writer.writeBytes(dynamicTriangleMeshToBytes((DynamicTriangleMesh) triangleMesh));
        } else {
            writer.write(WriterHelper.toBytes(Block.X300, Collections.singletonList(
                    triangleMesh.getUnkTriangleMeshData()
            )));
        }
    }

    private static byte[] dynamicTriangleMeshToBytes(DynamicTriangleMesh mesh) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.writeBytes(MeshWriter.meshPropertiesToBytes(mesh.getProperties()));
        buffer.write(mesh.getTriangleMeshSourceFile());
        buffer.write(mesh.getTriangleMeshSourceName());
        buffer.write(mesh.isHasDynamic());
        buffer.writeBytes(MeshWriter.dynamicToBytes(mesh.getDynamicData()));
        buffer.writeBytes(unkBlocks1ToBytes(mesh.unkBlock1List));
        buffer.writeBytes(unkBlocks2ToBytes(mesh.unkBlock2List));
        buffer.write(new UInt(mesh.unk4));

        return buffer.toBytes();
    }

    private static byte[] unkBlocks2ToBytes(List<DynamicTriangleMesh.UnkBlock2> blocks) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(new UNumber(blocks.size()));
        for (DynamicTriangleMesh.UnkBlock2 block : blocks) {
            buffer.write(WriterHelper.toBytes(Block.X200, Arrays.asList(

                    new Number(block.vIndices[0]),
                    new Number(block.vIndices[1]),
                    new Number(block.vIndices[2]),

                    block.uv[0],
                    block.uv[1],
                    block.uv[2],

                    block.unkS1,
                    block.unkS2,

                    block.unk11 <= Short.MAX_VALUE && block.unk11 >= Short.MIN_VALUE
                            ? new Number(block.unk11)
                            : new Int2(block.unk11)
            )));
        }
        return buffer.toBytes();
    }

    private static byte[] unkBlocks1ToBytes(List<DynamicTriangleMesh.UnkBlock1> blocks) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(new UNumber(blocks.size()));
        for (DynamicTriangleMesh.UnkBlock1 block : blocks) {
            buffer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(

                    block.d1[0],
                    block.d1[1],
                    block.d1[2],
                    block.d1[3],
                    block.d1[4],
                    block.d1[5],

                    new byte[]{0x08, (byte) block.i1[0]},
                    new byte[]{0x08, (byte) block.i1[1]},
                    new byte[]{0x08, (byte) block.i1[2]},

                    block.d2[0],
                    block.d2[1]
            )));
        }

        return buffer.toBytes();
    }
}
