package com.bluepowermod.client.render;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BPItemColor implements ItemColor {

    @Override
    public int getColor(ItemStack itemStack, int renderPass) {
        return ((IBPColoredItem)itemStack.getItem()).getColor(itemStack, renderPass);
    }
}
