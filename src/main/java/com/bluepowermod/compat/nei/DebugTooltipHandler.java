package com.bluepowermod.compat.nei;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
    
        if (itemstack != null) {
            currenttip.add("GameData name: " + GameData.getItemRegistry().getNameForObject(itemstack.getItem()));
            if (itemstack.hasTagCompound()) {
                NBTTagCompound tag = itemstack.getTagCompound();
                currenttip.add(tag.toString());
            }
        }
        return currenttip;
    }
}
