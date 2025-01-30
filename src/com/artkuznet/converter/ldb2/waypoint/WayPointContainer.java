package com.artkuznet.converter.ldb2.waypoint;

import java.util.ArrayList;
import java.util.List;

public class WayPointContainer {
    private List<WayPoint> wayPoints;

    public WayPointContainer() {
        this.wayPoints = new ArrayList<>();
    }

    public WayPoint get(int key) {
        return wayPoints.get(key);
    }

    public void set(int key, WayPoint value) {
        wayPoints.set(key, value);
    }

    public int size() {
        return wayPoints.size();
    }

    public void add(WayPoint wayPoint) {
        wayPoints.add(wayPoint);
    }

    public List<WayPoint> getList() {
        return wayPoints;
    }
}
