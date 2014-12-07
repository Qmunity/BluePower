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
 */

package com.bluepowermod.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.util.Refs;

public class ItemIndigoDye extends ItemBase {

    public ItemIndigoDye(String name) {

        this.setUnlocalizedName(name);
        this.setCreativeTab(BPCreativeTabs.items);
        this.setTextureName(Refs.MODID + ":" + name);
    }

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
	
		if (entity instanceof EntitySheep) {
			EntitySheep sheep = (EntitySheep) entity;
			if (!sheep.getSheared() && sheep.getFleeceColor() != 10) {
				sheep.setFleeceColor(10);
				--stack.stackSize;
			}
			return true;
		}
		return false;
	}
}
