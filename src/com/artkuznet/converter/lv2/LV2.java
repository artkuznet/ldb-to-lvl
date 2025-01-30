package com.artkuznet.converter.lv2;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.lv2.converter.MeshConverter;
import com.artkuznet.converter.mapper.Object3DMapper;
import com.artkuznet.converter.maxed2.document.DocumentPreferences;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.WorldGroup;
import com.artkuznet.converter.maxed2.material.Material;
import com.artkuznet.converter.maxed2.material.MaterialCategory;
import com.artkuznet.converter.maxed2.material.MaterialType;
import com.artkuznet.converter.maxed2.material.Texture;
import com.artkuznet.converter.obj.MTL;
import com.artkuznet.converter.obj.OBJ;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class LV2 {
    private String maxEdVersion = "MAXED1.1";
    private String maxEdBuild = " 2.0 build 103";
    private List<Texture> textures = new ArrayList<>();
    private List<MaterialCategory> materialCategories = new ArrayList<>();
    private DocumentPreferences preferences = new DocumentPreferences();
    private Entity worldGroup;
    private Vector3D cameraPosition = new Vector3D(0, 8, -8);
    private Vertex cameraRotation = new Vertex(0.7853982f, 0, 0);
    private double unkCam = 1;
    private byte[] unkData2 = new byte[]{8, 0, 8, 1};
    private byte[] lightmapData;
    private List<String> groups = new ArrayList<>(Arrays.asList("", "PLAYER_GROUP"));

    public LV2() {

    }

    public LV2(OBJ obj) {

        for (MTL.Material mtlMaterial : obj.getMTL().getMaterials()) {
            final byte[] bitmapData;
            try {
                bitmapData = Files.readAllBytes(Paths.get(mtlMaterial.getDiffuseFilename()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            textures.add(new Texture(mtlMaterial.getName(), bitmapData));
        }

        this.setMaterialCategories(Collections.singletonList(
                new MaterialCategory(
                        MaterialType.DEFAULT.name().toLowerCase(),
                        this.textures.stream()
                                .map(texture -> new Material(texture.getFilePath()))
                                .collect(Collectors.toList())
                )
        ));

        WorldGroup worldGroup = WorldGroup.getEmpty();

        Object3DMapper.convert(obj, new HashMap<>()).stream()
                .map(MeshConverter::convert)
                .forEach(mesh -> {
                    mesh.setParentEntity(worldGroup);
                    worldGroup.addChildEntity(mesh);
                });

        this.setWorldGroup(worldGroup);
    }

    public void setMaxEdVersion(String maxEdVersion) {
        this.maxEdVersion = maxEdVersion;
    }

    public void setMaxEdBuild(String maxEdBuild) {
        this.maxEdBuild = maxEdBuild;
    }

    public void setTextures(List<Texture> textures) {
        this.textures = textures;
    }

    public void addTextures(List<Texture> textures) {
        this.textures.addAll(textures);
    }

    public void setMaterialCategories(List<MaterialCategory> materialCategories) {
        this.materialCategories = materialCategories;
    }

    public void setPreferences(DocumentPreferences preferences) {
        this.preferences = preferences;
    }

    public void setWorldGroup(Entity worldGroup) {
        if (!(worldGroup instanceof WorldGroup)) {
            throw new RuntimeException();
        }
        this.worldGroup = worldGroup;
    }

    public void setCameraPosition(Vector3D cameraPosition) {
        this.cameraPosition = cameraPosition;
    }

    public void setCameraRotation(Vertex cameraRotation) {
        this.cameraRotation = cameraRotation;
    }

    public void setUnkCam(double unkCam) {
        this.unkCam = unkCam;
    }

    public void setUnkData2(byte[] unkData2) {
        this.unkData2 = unkData2;
    }

    public void setLightmapData(byte[] lightmapData) {
        this.lightmapData = lightmapData;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getMaxEdVersion() {
        return maxEdVersion;
    }

    public String getMaxEdBuild() {
        return maxEdBuild;
    }

    public List<Texture> getTextures() {
        return textures;
    }

    public List<MaterialCategory> getMaterialCategories() {
        return materialCategories;
    }

    public DocumentPreferences getPreferences() {
        return preferences;
    }

    public Entity getWorldGroup() {
        return worldGroup;
    }

    public Vector3D getCameraPosition() {
        return cameraPosition;
    }

    public Vertex getCameraRotation() {
        return cameraRotation;
    }

    public double getUnkCam() {
        return unkCam;
    }

    public byte[] getUnkData2() {
        return unkData2;
    }

    public byte[] getLightmapData() {
        return lightmapData;
    }

    public List<String> getGroups() {
        return groups;
    }
}
