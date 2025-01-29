package com.artkuznet.converter.maxed;

import com.artkuznet.converter.Vector3D;

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

            return Arrays.asList(t);
        }

        List<Vector3D> vertsCopy = new ArrayList<>(Arrays.stream(this.parentMesh.getVertices()).collect(Collectors.toList()));

        List<Vector3D> verts = Arrays.stream(this.edges)
                .map(edge -> vertsCopy.get(edge.getTo()))
                .collect(Collectors.toCollection(ArrayList::new));

        double[] polygons = new double[verts.size() * 3];

        for (int i = 0, j = 0; j < verts.size(); i += 3, j++) {
            polygons[i] = verts.get(j).getX();
            polygons[i + 1] = verts.get(j).getY();
            polygons[i + 2] = verts.get(j).getZ();
        }

        List<Integer> earcut = Earcut.earcut(polygons, null, 3);

        if (earcut.size() % 3 != 0) {
            throw new RuntimeException();
        }

        if (earcut.isEmpty()) {
            Triangle t = new Triangle();
            t.normal = normal.clone();
            t.vertices = new ArrayList<>();

            for (Edge edge : edges) {
                t.vertices.add(edge.getTo());
            }

            return Arrays.asList(t);
        }

        Vector3D nCalc = Vector3D.calculateNormal(
                verts.get(earcut.get(0)),
                verts.get(earcut.get(1)),
                verts.get(earcut.get(2))
        );

        if (!Vector3D.roughEquals(normal, nCalc)) {
            Collections.reverse(earcut);
        }

        List<Triangle> triangleList = new ArrayList<>();

        for (int i = 0; i < earcut.size(); i += 3) {
            Triangle triangle = new Triangle();
            triangle.normal = normal.clone();

            triangle.vertices = new ArrayList<>();

            triangle.vertices.add(vertsCopy.indexOf(verts.get(earcut.get(i))));
            triangle.vertices.add(vertsCopy.indexOf(verts.get(earcut.get(i + 1))));
            triangle.vertices.add(vertsCopy.indexOf(verts.get(earcut.get(i + 2))));

            triangleList.add(triangle);
        }

        return triangleList;
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
        Vector3D pNormal = this.normal.clone();

        List<Vector3D> triangleUV = Vector3D.findTriangle(this.UV);

        Vector3D uvN = Vector3D.calculateNormal(triangleUV.get(0), triangleUV.get(1), triangleUV.get(2));

        Vector3D axisP = Vector3D.P(pNormal, uvN);
        double angleP = pNormal.angle(uvN);

        List<Vector3D> testVertsCenter = Vector3D.moveCenter(this.testVertices);

        List<Vector3D> testVertsUV = Vector3D.moveCenter(this.UV).stream().collect(Collectors.toList());

        List<Vector3D> flatVertsXY = testVertsCenter.stream()
                .map(v -> v.clone().rotateAxis(-angleP, axisP))
                .collect(Collectors.toList());

        double uvSizeX = Vector3D.sizeX(testVertsUV);
        double uvSizeY = Vector3D.sizeY(testVertsUV);

        List<Double> uvX = testVertsUV.stream().map(Vector3D::getX).collect(Collectors.toList());
        double uvLx1 = uvX.get(2) - uvX.get(1);
        double uvLx2 = uvX.get(1) - uvX.get(0);
        double uvKx1 = uvLx1 / uvSizeX;
        double uvKx2 = uvLx2 / uvSizeX;

        List<Double> uvY = testVertsUV.stream().map(Vector3D::getY).collect(Collectors.toList());
        double uvLy1 = uvY.get(2) - uvY.get(1);
        double uvLy2 = uvY.get(1) - uvY.get(0);
        double uvKy1 = uvLy1 / uvSizeY;
        double uvKy2 = uvLy2 / uvSizeY;

        double from = -180;
        double to = 180;

        double calculatedAngle = 0;
        for (int i = 0; i < 10; i++) {
            double step = (to - from) / 180.0;
            Map<Double, Double> vals = findValues(from, to, step, flatVertsXY, uvKx1, uvKx2, uvKy1, uvKy2);
            Map.Entry<Double, Double> min = findMinEntry(vals);

            double d = ((to - from) / 2.0) / 4.0;

            from = min.getKey() - d;
            to = min.getKey() + d;

            calculatedAngle = min.getKey();

            if (min.getValue() < 0.0000001 || step < 0.000000000001) {
                break;
            }
        }

        double finalCalcAngle = calculatedAngle;
        List<Vector3D> newVertsXY = flatVertsXY.stream().map(v -> v.clone().rotateZ(finalCalcAngle)).collect(Collectors.toList());

        Vector3D baseVector = uvN.clone().rotateY(90 * (uvN.clone().hardSmooth().equals(new Vector3D(0, 0, 1)) ? -1 : 1));

        this.scaleU = baseVector.clone()
                .multiply(Vector3D.sizeX(newVertsXY) / this.sizeUV[0])
                .rotateZ(-calculatedAngle)
                .rotateAxis(angleP, axisP);

        this.scaleV = baseVector.clone()
                .multiply(Vector3D.sizeY(newVertsXY) / this.sizeUV[1])
                .rotateZ(-calculatedAngle - 90)
                .rotateAxis(angleP, axisP);
    }

    public static Map.Entry<Double, Double> findMinEntry(Map<Double, Double> vals) {
        Map.Entry<Double, Double> min = null;
        for (Map.Entry<Double, Double> entry : vals.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        return min;
    }

    private static Map<Double, Double> findValues(
            double from,
            double to,
            double step,
            List<Vector3D> flatVertsXY,
            double uvKx1,
            double uvKx2,
            double uvKy1,
            double uvKy2
    ) {
        Map<Double, Double> values = new HashMap<>();

        for (double angle = from; angle <= to; angle += step) {
            double finalAngle = angle;
            List<Vector3D> newVertsXY = flatVertsXY.stream().map(v -> v.clone().rotateZ(finalAngle)).collect(Collectors.toList());

            double newSizeX = Vector3D.sizeX(newVertsXY);
            double newSizeY = Vector3D.sizeY(newVertsXY);

            List<Double> newX = newVertsXY.stream().map(Vector3D::getX).collect(Collectors.toList());
            double newLx1 = newX.get(2) - newX.get(1);
            double newLx2 = newX.get(1) - newX.get(0);
            double newKx1 = newLx1 / newSizeX;
            double newKx2 = newLx2 / newSizeX;

            List<Double> newY = newVertsXY.stream().map(Vector3D::getY).collect(Collectors.toList());
            double newLy1 = newY.get(2) - newY.get(1);
            double newLy2 = newY.get(1) - newY.get(0);
            double newKy1 = newLy1 / newSizeY;
            double newKy2 = newLy2 / newSizeY;

            double e1 = Math.abs(newKx1 - uvKx1);
            double e2 = Math.abs(newKx2 - uvKx2);
            double e3 = Math.abs(newKy1 - uvKy1);
            double e4 = Math.abs(newKy2 - uvKy2);

            double eSumX = e1 * e1 + e2 * e2;
            double eSumY = e3 * e3 + e4 * e4;

            double eSum = Math.abs(eSumX - eSumY) + (eSumX + eSumY);

            values.put(angle, eSum);
        }

        return values;
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
