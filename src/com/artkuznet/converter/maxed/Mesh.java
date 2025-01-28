package com.artkuznet.converter.maxed;

import com.artkuznet.converter.Vector3D;

import java.util.*;
import java.util.stream.Collectors;

public class Mesh extends MaxObject {

    private Vector3D[] vertices;
    private LvlPolygon[] polygons;
    private PolyGroup[] polyGroups = new PolyGroup[0];

    private boolean isFlipFaces;

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

    public boolean getFlipFaces() {
        return isFlipFaces;
    }

    public void setFlipFaces(final boolean flipFaces) {
        isFlipFaces = flipFaces;
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
        var newVertices = new ArrayList<Vector3D>();

        for (LvlPolygon polygon : polygons) {
            for (int i = 0; i < polygon.getEdges().length; i++) {

                var vFrom = vertices[polygon.getEdges()[i].getFrom()].clone();
                var vTo = vertices[polygon.getEdges()[i].getTo()].clone();

                if (!newVertices.contains(vFrom)) {
                    newVertices.add(vFrom);
                }

                if (!newVertices.contains(vTo)) {
                    newVertices.add(vTo);
                }

                polygon.getEdges()[i] = new LvlPolygon.Edge(newVertices.indexOf(vFrom), newVertices.indexOf(vTo));
            }
        }

        vertices = newVertices.toArray(Vector3D[]::new);

        for (var p : this.polygons) {
            p.parentMesh = this;
        }

        return this;
    }

    public Mesh joinPolygons() {
        for (var polygon : polygons) {
            polygon.calculateTriangles();
        }

        var polygonList = new ArrayList<>(Arrays.stream(this.polygons).toList());

        polygonList.forEach(polygon -> {
            var neighborsToJoin = polygonList.stream()
                    .filter(p -> p.index != polygon.index
                            && p.normal.clone().softSmooth().equals(polygon.normal.clone().softSmooth())
                            && p.getMaterialName().equals(polygon.getMaterialName())
                            && p.getBitmapName().equals(polygon.getBitmapName())
                            && LvlPolygon.hasSharedEdgeAndUV(p, polygon)
                    )
                    .map(LvlPolygon::getIndex).toList();
            polygon.neighborToJoinIndices = neighborsToJoin;
        });

        var groupsToJoin = new ArrayList<List<LvlPolygon>>();

        var allCollected = false;
        do {
            var notJoinedPolygon = polygonList.stream().filter(p -> !p.joined).findFirst().orElse(null);

            if (Objects.isNull(notJoinedPolygon)) {
                allCollected = true;
                break;
            }

            var collection = new ArrayList<Short>();
            collectPolygonsToJoin(collection, polygonList, notJoinedPolygon.index);

            if (collection.isEmpty()) {
                continue;
            }

            groupsToJoin.add(
                    collection.stream()
                            .map(idx -> polygonList.stream()
                                    .filter(p -> p.index == idx).findFirst()
                                    .orElseThrow()
                            ).toList()
            );

        } while (!allCollected);

        var finalList = new ArrayList<LvlPolygon>();

        for (var group : groupsToJoin) {
            if (group.size() == 1) {
                finalList.add(group.get(0));
            } else {
                var mutableGroup = new ArrayList<>(group);
                var p = mutableGroup.remove(0);
                finalList.add(p.join(mutableGroup));
            }
        }

        this.polygons = finalList.toArray(LvlPolygon[]::new);

        return this;
    }

    private void collectPolygonsToJoin(List<Short> container, List<LvlPolygon> polygons, Short pIndex) {

        var p = polygons.stream().filter(lvlPolygon -> lvlPolygon.index == pIndex).findFirst().orElseThrow();

        if (p.joined || container.contains(p.index)) {
            p.joined = true;
            return;
        }

        p.joined = true;
        container.add(p.index);

        for (var n : p.neighborToJoinIndices) {
            collectPolygonsToJoin(container, polygons, n);
        }
    }

    public Mesh buildPolyGroups() {
        var groupedPolygons = Arrays.stream(this.polygons).filter(p -> p.geometryPolyGroup != 0)
                .collect(Collectors.groupingBy(LvlPolygon::getGeometryPolyGroup));

        if (!groupedPolygons.isEmpty()) {
            var pgList = new ArrayList<PolyGroup>();

            for (var polyGroupId : groupedPolygons.keySet()) {
                var pg = new PolyGroup();

                pg.setName("New polygroup");

                pg.setSmoothGeometry(true);
                pg.setSmoothLightMaps(true);

                Float maxAngle = null;
                Float maxEdgeLength = null;

                List<Integer> polygonNumbers = new ArrayList<>();

                for (int i = 0; i < this.polygons.length; i++) {
                    if (this.polygons[i].geometryPolyGroup == polyGroupId) {
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

            this.polyGroups = pgList.toArray(PolyGroup[]::new);
        }

        return this;
    }

    @Override
    public double[] getPosition() {
        calculatePosition();
        return position;
    }

    public void calculatePosition() {
        var transformPosition = new Vector3D(0, 0, 0);
        var transformVertices = Arrays.stream(this.vertices).map(Vector3D::clone).toList();

        MaxObject object = this;

        while (object != null) {
            var t = object.getTransform();

            var thisPos = new Vector3D(t[3][0], t[3][1], t[3][2]);
            var thisMatrix = new double[][]{t[0], t[1], t[2]};

            transformVertices = transformVertices.stream().map(v -> v.rotate(thisMatrix)).toList();
            transformPosition = transformPosition.rotate(thisMatrix);
            transformPosition = transformPosition.minus(thisPos.clone().multiply(-1));

            object = object.parentObject;
        }

        var minX = transformVertices.stream().map(Vector3D::getX).min(Double::compareTo).orElseThrow();
        var maxX = transformVertices.stream().map(Vector3D::getX).max(Double::compareTo).orElseThrow();

        var minY = transformVertices.stream().map(Vector3D::getY).min(Double::compareTo).orElseThrow();
        var maxY = transformVertices.stream().map(Vector3D::getY).max(Double::compareTo).orElseThrow();

        var minZ = transformVertices.stream().map(Vector3D::getZ).min(Double::compareTo).orElseThrow();
        var maxZ = transformVertices.stream().map(Vector3D::getZ).max(Double::compareTo).orElseThrow();

        var min = new Vector3D(minX, minY, minZ);
        var max = new Vector3D(maxX, maxY, maxZ);

        var v1 = transformPosition.clone().minus(min.clone().multiply(-1));
        var v2 = transformPosition.clone().minus(max.clone().multiply(-1));

        this.position = new double[]{v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), 0};
    }
}
