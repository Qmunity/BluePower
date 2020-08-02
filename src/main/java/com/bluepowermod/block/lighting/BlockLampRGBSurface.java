package com.bluepowermod.block.lighting;

import com.bluepowermod.api.misc.MinecraftColor;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import java.awt.*;

public class BlockLampRGBSurface extends BlockLampSurface {

    public BlockLampRGBSurface(String name, boolean isInverted, VoxelShape size) {
        super(name, isInverted, MinecraftColor.NONE, size);
    }

    @Override
    public int getColor(BlockState state, IBlockReader w, BlockPos pos, int tint) {
        return Color.getHSBColor((System.currentTimeMillis() % 10000) / 10000F, 1, 1).getRGB();
    }

    @Override
    public int getColor(ItemStack stack, int tint) {
        return Color.getHSBColor((System.currentTimeMillis() % 10000) / 10000F, 1, 1).getRGB();
    }

}
