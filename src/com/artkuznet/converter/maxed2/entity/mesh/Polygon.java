package com.artkuznet.converter.maxed2.entity.mesh;

import com.artkuznet.converter.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    public String linkedPortalName; // todo refactor

    private boolean portal = false;

    public boolean isPortal() {
        return portal;
    }

    public void setPortal() {
        this.portal = true;
    }

    public static class Edge {
        public int from;
        public int to;

        public Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        @Override
        public int hashCode() {
            int result = from;
            result = 31 * result + to;

            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Edge)) {
                return false;
            }

            Edge otherEdge = (Edge) obj;

            return otherEdge.from == from && otherEdge.to == to;
        }

        public Edge normalize() {
            return to > from ? this : new Edge(to, from);
        }
    }

    public static class Triangle {
        private Vector3D normal;
        private List<Integer> vertexIndices = new ArrayList<>();

        public void setNormal(Vector3D normal) {
            this.normal = normal;
        }

        public void setVertexIndices(List<Integer> vertexIndices) {
            if (vertexIndices.size() > 0xFF) {
                throw new RuntimeException();
            }

            this.vertexIndices = vertexIndices;
        }

        public Vector3D getNormal() {
            return normal;
        }

        public List<Integer> getVertexIndices() {
            return vertexIndices;
        }
    }

    private int index;

    private Vector3D norm1;

    private Vector3D norm2;

    private double[][] matr1 = new double[][]{
            new double[]{1, 0, 0},
            new double[]{0, 1, 0},
            new double[]{0, 0, 1},
    };
    private Vector3D norm3;

    private Vector3D vertexXYZ = new Vector3D(0, 0, 0);

    private Vector3D scaleU;

    private Vector3D scaleV;

    private Vector3D norm4;

    private double[] textureVertexUV = new double[]{0, 0};

    private Vector3D unkVertexXYZ = new Vector3D(0, 0, 0);

    private Unk1 unk3;
    private float texelsPerMeter = 4f;
    private int[] unk4;
    private String materialCategory = "";
    private String materialName = "";
    private List<Edge> edges = new ArrayList<>();
    private List<Triangle> triangles = new ArrayList<>();

    private List<Unk1> unk5 = null;

    public static class Unk1 {
        public int unkInt = 0;

        public Vector3D norm1 = new Vector3D(1, 0, 0);

        public double[][] matr = new double[][]{
                new double[]{1, 0, 0},
                new double[]{0, 1, 0},
                new double[]{0, 0, 1},
        };

        public Vector3D norm2 = new Vector3D(1, 0, 0);

        public double[] unkD = new double[]{0, 0};
    }

    public int getIndex() {
        return index;
    }

    public Vector3D getNorm1() {
        return norm1;
    }

    public Vector3D getNorm2() {
        return norm2;
    }

    public double[][] getMatr1() {
        return matr1;
    }

    public Vector3D getNorm3() {
        return norm3;
    }

    public Vector3D getVertexXYZ() {
        return vertexXYZ;
    }

    public Vector3D getScaleU() {
        return scaleU;
    }

    public Vector3D getScaleV() {
        return scaleV;
    }

    public Vector3D getNorm4() {
        return norm4;
    }

    public double[] getTextureVertexUV() {
        return textureVertexUV;
    }

    public Vector3D getUnkVertexXYZ() {
        return unkVertexXYZ;
    }

    public Unk1 getUnk3() {
        if (unk3 == null) {
            unk3 = new Unk1();
            unk3.unkInt = 0;

            unk3.matr = new double[][]{
                    new double[]{1, 0, 0},
                    new double[]{0, 1, 0},
                    new double[]{0, 0, 1},
            };

            unk3.norm1 = new Vector3D(1, 0, 0);
            unk3.norm2 = new Vector3D(1, 0, 0);

        }
        return unk3;
    }

    public float getTexelsPerMeter() {
        return texelsPerMeter;
    }

    public int[] getUnk4() {
        return unk4;
    }

    public String getMaterialCategory() {
        return materialCategory;
    }

    public String getMaterialName() {
        return materialName;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public List<Unk1> getUnk5() {

        if (unk5 != null) {
            return unk5;
        }

        List<Unk1> list = new ArrayList<>();

        Unk1 u1 = getUnk3();
        u1.unkInt = 1;
        list.add(u1);

        return list;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setNorm1(Vector3D norm1) {
        this.norm1 = norm1;
    }

    public void setNorm2(Vector3D norm2) {
        this.norm2 = norm2;
    }

    public void setMatr1(double[][] matr1) {
        this.matr1 = matr1;
    }

    public void setNorm3(Vector3D norm3) {
        this.norm3 = norm3;
    }

    public void setVertexXYZ(Vector3D vertexXYZ) {
        this.vertexXYZ = vertexXYZ;
    }

    public void setScaleU(Vector3D scaleU) {
        this.scaleU = scaleU;
    }

    public void setScaleV(Vector3D scaleV) {
        this.scaleV = scaleV;
    }

    public void setNorm4(Vector3D norm4) {
        this.norm4 = norm4;
    }

    public void setTextureVertexUV(double[] textureVertexUV) {
        this.textureVertexUV = textureVertexUV;
    }

    private double area;

    public void setArea(double area) {
        this.area = area;
    }

    public double getArea() {
        return area;
    }

    public void setUnkVertexXYZ(Vector3D unkVertexXYZ) {
        this.unkVertexXYZ = unkVertexXYZ;
    }

    public void setUnk3(Unk1 unk3) {
        this.unk3 = unk3;
    }

    public void setTexelsPerMeter(float texelsPerMeter) {
        this.texelsPerMeter = texelsPerMeter;
    }

    public void setUnk4(int[] unk4) {
        if (unk4[0] > 1 || unk4[0] < 0) {
            throw new RuntimeException();
        }
        this.unk4 = unk4;
    }

    public void setMaterialCategory(String materialCategory) {
        this.materialCategory = materialCategory;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void setTriangles(List<Triangle> triangles) {
        this.triangles = triangles;
    }

    public void setUnk5(List<Unk1> unk5) {
        this.unk5 = unk5;
    }
}
