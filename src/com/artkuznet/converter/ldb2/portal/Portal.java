package com.artkuznet.converter.ldb2.portal;

import com.artkuznet.converter.ldb.vertex.Vertex;

import java.util.List;

public class Portal {
    private String name;
    private Vertex normal;
    private int unk1;
    private int unk2;
    private List<Vertex> points;

    public Portal(String name, Vertex normal, int unk1, int unk2, List<Vertex> points) {
        this.name = name;
        this.normal = normal;
        this.unk1 = unk1;
        this.unk2 = unk2;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vertex getNormal() {
        return normal;
    }

    public void setNormal(Vertex normal) {
        this.normal = normal;
    }

    public int getUnk1() {
        return unk1;
    }

    public void setUnk1(int unk1) {
        this.unk1 = unk1;
    }

    public int getUnk2() {
        return unk2;
    }

    public void setUnk2(int unk2) {
        this.unk2 = unk2;
    }

    public List<Vertex> getPoints() {
        return points;
    }

    public void setPoints(List<Vertex> points) {
        this.points = points;
    }
}
