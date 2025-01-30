package com.artkuznet.converter.maxed2.material;

import java.util.ArrayList;
import java.util.List;

public class MaterialCategory {

    private String name;

    private List<Material> materials = new ArrayList<>();

    public MaterialCategory() {

    }

    public MaterialCategory(String name, List<Material> materials) {
        this.name = name;
        this.materials = materials;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMaterial(Material material) {
        materials.add(material);
    }

    public String getName() {
        return name;
    }

    public List<Material> getMaterials() {
        return materials;
    }
}
