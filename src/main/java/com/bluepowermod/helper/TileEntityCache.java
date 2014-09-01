/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.helper;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * 
 * @author MineMaarten
 */

public class TileEntityCache {
    
    private TileEntity  te;
    private final World world;
    private final int   x, y, z;
    
    public TileEntityCache(World world, int x, int y, int z) {
    
        if (world == null) throw new NullPointerException("World can't be null!");
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        update();
    }
    
    public void update() {
    
        te = world.getTileEntity(x, y, z);
    }
    
    public TileEntity getTileEntity() {
    
        return te;
    }
    
    public static TileEntityCache[] getDefaultCache(World world, int x, int y, int z) {
    
        TileEntityCache[] cache = new TileEntityCache[6];
        for (int i = 0; i < 6; i++) {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            cache[i] = new TileEntityCache(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ);
        }
        return cache;
    }
    
}
