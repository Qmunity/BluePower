package com.bluepowermod.block.machine;

import java.awt.Color;

import net.minecraft.world.IBlockAccess;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileLamp;

public class BlockLampRGB extends BlockLamp {

    public BlockLampRGB(boolean isInverted) {

        super(isInverted, MinecraftColor.NONE);

        setBlockName(Refs.LAMP_NAME + ".rgb" + (isInverted ? ".inverted" : ""));
    }

    @Override
    public int getColor(IBlockAccess w, int x, int y, int z) {

        TileLamp te = get(w, x, y, z);
        if (te == null)
            return 0;

        return te.getColor();
    }

    @Override
    public int getColor() {

        return Color.getHSBColor((System.currentTimeMillis() % 10000) / 10000F, 1, 1).getRGB();
    }

}
