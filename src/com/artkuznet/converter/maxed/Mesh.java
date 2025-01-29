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

        List<LvlPolygon> polygonList = new ArrayList<>(Arrays.stream(this.polygons).collect(Collectors.toList()));

        polygonList.forEach(polygon -> {
            List<Short> neighborsToJoin = polygonList.stream()
                    .filter(p -> p.index != polygon.index
                            && p.normal.clone().softSmooth().equals(polygon.normal.clone().softSmooth())
                            && p.getMaterialName().equals(polygon.getMaterialName())
                            && p.getBitmapName().equals(polygon.getBitmapName())
                            && LvlPolygon.hasSharedEdgeAndUV(p, polygon)
                    )
                    .map(LvlPolygon::getIndex).collect(Collectors.toList());
            polygon.neighborToJoinIndices = neighborsToJoin;
        });

        List<List<LvlPolygon>> groupsToJoin = new ArrayList<List<LvlPolygon>>();

        boolean allCollected = false;
        do {
            LvlPolygon notJoinedPolygon = polygonList.stream().filter(p -> !p.joined).findFirst().orElse(null);

            if (Objects.isNull(notJoinedPolygon)) {
                allCollected = true;
                break;
            }

            List<Short> collection = new ArrayList<>();
            collectPolygonsToJoin(collection, polygonList, notJoinedPolygon.index);

            if (collection.isEmpty()) {
                continue;
            }

            groupsToJoin.add(
                    collection.stream()
                            .map(idx -> polygonList.stream()
                                    .filter(p -> p.index == idx).findFirst()
                                    .orElseThrow(null)
                            ).collect(Collectors.toList())
            );

        } while (!allCollected);

        List<LvlPolygon> finalList = new ArrayList<>();

        for (List<LvlPolygon> group : groupsToJoin) {
            if (group.size() == 1) {
                finalList.add(group.get(0));
            } else {
                List<LvlPolygon> mutableGroup = new ArrayList<>(group);
                LvlPolygon p = mutableGroup.remove(0);
                finalList.add(p.join(mutableGroup));
            }
        }

        this.polygons = finalList.toArray(new LvlPolygon[0]);

        return this;
    }

    private void collectPolygonsToJoin(List<Short> container, List<LvlPolygon> polygons, Short pIndex) {
        LvlPolygon p = polygons.stream().filter(lvlPolygon -> lvlPolygon.index == pIndex).findFirst().orElseThrow(null);

        if (p.joined || container.contains(p.index)) {
            p.joined = true;
            return;
        }

        p.joined = true;
        container.add(p.index);

        for (Short n : p.neighborToJoinIndices) {
            collectPolygonsToJoin(container, polygons, n);
        }
    }

    public Mesh buildPolyGroups() {
        Map<Integer, List<LvlPolygon>> groupedPolygons = Arrays.stream(this.polygons).filter(p -> p.geometryPolyGroup != 0)
                .collect(Collectors.groupingBy(LvlPolygon::getGeometryPolyGroup));

        if (!groupedPolygons.isEmpty()) {
            List<PolyGroup> pgList = new ArrayList<>();

            for (Integer polyGroupId : groupedPolygons.keySet()) {
                PolyGroup pg = new PolyGroup();

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

        Double minX = transformVertices.stream().map(Vector3D::getX).min(Double::compareTo).orElseThrow(null);
        Double maxX = transformVertices.stream().map(Vector3D::getX).max(Double::compareTo).orElseThrow(null);

        Double minY = transformVertices.stream().map(Vector3D::getY).min(Double::compareTo).orElseThrow(null);
        Double maxY = transformVertices.stream().map(Vector3D::getY).max(Double::compareTo).orElseThrow(null);

        Double minZ = transformVertices.stream().map(Vector3D::getZ).min(Double::compareTo).orElseThrow(null);
        Double maxZ = transformVertices.stream().map(Vector3D::getZ).max(Double::compareTo).orElseThrow(null);

        Vector3D min = new Vector3D(minX, minY, minZ);
        Vector3D max = new Vector3D(maxX, maxY, maxZ);

        Vector3D v1 = transformPosition.clone().minus(min.clone().multiply(-1));
        Vector3D v2 = transformPosition.clone().minus(max.clone().multiply(-1));

        this.position = new double[]{v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ(), 0};
    }
}
