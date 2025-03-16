package com.artkuznet.converter.util;

import java.util.Objects;

public class TgaParser {

    public static class TgaImage {

        private int lightmapId;

        private int idLength;
        private int colorMapType;
        private int imageType;
        private int width;
        private int height;
        private int pixelDepth;
        private int imageDescriptor;
        private byte[] imageData;

        public byte[] getImageData() {
            return imageData;
        }

        public void setImageData(byte[] imageData) {
            this.imageData = imageData;
        }

        public int getPixelColor(int x, int y) {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                throw new IllegalArgumentException("Coordinates out of bounds");
            }

            boolean isTopToBottom = (imageDescriptor & 0x20) == 0x20;
            if (!isTopToBottom) {
                y = height - 1 - y;
            }

            int bytesPerPixel = pixelDepth / 8;
            int offset = (y * width + x) * bytesPerPixel;

            if (offset + bytesPerPixel > imageData.length) {
                throw new IllegalArgumentException("Invalid pixel data");
            }

            int b = imageData[offset] & 0xFF;
            int g = imageData[offset + 1] & 0xFF;
            int r = imageData[offset + 2] & 0xFF;
            int a = (bytesPerPixel == 4) ? (imageData[offset + 3] & 0xFF) : 0xFF;

            return (a << 24) | (r << 16) | (g << 8) | b;
        }

        public int getLightmapId() {
            return lightmapId;
        }

        public void setLightmapId(int lightmapId) {
            this.lightmapId = lightmapId;
        }

        public int getIdLength() {
            return idLength;
        }

        public void setIdLength(int idLength) {
            this.idLength = idLength;
        }

        public int getColorMapType() {
            return colorMapType;
        }

        public void setColorMapType(int colorMapType) {
            this.colorMapType = colorMapType;
        }

        public int getImageType() {
            return imageType;
        }

        public void setImageType(int imageType) {
            this.imageType = imageType;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getPixelDepth() {
            return pixelDepth;
        }

        public void setPixelDepth(int pixelDepth) {
            this.pixelDepth = pixelDepth;
        }

        public int getImageDescriptor() {
            return imageDescriptor;
        }

        public void setImageDescriptor(int imageDescriptor) {
            this.imageDescriptor = imageDescriptor;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lightmapId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TgaImage that = (TgaImage) o;
            return Objects.equals(this.lightmapId, that.lightmapId);
        }
    }

    public static TgaImage parse(int lightmapId, byte[] data) {
        if (data.length < 18) {
            throw new IllegalArgumentException("Invalid TGA file");
        }

        TgaImage image = new TgaImage();

        image.setLightmapId(lightmapId);

        image.setIdLength(data[0] & 0xFF);
        image.setColorMapType(data[1] & 0xFF);
        image.setImageType(data[2] & 0xFF);

        int colorMapStart = readLittleEndianShort(data, 3);
        int colorMapLength = readLittleEndianShort(data, 5);
        int colorMapDepth = data[7] & 0xFF;

        int xOrigin = readLittleEndianShort(data, 8);
        int yOrigin = readLittleEndianShort(data, 10);

        image.setWidth(readLittleEndianShort(data, 12));
        image.setHeight(readLittleEndianShort(data, 14));
        image.setPixelDepth(data[16] & 0xFF);
        image.setImageDescriptor(data[17] & 0xFF);

        if (image.getImageType() != 2) {
            throw new UnsupportedOperationException("Unsupported image type");
        }
        if (image.getPixelDepth() != 24 && image.getPixelDepth() != 32) {
            throw new UnsupportedOperationException("Unsupported pixel depth");
        }

        int pixelDataOffset = 18 + image.getIdLength();
        if (image.getColorMapType() == 1) {
            int colorMapEntrySize = (colorMapDepth + 7) / 8;
            pixelDataOffset += colorMapLength * colorMapEntrySize;
        }

        int bytesPerPixel = image.getPixelDepth() / 8;
        int expectedDataLength = image.getWidth() * image.getHeight() * bytesPerPixel;
        if (pixelDataOffset + expectedDataLength > data.length) {
            throw new IllegalArgumentException("Incomplete pixel data");
        }

        byte[] pixelData = new byte[expectedDataLength];
        System.arraycopy(data, pixelDataOffset, pixelData, 0, expectedDataLength);
        image.setImageData(pixelData);

        return image;
    }

    private static int readLittleEndianShort(byte[] data, int offset) {
        return (data[offset + 1] & 0xFF) << 8 | (data[offset] & 0xFF);
    }
}
