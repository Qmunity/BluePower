package com.bluepowermod.api.bluestone;

public enum BluestoneConnectionType {

    BLUESTONE_DIGITAL(-1), //
    BLUESTONE_WHITE_DIGITAL(0), //
    BLUESTONE_ORANGE_DIGITAL(1), //
    BLUESTONE_MAGENTA_DIGITAL(2), //
    BLUESTONE_LIGHTBLUE_DIGITAL(3), //
    BLUESTONE_YELLOW_DIGITAL(4), //
    BLUESTONE_LIME_DIGITAL(5), //
    BLUESTONE_PINK_DIGITAL(6), //
    BLUESTONE_GRAY_DIGITAL(7), //
    BLUESTONE_LIGHTGRAY_DIGITAL(8), //
    BLUESTONE_CYAN_DIGITAL(9), //
    BLUESTONE_PURPLE_DIGITAL(10), //
    BLUESTONE_BLUE_DIGITAL(11), //
    BLUESTONE_BROWN_DIGITAL(12), //
    BLUESTONE_GREEN_DIGITAL(13), //
    BLUESTONE_RED_DIGITAL(14), //
    BLUESTONE_BLACK_DIGITAL(15);

    private int color;

    private BluestoneConnectionType(int color) {

        this.color = color;
    }

    public int getColor() {

        return color;
    }

    public boolean isColored() {

        return color >= 0;
    }

}
