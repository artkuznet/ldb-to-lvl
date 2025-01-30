package com.artkuznet.converter;

import com.artkuznet.converter.ldb.MaxLDBReader;
import com.artkuznet.converter.lvl.LVL;

import java.util.*;

public class Main {

    public static void main(final String[] args) {
        String ldbFilename = Arrays.stream(args).filter(a -> a.toLowerCase().endsWith(".ldb")).findFirst().orElse(null);

        if (ldbFilename == null) {
            throw new RuntimeException("Missing .ldb filename");
        }

        System.out.println("LDB file: " + ldbFilename);

        Options options = Options.getInstance();

        if (Arrays.stream(args).anyMatch("--skip-join-polygons"::equalsIgnoreCase)) {
            options.skipJoinPolygons = true;
            System.out.println("Skip polygon joining");
        }
        if (Arrays.stream(args).anyMatch("--skip-dynamic-fsm"::equalsIgnoreCase)) {
            options.skipDynamicFSM = true;
            System.out.println("Skip dynamic mesh fsm data");
        }

        String lvlFilename = ldbFilename.replace(".ldb", ".lvl");

        Writer writer = new Writer(lvlFilename);

        List<Byte> bytesList = new LVL(new MaxLDBReader(ldbFilename).getLdb()).toBytes();
        byte[] bytesArray = new byte[bytesList.size()];
        for (int i = 0; i < bytesArray.length; i++) {
            bytesArray[i] = bytesList.get(i);
        }
        writer.writeBytes(bytesArray);

        try {
            writer.save();
            System.out.println("Saved as: " + lvlFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Done");
    }
}
