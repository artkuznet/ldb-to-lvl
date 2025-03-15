package com.artkuznet.converter.maxed;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.util.Triangulator;
import com.artkuznet.converter.util.VectorCalculator;

import java.util.*;
import java.util.stream.Collectors;

public class LvlPolygon {

    public void setEdges(Edge[] edges) {
        this.edges = edges;
    }

    public static class VertexEdge {
        public Vector3D v1;
        public Vector3D v2;
        public VertexUV uv1;
        public VertexUV uv2;

        public VertexEdge() {

        }

        public VertexEdge(Vector3D v1, Vector3D v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        private static VertexEdge normalize(VertexEdge edge) {
            if (compareVectors(edge.v1, edge.v2) <= 0) {
                return new VertexEdge(edge.v1, edge.v2);
            } else {
                return new VertexEdge(edge.v2, edge.v1);
            }
        }

        private static int compareVectors(Vector3D a, Vector3D b) {
            int cmp = Double.compare(a.getX(), b.getX());
            if (cmp != 0) return cmp;
            cmp = Double.compare(a.getY(), b.getY());
            if (cmp != 0) return cmp;
            return Double.compare(a.getZ(), b.getZ());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VertexEdge that = (VertexEdge) o;
            VertexEdge thisNorm = normalize(this);
            VertexEdge thatNorm = normalize(that);
            return thisNorm.v1.equals(thatNorm.v1) && thisNorm.v2.equals(thatNorm.v2);
        }

        @Override
        public int hashCode() {
            VertexEdge norm = normalize(this);
            return Objects.hash(norm.v1, norm.v2);
        }

    }

    public static class VertexPolygon {
        public short index;
        public List<VertexEdge> edges;
        public String materialName;
        public String bitmapName;
        public Vector3D normal;
        public Vector3D uvNormal;
    }

    public short index;

    public int geometryPolyGroup = 0;

    public int getGeometryPolyGroup() {
        return geometryPolyGroup;
    }

    public float maxEdgeLength = 0;
    public float maxAngle = 0;

    public short getIndex() {
        return index;
    }

    public List<Short> neighborIndices = new ArrayList<>();

    public boolean grouped = false;

    public List<Vector3D> testVertices;

    private Edge[] edges;
    private String materialName;
    private String bitmapName;

    public Vector3D normal;

    public Vector3D unkVector1;
    public double[][] unkTransform;
    public Vector3D unkVertex1 = new Vector3D(0., 0., 0.);
    public Vector3D unkVertex2 = new Vector3D(0., 0., 0.);

    public double[] sizeUV;

    public List<Vector3D> UV;

    public Vector3D scaleU;
    public Vector3D scaleV;

    public double[] textureOffset;

    public double lightIntensity = 1.0;
    public double lightmapResolution = 4.0;
    public short pointPolygonIndex = -1;

    public static class Triangle {
        public Vector3D normal;

        public List<Integer> vertices; // vertex indices
    }

    public List<Triangle> triangles;

    public Mesh parentMesh;

    public List<Triangle> getTriangles() {
        return triangles == null ? getDefaultTriangles() : triangles;
    }

    public Vector3D[] unk11;

    public static class Edge {
        private final int from;
        private final int to;

        public Edge(final int from, final int to) {
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

            int result2 = to;
            result2 = 31 * result2 + from;

            return result | result2;
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

            return otherEdge.from == from && otherEdge.to == to || otherEdge.from == to && otherEdge.to == from;
        }
    }

    public LvlPolygon(final Edge[] edges, final String materialName, final String bitmapName, Vector3D normal) {
        this.edges = edges;
        this.materialName = materialName;
        this.bitmapName = bitmapName;
        this.normal = normal;

        this.unk11 = new Vector3D[]{
                new Vector3D(0, 0, 0),
                new Vector3D(0, 0, 0),
                new Vector3D(0, 0, 0),
                new Vector3D(0, 0, 0),
                new Vector3D(0, 0, 0),
                new Vector3D(0, 0, 0),
                new Vector3D(0, 0, 0),
        };
    }

