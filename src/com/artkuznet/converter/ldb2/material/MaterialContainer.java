package com.artkuznet.converter.ldb2.material;

import java.util.ArrayList;
import java.util.List;

public class MaterialContainer {
    private List<LdbMaterial> materials;

    public MaterialContainer() {
        this.materials = new ArrayList<>();
    }

    public LdbMaterial get(int key) {
        return materials.get(key);
    }

    public int size() {
        return materials.size();
    }

    public void add(LdbMaterial material) {
        materials.add(material);
    }

//    public LdbMaterial findMaterialByCategoryAndName(String categoryName, String materialName) {
//        for (LdbMaterial material : materials) {
//            if (material.getCategoryName().equals(categoryName) && material.getMaterialName().equals(materialName)) {
//                return material;
//            }
//        }
//        return null;
//    }

    public LdbMaterial getMaterialById(int id) {
        for (LdbMaterial material : materials) {
            if (material.getId() == id) {
                return material;
            }
        }
        throw new IllegalArgumentException("LdbMaterial not found with id " + id);
    }

    public List<LdbMaterial> getList() {
        return materials;
    }
}
