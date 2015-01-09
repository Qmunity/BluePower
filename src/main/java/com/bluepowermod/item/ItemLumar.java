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
 *     @author Quetzi
 */

package com.bluepowermod.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLumar extends ItemBase {

    public ItemLumar() {

        super();
        this.setCreativeTab(BPCreativeTabs.items);
        this.setHasSubtypes(true);
        this.setUnlocalizedName(Refs.LUMAR_NAME);
        this.setTextureName(Refs.MODID + ":" + Refs.LUMAR_NAME);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {

        return super.getUnlocalizedName() + "." + Refs.oreDictDyes[15 - itemStack.getItemDamage()].substring(3).toLowerCase();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int colour) {

        int damage = itemStack.getItemDamage();
        if (damage >= 0 && damage < ItemDye.field_150922_c.length) { return ItemDye.field_150922_c[15 - damage]; }
        return 16777215;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {

        for (int i = 0; i < ItemDye.field_150922_c.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }
}
