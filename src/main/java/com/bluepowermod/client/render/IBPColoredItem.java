package com.bluepowermod.client.render;

import net.minecraft.item.ItemStack;

/**
 * @author MoreThanHidden
 */
public interface IBPColoredItem {

    int getColor(ItemStack stack, int tintIndex);

}
