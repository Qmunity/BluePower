/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.tile.BPTileEntityType;
import net.minecraft.tileentity.TileEntity;

/**
 * @author Koen Beckers (K4Unl) and Amadornes.
 */
public class TileLamp extends TileEntity {

    public TileLamp() {
        super(BPTileEntityType.LAMP);
    }

/*
    @Optional.Method(modid="albedo")
    @Override
    public Light provideLight() {

        BlockLamp block = (BlockLamp) world.getBlockState(pos).getBlock();
        int value = block.getLightValue(world.getBlockState(pos), world, pos);

        int color = block.getColor(world, pos, 0);

        int redMask = 0xff0000, greenMask = 0xff00, blueMask = 0xff;
        int r = (color & redMask) >> 16;
        int g = (color & greenMask) >> 8;
        int b = (color & blueMask);

        return Light.builder()
                .pos(this.pos)
                .color(r ,g,b, BPConfig.albedoBrightness)
                .radius(Math.max(value / 2, 1))
                .build();
    }*/
}
