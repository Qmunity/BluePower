package com.bluepowermod.api.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IScrewdriver {

    public boolean damage(ItemStack stack, int damage, EntityPlayer player, boolean simulated);

}
