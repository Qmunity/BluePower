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

import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.util.Refs;

public class ItemGemHoe extends ItemHoe {

    public    Item    customCraftingMaterial = null;
    protected boolean canRepair              = true;

    public ItemGemHoe(ToolMaterial material, String name, Item repairItem) {

        super(material);
        this.setUnlocalizedName(name);
        this.setCreativeTab(BPCreativeTabs.tools);
        this.setTextureName(Refs.MODID + ":" + name);
        this.customCraftingMaterial = repairItem;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return String.format("item.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    @Override
    public boolean isRepairable() {

        return canRepair && isDamageable();
    }

    @Override
    public boolean getIsRepairable(ItemStack is1, ItemStack is2) {

        return ((is1.getItem() == this || is2.getItem() == this) && (is1.getItem() == this.customCraftingMaterial || is2.getItem() == this.customCraftingMaterial));
    }
}
