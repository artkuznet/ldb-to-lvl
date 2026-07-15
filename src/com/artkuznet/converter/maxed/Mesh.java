package com.artkuznet.converter.maxed;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.maxed.helper.MeshLightingProcessor;
import com.artkuznet.converter.util.*;

import java.util.*;
import java.util.stream.Collectors;

public class Mesh extends MaxObject {

    private Vector3D[] vertices;
    private LvlPolygon[] polygons;
    private PolyGroup[] polyGroups = new PolyGroup[0];

    private boolean room = false;

    private float aiNetDensity = 1.0f;

    public byte[] flags = new byte[]{1, 0, 0, 0, 0, 0, 0};

    public boolean blockExplosions = true;
    public boolean pointlightAffected = true;
    public boolean bulletCollisions = true;
    public boolean dynamicCollisions = true;
    public boolean lightMapped = true;
    public boolean contUpdate = true;

    public void setVertices(final Vector3D[] vertices) {
        this.vertices = vertices;
    }

    public Vector3D[] getVertices() {
        return vertices;
    }

    public boolean isRoom() {
        return room;
    }

    public float getAiNetDensity() {
        return aiNetDensity;
    }

    public void setIsRoom(final boolean room) {
        this.room = room;
    }

    public void setAiNetDensity(float aiNetDensity) {
        this.aiNetDensity = aiNetDensity;
    }

    public void setPolygons(final LvlPolygon[] polygons) {
        this.polygons = polygons;
    }

    public LvlPolygon[] getPolygons() {
        return polygons;
    }

    public void setPolyGroups(final PolyGroup[] polyGroups) {
        this.polyGroups = polyGroups;
    }

    public PolyGroup[] getPolyGroups() {
        return polyGroups;
    }

    public static int getObjectType() {
        return 1;
    }

    public Mesh optimize() {
        List<Vector3D> newVertices = new ArrayList<>();

        for (LvlPolygon polygon : polygons) {
            for (int i = 0; i < polygon.getEdges().length; i++) {
                Vector3D vFrom = vertices[polygon.getEdges()[i].getFrom()].clone();
                Vector3D vTo = vertices[polygon.getEdges()[i].getTo()].clone();

                if (!newVertices.contains(vFrom)) {
                    newVertices.add(vFrom);
                }

                if (!newVertices.contains(vTo)) {
                    newVertices.add(vTo);
                }

                polygon.getEdges()[i] = new LvlPolygon.Edge(newVertices.indexOf(vFrom), newVertices.indexOf(vTo));
            }
        }

        vertices = newVertices.toArray(new Vector3D[0]);

        for (LvlPolygon p : this.polygons) {
            p.parentMesh = this;
        }

        return this;
    }

