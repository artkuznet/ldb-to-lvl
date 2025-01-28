package com.artkuznet.converter.ldb.waypoint;

import java.util.ArrayList;
import java.util.List;

public class WaypointContainer {

    private List<Waypoint> waypoints = new ArrayList<>();

    public void add(Waypoint waypoint) {
        waypoints.add(waypoint);
    }

    public List<Waypoint> getList() {
        return waypoints;
    }
}
