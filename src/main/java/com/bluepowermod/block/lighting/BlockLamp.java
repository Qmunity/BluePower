/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.lighting;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.block.BlockBase;
import com.bluepowermod.client.render.IBPColoredBlock;
import com.bluepowermod.tile.tier1.TileLamp;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * @author Koen Beckers (K4Unl)
 *
 */
public class BlockLamp extends BlockBase implements IBPColoredBlock, EntityBlock {

    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

    private final boolean isInverted;
    private final MinecraftColor color;
    private int tick = 0;

    public BlockLamp(boolean isInverted, MinecraftColor color) {
        super(Properties.of(Material.DECORATION).sound(SoundType.STONE).strength(1.0F));
        this.isInverted = isInverted;
        this.color = color;
        registerDefaultState(this.defaultBlockState().setValue(POWER, isInverted ? 15 : 0));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileLamp(pos, state);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(POWER);
    }

    @Override
    public int getColor(BlockState state, BlockGetter w, BlockPos pos, int tint) {
        return color.getHex();
    }

    @Override
    public int getColor(ItemStack stack, int tint) {
        return color.getHex();
    }

    public boolean isInverted() {
        return isInverted;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return !(world.getBlockState(pos).getBlock() instanceof BlockLampRGB) && super.canConnectRedstone(state, world, pos, side);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(POWER);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int redstoneValue = context.getLevel().getBestNeighborSignal(context.getClickedPos());
        if(isInverted){redstoneValue = 15 - redstoneValue;}
        return this.defaultBlockState().setValue(POWER, redstoneValue);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops =  super.getDrops(state, builder);
        drops.add(new ItemStack(this));
        return drops;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if(tick <= 1){
            int redstoneValue = world.getBestNeighborSignal(pos);
            if(isInverted){redstoneValue = 15 - redstoneValue;}
            world.setBlock(pos, this.defaultBlockState().setValue(POWER, redstoneValue), 2);
        }
        tick++;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        int redstoneValue = world.getBestNeighborSignal(pos);
        if(isInverted){redstoneValue = 15 - redstoneValue;}
        world.setBlock(pos, state.setValue(POWER, redstoneValue), 2);
    }

}