    public Mesh joinPolygons() {

        for (LvlPolygon polygon : polygons) {
            polygon.calculateTriangles();
        }

        // TODO exclude list
        if (this.getName().equals("Mesh_402")
                || this.getChecksum() == 450681776500423730L
                || this.getChecksum() == 4977642932638294487L
        ) {

//            String name = this.getName();
//            long sum = this.getChecksum();

            return this;
        }

        List<LvlPolygon> polygonList = Arrays.stream(this.polygons).collect(Collectors.toList());

        List<LvlPolygon.VertexPolygon> vertexPolygons = polygonList.stream()
                .filter(lvlPolygon -> !(lvlPolygon instanceof LvlExit))
                .map(lvlPolygon -> {
                    LvlPolygon.VertexPolygon vPolygon = new LvlPolygon.VertexPolygon();

                    vPolygon.index = lvlPolygon.index;
                    vPolygon.materialName = lvlPolygon.getMaterialName();
                    vPolygon.bitmapName = lvlPolygon.getBitmapName();
                    vPolygon.normal = lvlPolygon.getNormal().clone();
                    vPolygon.uvNormal = Vector3D.calculateNormal(
                            Vector3D.findTriangle(lvlPolygon.UV.stream()
                                    .map(Vector3D::new)
                                    .collect(Collectors.toList())
                            )
                    );

                    vPolygon.edges = new ArrayList<>();

                    for (int i = 0; i < lvlPolygon.getEdges().length; i++) {
                        LvlPolygon.Edge edge = lvlPolygon.getEdges()[i];

                        LvlPolygon.VertexEdge vEdge = new LvlPolygon.VertexEdge();

                        vEdge.v1 = vertices[edge.getFrom()];
                        vEdge.v2 = vertices[edge.getTo()];

                        vEdge.uv1 = lvlPolygon.UV.get(i);
                        vEdge.uv2 = lvlPolygon.UV.get((i + 1) % lvlPolygon.UV.size());

                        vPolygon.edges.add(vEdge);
                    }

                    return vPolygon;
                })
                .distinct()
                .map(PolygonProcessor::fixClockwise)
                .collect(Collectors.toList());

        List<List<LvlPolygon.VertexPolygon>> vGroups = PolygonGrouper.groupPolygons(vertexPolygons).stream()
                .map(PolygonProcessor::processPolygons)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<Vector3D> verticesList = Arrays.stream(vertices).collect(Collectors.toList());

        List<LvlPolygon> lvlPolygonList = Arrays.stream(this.polygons).collect(Collectors.toList());

        List<LvlPolygon> joinedPolygons = vGroups.stream().map(vPolygons -> {

            List<List<LvlPolygon.VertexEdge>> edgeContours = ContourFinder.findContours(vPolygons);

            List<LvlPolygon.VertexEdge> vEdges = edgeContours.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            LvlPolygon p1 = lvlPolygonList.stream()
                    .filter(p -> p.index == vPolygons.get(0).index)
                    .findAny()
                    .orElseThrow(RuntimeException::new);

            List<Integer> groupedIndices = vPolygons.stream().map(vp -> vp.index).collect(Collectors.toList());

            p1.setEdges(vEdges.stream()
                    .map(vertexEdge ->
                            new LvlPolygon.Edge(verticesList.indexOf(vertexEdge.v1), verticesList.indexOf(vertexEdge.v2))
                    )
                    .toArray(LvlPolygon.Edge[]::new)
            );

            p1.UV = vEdges.stream().map(vertexEdge -> vertexEdge.uv1).collect(Collectors.toList());

            p1.setTriangles(
                    lvlPolygonList.stream()
                            .filter(p -> groupedIndices.contains(p.index))
                            .map(LvlPolygon::getTriangles)
                            .flatMap(List::stream)
                            .collect(Collectors.toList())
            );

            p1.setArea(lvlPolygonList.stream()
                    .filter(p -> groupedIndices.contains(p.index))
                    .map(LvlPolygon::getArea)
                    .reduce(Double::sum)
                    .orElseThrow(RuntimeException::new)
            );

            return p1;
        }).collect(Collectors.toList());

        joinedPolygons.addAll(polygonList.stream()
                .filter(p -> p instanceof LvlExit)
                .collect(Collectors.toList())
        );

        this.polygons = joinedPolygons.toArray(new LvlPolygon[0]);

        return this;
    }

