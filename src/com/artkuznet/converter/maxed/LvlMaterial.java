package com.artkuznet.converter.maxed;

import java.util.stream.Collectors;

public class LvlMaterial {

    public static class MaterialBitmap {
        private final String name;
        private final String shortName;
        private final int layerType; // 0 - no layer; 1 - alpha, 4 - light

        private final boolean dualsided;
        private final boolean alphaTest;
        private final boolean adultContent;

        private final double unk1 = 0;
        private final double unk2 = 1;
        private final double unk3 = 1;

        public MaterialBitmap(
                final String name,
                final String shortName,
                final int layerType,
                final boolean dualsided,
                final boolean alphaTest,
                final boolean adultContent
        ) {
            this.name = name;
            this.shortName = shortName;
            this.layerType = layerType;
            this.dualsided = dualsided;
            this.alphaTest = alphaTest;
            this.adultContent = adultContent;
        }

        public String getName() {
            return replace8bit(name);
        }

        public String getShortName() {
            return replace8bit(shortName);
        }

        public int getLayerType() {
            return layerType;
        }

        public boolean isDualsided() {
            return dualsided;
        }

        public boolean hasAlphaTest() {
            return false; // todo return alphaTest;
        }

        public boolean hasAdultContent() {
            return adultContent;
        }

        public double getUnk1() {
            return unk1;
        }

        public double getUnk2() {
            return unk2;
        }

        public double getUnk3() {
            return unk3;
        }
    }

    public static class BitmapLayer extends MaterialBitmap {
        private final String layerBitmapName;

        public BitmapLayer(
                final String name,
                final String shortName,
                final int layerType,
                final boolean dualsided,
                final boolean alphaTest,
                final boolean adultContent,
                final String layerBitmapName
        ) {
            super(name, shortName, layerType, dualsided, alphaTest, adultContent);
            this.layerBitmapName = layerBitmapName;
        }

        public String getLayerBitmapName() {
            return layerBitmapName;
        }
    }

    private final String name;
    private final MaterialBitmap[] bitmaps;

    public LvlMaterial(final String name, final MaterialBitmap[] bitmaps) {
        this.name = name;
        this.bitmaps = bitmaps;
    }

    public String getName() {
        return name;
    }

    public MaterialBitmap[] getBitmaps() {
        return bitmaps;
    }

    // todo util
    private static String replace8bit(String str) {
        return str.chars()
                .mapToObj(c -> (c <= 255) ? String.valueOf((char) c) : "?")
                .collect(Collectors.joining());
    }
}
