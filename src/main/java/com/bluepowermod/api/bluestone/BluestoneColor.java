package com.bluepowermod.api.bluestone;

public enum BluestoneColor {

    INVALID(-2, "invalid", false), //
    NONE(-1, "none", false), //
    WHITE(0, "white", true), //
    ORANGE(1, "orange", true), //
    MAGENTA(2, "magenta", true), //
    LIGHTBLUE(3, "light_blue", true), //
    YELLOW(4, "yellow", true), //
    LIME(5, "lime", true), //
    PINK(6, "pink", true), //
    GRAY(7, "gray", true), //
    LIGHTGRAY(8, "silver", true), //
    CYAN(9, "cyan", true), //
    PURPLE(10, "purple", true), //
    BLUE(11, "blue", true), //
    BROWN(12, "brown", true), //
    GREEN(13, "green", true), //
    RED(14, "red", true), //
    BLACK(15, "black", true);

    public static final BluestoneColor[] VALID_COLORS = new BluestoneColor[] {//
        WHITE, ORANGE, MAGENTA, LIGHTBLUE, YELLOW, LIME, PINK, GRAY, LIGHTGRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK //
    };

    private int color;
    private String name;
    private boolean valid;

    private BluestoneColor(int color, String name, boolean valid) {

        this.color = color;
        this.name = name;
        this.valid = valid;
    }

    public int getColor() {

        return color;
    }

    public String getName() {

        return name;
    }

    public boolean isValid() {

        return valid;
    }

}