    public Mesh buildPolyGroups() {

        if (!(this instanceof DynamicMesh)) {
            PolyGroupAssigner.assignGeometryPolyGroups(this.polygons); // todo feature flag
        }

        Map<Integer, List<LvlPolygon>> groupedPolygons = Arrays.stream(this.polygons).filter(p -> p.getGeometryPolyGroup() != 0)
                .collect(Collectors.groupingBy(LvlPolygon::getGeometryPolyGroup));

        if (!groupedPolygons.isEmpty()) {
            List<PolyGroup> pgList = new ArrayList<>();

            for (Integer polyGroupId : groupedPolygons.keySet()) {
                PolyGroup pg = new PolyGroup();

                pg.setName("New polygroup");

                if (polyGroupId > 0) {
                    pg.setSmoothGeometry(true);
                }
                pg.setSmoothLightMaps(true);

                Float maxAngle = null;
                Float maxEdgeLength = null;

                List<Integer> polygonNumbers = new ArrayList<>();

                for (int i = 0; i < this.polygons.length; i++) {
                    if (this.polygons[i].getGeometryPolyGroup() == polyGroupId) {
                        polygonNumbers.add(i);
                        if (maxAngle == null) {
                            maxAngle = (float) ((this.polygons[i].maxAngle * 180.) / Math.PI);
                        }
                        if (maxEdgeLength == null) {
                            maxEdgeLength = this.polygons[i].maxEdgeLength;
                        }
                    }
                }

                if (maxAngle == null) {
                    throw new RuntimeException("poly group max angle is null");
                }

                if (maxEdgeLength == null) {
                    throw new RuntimeException("poly group max edge length is null");
                }

                pg.setMaxAngle(maxAngle);
                pg.setMaxEdgeLength(maxEdgeLength);
                pg.setPolygons(polygonNumbers.stream().mapToInt(Integer::intValue).toArray());

                pgList.add(pg);
            }

            this.polyGroups = pgList.toArray(new PolyGroup[0]);
        }

        return this;
    }

    @Override
    public double[] getPosition() {
        calculatePosition();
        return position;
    }

    public void calculatePosition() {
        Vector3D transformPosition = new Vector3D(0, 0, 0);
        List<Vector3D> transformVertices = Arrays.stream(this.vertices).map(Vector3D::clone).collect(Collectors.toList());

        MaxObject object = this;

        while (object != null) {
            double[][] t = object.getTransform();

            Vector3D thisPos = new Vector3D(t[3][0], t[3][1], t[3][2]);
            double[][] thisMatrix = new double[][]{t[0], t[1], t[2]};

            transformVertices = transformVertices.stream().map(v -> v.rotate(thisMatrix)).collect(Collectors.toList());
            transformPosition = transformPosition.rotate(thisMatrix);
            transformPosition = transformPosition.minus(thisPos.clone().multiply(-1));

            object = object.parentObject;
        }

        Double minX = transformVertices.stream().map(Vector3D::getX).min(Double::compareTo).orElseThrow(RuntimeException::new);
        Double maxX = transformVertices.stream().map(Vector3D::getX).max(Double::compareTo).orElseThrow(RuntimeException::new);

        Double minY = transformVertices.stream().map(Vector3D::getY).min(Double::compareTo).orElseThrow(RuntimeException::new);
        Double maxY = transformVertices.stream().map(Vector3D::getY).max(Double::compareTo).orElseThrow(RuntimeException::new);

        Double minZ = transformVertices.stream().map(Vector3D::getZ).min(Double::compareTo).orElseThrow(RuntimeException::new);
        Double maxZ = transformVertices.stream().map(Vector3D::getZ).max(Double::compareTo).orElseThrow(RuntimeException::new);

        Vector3D min = new Vector3D(minX, minY, minZ);
        Vector3D max = new Vector3D(maxX, maxY, maxZ);

        Vector3D v1 = transformPosition.clone().minus(min.clone().multiply(-1));
        Vector3D v2 = transformPosition.clone().minus(max.clone().multiply(-1));

        this.position = new double[]{v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), v1.clone().minus(v2.clone()).magnitude() / 2.0};
    }

    public long getChecksum() {
        return ChecksumGenerator.generateChecksum(Arrays.stream(this.getPolygons())
                .map(LvlPolygon::getArea)
                .sorted(Double::compareTo)
                .map(value -> String.format("%.3f", value))
                .collect(Collectors.joining(";"))
                + Arrays.stream(this.getPolygons())
                .map(LvlPolygon::getEdges)
                .map(e -> e.length)
                .sorted(Integer::compareTo)
                .map(Object::toString)
                .collect(Collectors.joining(";")));
    }

    public Mesh processLighting() {
        MeshLightingProcessor.process(this);

        return this;
    }
}
