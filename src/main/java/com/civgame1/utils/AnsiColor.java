package com.civgame1.utils;

public final class AnsiColor {
    private AnsiColor() {}

    public static final String RESET   = "\u001B[0m";
    public static final String BOLD    = "\u001B[1m";

    public static final String BLACK   = "\u001B[30m";
    public static final String RED     = "\u001B[31m";
    public static final String GREEN   = "\u001B[32m";
    public static final String YELLOW  = "\u001B[33m";
    public static final String BLUE    = "\u001B[34m";
    public static final String PURPLE  = "\u001B[35m";
    public static final String CYAN    = "\u001B[36m";
    public static final String WHITE   = "\u001B[37m";

    public static final String BG_GREEN  = "\u001B[42m";
    public static final String BG_BLUE   = "\u001B[44m";
    public static final String BG_YELLOW = "\u001B[43m";

    public static String color(String text, String ansiCode) {
        return ansiCode + text + RESET;
    }
}
