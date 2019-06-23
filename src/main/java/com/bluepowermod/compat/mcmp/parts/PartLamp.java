package com.bluepowermod.compat.mcmp.parts;

import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class PartLamp implements IMultipart {
    private Block block;

    public PartLamp (Block block){
        this.block = block;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public IPartSlot getSlotForPlacement(World world, BlockPos pos, BlockState state, Direction facing, float hitX, float hitY, float hitZ, LivingEntity placer) {
        return EnumFaceSlot.fromFace(facing);
    }

    @Override
    public IPartSlot getSlotFromWorld(IBlockReader world, BlockPos pos, BlockState state) {
        return EnumFaceSlot.DOWN;
    }

}
