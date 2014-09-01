/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.compat;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;

public interface IMultipartCompat {
    
    public BPPart getClickedPart(Vector3 loc, Vector3 subLoc, EntityPlayer player, TileEntity tile);
    
    public void removePart(TileEntity tile, BPPart part);
    
    public int getInput(World w, int x, int y, int z, ForgeDirection side, ForgeDirection face);
    
    public void sendUpdatePacket(BPPart part);
    
    public boolean isMultipart(TileEntity te);
    
    public boolean isOccupied(TileEntity te, AxisAlignedBB box);
    
    public <T> T getBPPart(TileEntity te, Class<T> searchedClass);
    
    public <T> T getBPPartOnFace(TileEntity te, Class<T> searchedClass, ForgeDirection face);
    
    public <T> List<T> getBPParts(TileEntity te, Class<T> searchedClass);
    
    public int getMOPData(MovingObjectPosition mop);
    
    public static final class MultipartCompat {
        
        public static Class<? extends TileEntity> tile = null;
        
    }
    
}
