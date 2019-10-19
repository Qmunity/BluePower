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

import java.util.List;
import java.util.Random;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

import com.bluepowermod.init.BPCreativeTabs;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemSaw extends ItemBase{

    private final int sawLevel;

    public ItemSaw(int sawLevel, String name) {
        super(new Properties().maxStackSize(1).maxDamage(1 << sawLevel + 8), BPCreativeTabs.tools);
        setRegistryName(Refs.MODID + ":" + name);
        this.sawLevel = sawLevel;
    }

    public int getSawLevel() {

        return sawLevel;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(MinecraftColor.LIGHT_BLUE.getChatColor())
                .appendSibling(new TranslationTextComponent("item." + Refs.MODID + ".saw.info")) );
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {

        ItemStack container = itemStack.copy();
        container.attemptDamageItem(1, new Random(), null);
        return container;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {

        return true;
    }
}