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

package com.bluepowermod.compat;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import com.bluepowermod.api.misc.IScrewdriver;

public abstract class CompatModule {

    public abstract void preInit(FMLPreInitializationEvent ev);

    public abstract void init(FMLInitializationEvent ev);

    public abstract void postInit(FMLPostInitializationEvent ev);

    public abstract void registerBlocks();

    public abstract void registerItems();

    @SideOnly(Side.CLIENT)
    public abstract void registerRenders();

    public boolean isScrewdriver(ItemStack item) {

        return item != null && item.getItem() instanceof IScrewdriver && ((IScrewdriver)item.getItem()).isScrewdriver(item);
    }

}
