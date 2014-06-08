/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.api.part;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.util.ForgeDirectionUtils;

public abstract class BPPartFace extends BPPart implements IBPFacePart, IBPRedstonePart {
    
    private int face = 0;
    
    @Override
    public int getFace() {
    
        return face;
    }
    
    public void setFace(int face) {
    
        this.face = face;
        System.out.println("Set " + FMLCommonHandler.instance().getEffectiveSide());
    }
    
    @Override
    public boolean canPlacePart(ItemStack is, EntityPlayer player, Vector3 block, MovingObjectPosition mop) {
    
        this.world = block.getWorld();
        this.x = block.getBlockX();
        this.y = block.getBlockY();
        this.z = block.getBlockZ();
        
        ForgeDirection dir = ForgeDirection.getOrientation(mop.sideHit).getOpposite();
        if (dir == ForgeDirection.DOWN) {
            dir = ForgeDirection.UP;
        } else {
            if (dir == ForgeDirection.UP) dir = ForgeDirection.DOWN;
        }
        
        setFace(ForgeDirectionUtils.getSide(dir));
        
        return true;
    }
    
    @Override
    public boolean canStay() {
    
        return true;
    }
    
    @Override
    public final boolean canConnect(ForgeDirection side) {
    
        return false;
    }
    
    @Override
    public final int getStrongOutput(ForgeDirection side) {
    
        return 0;
    }
    
    @Override
    public final int getWeakOutput(ForgeDirection side) {
    
        return 0;
    }
    
}
