/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MoreThanHidden
 */
public class BlockBPMultipart extends ContainerBlock implements IWaterLoggable {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockBPMultipart() {
        super(Block.Properties.of(Material.STONE).noOcclusion().strength(2));
        setRegistryName(Refs.MODID + ":multipart");
        BPBlocks.blockList.add(this);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    public boolean placeLiquid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidStateIn.getType() == Fluids.WATER && !getShape(state, worldIn, pos, ISelectionContext.empty()).equals(VoxelShapes.block())) {
            if (!worldIn.isClientSide()) {
                worldIn.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, Boolean.TRUE), 3);
                worldIn.getLiquidTicks().scheduleTick(pos, fluidStateIn.getType(), fluidStateIn.getType().getTickDelay(worldIn));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder){
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        TileEntity te = worldIn.getBlockEntity(pos);
        if(te instanceof TileBPMultipart){
            List<VoxelShape> shapeList = new ArrayList<>();
            ((TileBPMultipart) te).getStates().forEach(s -> shapeList.add(s.getShape(worldIn, pos)));
            if(shapeList.size() > 0)
                return shapeList.stream().reduce(shapeList.get(0), VoxelShapes::or);
        }
        //Shouldn't be required but allows the player to break an empty multipart
        return Block.box(6,6,6,10,10,10);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        BlockState partState = MultipartUtils.getClosestState(player, pos);
        TileEntity te = world.getBlockEntity(pos);
        if(partState != null && partState.getBlock() instanceof IBPPartBlock && te instanceof TileBPMultipart) {
            //Remove Selected Part
            ((TileBPMultipart) te).removeState(partState);
            //Call onMultipartReplaced
            ((IBPPartBlock)partState.getBlock()).onMultipartReplaced(partState, world, pos, state, false);
            //Play Break Sound
            world.playSound(null, pos, SoundEvents.STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return false;
        }
        return false;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        ItemStack itemStack = ItemStack.EMPTY;
        BlockState partState = MultipartUtils.getClosestState(player, pos);
        if(partState != null)
            itemStack = partState.getPickBlock(target, world, pos, player);
        return itemStack;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity tileentity = builder.getParameter(LootParameters.BLOCK_ENTITY);
        List<ItemStack> itemStacks = new ArrayList<>();
        if (tileentity instanceof TileBPMultipart) {
            ((TileBPMultipart) tileentity).getStates().forEach(s -> itemStacks.addAll(s.getBlock().getDrops(s, builder)));
        }
        return itemStacks;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        TileEntity te = world.getBlockEntity(pos);
        if(te instanceof TileBPMultipart) {
            ((TileBPMultipart) te).getStates().forEach(s -> s.neighborChanged(world, pos, blockIn, fromPos, bool));
        }
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new TileBPMultipart();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
