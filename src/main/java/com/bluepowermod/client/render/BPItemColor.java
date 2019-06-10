package com.bluepowermod.client.render;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class BPItemColor implements IItemColor {

    @Override
    @OnlyIn(Dist.CLIENT)
    public int colorMultiplier(ItemStack itemStack, int renderPass) {
        return ((IBPColoredItem)itemStack.getItem()).getColor(itemStack, renderPass);
    }

}
