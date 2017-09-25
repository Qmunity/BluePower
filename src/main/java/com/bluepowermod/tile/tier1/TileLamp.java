/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.block.machine.BlockLampRGB;
import com.bluepowermod.client.render.RenderLamp;
import com.bluepowermod.helper.MathHelper;
import com.bluepowermod.tile.TileBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


/**
 * @author Koen Beckers (K4Unl) and Amadornes. Yes. I only need this class to do the getPower() function.. damn :(
 */
public class TileLamp extends TileBase{

    private byte[] bundledPower = new byte[16];

    @Override
    protected void writeToPacketNBT(NBTTagCompound tCompound) {
        if (blockType instanceof BlockLampRGB) {
            tCompound.setByte("red", bundledPower[MinecraftColor.RED.ordinal()]);
            tCompound.setByte("green", bundledPower[MinecraftColor.GREEN.ordinal()]);
            tCompound.setByte("blue", bundledPower[MinecraftColor.BLUE.ordinal()]);
        }
    }

    @Override
    protected void readFromPacketNBT(NBTTagCompound tCompound) {
        if (tCompound.hasKey("red")) {
            byte[] pow = bundledPower;
            pow[MinecraftColor.RED.ordinal()] = tCompound.getByte("red");
            pow[MinecraftColor.GREEN.ordinal()] = tCompound.getByte("green");
            pow[MinecraftColor.BLUE.ordinal()] = tCompound.getByte("blue");
            bundledPower = pow;
        }
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        RenderLamp.pass = pass;
        return true;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    public int getColor() {

        int r = MathHelper.map(bundledPower[MinecraftColor.RED.ordinal()] & 0xFF, 0, 255, 20, 235);
        int g = MathHelper.map(bundledPower[MinecraftColor.GREEN.ordinal()] & 0xFF, 0, 255, 20, 235);
        int b = MathHelper.map(bundledPower[MinecraftColor.BLUE.ordinal()] & 0xFF, 0, 255, 20, 235);

        return (r << 16) + (g << 8) + b;
    }


}
