package com.bluepowermod.part.wire.redstone;

import com.bluepowermod.api.wire.redstone.RedwireType;

public class WireHelper {

    public static int getColorForPowerLevel(RedwireType type, byte power) {

        return getColorForPowerLevel(type.getMinColor(), type.getMaxColor(), power);
    }

    public static int getColorForPowerLevel(int minColor, int maxColor, byte power) {

        double mul = (power & 0xFF) / 255D;

        int minRed = (minColor & 0xFF0000);
        int minGreen = (minColor & 0x00FF00);
        int minBlue = minColor & 0x0000FF;

        int maxRed = (int) (((maxColor & 0xFF0000) - minRed) * mul) & 0xFF0000;
        int maxGreen = (int) (((maxColor & 0x00FF00) - minGreen) * mul) & 0x00FF00;
        int maxBlue = (int) (((maxColor & 0x0000FF) - minBlue) * mul) & 0x0000FF;

        return minRed + maxRed + minGreen + maxGreen + minBlue + maxBlue;
    }

    public static int getColorForPowerLevel(int color, byte power) {

        double mul = (0.3 + (0.7 * ((power & 0xFF) / 255D)));
        return ((int) ((color & 0xFF0000) * mul) & 0xFF0000) + ((int) ((color & 0x00FF00) * mul) & 0x00FF00)
                + ((int) ((color & 0x0000FF) * mul) & 0x0000FF);
    }

}
