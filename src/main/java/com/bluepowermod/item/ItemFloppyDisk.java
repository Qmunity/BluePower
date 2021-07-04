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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFloppyDisk extends ItemBase implements IItemColor{

    public ItemFloppyDisk(String name) {
        super(new Properties());
        this.setRegistryName(Refs.MODID + ":" + name);
        BPItems.itemList.add(this);
    }

    public static void finaliseDisk(ItemStack itemStack, String name, MinecraftColor color){
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("color", color.getHex());
        nbt.putString("name", name);
        itemStack.setTag(nbt);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(stack.getTag() != null && stack.getTag().contains("name")) {
            tooltip.add(new StringTextComponent(stack.getTag().getString("name")));
        }
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if(tintIndex == 0 && stack.getTag() != null && stack.getTag().contains("color")) {
            return stack.getTag().getInt("color");
        }
        return -1;
    }
}
