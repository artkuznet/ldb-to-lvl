package com.artkuznet.converter;

import com.artkuznet.converter.ldb.MaxLDBReader;
import com.artkuznet.converter.lvl.LVL;

import java.util.*;

public class Main {

    public static void main(final String[] args) {
        for (String ldbFilename : args) {
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
            } catch (Exception ignored) {
            }
        }
    }
}
