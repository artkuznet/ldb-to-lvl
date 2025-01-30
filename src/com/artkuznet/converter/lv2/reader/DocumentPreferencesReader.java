package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.document.DocumentPreferences;

public class DocumentPreferencesReader {

    public static DocumentPreferences read(MaxTypeReader reader) {

        reader.rememberOffset();

        ReaderHelper.read600(reader);

        int dataSize = (int) reader.readObject();

        DocumentPreferences preferences = new DocumentPreferences();

        preferences.setUnk1((int) reader.readObject());
        preferences.setBackplane((double) reader.readObject());
        preferences.setFrontplane((double) reader.readObject());
        preferences.setFOV((double) reader.readObject());
        preferences.setUnk2(1 == (int) reader.readObject());
        preferences.setObjectMode((int) reader.readObject());
        preferences.setConnectedRooms((int) reader.readObject());
        preferences.setUnk3((double) reader.readObject());
        preferences.setAngleSnap((String) reader.readObject());
        preferences.setGridScaleSteps((String) reader.readObject());
        preferences.setDefaultUVScale((int) reader.readObject());
        preferences.setLightmapRes((double) reader.readObject());
        preferences.setAverageLightmapBorder(1 == (int) reader.readObject());
        preferences.setViewportSize((int) reader.readObject());
        preferences.setGlobalLightmapRes((double) reader.readObject());
        preferences.setDefaultLightColor(new DocumentPreferences.Color((int) reader.readObject()));
        preferences.setDefaultLightIntensity((float) reader.readObject());
        preferences.setGisMaxEnergyPerShoot((float) reader.readObject());
        preferences.setGisStopAtEnergy((float) reader.readObject());
        preferences.setIgnorePortals(1 == (int) reader.readObject());
        preferences.setLdbExportParameters((String) reader.readObject());
        preferences.setBackgroudColor(new DocumentPreferences.Color(
                (int) reader.readObject(),
                (int) reader.readObject(),
                (int) reader.readObject(),
                (int) reader.readObject()
        ));

        reader.validateDataSize(dataSize);

        return preferences;
    }
}
