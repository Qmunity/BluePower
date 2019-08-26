package com.bluepowermod.api.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IScrewdriver {

    public boolean damage(ItemStack stack, int damage, PlayerEntity player, boolean simulated);

}
