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

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.client.render.IBPColoredItem;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemLumar extends ItemBase implements IBPColoredItem{

    public ItemLumar() {
        super(new Properties());
        this.setRegistryName(Refs.MODID + ":" + Refs.LUMAR_NAME);
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {

        return super.getTranslationKey() + "." + Refs.oreDictDyes[15 - itemStack.getDamage()].substring(3).toLowerCase();
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        int damage = stack.getDamage();
        if (damage >= 0 && damage < MinecraftColor.values().length) { return MinecraftColor.values()[15 - damage].getHex(); }
        return 16777215;
    }
}
