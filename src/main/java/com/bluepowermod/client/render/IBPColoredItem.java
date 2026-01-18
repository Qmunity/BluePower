package com.bluepowermod.client.render;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * @author MoreThanHidden
 */
public interface IBPColoredItem {

    int getColor(ItemStack stack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity);

}
