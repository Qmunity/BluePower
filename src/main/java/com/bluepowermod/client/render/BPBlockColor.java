package com.bluepowermod.client.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class BPBlockColor implements IBlockColor, IItemColor {

    @Override
    public int getColor(BlockState state, @Nullable ILightReader world, @Nullable BlockPos pos, int tintIndex) {
        return ((IBPColoredBlock)state.getBlock()).getColor(world, pos, tintIndex);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getColor(ItemStack stack, int tintIndex) {
        return ((IBPColoredBlock)Block.getBlockFromItem(stack.getItem())).getColor(tintIndex);
    }
}
