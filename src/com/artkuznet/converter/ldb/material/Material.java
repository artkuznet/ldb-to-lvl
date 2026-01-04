package com.artkuznet.converter.ldb.material;

import com.artkuznet.converter.ldb.texture.Texture;

import java.util.Arrays;

public class Material {

    private static String[] CATEGORIES = {
            "ai_node_collision_nodraw",
            "cameracollision",
            "cardboard",
            "carpet",
            "character",
            "charactercollision_nodraw",
            "couch",
            "default",
            "drape",
            "dummy",
            "electricpanel",
            "externalwall",
            "flesh",
            "glass",
            "glass_bulletproof",
            "graffiti",
            "gravel",
            "laser",
            "leaves",
            "lights",
            "marble",
            "metal",
            "metal_hollow",
            "metal_outside",
            "metal_solid",
            "mirror",
            "nocollision",
            "nodecals",
            "paper",
            "pipes",
            "pipes_steam",
            "plastic",
            "rock",
            "skybox",
            "snow",
            "sortmaterial_high",
            "sortmaterial_medium",
            "stucco",
            "tile",
            "water",
            "water_bottom",
            "watertank",
            "winebarrel",
            "wireframe",
            "wood",
            "woodframe",
    };

    private int idx;

    private int id;

    private String categoryName;

    private String materialName;

    private Texture diffuseTexture;

    private Texture alphaTexture;

    private MaterialProperties properties = new MaterialProperties();

    public Material(int idx, int id, String categoryName, String materialName) {
        this.idx = idx;
        this.id = id;
        this.categoryName = categoryName;
        this.materialName = materialName;
    }

    public static String extractCategoryName(String materialName) {
        String[] categoriesSorted = CATEGORIES.clone();

        Arrays.sort(categoriesSorted, (a, b) -> Integer.compare(b.length(), a.length()));

        for (String category : categoriesSorted) {
            if (materialName.toLowerCase().startsWith(category.toLowerCase())) {
                return category;
            }
        }

        return "default";
    }

    public int getIdx() {
        return idx;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public Texture getAlphaTexture() {
        return alphaTexture;
    }

    public Texture getDiffuseTexture() {
        return diffuseTexture;
    }

    public void setDiffuseTexture(Texture texture) {
        diffuseTexture = texture;
    }

    public void setAlphaTexture(Texture texture) {
        alphaTexture = texture;
    }

    public void setProperties(int hasAlphaTest, int hasAdultContent) {
        properties.setHasAlphaTest(hasAlphaTest);
        properties.setHasAdultContent(hasAdultContent);
    }

    public MaterialProperties getProperties() {
        return properties;
    }
}
