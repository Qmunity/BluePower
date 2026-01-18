package com.bluepowermod.client.render;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class BPItemColor implements ItemTintSource {

    public static final MapCodec<BPItemColor> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.point(new BPItemColor())
    );

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        return ARGB.opaque(((IBPColoredItem)itemStack.getItem()).getColor(itemStack, clientLevel, livingEntity));
    }

    @Override
    public MapCodec<BPItemColor> type() {
        return MAP_CODEC;
    }
}
