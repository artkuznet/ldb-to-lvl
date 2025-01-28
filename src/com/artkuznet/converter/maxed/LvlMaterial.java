package com.artkuznet.converter.maxed;

public class LvlMaterial {

    public static class MaterialBitmap {
        private final String name;
        private final String shortName;
        private final int layerType; // 0 - no layer; 1 - alpha, 4 - light

        public MaterialBitmap(final String name, final String shortName, final int layerType) {
            this.name = name;
            this.shortName = shortName;
            this.layerType = layerType;
        }

        public String getName() {
            return name;
        }

        public String getShortName() {
            return shortName;
        }

        public int getLayerType() {
            return layerType;
        }
    }

    public static class BitmapLayer extends MaterialBitmap {
        private final String layerBitmapName;

        public BitmapLayer(final String name, final String shortName, final int type, final String layerBitmapName) {
            super(name, shortName, type);
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
}
