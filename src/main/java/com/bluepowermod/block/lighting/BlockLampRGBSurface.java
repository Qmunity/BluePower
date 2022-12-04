package com.bluepowermod.block.lighting;

import com.bluepowermod.api.misc.MinecraftColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

import java.awt.*;

public class BlockLampRGBSurface extends BlockLampSurface {

    public BlockLampRGBSurface(boolean isInverted, VoxelShape size) {
        super( isInverted, MinecraftColor.NONE, size);
    }

    @Override
    public int getColor(BlockState state, BlockGetter w, BlockPos pos, int tint) {
        return Color.getHSBColor((System.currentTimeMillis() % 10000) / 10000F, 1, 1).getRGB();
    }

    @Override
    public int getColor(ItemStack stack, int tint) {
        return Color.getHSBColor((System.currentTimeMillis() % 10000) / 10000F, 1, 1).getRGB();
    }

}
