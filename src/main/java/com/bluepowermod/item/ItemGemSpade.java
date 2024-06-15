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

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class ItemGemSpade extends ShovelItem {

    public Item customCraftingMaterial;
    protected boolean canRepair              = true;

    public ItemGemSpade(Tier itemTier, Item repairItem) {
        super(itemTier, new Properties());
        this.customCraftingMaterial = repairItem;
    }

    @Override
    public boolean isValidRepairItem(ItemStack is1, ItemStack is2) {
        return ((is1.getItem() == this || is2.getItem() == this) && (is1.getItem() == this.customCraftingMaterial || is2.getItem() == this.customCraftingMaterial));
    }
}
