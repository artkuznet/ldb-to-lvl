package com.artkuznet.converter.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MTL {
    private static final String NEWMTL = "newmtl";
    private static final String MAP_KD = "map_Kd";
    private static final String ERROR_MSG = "Unable to parse mtl file";

    public static class Material {
        private String name;
        private String diffuseFilename;

        public Material(String name, String filenameDiffuse) {
            this.name = name;
            this.diffuseFilename = filenameDiffuse.replace("\\", "/");
        }

        public String getName() {
            return name;
        }

        public String getDiffuseFilename() {
            return diffuseFilename;
        }
    }

    private List<Material> materials = new ArrayList<>();

    public List<Material> getMaterials() {
        return materials;
    }

    public MTL(String filename) {
        try {
            BufferedReader sr = new BufferedReader(new FileReader(filename));

            String dirName = new File(filename).getParent();

            String materialName = null;
            String filenameDiffuse = null;

            String line;
            while ((line = sr.readLine()) != null) {
                line = line.trim();

                if (line.startsWith(NEWMTL + " ")) {
                    if (materialName != null) {
                        throw new RuntimeException(ERROR_MSG);
                    }
                    materialName = line.substring(NEWMTL.length()).trim();
                }
                if (line.startsWith(MAP_KD + " ")) {
                    if (filenameDiffuse != null) {
                        throw new RuntimeException(ERROR_MSG);
                    }
                    filenameDiffuse = line.substring(MAP_KD.length()).trim();
                }

                if (materialName != null && filenameDiffuse != null) {
                    String filenameDiffuseFull = filenameDiffuse.contains(":") || dirName == null
                            ? filenameDiffuse
                            : dirName + "\\" + filenameDiffuse;
                    if (materials.stream().anyMatch(m -> m.getDiffuseFilename().equals(filenameDiffuseFull))) {
                        throw new RuntimeException("Material filename collision: " + filenameDiffuseFull);
                    }
                    materials.add(new Material(materialName, filenameDiffuseFull));

                    materialName = null;
                    filenameDiffuse = null;
                }
            }

            if (materials.isEmpty()) {
                System.out.println("No materials found!");
            }

            sr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
