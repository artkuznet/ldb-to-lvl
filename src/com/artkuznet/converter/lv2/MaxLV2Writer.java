package com.artkuznet.converter.lv2;

import com.artkuznet.converter.*;
import com.artkuznet.converter.lv2.writer.EntityWriter;
import com.artkuznet.converter.lv2.writer.WriterHelper;
import com.artkuznet.converter.maxed2.document.DocumentPreferences;
import com.artkuznet.converter.maxed2.material.Material;
import com.artkuznet.converter.maxed2.material.MaterialCategory;
import com.artkuznet.converter.maxed2.material.Texture;

import java.io.IOException;
import java.util.Arrays;

public class MaxLV2Writer {

    private MaxTypeWriter writer;

    private LV2 lv2;

    public MaxLV2Writer(LV2 lv2, String filename) {
        this.lv2 = lv2;
        this.writer = new MaxTypeWriter(filename);
    }

    public void write() {
        writeHeader();
        writeTextures();
        writeMaterialCategories();
        writeLightmapData();
        writeDocumentPreferences();
        EntityWriter.write(writer, lv2.getWorldGroup());
        writer.writeBytes(new byte[]{8, 2});
        writeUnkData2();
        writer.write(lv2.getCameraPosition());
        writer.write(lv2.getCameraRotation());
        writer.write(lv2.getUnkCam());
        writeGroups();

        try {
            writer.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeHeader() {
        writer.writeBytes(
                WriterHelper.toBytes(Block.X100, Arrays.asList(
                        lv2.getMaxEdVersion(),
                        (byte) 3,
                        lv2.getMaxEdBuild()
                )));
    }

    private void writeTextures() {
        MaxTypeWriter buffer = new MaxTypeWriter();
        buffer.write(new UNumber(lv2.getTextures().size()));
        for (Texture texture : lv2.getTextures()) {
            buffer.writeBytes(WriterHelper.toBytes(Block.X100, Arrays.asList(
                    new UNumber(texture.getFileType()),
                    texture.getData().length <= Short.MAX_VALUE
                            ? (short) texture.getData().length
                            : new Int3(texture.getData().length),
                    texture.getFilePath(),
                    texture.getData()
            )));
        }
        writer.writeBytes(WriterHelper.toBytes(Block.X100, buffer.toBytes()));
    }

    private void writeMaterialCategories() {
        writer.write(new UNumber(lv2.getMaterialCategories().size()));

        for (MaterialCategory category : lv2.getMaterialCategories()) {

            writer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(
                    new UNumber(category.getMaterials().size()),
                    category.getName()
            )));

            for (Material material : category.getMaterials()) {
                writer.write(WriterHelper.toBytes(Block.X500, Arrays.asList(
                                material.getName(),
                                new UNumber(material.getDiffuseTextureNames().size()),
                                material.getDiffuseTextureNames(),
                                material.getLightLayerTextureName(),
                                material.getDetailTextureName(),
                                materialFloatsToBytes(material),
                                material.isDualsided(),
                                new UNumber(material.getReferenceValue()),
                                material.isAdultContent(),
                                (byte) material.getBlendMode(),
                                material.isEdgeBlend(),
                                material.isReflection(),
                                material.isGloss(),
                                material.getReflectionTextureName(),
                                material.getGlossTextureName(),
                                (byte) material.getUiVisibleFrame(),
                                (byte) material.getFramerate()
                        ))
                );
            }
        }
    }

    private static byte[] materialFloatsToBytes(Material material) {
        MaxTypeWriter buffer = new MaxTypeWriter();
        for (float[] floats : material.getUnkFloats()) {
            buffer.writeByte((byte) 10);
            buffer.writeFloat(floats[0]);
            buffer.writeFloat(floats[1]);
        }
        return buffer.toBytes();
    }

    private void writeLightmapData() {
        if (lv2.getLightmapData() == null) {
            writer.writeBytes(WriterHelper.toBytes(Block.X100, Arrays.asList(
                    (byte) 0, WriterHelper.toBytes(Block.X100, Arrays.asList(128f, 0f, 255f, 0f))
            )));
        } else {
            writer.write(WriterHelper.toBytes(Block.X100, lv2.getLightmapData()));
        }
    }

    private void writeUnkData2() {
        writer.write(WriterHelper.toBytes(Block.X100, lv2.getUnkData2()));
    }

    private void writeGroups() {
        writer.write(
                WriterHelper.toBytes(
                        Block.X100,
                        Arrays.asList(
                                new UNumber(lv2.getGroups().size()),
                                lv2.getGroups()
                        )
                )
        );
    }

    private void writeDocumentPreferences() {
        DocumentPreferences p = lv2.getPreferences();
        writer.writeBytes(WriterHelper.toBytes(Block.X600, Arrays.asList(
                new UNumber(p.getUnk1()),
                p.getBackplane(),
                p.getFrontplane(),
                p.getFOV(),
                p.getUnk2(),
                (byte) p.getObjectMode(),
                new UNumber(p.getConnectedRooms()),
                p.getUnk3(),
                p.getAngleSnap(),
                p.getGridScaleSteps(),
                new UNumber(p.getDefaultUVScale()),
                p.getLightmapRes(),
                p.isAverageLightmapBorder(),
                new UShort(p.getViewportSize()),
                p.getGlobalLightmapRes(),
                new UInt(p.getDefaultLightColor().toInt()),
                p.getDefaultLightIntensity(),
                p.getGisMaxEnergyPerShoot(),
                p.getGisStopAtEnergy(),
                p.isIgnorePortals(),
                p.getLdbExportParameters(),
                new byte[]{8, (byte) p.getBackgroudColor().getR()},
                new byte[]{8, (byte) p.getBackgroudColor().getG()},
                new byte[]{8, (byte) p.getBackgroudColor().getB()},
                new byte[]{8, (byte) p.getBackgroudColor().getA()}
        )));
    }
}
