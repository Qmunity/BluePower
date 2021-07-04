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

package com.bluepowermod.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

public class ItemIndigoDye extends ItemBase {

    public ItemIndigoDye(String name) {
		super(new Properties());
        this.setRegistryName(Refs.MODID + ":" + name);
    }

	@Override
	public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity entity, Hand hand) {
		if (entity instanceof SheepEntity) {
			SheepEntity sheep = (SheepEntity) entity;
			if (!sheep.isSheared() && sheep.getColor() != DyeColor.PURPLE) {
				sheep.setColor(DyeColor.PURPLE);
				stack.setCount(stack.getCount() - 1);
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
