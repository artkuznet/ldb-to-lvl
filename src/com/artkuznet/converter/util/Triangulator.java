package com.artkuznet.converter.util;

import com.artkuznet.converter.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Triangulator {

    private static class Point2D {
        double x, y;

        Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static List<List<Vector3D>> triangulate(List<Vector3D> polygon) {
        List<List<Vector3D>> triangles = new ArrayList<>();
        if (polygon.size() < 3) return triangles;

        Vector3D normal = Vector3D.calculateNormal(Vector3D.findTriangle(polygon));
        int projectionPlane = determineProjectionPlane(normal);
        List<Point2D> projected = projectPolygon(polygon, projectionPlane);
        boolean isCCW = isCCW(projected);

        List<Integer> activeIndices = new ArrayList<>();
        for (int i = 0; i < polygon.size(); i++) activeIndices.add(i);

        while (activeIndices.size() > 3) {
            int earIndex = findEarIndex(activeIndices, projected, isCCW);
            if (earIndex == -1) throw new RuntimeException("Triangulation failed");

            int prevIdx = activeIndices.get((earIndex - 1 + activeIndices.size()) % activeIndices.size());
            int currentIdx = activeIndices.get(earIndex);
            int nextIdx = activeIndices.get((earIndex + 1) % activeIndices.size());

            triangles.add(createTriangle(polygon, prevIdx, currentIdx, nextIdx));
            activeIndices.remove(earIndex);
        }

        triangles.add(createTriangle(polygon, activeIndices.get(0), activeIndices.get(1), activeIndices.get(2)));
        return triangles;
    }

    private static int determineProjectionPlane(Vector3D normal) {
        double absX = Math.abs(normal.getX()), absY = Math.abs(normal.getY()), absZ = Math.abs(normal.getZ());
        if (absX >= absY && absX >= absZ) return 0;
        if (absY >= absX && absY >= absZ) return 1;
        return 2;
    }

    private static List<Point2D> projectPolygon(List<Vector3D> polygon, int plane) {
        List<Point2D> projected = new ArrayList<>();
        for (Vector3D v : polygon) {
            switch (plane) {
                case 0: projected.add(new Point2D(v.getY(), v.getZ())); break;
                case 1: projected.add(new Point2D(v.getX(), v.getZ())); break;
                case 2: projected.add(new Point2D(v.getX(), v.getY())); break;
            }
        }
        return projected;
    }

    private static boolean isCCW(List<Point2D> polygon) {
        double area = 0;
        int n = polygon.size();
        for (int i = 0; i < n; i++) {
            Point2D curr = polygon.get(i), next = polygon.get((i+1)%n);
            area += curr.x * next.y - next.x * curr.y;
        }
        return area > 0;
    }

    private static int findEarIndex(List<Integer> indices, List<Point2D> projected, boolean isCCW) {
        int n = indices.size();
        for (int i = 0; i < n; i++) {
            int prevIdx = indices.get((i-1+n)%n);
            int currIdx = indices.get(i);
            int nextIdx = indices.get((i+1)%n);
            Point2D a = projected.get(prevIdx), b = projected.get(currIdx), c = projected.get(nextIdx);

            double cross = (b.x - a.x)*(c.y - b.y) - (b.y - a.y)*(c.x - b.x);
            boolean convex = (isCCW && cross > 0) || (!isCCW && cross < 0);
            if (!convex) continue;

            if (isEar(prevIdx, currIdx, nextIdx, projected, indices)) return i;
        }
        return -1;
    }

    private static boolean isEar(int prevIdx, int currIdx, int nextIdx, List<Point2D> projected, List<Integer> indices) {
        Point2D a = projected.get(prevIdx), b = projected.get(currIdx), c = projected.get(nextIdx);
        for (int idx : indices) {
            if (idx == prevIdx || idx == currIdx || idx == nextIdx) continue;
            Point2D p = projected.get(idx);
            if (isPointInside(a, b, c, p)) return false;
        }
        return true;
    }

    private static boolean isPointInside(Point2D a, Point2D b, Point2D c, Point2D p) {
        double cross1 = (b.x - a.x)*(p.y - a.y) - (b.y - a.y)*(p.x - a.x);
        double cross2 = (c.x - b.x)*(p.y - b.y) - (c.y - b.y)*(p.x - b.x);
        double cross3 = (a.x - c.x)*(p.y - c.y) - (a.y - c.y)*(p.x - c.x);
        return !((cross1 < 0 || cross2 < 0 || cross3 < 0) && (cross1 > 0 || cross2 > 0 || cross3 > 0));
    }

    private static List<Vector3D> createTriangle(List<Vector3D> polygon, int i, int j, int k) {
        List<Vector3D> triangle = new ArrayList<>();
        triangle.add(polygon.get(i));
        triangle.add(polygon.get(j));
        triangle.add(polygon.get(k));
        return triangle;
    }
}
