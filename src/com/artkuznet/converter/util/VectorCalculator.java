package com.artkuznet.converter.util;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;

import java.util.List;

public class VectorCalculator {

    public static Vector3D[] calculateUVVectors(List<Vector3D> vertices, List<VertexUV> uvList) {
        if (vertices.size() < 3) {
            throw new RuntimeException();
        }

        Vector3D v1 = vertices.get(0);
        Vector3D v2 = vertices.get(1);
        Vector3D v3 = vertices.get(2);

        VertexUV uv1 = uvList.get(0);
        VertexUV uv2 = uvList.get(1);
        VertexUV uv3 = uvList.get(2);

        double deltaU1 = uv2.getU() - uv1.getU();
        double deltaV1 = uv2.getV() - uv1.getV();

        double deltaX1 = v2.getX() - v1.getX();
        double deltaY1 = v2.getY() - v1.getY();
        double deltaZ1 = v2.getZ() - v1.getZ();

        double deltaU2 = uv3.getU() - uv1.getU();
        double deltaV2 = uv3.getV() - uv1.getV();

        double deltaX2 = v3.getX() - v1.getX();
        double deltaY2 = v3.getY() - v1.getY();
        double deltaZ2 = v3.getZ() - v1.getZ();

        Vector3D vectorU = new Vector3D(0, 0, 0);
        Vector3D vectorV = new Vector3D(0, 0, 0);

        solveComponent(deltaU1, deltaV1, deltaX1,
                deltaU2, deltaV2, deltaX2,
                vectorU, vectorV, 'x'
        );

        solveComponent(deltaU1, deltaV1, deltaY1,
                deltaU2, deltaV2, deltaY2,
                vectorU, vectorV, 'y'
        );

        solveComponent(deltaU1, deltaV1, deltaZ1,
                deltaU2, deltaV2, deltaZ2,
                vectorU, vectorV, 'z'
        );

        return new Vector3D[]{vectorU, vectorV};
    }

    private static void solveComponent(
            double deltaU1, double deltaV1, double deltaVal1,
            double deltaU2, double deltaV2, double deltaVal2,
            Vector3D vectorU, Vector3D vectorV, char component
    ) {
        double det = deltaU1 * deltaV2 - deltaU2 * deltaV1;

        if (Math.abs(det) < 1e-8) {

            double a = deltaU1, b = deltaV1;
            double c = deltaU2, d = deltaV2;

            double scale = a * a + b * b + c * c + d * d;
            if (scale < 1e-8) {
                return;
            }

            double invScale = 1.0 / scale;
            double u = (a * deltaVal1 + c * deltaVal2) * invScale;
            double v = (b * deltaVal1 + d * deltaVal2) * invScale;

            setComponent(vectorU, component, u);
            setComponent(vectorV, component, v);
        } else {
            double u = (deltaVal1 * deltaV2 - deltaVal2 * deltaV1) / det;
            double v = (deltaU1 * deltaVal2 - deltaU2 * deltaVal1) / det;

            setComponent(vectorU, component, u);
            setComponent(vectorV, component, v);
        }
    }

    private static void setComponent(Vector3D vec, char component, double value) {
        switch (component) {
            case 'x':
                vec.setX(value);
                break;
            case 'y':
                vec.setY(value);
                break;
            case 'z':
                vec.setZ(value);
                break;
        }
    }
}
