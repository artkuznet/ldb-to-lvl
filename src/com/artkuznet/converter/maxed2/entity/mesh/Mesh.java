package com.artkuznet.converter.maxed2.entity.mesh;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.util.ChecksumGenerator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mesh extends Entity {

    private MeshProperties properties = new MeshProperties();

    protected boolean trigger = false;
    private boolean flipNormals = false; // false = room

    private boolean meshUnk4;

    private List<Vector3D> vertices = new ArrayList<>();
    private List<Polygon> polygons = new ArrayList<>();

    private int[] unkInt;
    private Vertex[] unkVertex;
    private byte[] unkData;

    public boolean getHasDynamic() {
        return hasDynamic;
    }

    public void setHasDynamic(boolean hasDynamic) {
        if (hasDynamic && !(this instanceof DynamicMesh)) {
            throw new RuntimeException();
        }

        this.hasDynamic = hasDynamic;
    }

    protected boolean hasDynamic;

    public void setTrigger(boolean trigger) {
        if (trigger && !(this instanceof DynamicMesh)) {
            throw new RuntimeException();
        }

        this.trigger = trigger;
    }

    public void setMeshUnk4(boolean meshUnk4) {
        if (meshUnk4 && (this instanceof DynamicMesh)) {
            throw new RuntimeException();
        }

        this.meshUnk4 = meshUnk4;
    }

    public void setFlipNormals(boolean flipNormals) {
        this.flipNormals = flipNormals;
    }

    public boolean isMeshUnk4() {
        return meshUnk4;
    }

    public void setVertices(List<Vector3D> vertices) {
        this.vertices = vertices;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }


    public void setUnkInt(int[] unkInt) {
        if (
                (unkInt[1] & 0xFFFFFF00) != 0
                        || ((unkInt[2] & 0xFFFFFF00) != 0)
                        || ((unkInt[3] & 0xFFFFFF00) != 0)
        ) {
            throw new RuntimeException();
        }
        this.unkInt = unkInt;
    }

    public void setUnkVertex(Vertex[] unkVertex) {
        this.unkVertex = unkVertex;
    }

    public void setUnkData(byte[] unkData) {
        this.unkData = unkData;
    }

    public boolean isTrigger() {
        return trigger;
    }

    public boolean isFlipNormals() {
        return flipNormals;
    }

    public List<Vector3D> getVertices() {
        return vertices;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public int[] getUnkInt() {
        return unkInt;
    }

    public Vertex[] getUnkVertex() {
        return unkVertex;
    }

    public byte[] getUnkData() {
        return unkData;
    }

    public MeshProperties getProperties() {
        return properties;
    }

    public void setProperties(MeshProperties properties) {
        this.properties = properties;
    }

    public Mesh optimize() {
        throw new RuntimeException("not implemented yet");
    }

    public Vector3D getPosition() {
        return new Vector3D(localMatrix[3][0], localMatrix[3][1], localMatrix[3][2]);
    }

    public Vector3D getCenter() {
        List<Vector3D> v = polygons.stream()
                .map(Polygon::getEdges)
                .flatMap(List::stream)
                .map(e -> Arrays.asList(vertices.get(e.from), vertices.get(e.to)))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new Vector3D(
                v.stream().map(Vector3D::getX).reduce(Double::sum).orElseThrow(RuntimeException::new) / v.size(),
                v.stream().map(Vector3D::getY).reduce(Double::sum).orElseThrow(RuntimeException::new) / v.size(),
                v.stream().map(Vector3D::getZ).reduce(Double::sum).orElseThrow(RuntimeException::new) / v.size()
        );
    }

    public Vector3D getSize() {
        return new Vector3D(
                vertices.stream().map(Vector3D::getX).max(Double::compareTo).orElseThrow(RuntimeException::new) - vertices.stream().map(Vector3D::getX).min(Double::compareTo).orElseThrow(RuntimeException::new),
                vertices.stream().map(Vector3D::getY).max(Double::compareTo).orElseThrow(RuntimeException::new) - vertices.stream().map(Vector3D::getY).min(Double::compareTo).orElseThrow(RuntimeException::new),
                vertices.stream().map(Vector3D::getZ).max(Double::compareTo).orElseThrow(RuntimeException::new) - vertices.stream().map(Vector3D::getZ).min(Double::compareTo).orElseThrow(RuntimeException::new)
        );
    }

    public long getChecksum() {
        return ChecksumGenerator.generateChecksum(this.getPolygons().stream()
                .map(Polygon::getArea)
                .sorted(Double::compareTo)
                .map(value -> String.format("%.3f", value))
                .collect(Collectors.joining(";"))
                + this.getPolygons().stream()
                .map(Polygon::getEdges)
                .map(List::size)
                .sorted(Integer::compareTo)
                .map(Object::toString)
                .collect(Collectors.joining(";")));
    }

    public boolean isClosed() {
        return polygons.stream()
                .map(Polygon::getEdges)
                .flatMap(List::stream)
                .map(Polygon.Edge::normalize)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values().stream().allMatch(v -> 2 == v);
    }
}
