/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.item;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * @author MineMaarten
 */
public class ItemColorableOverlay extends ItemBase implements IItemColor {

    public ItemColorableOverlay(String name) {
    
        setUnlocalizedName(name);
        setCreativeTab(BPCreativeTabs.items);
        setRegistryName(Refs.MODID + ":" + name);
        setHasSubtypes(true);
        setMaxStackSize(1);
    }


    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (int i = 0; i < 16; i++) {
            subItems.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int getColorFromItemstack(ItemStack itemStack, int renderPass) {
        return renderPass == 0 || itemStack.getItemDamage() >= 16 ? -1 : ItemDye.DYE_COLORS[15 - itemStack.getItemDamage()];
    }



    @Override
    public String getUnlocalizedName(ItemStack stack) {
    
        return super.getUnlocalizedName() + "." + (stack.getItemDamage() >= 16 ? "empty" : MinecraftColor.values()[stack.getItemDamage()].name().toLowerCase());
    }
}
