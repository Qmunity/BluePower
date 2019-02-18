package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.tile.tier1.TileLamp;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.awt.*;

public class BlockLampRGBSurface extends BlockLampSurface {

    public BlockLampRGBSurface(String name, boolean isInverted, AxisAlignedBB size) {
        super(name, isInverted, MinecraftColor.NONE, size);
        setTranslationKey(name + ".rgb" + (isInverted ? ".inverted" : ""));
    }

    @Override
    public int getColor(IBlockAccess w, BlockPos pos, int tint) {

        TileLamp te = get(w, pos);
        if (te == null)
            return 0;

        return te.getColor();
    }

    @Override
    public int getColor(int tint) {

        return Color.getHSBColor((System.currentTimeMillis() % 10000) / 10000F, 1, 1).getRGB();
    }

}
