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

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.util.Dependencies;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

import java.util.Random;


@Optional.Interface(modid = Dependencies.FMP, iface = "codechicken.microblock.Saw")
public class ItemSaw extends ItemBase implements Saw {

    private final int sawLevel;

    public ItemSaw(int sawLevel, String name) {

        setCreativeTab(BPCreativeTabs.tools);
        this.sawLevel = sawLevel;
        setRegistryName(Refs.MODID + ":" + name);
        setUnlocalizedName(name);
        maxStackSize = 1;
        setMaxDamage(1 << sawLevel + 8);
    }

    public int getSawLevel() {

        return sawLevel;
    }

    @Override
    @Optional.Method(modid = Dependencies.FMP)
    public int getCuttingStrength(ItemStack itemstack) {

        return sawLevel;
    }

    @Override
    @Optional.Method(modid = Dependencies.FMP)
    public int getMaxCuttingStrength() {

        return sawLevel;
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack par1ItemStack) {

        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {

        ItemStack container = itemStack.copy();
        container.attemptDamageItem(1, new Random());
        return container;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {

        return true;
    }

    @Override
    public boolean isRepairable() {

        return false;
    }
}
