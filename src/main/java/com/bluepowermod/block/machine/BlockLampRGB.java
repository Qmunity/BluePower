package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.tile.tier1.TileLamp;
import com.bluepowermod.tile.tier1.TileLampRGB;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.awt.*;

public class BlockLampRGB extends BlockLamp {

    public BlockLampRGB(String name, boolean isInverted) {
        super(name, isInverted, MinecraftColor.NONE);
    }

    @Override
    public int getColor(IBlockReader w, BlockPos pos, int tint) {
        if(w != null){
            TileLampRGB te = (TileLampRGB) w.getTileEntity(pos);
            if (te != null)
                return te.getColor();
        }
        return Color.getHSBColor((System.currentTimeMillis() % 10000) / 10000F, 1, 1).getRGB();
    }

    @Override
    public int getColor(int tint) {

        return Color.getHSBColor((System.currentTimeMillis() % 10000) / 10000F, 1, 1).getRGB();
    }

}
