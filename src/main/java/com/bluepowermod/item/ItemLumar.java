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

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLumar extends ItemBase implements IItemColor{

    public ItemLumar() {

        super();
        this.setCreativeTab(BPCreativeTabs.items);
        this.setHasSubtypes(true);
        this.setUnlocalizedName(Refs.LUMAR_NAME);
        this.setRegistryName(Refs.MODID + ":" + Refs.LUMAR_NAME);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {

        return super.getUnlocalizedName() + "." + Refs.oreDictDyes[15 - itemStack.getItemDamage()].substring(3).toLowerCase();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (int i = 0; i < ItemDye.DYE_COLORS.length; i++) {
            subItems.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        int damage = stack.getItemDamage();
        if (damage >= 0 && damage < ItemDye.DYE_COLORS.length) { return ItemDye.DYE_COLORS[15 - damage]; }
        return 16777215;
    }

}
