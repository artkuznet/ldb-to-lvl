package com.artkuznet.converter.util;

import com.artkuznet.converter.ldb.vertex.VertexUV;

import java.util.ArrayList;
import java.util.List;

public class PixelColorExtractor {

    public static List<Integer> getPixelsInUVArea(TgaParser.TgaImage image, List<VertexUV> uvList) {
        List<Point2D> polygon = new ArrayList<>();
        for (VertexUV uv : uvList) {
            double x = uv.getU() * (double) (image.getWidth() - 1);
            double y = uv.getV() * (double) (image.getHeight() - 1);
            polygon.add(new Point2D(x, y));
        }

        List<Integer> result = new ArrayList<>();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                double centerX = x + 0.5;
                double centerY = y + 0.5;
                if (isPointInsidePolygon(centerX, centerY, polygon)) {
                    result.add(image.getPixelColor(x, y));
                }
            }
        }

        return result;
    }

    private static boolean isPointInsidePolygon(double px, double py, List<Point2D> polygon) {
        boolean inside = false;
        int n = polygon.size();
        for (int i = 0, j = n - 1; i < n; j = i++) {
            Point2D vi = polygon.get(i);
            Point2D vj = polygon.get(j);
            double xi = vi.x, yi = vi.y;
            double xj = vj.x, yj = vj.y;

            boolean intersect = ((yi > py) != (yj > py))
                    && (px < (xj - xi) * (py - yi) / (yj - yi) + xi);
            if (intersect) {
                inside = !inside;
            }
        }
        return inside;
    }

    private static class Point2D {
        public final double x, y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
