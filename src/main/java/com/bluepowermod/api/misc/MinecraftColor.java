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
package com.bluepowermod.api.misc;

public enum MinecraftColor {

    WHITE(0xEBEBEB, 0xF0F0F0, 0xFFFFFF, 'f'), //
    ORANGE(0xCC7437, 0xEB8844, 0xF7A165, '6'), //
    MAGENTA(0xB041BA, 0xC354CD, 0xDF79E8, '1'), //
    LIGHT_BLUE(0x4D73C4, 0x6689D3, 0xA5BEF2, 'b'), //
    YELLOW(0xBAAD1E, 0xDECF2A, 0xFAE92F, 'w'), //
    LIME(0x3BB830, 0x41CD34, 0x8FF086, 'a'), //
    PINK(0xB0687B, 0xD88198, 0xE8B3C1, 'd'), //
    GRAY(0x2B2B2B, 0x434343, 0x575757, '8'), //
    LIGHT_GRAY(0xFFFFFF, 0xABABAB, 0xFFFFFF, '7'), //
    CYAN(0x195C78, 0x287697, 0x4688A3, '3'), //
    PURPLE(0x591D8F, 0x7B2FBE, 0x9C60D1, '5'), //
    BLUE(0x172173, 0x253192, 0x3443BF, '1'), //
    BROWN(0x2E1B0F, 0x51301A, 0x63381B, '4'), //
    GREEN(0x25330F, 0x3B511A, 0x577826, '2'), //
    RED(0x872926, 0xB3312C, 0xE03E38, 'c'), //
    BLACK(0x040404, 0x1E1B1B, 0x2B2B2B, '0'), //
    NONE(-1, -1, -1, (char) 0), //
    ANY(-1, -1, -1, (char) 0);

    public static MinecraftColor[] VALID_COLORS = { WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE,
        BLUE, BROWN, GREEN, RED, BLACK };
    public static MinecraftColor[] WIRE_COLORS = { WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE,
        BLUE, BROWN, GREEN, RED, BLACK, NONE };

    private int hexDark, hex, hexLight;
    private char chatColorChar;

    private MinecraftColor(int hexDark, int hex, int hexLight, char chatColorChar) {

        this.hexDark = hexDark;
        this.hex = hex;
        this.hexLight = hexLight;
        this.chatColorChar = chatColorChar;
    }

    public int getHexDark() {

        return hexDark;
    }

    public int getHex() {

        return hex;
    }

    public int getHexLight() {

        return hexLight;
    }

    public String getChatColor() {

        return String.valueOf('\u00a7') + chatColorChar;
    }

    public char getChatColorChar() {

        return chatColorChar;
    }

    public boolean matches(MinecraftColor color) {

        if (this == ANY || color == ANY)
            return true;

        return this == color;
    }

    public boolean canConnect(MinecraftColor color) {

        if (this == ANY || color == ANY || this == NONE || color == NONE)
            return true;

        return this == color;
    }

    @Override
    public String toString() {

        return getChatColor();
    }

}
