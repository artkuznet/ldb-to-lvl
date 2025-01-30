package com.artkuznet.converter.util;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.lv2.converter.helper.LdbTriangleDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriangleMapper {

    private static final double EPSILON = 1e-8;

    public static Map<LdbTriangleDTO, LdbTriangleDTO> mapTriangles(
            List<LdbTriangleDTO> meshTriangles,
            List<LdbTriangleDTO> collisionTriangles) {

        Map<LdbTriangleDTO, LdbTriangleDTO> result = new HashMap<>();

        for (LdbTriangleDTO meshTri : meshTriangles) {
            for (LdbTriangleDTO collisionTri : collisionTriangles) {
                if (areNormalsCollinearAndSameDirection(meshTri.getNormal(), collisionTri.getNormal()) &&
                        areTrianglesInSamePlane(meshTri, collisionTri) &&
                        checkSurfaceIntersection(meshTri, collisionTri)) {

                    result.put(meshTri, collisionTri);
                    break; // Assuming first match is sufficient
                }
            }
        }

        return result;
    }

    private static boolean areNormalsCollinearAndSameDirection(Vector3D n1, Vector3D n2) {
        // Check if cross product is near zero
        double crossX = n1.getY() * n2.getZ() - n1.getZ() * n2.getY();
        double crossY = n1.getZ() * n2.getX() - n1.getX() * n2.getZ();
        double crossZ = n1.getX() * n2.getY() - n1.getY() * n2.getX();

        if (Math.abs(crossX) > EPSILON || Math.abs(crossY) > EPSILON || Math.abs(crossZ) > EPSILON) {
            return false;
        }

        // Check if dot product is positive
        double dot = n1.getX() * n2.getX() + n1.getY() * n2.getY() + n1.getZ() * n2.getZ();
        return dot > EPSILON;
    }

    private static boolean areTrianglesInSamePlane(LdbTriangleDTO tri1, LdbTriangleDTO tri2) {
        Vector3D normal = tri1.getNormal();
        Vector3D point = tri1.getVertices().get(0);
        Vector3D testPoint = tri2.getVertices().get(0);

        // Calculate distance from testPoint to tri1's plane
        double distance = normal.getX() * (testPoint.getX() - point.getX()) +
                normal.getY() * (testPoint.getY() - point.getY()) +
                normal.getZ() * (testPoint.getZ() - point.getZ());

        return Math.abs(distance) < EPSILON;
    }

    private static boolean checkSurfaceIntersection(LdbTriangleDTO tri1, LdbTriangleDTO tri2) {
        int axis = getProjectionAxis(tri1.getNormal());

        Point2D[] proj1 = projectTriangle(tri1, axis);
        Point2D[] proj2 = projectTriangle(tri2, axis);

        return doTrianglesIntersect(proj1, proj2);
    }

    private static int getProjectionAxis(Vector3D normal) {
        double xAbs = Math.abs(normal.getX());
        double yAbs = Math.abs(normal.getY());
        double zAbs = Math.abs(normal.getZ());

        if (xAbs >= yAbs && xAbs >= zAbs) {
            return 0; // YZ plane
        } else if (yAbs >= zAbs) {
            return 1; // XZ plane
        } else {
            return 2; // XY plane
        }
    }

    private static Point2D[] projectTriangle(LdbTriangleDTO tri, int axis) {
        return tri.getVertices().stream()
                .map(v -> projectPoint(v, axis))
                .toArray(Point2D[]::new);
    }

    private static Point2D projectPoint(Vector3D point, int axis) {
        switch (axis) {
            case 0:
                return new Point2D(point.getY(), point.getZ());
            case 1:
                return new Point2D(point.getX(), point.getZ());
            case 2:
                return new Point2D(point.getX(), point.getY());
            default:
                throw new IllegalArgumentException("Invalid axis");
        }
    }

    private static boolean doTrianglesIntersect(Point2D[] triA, Point2D[] triB) {
        // Check if any point of triA is inside triB
        for (Point2D p : triA) {
            if (isPointInsideTriangle(p, triB[0], triB[1], triB[2])) {
                return true;
            }
        }

        // Check if any point of triB is inside triA
        for (Point2D p : triB) {
            if (isPointInsideTriangle(p, triA[0], triA[1], triA[2])) {
                return true;
            }
        }

        // Check edge intersections
        for (int i = 0; i < 3; i++) {
            Point2D a1 = triA[i];
            Point2D a2 = triA[(i + 1) % 3];
            for (int j = 0; j < 3; j++) {
                Point2D b1 = triB[j];
                Point2D b2 = triB[(j + 1) % 3];
                if (doSegmentsIntersect(a1, a2, b1, b2)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isPointInsideTriangle(Point2D p, Point2D a, Point2D b, Point2D c) {
        double v0x = c.x - a.x;
        double v0y = c.y - a.y;
        double v1x = b.x - a.x;
        double v1y = b.y - a.y;
        double v2x = p.x - a.x;
        double v2y = p.y - a.y;

        double dot00 = v0x * v0x + v0y * v0y;
        double dot01 = v0x * v1x + v0y * v1y;
        double dot02 = v0x * v2x + v0y * v2y;
        double dot11 = v1x * v1x + v1y * v1y;
        double dot12 = v1x * v2x + v1y * v2y;

        double denom = dot00 * dot11 - dot01 * dot01;
        if (Math.abs(denom) < EPSILON) return false;

        double invDenom = 1.0 / denom;
        double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        return (u >= -EPSILON) && (v >= -EPSILON) && (u + v <= 1.0 + EPSILON);
    }

    private static boolean doSegmentsIntersect(Point2D a1, Point2D a2, Point2D b1, Point2D b2) {
        double dx1 = a2.x - a1.x;
        double dy1 = a2.y - a1.y;
        double dx2 = b2.x - b1.x;
        double dy2 = b2.y - b1.y;

        double denominator = dy2 * dx1 - dx2 * dy1;
        if (Math.abs(denominator) < EPSILON) return false;

        double t1 = (dx2 * (a1.y - b1.y) + dy2 * (b1.x - a1.x)) / denominator;
        double t2 = (dx1 * (a1.y - b1.y) + dy1 * (b1.x - a1.x)) / denominator;

        return t1 >= -EPSILON && t1 <= 1.0 + EPSILON &&
                t2 >= -EPSILON && t2 <= 1.0 + EPSILON;
    }

    // Helper class for 2D points
    private static class Point2D {
        public final double x;
        public final double y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
