package com.artkuznet.converter.ldb.material;

import java.util.ArrayList;
import java.util.List;

public class MaterialContainer {

    private List<Material> materials = new ArrayList<>();

    public void add(Material material) {
        materials.add(material);
    }

    public List<Material> getList() {
        return materials;
    }

    public Material findMaterialByCategoryAndName(String categoryName, String materialName) {
        return materials.stream()
                .filter(material ->
                        material.getCategoryName().equals(categoryName)
                                && material.getMaterialName().equals(materialName)
                )
                .findFirst()
                .orElse(null);
    }

    public Material getMaterialByIndex(int idx) {
        return materials.stream()
                .filter(material -> material.getIdx() == idx)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Material not found with index " + idx));
    }
}
