/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.block.machine.BlockLamp;
import com.bluepowermod.block.machine.BlockLampRGB;
import com.bluepowermod.client.render.RenderLamp;
import com.bluepowermod.helper.MathHelper;
import com.bluepowermod.tile.TileBase;
import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;


/**
 * @author Koen Beckers (K4Unl) and Amadornes.
 */
@Optional.Interface(iface="elucent.albedo.lighting.ILightProvider", modid="albedo")
public class TileLamp extends TileBase implements ILightProvider{

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

    @Override
    protected void onTileLoaded() {
        world.getBlockState(pos).neighborChanged(world, pos, world.getBlockState(pos).getBlock(), pos);
    }

    @Optional.Method(modid="albedo")
    @Override
    public Light provideLight() {

        BlockLamp block = (BlockLamp) world.getBlockState(pos).getBlock();
        int value = block.getLightValue(world.getBlockState(pos), world, pos);

        int color = block.getColor(world, pos, 0);

        int redMask = 0xff0000, greenMask = 0xff00, blueMask = 0xff;
        int r = ((color & redMask) >> 16) * value;
        int g = ((color & greenMask) >> 8) * value;
        int b = (color & blueMask) * value;

        return Light.builder()
                .pos(this.pos)
                .color(r / 650,g / 650,b / 650)
                .radius(Math.max(value / 2, 1))
                .build();
    }
}
