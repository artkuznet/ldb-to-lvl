package com.artkuznet.converter;

import com.artkuznet.converter.ldb.MaxLDBReader;
import com.artkuznet.converter.lvl.LVL;

import java.util.*;

public class Main {

    public static void main(final String[] args) {

        var ldbFilename = "C:\\Program Files (x86)\\MAX-FX Tools\\MaxEd\\Examples\\BasicRoom.ldb";

        var lvlFilename = Arrays.stream(ldbFilename.split("\\\\"))
                .filter(e -> e.endsWith(".ldb"))
                .findFirst()
                .orElseThrow()
                .replace(".ldb", "_") + Math.abs(new Random().nextInt()) + ".lvl";


        var writer = new Writer(lvlFilename);

        var bytesList = new LVL(new MaxLDBReader(ldbFilename).getLdb()).toBytes();
        var bytesArray = new byte[bytesList.size()];
        for (int i = 0; i < bytesArray.length; i++) {
            bytesArray[i] = bytesList.get(i);
        }
        writer.writeBytes(bytesArray);

        try {
            writer.save();
        } catch (Exception ignored) {
        }
    }
}
