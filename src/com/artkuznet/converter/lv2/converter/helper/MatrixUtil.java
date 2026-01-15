package com.artkuznet.converter.lv2.converter.helper;

import com.artkuznet.converter.Vector3D;

public class MatrixUtil {

    public static double[][] matrixFloatToDouble(float[][] m) {
        return new double[][]{
                new double[]{m[0][0], m[0][1], m[0][2]},
                new double[]{m[1][0], m[1][1], m[1][2]},
                new double[]{m[2][0], m[2][1], m[2][2]},
                new double[]{m[3][0], m[3][1], m[3][2]},
        };
    }

    public static double[][] matrixFloatToDouble(float[][] m, Vector3D offset) {
        return new double[][]{
                new double[]{m[0][0], m[0][1], m[0][2]},
                new double[]{m[1][0], m[1][1], m[1][2]},
                new double[]{m[2][0], m[2][1], m[2][2]},
                new double[]{m[3][0] - offset.getX(), m[3][1] - offset.getY(), m[3][2] - offset.getZ()},
        };
    }

    public static Vector3D getPosition(float[][] m) {
        return new Vector3D(m[3][0], m[3][1], m[3][2]);
    }
}
