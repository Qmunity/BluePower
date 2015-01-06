/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 *     
 *     @author Lumien
 */

package com.bluepowermod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.bluepowermod.BluePower;
import com.bluepowermod.reference.GuiIDs;

public class ItemCanvasBag extends ItemColorableOverlay {
    
    public ItemCanvasBag(String name) {
    
        super(name);
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World worldObj, EntityPlayer playerEntity) {
    
        if (!worldObj.isRemote) {
            playerEntity.openGui(BluePower.instance, GuiIDs.CANVAS_BAG.ordinal(), worldObj, (int) playerEntity.posX, (int) playerEntity.posY, (int) playerEntity.posZ);
        }
        return itemstack;
    }
    
}
