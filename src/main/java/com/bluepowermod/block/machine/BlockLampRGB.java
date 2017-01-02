package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileLamp;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.awt.*;

public class BlockLampRGB extends BlockLamp {

    public BlockLampRGB(boolean isInverted) {

        super(isInverted, MinecraftColor.NONE);

        setUnlocalizedName(Refs.LAMP_NAME + ".rgb" + (isInverted ? ".inverted" : ""));
    }

    @Override
    public int getColor(IBlockAccess w, BlockPos pos) {

        TileLamp te = get(w, pos);
        if (te == null)
            return 0;

        return te.getColor();
    }

    @Override
    public int getColor() {

        return Color.getHSBColor((System.currentTimeMillis() % 10000) / 10000F, 1, 1).getRGB();
    }

}
