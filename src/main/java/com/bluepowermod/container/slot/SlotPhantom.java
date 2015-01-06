/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * This class is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SlotPhantom extends Slot implements IPhantomSlot {
    
    // used for filters
    public SlotPhantom(IInventory par2IInventory, int par3, int par4, int par5) {
    
        super(par2IInventory, par3, par4, par5);
    }
    
    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
    
        return false;
    }
    
    @Override
    public boolean canAdjust() {
    
        return true;
    }
    
}