    public Vector3D[] getUnknownVectors() {
        if (unkVector1.getX() > 0) {
            return new Vector3D[]{
                    new Vector3D(0, 0, -1),
                    new Vector3D(0, -1, 0),
            };
        } else if (unkVector1.getX() < 0) {
            return new Vector3D[]{
                    new Vector3D(0, 0, 1),
                    new Vector3D(0, -1, 0),
            };
        } else if (unkVector1.getY() > 0) {
            return new Vector3D[]{
                    new Vector3D(-1, 0, 0),
                    new Vector3D(0, 0, -1),
            };
        } else if (unkVector1.getY() < 0) {
            return new Vector3D[]{
                    new Vector3D(-1, 0, 0),
                    new Vector3D(0, 0, 1),
            };
        } else if (unkVector1.getZ() < 0) {
            return new Vector3D[]{
                    new Vector3D(0, 1, 0),
                    new Vector3D(-1, 0, 0),
            };
        } else {
            return new Vector3D[]{
                    new Vector3D(0, 1, 0),
                    new Vector3D(1, 0, 0),
            };
        }
    }

    public Edge[] getEdges() {
        return edges;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getBitmapName() {
        return bitmapName;
    }

    public String getShortBitmapName() {
        String substring = bitmapName;
        if (substring.contains("\\")) {
            substring = substring.substring(substring.lastIndexOf("\\") + 1);
        }
        return substring.contains(".") ? substring.substring(0, substring.lastIndexOf(".")) : substring;
    }

    public double[][] getDefaultTransform() {
        if (unkTransform != null) {
            return unkTransform;
        }

        return new double[][]{
                new double[]{1., 0., 0.},
                new double[]{0., 1., 0.},
                new double[]{0., 0., 1.},
                new double[]{normal.getX(), normal.getY(), normal.getZ()}
        };
    }

    public Vector3D[] getDefaultScaleUV() {
        if (Math.abs(normal.getX()) > 0) {
            return new Vector3D[]{
                    new Vector3D(0., 1., 0.),
                    new Vector3D(0., 0., 1.),
            };
        } else if (Math.abs(normal.getY()) > 0) {
            return new Vector3D[]{
                    new Vector3D(1., 0., 0.),
                    new Vector3D(0., 0., 1.),
            };
        } else {
            return new Vector3D[]{
                    new Vector3D(1., 0., 0.),
                    new Vector3D(0., 1., 0.),
            };
        }
    }

    public void calculateTriangles() {
        this.triangles = getDefaultTriangles();
    }

    public void setTriangles(List<Triangle> triangles) {
        this.triangles = triangles;
    }

    public List<Triangle> getDefaultTriangles() {
        if (triangles != null) {
            return triangles;
        }

        if (this.edges.length <= 4 || !(this instanceof LvlExit)) {
            Triangle t = new Triangle();
            t.normal = normal.clone();
            t.vertices = new ArrayList<>();

            for (Edge edge : edges) {
                t.vertices.add(edge.getTo());
            }

            return Collections.singletonList(t);
        }

        List<Vector3D> vertsCopy = Arrays.stream(this.parentMesh.getVertices()).collect(Collectors.toList());

        List<Vector3D> verts = Arrays.stream(this.edges)
                .map(edge -> vertsCopy.get(edge.getTo()))
                .collect(Collectors.toCollection(ArrayList::new));

        List<List<Vector3D>> triangulatedVertices = Triangulator.triangulate(verts);

        return triangulatedVertices.stream()
                .map(v -> {
                    Triangle t = new Triangle();
                    t.normal = normal.clone();
                    t.vertices = v.stream().map(vertsCopy::indexOf).collect(Collectors.toList());
                    return t;
                }).collect(Collectors.toList());
    }

    public Vector3D getDefaultUnk5() {
        Double max = Arrays.stream(new Double[]{normal.getX(), normal.getY(), normal.getZ()})
                .map(Math::abs)
                .max(Double::compareTo).orElseThrow(null);

        return new Vector3D(
                (Math.abs(normal.getX()) >= max) ? (normal.getX() > 0 ? 1.0 : -1.0) : 0.0,
                (Math.abs(normal.getY()) >= max) ? (normal.getY() > 0 ? 1.0 : -1.0) : 0.0,
                (Math.abs(normal.getZ()) >= max) ? (normal.getZ() > 0 ? 1.0 : -1.0) : 0.0
        );
    }

    public void calculateScaleUV() {
        Vector3D[] vectors = VectorCalculator.calculateUVVectors(
                this.testVertices,
                this.UV.stream().map(VertexUV::new).collect(Collectors.toList())
        );

        this.scaleU = vectors[0];
        this.scaleV = vectors[1];
    }
}
