package com.artkuznet.converter;

import com.artkuznet.converter.ldb.MaxLDBReader;
import com.artkuznet.converter.lvl.LVL;
import com.artkuznet.converter.obj.OBJ;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(final String[] args) {

        List<String> filenames = Arrays.stream(args)
                .filter(a -> a.toLowerCase().endsWith(".ldb") || a.toLowerCase().endsWith(".obj"))
                .collect(Collectors.toList());

        if (filenames.isEmpty()) {
            throw new RuntimeException("Filename(s) missing");
        }

        Options options = Options.getInstance();

        if (Arrays.stream(args).anyMatch("--skip-join-polygons"::equalsIgnoreCase)) {
            options.skipJoinPolygons = true;
            System.out.println("Skip polygon joining");
        }

        for (String filename : filenames) {
            if (filename.toLowerCase().endsWith(".ldb")) {
                System.out.printf("LDB file: \"%s\"%n", filename);
                saveLvl(filename.replace(".ldb", ".lvl"), new LVL(new MaxLDBReader(filename).getLdb()));
            }
            if (filename.toLowerCase().endsWith(".obj")) {
                System.out.printf("OBJ file: \"%s\"%n", filename);
                saveLvl(filename.replace(".obj", ".lvl"), new LVL(new OBJ(filename)));
            }
        }

        System.out.println("Done");
    }

    private static void saveLvl(String filename, LVL lvl) {
        Writer writer = new Writer(filename);

        List<Byte> bytesList = lvl.toBytes();
        byte[] bytesArray = new byte[bytesList.size()];
        for (int i = 0; i < bytesArray.length; i++) {
            bytesArray[i] = bytesList.get(i);
        }
        writer.writeBytes(bytesArray);

        try {
            writer.save();
            System.out.printf("Saved as: \"%s\"%n%n", filename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
