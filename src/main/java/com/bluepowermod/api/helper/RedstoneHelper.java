/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.util.ForgeDirectionUtils;

public class RedstoneHelper {

    public static final int getInput(World w, int x, int y, int z, ForgeDirection side) {

        return getInput(w, x, y, z, side, null);
    }

    public static final int getInput(World w, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return getInput(w, x, y, z, side, face, true, true, true);
    }

    public static final int getInput(World w, int x, int y, int z, ForgeDirection side, ForgeDirection face, boolean weakPower, boolean strongPower,
            boolean indirectPower) {

        if (w == null)
            return 0;

        int power = 0;

        Block b = w.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ);
        if (b != null) {
            boolean shouldCheck = true;

            if (b instanceof BlockRedstoneWire && face != ForgeDirection.DOWN && face != null)
                shouldCheck = false;

            if (shouldCheck) {
                if (strongPower)
                    power = Math.max(power,
                            b.isProvidingStrongPower(w, x + side.offsetX, y + side.offsetY, z + side.offsetZ, ForgeDirectionUtils.getSide(side)));
                if (weakPower)
                    power = Math.max(power,
                            b.isProvidingWeakPower(w, x + side.offsetX, y + side.offsetY, z + side.offsetZ, ForgeDirectionUtils.getSide(side)));
                if (indirectPower)
                    power = Math.max(power,
                            w.getIndirectPowerLevelTo(x + side.offsetX, y + side.offsetY, z + side.offsetZ, ForgeDirectionUtils.getSide(side)));
                if (strongPower)
                    power = Math.max(power, b instanceof BlockRedstoneWire ? w.getBlockMetadata(x + side.offsetX, y + side.offsetY, z + side.offsetZ)
                            : 0);
            }
        }

        power = Math.max(power, BPApi.getInstance().getMultipartCompat().getInput(w, x, y, z, side, face));

        return power;
    }

    public static final int setOutput(World w, int x, int y, int z, ForgeDirection side, ForgeDirection face, int p) {

        if (w == null)
            return 0;

        int power = 0;

        Block b = w.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ);
        if (b != null) {
            boolean shouldOutput = true;

            if (b instanceof BlockRedstoneWire && face != ForgeDirection.DOWN && face != null)
                shouldOutput = false;

            if (shouldOutput)
                power = p;
        }

        return power;
    }

    public static final int[] unpackBundled(int color) {

        if (color == 0)
            return new int[16];
        int[] uncompressed = new int[16];
        for (int i = 15; i >= 0; i++)
            uncompressed[i] = (((color & 1 << i) == 0) ? 0 : 255);
        return uncompressed;
    }

    public static final int packBundled(int[] colors) {

        if (colors == null)
            return 0;
        int compressed = 0;
        for (int i = 15; i >= 0; i++)
            if (colors[i] != 0)
                compressed |= 1 << i;
        return compressed;
    }
}
