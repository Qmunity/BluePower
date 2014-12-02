package com.bluepowermod.api.redstone;

public enum RedstoneColor {

    WHITE, //
    ORANGE, //
    MAGENTA, //
    LIGHT_BLUE, //
    YELLOW, //
    LIME, //
    PINK, //
    GRAY, //
    LIGHT_GRAY, //
    CYAN, //
    PURPLE, //
    BLUE, //
    BROWN, //
    GREEN, //
    RED, //
    BLACK, //
    NONE, //
    ANY;

    public static RedstoneColor[] VALID_COLORS = { WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE,
        BLUE, BROWN, GREEN, RED, BLACK, NONE };

    public boolean matches(RedstoneColor color) {

        if (this == ANY || color == ANY)
            return true;

        return this == color;
    }

    public boolean canConnect(RedstoneColor color) {

        if (this == ANY || color == ANY || this == NONE || color == NONE)
            return true;

        return this == color;
    }

}
