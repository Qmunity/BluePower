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

import com.bluepowermod.api.misc.MinecraftColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class ItemFloppyDisk extends ItemBase implements ItemColor{

    public ItemFloppyDisk() {
        super(new Properties());
    }

    public static void finaliseDisk(ItemStack itemStack, String name, MinecraftColor color){
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("color", color.getHex());
        nbt.putString("name", name);
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains("name")) {
            tooltipComponents.add(Component.literal(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("name")));
        }
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if(tintIndex == 0 && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains("color")) {
            return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("color");
        }
        return -1;
    }
}
