package com.artkuznet.converter.maxed2.document;

public class DocumentPreferences {

    public static class Color {
        private int R = 255;
        private int G = 255;
        private int B = 255;
        private int A = 255;

        public Color(int R, int G, int B, int A) {
            this.R = R;
            this.G = G;
            this.B = B;
            this.A = A;
        }

        public Color(int R, int G, int B) {
            this.R = R;
            this.G = G;
            this.B = B;
        }

        public Color(int value) {
            this.R = value & 0xFF;
            this.G = (value & 0xFF00) >> 8;
            this.B = (value & 0xFF0000) >> 16;
        }

        public int toInt() {
            return R | (G << 8) | (B << 16);
        }

        public int getR() {
            return R;
        }

        public int getG() {
            return G;
        }

        public int getB() {
            return B;
        }

        public int getA() {
            return A;
        }
    }

    private int unk1 = 0;
    private double backplane = 200;
    private double frontplane = 0.1;
    private double FOV = 70;
    private boolean unk2 = false;
    private int objectMode = 2;
    private int connectedRooms = 3;
    private double unk3 = 2;
    private String angleSnap = "0.5;1;1.25;2;2.5;5;10;11.25;15;22.5;45";
    private String gridScaleSteps = "0.0078125;0.015625;0.03125;0.0625;0.125;0.25;0.5;1.0;2.0;4.0;8.0;16.0;32.0;64.0;128.0;256.0;512.0";
    private int defaultUVScale = 128;
    private double lightmapRes = 2;
    private boolean averageLightmapBorder = false;
    private int viewportSize = 512;
    private double globalLightmapRes = 100;
    private Color defaultLightColor = new Color(255, 255, 255);
    private float defaultLightIntensity = 100;
    private float gisMaxEnergyPerShoot = 2;
    private float gisStopAtEnergy = 0.5f;
    private boolean ignorePortals = false;
    private String ldbExportParameters = "";
    private Color backgroudColor = new Color(0, 0, 0);

    public void setUnk1(int unk1) {
        this.unk1 = unk1;
    }

    public void setBackplane(double backplane) {
        this.backplane = backplane;
    }

    public void setFrontplane(double frontplane) {
        this.frontplane = frontplane;
    }

    public void setFOV(double FOV) {
        this.FOV = FOV;
    }

    public void setUnk2(boolean unk2) {
        this.unk2 = unk2;
    }

    public void setObjectMode(int objectMode) {
        this.objectMode = objectMode;
    }

    public void setConnectedRooms(int connectedRooms) {
        this.connectedRooms = connectedRooms;
    }

    public void setUnk3(double unk3) {
        this.unk3 = unk3;
    }

    public void setAngleSnap(String angleSnap) {
        this.angleSnap = angleSnap;
    }

    public void setGridScaleSteps(String gridScaleSteps) {
        this.gridScaleSteps = gridScaleSteps;
    }

    public void setDefaultUVScale(int defaultUVScale) {
        this.defaultUVScale = defaultUVScale;
    }

    public void setLightmapRes(double lightmapRes) {
        this.lightmapRes = lightmapRes;
    }

    public void setAverageLightmapBorder(boolean averageLightmapBorder) {
        this.averageLightmapBorder = averageLightmapBorder;
    }

    public void setViewportSize(int viewportSize) {
        this.viewportSize = viewportSize;
    }

    public void setGlobalLightmapRes(double globalLightmapRes) {
        this.globalLightmapRes = globalLightmapRes;
    }

    public void setDefaultLightColor(Color defaultLightColor) {
        this.defaultLightColor = defaultLightColor;
    }

    public void setDefaultLightIntensity(float defaultLightIntensity) {
        this.defaultLightIntensity = defaultLightIntensity;
    }

    public void setGisMaxEnergyPerShoot(float gisMaxEnergyPerShoot) {
        this.gisMaxEnergyPerShoot = gisMaxEnergyPerShoot;
    }

    public void setGisStopAtEnergy(float gisStopAtEnergy) {
        this.gisStopAtEnergy = gisStopAtEnergy;
    }

    public void setIgnorePortals(boolean ignorePortals) {
        this.ignorePortals = ignorePortals;
    }

    public void setLdbExportParameters(String ldbExportParameters) {
        this.ldbExportParameters = ldbExportParameters;
    }

    public void setBackgroudColor(Color backgroudColor) {
        this.backgroudColor = backgroudColor;
    }

    public int getUnk1() {
        return unk1;
    }

    public double getBackplane() {
        return backplane;
    }

    public double getFrontplane() {
        return frontplane;
    }

    public double getFOV() {
        return FOV;
    }

    public boolean getUnk2() {
        return unk2;
    }

    public int getObjectMode() {
        return objectMode;
    }

    public int getConnectedRooms() {
        return connectedRooms;
    }

    public double getUnk3() {
        return unk3;
    }

    public String getAngleSnap() {
        return angleSnap;
    }

    public String getGridScaleSteps() {
        return gridScaleSteps;
    }

    public int getDefaultUVScale() {
        return defaultUVScale;
    }

    public double getLightmapRes() {
        return lightmapRes;
    }

    public boolean isAverageLightmapBorder() {
        return averageLightmapBorder;
    }

    public int getViewportSize() {
        return viewportSize;
    }

    public double getGlobalLightmapRes() {
        return globalLightmapRes;
    }

    public Color getDefaultLightColor() {
        return defaultLightColor;
    }

    public float getDefaultLightIntensity() {
        return defaultLightIntensity;
    }

    public float getGisMaxEnergyPerShoot() {
        return gisMaxEnergyPerShoot;
    }

    public float getGisStopAtEnergy() {
        return gisStopAtEnergy;
    }

    public boolean isIgnorePortals() {
        return ignorePortals;
    }

    public String getLdbExportParameters() {
        return ldbExportParameters;
    }

    public Color getBackgroudColor() {
        return backgroudColor;
    }
}
