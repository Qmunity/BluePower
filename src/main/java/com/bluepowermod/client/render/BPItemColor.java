package com.bluepowermod.client.render;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BPItemColor implements IItemColor {

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(ItemStack itemStack, int renderPass) {
        return ((IBPColoredItem)itemStack.getItem()).getColor(itemStack, renderPass);
    }

}
