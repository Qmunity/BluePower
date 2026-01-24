/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.block.lighting.BlockLampRGB;
import com.bluepowermod.helper.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;


/**
 * @author Koen Beckers (K4Unl) and Amadornes.
 */
public class TileLampRGB extends TileLamp {

    private byte[] bundledPower = new byte[16];

    public TileLampRGB(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (getBlockState().getBlock() instanceof BlockLampRGB) {
            output.putByte("red", bundledPower[MinecraftColor.RED.ordinal()]);
            output.putByte("green", bundledPower[MinecraftColor.GREEN.ordinal()]);
            output.putByte("blue", bundledPower[MinecraftColor.BLUE.ordinal()]);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
            byte[] pow = bundledPower;
            pow[MinecraftColor.RED.ordinal()] = input.getByteOr("red", (byte) 0);
            pow[MinecraftColor.GREEN.ordinal()] = input.getByteOr("green", (byte) 0);
            pow[MinecraftColor.BLUE.ordinal()] = input.getByteOr("blue", (byte) 0);
            bundledPower = pow;
    }

    public int getColor() {

        int r = MathHelper.map(bundledPower[MinecraftColor.RED.ordinal()] & 0xFF, 0, 255, 20, 235);
        int g = MathHelper.map(bundledPower[MinecraftColor.GREEN.ordinal()] & 0xFF, 0, 255, 20, 235);
        int b = MathHelper.map(bundledPower[MinecraftColor.BLUE.ordinal()] & 0xFF, 0, 255, 20, 235);

        return (r << 16) + (g << 8) + b;
    }

}
