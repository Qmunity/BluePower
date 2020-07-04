/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.block.BlockBase;
import com.bluepowermod.client.render.IBPColoredBlock;
import com.bluepowermod.tile.tier1.TileLamp;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * @author Koen Beckers (K4Unl)
 *
 */
public class BlockLamp extends BlockBase implements IBPColoredBlock{

    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

    private final boolean isInverted;
    private final MinecraftColor color;
    private final String name;
    private int tick = 0;

    public BlockLamp(String name, boolean isInverted, MinecraftColor color) {
        super(Properties.create(Material.REDSTONE_LIGHT).sound(SoundType.STONE).hardnessAndResistance(1.0F));
        this.isInverted = isInverted;
        this.color = color;
        this.name = name;
        setDefaultState(this.getDefaultState().with(POWER, isInverted ? 15 : 0));
        setRegistryName(name + (isInverted ? "inverted" : "") + "_"+ (color == MinecraftColor.NONE ? "rgb" : color.name().toLowerCase()));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileLamp();
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(POWER);
    }

    @Override
    public int getColor(IBlockReader w, BlockPos pos, int tint) {

        return color.getHex();
    }

    @Override
    public int getColor(int tint) {

        return color.getHex();

    }

    public boolean isInverted() {

        return isInverted;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return !(world.getBlockState(pos).getBlock() instanceof BlockLampRGB) && super.canConnectRedstone(state, world, pos, side);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(POWER);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        int redstoneValue = context.getWorld().getRedstonePowerFromNeighbors(context.getPos());
        if(isInverted){redstoneValue = 15 - redstoneValue;}
        return this.getDefaultState().with(POWER, redstoneValue);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops =  super.getDrops(state, builder);
        drops.add(new ItemStack(this));
        return drops;
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(tick <= 1){
            int redstoneValue = world.getRedstonePowerFromNeighbors(pos);
            if(isInverted){redstoneValue = 15 - redstoneValue;}
            world.setBlockState(pos, this.getDefaultState().with(POWER, redstoneValue), 2);
        }
        tick++;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        int redstoneValue = world.getRedstonePowerFromNeighbors(pos);
        if(isInverted){redstoneValue = 15 - redstoneValue;}
        world.setBlockState(pos, state.with(POWER, redstoneValue), 2);
    }

}
