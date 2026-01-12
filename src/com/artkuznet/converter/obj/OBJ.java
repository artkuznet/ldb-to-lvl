package com.artkuznet.converter.obj;

import com.artkuznet.converter.Vector3D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OBJ {
    private MTL mtl;
    private List<Object3D> objects = new ArrayList<>();

    private static final String MTLLIB = "mtllib";
    private static final String O = "o";
    private static final String V = "v";
    private static final String VN = "vn";
    private static final String VT = "vt";
    private static final String USEMTL = "usemtl";
    private static final String F = "f";
    private static final String ERROR_MSG = "Unable to parse obj file";

    public static class Object3D {
        private String name;
        private List<Vector3D> vertices = new ArrayList<>();
        private List<Vector3D> normals = new ArrayList<>();
        private List<UV> uv = new ArrayList<>();
        private List<Face> faces = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void addVertex(Vector3D vertex) {
            vertices.add(vertex);
        }

        public List<Vector3D> getVertices() {
            return vertices;
        }

        public List<UV> getUV() {
            return uv;
        }

        public void addUV(UV uv) {
            this.uv.add(new UV(uv.getU(), -uv.getV()));
        }

        public void addNormal(Vector3D normal) {
            normals.add(normal);
        }

        public void addFace(Face face) {
            faces.add(face);
        }

        public List<Face> getFaces() {
            return faces;
        }

        public int getMinVertexIndex() {
            int min = Integer.MAX_VALUE;

            for (Face face : faces) {
                for (Face.Vertex v : face.getVertices()) {
                    if (v.getIndex() < min) {
                        min = v.getIndex();
                    }
                }
            }
            return min;
        }

        public int getMinUvIndex() {
            int min = Integer.MAX_VALUE;

            for (Face face : faces) {
                for (Face.Vertex v : face.getVertices()) {
                    if (v.getUvIndex() < min) {
                        min = v.getUvIndex();
                    }
                }
            }
            return min;
        }
    }

    public static class UV {
        private double u;
        private double v;

        public UV(double u, double v) {
            this.u = u;
            this.v = v;
        }

        public double getU() {
            return u;
        }

        public double getV() {
            return v;
        }
    }

    public static class Face {
        public static class Vertex {
            private int vertexIndex;
            private int uvIndex;
            private int normalIndex;

            public Vertex(int vertexIndex, int uvIndex, int normalIndex) {
                this.vertexIndex = vertexIndex;
                this.uvIndex = uvIndex;
                this.normalIndex = normalIndex;
            }

            public int getIndex() {
                return vertexIndex;
            }

            public int getUvIndex() {
                return uvIndex;
            }
        }

        private List<Vertex> vertices;
        private String materialName;
        private Object3D parentObject;

        public Face(List<Vertex> vertices, String materialName, Object3D parentObject) {
            this.vertices = vertices;
            this.materialName = materialName;
            this.parentObject = parentObject;
        }

        public List<Vertex> getVertices() {
            return vertices;
        }

        public String getMaterialName() {
            return materialName;
        }

        public List<UV> getUV() {
            int parentObjectMinUvIndex = parentObject.getMinUvIndex();

            List<UV> parentUV = parentObject.getUV();
            List<UV> uv = new ArrayList<>();
            for (Vertex v : vertices) {
                uv.add(parentUV.get(v.getUvIndex() - parentObjectMinUvIndex));
            }

            return uv;
        }
    }

    public List<Object3D> getObjects() {
        return objects;
    }

    public MTL getMTL() {
        return mtl;
    }

    public void addObject3D(Object3D object3D) {
        objects.add(object3D);
    }

    public OBJ() {

    }

    public OBJ(String filename) {
        try {
            BufferedReader sr = new BufferedReader(new FileReader(filename));

            String dirName = new File(filename).getParent();

            String materialName = null;
            String objectName = null;

            Object3D o = new Object3D();

            String line;
            while ((line = sr.readLine()) != null) {
                line = line.trim();

                if (line.startsWith(MTLLIB + " ")) {
                    if (mtl != null) {
                        throw new RuntimeException(ERROR_MSG);
                    }
                    mtl = new MTL((dirName != null ? dirName + "\\" : "") + line.substring(MTLLIB.length()).trim());
                }

                if (line.startsWith(V + " ")) {
                    String[] vertexStr = line.substring(V.length()).trim().split(" ");
                    if (vertexStr.length != 3) {
                        throw new RuntimeException(ERROR_MSG);
                    }
                    o.addVertex(new Vector3D(toDouble(vertexStr[0]), toDouble(vertexStr[1]), toDouble(vertexStr[2])));
                }

                if (line.startsWith(VN + " ")) {
                    String[] normalStr = line.substring(VN.length()).trim().split(" ");
                    if (normalStr.length != 3) {
                        throw new RuntimeException(ERROR_MSG);
                    }
                    o.addNormal(new Vector3D(toDouble(normalStr[0]), toDouble(normalStr[1]), toDouble(normalStr[2])));
                }

                if (line.startsWith(VT + " ")) {
                    String[] uvStr = line.substring(VT.length()).trim().split(" ");
                    if (uvStr.length != 2 && uvStr.length != 3) {
                        throw new RuntimeException(ERROR_MSG);
                    }
                    o.addUV(new UV(toDouble(uvStr[0]), toDouble(uvStr[1])));
                }

                if (line.startsWith(USEMTL + " ")) {
                    materialName = line.substring(USEMTL.length()).trim();
                }

                if (line.startsWith(O + " ")) {
                    if (objectName != null) {
                        objects.add(o);
                        o = new Object3D();
                    }
                    objectName = line.substring(O.length()).trim();
                    o.setName(objectName);
                }

                if (line.startsWith(F + " ")) {
                    String[] faceStr = line.substring(F.length()).trim().split(" ");
                    if (faceStr.length < 3) {
                        throw new RuntimeException(ERROR_MSG);
                    }

                    List<Face.Vertex> faceVertices = new ArrayList<>();
                    for (String fStr : faceStr) {
                        String[] vStr = fStr.split("/");

                        int vIndex = Integer.parseInt(vStr[0]) - 1;
                        int vtIndex = vStr.length > 1 && vStr[1].length() > 0 ? (Integer.parseInt(vStr[1]) - 1) : -1;
                        int vnIndex = vStr.length > 2 ? (Integer.parseInt(vStr[2]) - 1) : -1;

                        faceVertices.add(new Face.Vertex(vIndex, vtIndex, vnIndex));
                    }

                    Collections.reverse(faceVertices);

                    o.addFace(new Face(faceVertices, materialName, o));
                }
            }

            objects.add(o);
            sr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String asText() {
        int vertexOffset = 0;
        int uvOffset = 0;

        StringBuilder sb = new StringBuilder();

        for (Object3D object3D : objects) {
            sb.append(O + " ").append(object3D.name).append("\r\n");
            for (Vector3D vertex : object3D.vertices) {
                sb.append(V + " ");
                sb.append(" ").append(formatDouble(vertex.getX()));
                sb.append(" ").append(formatDouble(vertex.getY()));
                sb.append(" ").append(formatDouble(-vertex.getZ()));
                sb.append("\r\n");
            }

            for (UV uv : object3D.uv) {
                sb.append(VT + " ");
                sb.append(" ").append(formatDouble(uv.getU()));
                sb.append(" ").append(formatDouble(uv.getV()));
                sb.append("\r\n");
            }

            for (Vector3D vertex : object3D.normals) {
                sb.append(VN + " ");
                sb.append(" ").append(formatDouble(vertex.getX()));
                sb.append(" ").append(formatDouble(vertex.getY()));
                sb.append(" ").append(formatDouble(vertex.getZ()));
                sb.append("\r\n");
            }

            String currentMaterialName = null;

            for (Face face : object3D.faces) {
                if (!Objects.equals(currentMaterialName, face.getMaterialName())) {
                    sb.append(USEMTL + " ").append(face.getMaterialName()).append("\r\n");
                    currentMaterialName = face.getMaterialName();
                }
                sb.append(F + " ");
                for (Face.Vertex vertex : face.getVertices()) {
                    sb.append(" ").append(vertex.getIndex() + vertexOffset);
                    sb.append("/").append(vertex.getUvIndex() + uvOffset);
                }
                sb.append("\r\n");
            }

            vertexOffset += object3D.vertices.size();
            uvOffset += object3D.uv.size();
        }

        return sb.toString();

    }

    private static String formatDouble(double d) {
        return String.format("%.6f", d).replace(",", ".");
    }

    private static double toDouble(String value) {
        return Double.parseDouble(value);
    }
}
