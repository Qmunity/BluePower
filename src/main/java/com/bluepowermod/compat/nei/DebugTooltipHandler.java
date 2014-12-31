/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.nei;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.nei.guihook.IContainerTooltipHandler;
import cpw.mods.fml.common.registry.GameData;

public class DebugTooltipHandler implements IContainerTooltipHandler {

    @Override
    public List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {

        return currenttip;
    }

    @Override
    public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {

        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mousex, int mousey, List<String> currenttip) {

        // Remove comments to show the GameData name of an item when hovering over
        // it.

        if (itemstack != null) {
            currenttip.add("GameData name: " + GameData.getItemRegistry().getNameForObject(itemstack.getItem()));
            int[] ids = OreDictionary.getOreIDs(itemstack);
            if (ids.length > 0) {
                currenttip.add("OreDict names:");
                for (int i : ids)
                    currenttip.add(" - " + OreDictionary.getOreName(i));
            }
        }
        return currenttip;
    }
}
