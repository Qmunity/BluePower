package com.bluepowermod.client.render;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class BPBlockColor implements IBlockColor, IItemColor {

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {
        //Color for Block
        return ((IBPColoredBlock)state.getBlock()).getColor(world, pos, tintIndex);
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return ((IBPColoredBlock)Block.getBlockFromItem(stack.getItem())).getColor(tintIndex);
    }
}
