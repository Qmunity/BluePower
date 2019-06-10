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
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFloppyDisk extends ItemBase implements IItemColor{

    public ItemFloppyDisk(String name) {
        this.setTranslationKey(name);
        this.setRegistryName(Refs.MODID + ":" + name);
        BPItems.itemList.add(this);
    }

    public static void finaliseDisk(ItemStack itemStack, String name, MinecraftColor color){
        CompoundNBT nbt = new CompoundNBT();
        nbt.setInteger("color", color.getHex());
        nbt.setString("name", name);
        itemStack.setTagCompound(nbt);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("name")) {
            tooltip.add(stack.getTagCompound().getString("name"));
        }
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        if(tintIndex == 0 && stack.getTagCompound() != null && stack.getTagCompound().hasKey("color")) {
            return stack.getTagCompound().getInteger("color");
        }
        return -1;
    }
}
