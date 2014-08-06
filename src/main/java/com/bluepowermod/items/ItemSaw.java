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

package com.bluepowermod.items;

import net.minecraft.item.ItemStack;
import codechicken.microblock.Saw;

import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.util.Dependencies;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.Optional;

@Optional.Interface(modid = Dependencies.FMP, iface = "codechicken.microblock.Saw")
public class ItemSaw extends ItemBase implements Saw {

    private int sawLevel;

    public ItemSaw(int sawLevel, String name) {

        setCreativeTab(CustomTabs.tabBluePowerTools);
        this.sawLevel = sawLevel;
        setTextureName(Refs.MODID + ":" + name);
        setUnlocalizedName(name);
        maxStackSize = 1;
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
}
