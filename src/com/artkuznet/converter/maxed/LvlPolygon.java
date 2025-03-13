package com.artkuznet.converter.maxed;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.util.Triangulator;
import com.artkuznet.converter.util.VectorCalculator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LvlPolygon {

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
    public List<Short> neighborToJoinIndices = new ArrayList<>();

    public boolean grouped = false;

    public boolean joined = false;

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
    public double lightmapResolution = 2.0;
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

    public LvlPolygon join(List<LvlPolygon> polygons) {
        if (polygons.stream().map(LvlPolygon::getIndex).collect(Collectors.toSet()).contains(this.index)) {
            throw new RuntimeException("join failed");
        }

        List<Triangle> joinedTriangles = new ArrayList<>(this.triangles);
        joinedTriangles.addAll(polygons.stream().map(p -> p.triangles).flatMap(List::stream).collect(Collectors.toList()));
        this.triangles = joinedTriangles;

        List<Edge> edges = this.triangles.stream()
                .map((Function<Triangle, List<Edge>>) triangle -> IntStream.range(0, triangle.vertices.size())
                        .mapToObj(i -> new Edge(
                                triangle.vertices.get(i),
                                triangle.vertices.get(i == triangle.vertices.size() - 1 ? 0 : i + 1))
                        )
                        .collect(Collectors.toCollection(ArrayList::new))
                ).flatMap(List::stream).collect(Collectors.toList());

        List<Edge> facedEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edges.stream().filter(edge::equals).count() == 1) {
                facedEdges.add(edge);
            }
        }

        List<List<Edge>> faces = new ArrayList<>();
        while (!facedEdges.isEmpty()) {
            List<Edge> face = new ArrayList<>();
            Edge edgeStart = facedEdges.remove(0);
            face.add(edgeStart);

            while (true) {
                Edge finalEdgeStart = edgeStart;
                Edge edgeNext = facedEdges.stream()
                        .filter(e -> finalEdgeStart.to == e.from)
                        .findFirst()
                        .orElse(null);

                if (edgeNext == null) {
                    break;
                }

                face.add(edgeNext);
                facedEdges.remove(edgeNext);
                edgeStart = edgeNext;
            }

            faces.add(face);
        }

        this.edges = faces.stream().flatMap(List::stream).toArray(Edge[]::new);

        return this;
    }

    public static boolean hasSharedEdgeAndUV(LvlPolygon polygon1, LvlPolygon polygon2) {
        if (polygon1.UV == null || polygon2.UV == null) {
            return false;
        }

        for (int i = 0; i < polygon1.edges.length; i++) {
            for (int j = 0; j < polygon2.edges.length; j++) {
                if (polygon1.edges[i].equals(polygon2.edges[j])) {
                    Vector3D e1uv1 = polygon1.UV.get(i).clone().softSmooth();
                    Vector3D e1uv2 = polygon1.UV.get(i == polygon1.edges.length - 1 ? 0 : (1 + i)).clone().softSmooth();

                    Vector3D e2uv1 = polygon2.UV.get(j).clone().softSmooth();
                    Vector3D e2uv2 = polygon2.UV.get(j == polygon2.edges.length - 1 ? 0 : (1 + j)).clone().softSmooth();

                    Vector3D n1 = Vector3D.calculateNormal(Vector3D.findTriangle(polygon1.UV)).softSmooth();
                    Vector3D n2 = Vector3D.calculateNormal(Vector3D.findTriangle(polygon2.UV)).softSmooth();

                    if (n1.equals(n2)
                            && polygon1.normal.clone().softSmooth().equals(polygon2.normal.clone().softSmooth())
                            && (e1uv1.equals(e2uv1) && e1uv2.equals(e2uv2) || e1uv1.equals(e2uv2) && e1uv2.equals(e2uv1))
                    ) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
