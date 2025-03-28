package com.artkuznet.converter;

import com.artkuznet.converter.ldb.MaxLDBReader;
import com.artkuznet.converter.lvl.LVL;
import com.artkuznet.converter.obj.OBJ;

import java.util.*;

public class Main {

    public static void main(final String[] args) {

        Options options = Options.getInstance();

        if (Arrays.stream(args).anyMatch("--skip-join-polygons"::equalsIgnoreCase)) {
            options.skipJoinPolygons = true;
            System.out.println("Skip polygon joining");
        }

        Optional<String> scaleArg = Arrays.stream(args)
                .map(String::toLowerCase)
                .filter(s1 -> s1.startsWith("--scale="))
                .findFirst();

        if (scaleArg.isPresent()) {
            options.scale = Double.parseDouble(scaleArg.get().substring("--scale=".length()).replace(",", "."));
            System.out.println("Scale = " + options.scale);
        }

        String ldbFilename = Arrays.stream(args).filter(a -> a.toLowerCase().endsWith(".ldb")).findFirst().orElse(null);
        String objFilename = Arrays.stream(args).filter(a -> a.toLowerCase().endsWith(".obj")).findFirst().orElse(null);

        // todo refactor

        if (objFilename != null) {

            System.out.println("OBJ file: " + objFilename);

            try {
                String lvlFilename = objFilename.replace(".obj", ".lvl");

                Writer writer = new Writer(lvlFilename);
                List<Byte> bytesList = new LVL(new OBJ(objFilename)).toBytes();
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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return;
        }

        if (ldbFilename == null) {
            throw new RuntimeException("Missing .ldb filename");
        }

        System.out.println("LDB file: " + ldbFilename);

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
