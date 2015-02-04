/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.wire.redstone;

import com.bluepowermod.api.wire.redstone.RedwireType;

public class WireCommons {

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
