package com.artkuznet.converter.util;

public class Out {

    public enum Color {
        RESET("\033[0m"),

        BLACK("\033[0;30m"),
        RED("\033[0;31m"),
        GREEN("\033[0;32m"),
        YELLOW("\033[0;33m"),
        BLUE("\033[0;34m"),
        MAGENTA("\033[0;35m"),
        CYAN("\033[0;36m"),
        WHITE("\033[0;37m");

        private final String code;

        Color(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }

    public static void println(String text) {
        println(text, Color.RESET);
    }

    public static void println(String text, Color color) {
        System.out.printf("%s%s%s%n", color, text, Color.RESET);
    }
}
