/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.api.tube.ITubeConnection;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.tier2.TileTube;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PipeBlock;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.List;

/**
 * @author MoreThanHidden
 */
public class BlockTube extends PipeBlock implements IBPPartBlock, EntityBlock {
    public BlockTube() {
        super(0.25F, Properties.of().noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));
        BPBlocks.blockList.add(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        super.appendHoverText(stack, world, tooltip, advanced);
        tooltip.add(Component.literal(MinecraftColor.RED.getChatColor() + "WIP") );
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.makeConnections(context.getLevel(), context.getClickedPos());
    }

    public BlockState makeConnections(BlockGetter world, BlockPos pos) {
        BlockEntity blockDown = world.getBlockEntity(pos.below());
        BlockEntity blockUp = world.getBlockEntity(pos.above());
        BlockEntity blockNorth = world.getBlockEntity(pos.north());
        BlockEntity blockEast = world.getBlockEntity(pos.east());
        BlockEntity blockSouth = world.getBlockEntity(pos.south());
        BlockEntity blockWest = world.getBlockEntity(pos.west());
        return this.defaultBlockState()
                .setValue(DOWN, blockDown instanceof ITubeConnection || (blockDown != null && blockDown.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP).isPresent()))
                .setValue(UP,  blockUp instanceof ITubeConnection || (blockUp != null && blockUp.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).isPresent()))
                .setValue(NORTH, blockNorth instanceof ITubeConnection || (blockNorth != null && blockNorth.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.SOUTH).isPresent()))
                .setValue(EAST, blockEast instanceof ITubeConnection || (blockEast != null && blockEast.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.WEST).isPresent()))
                .setValue(SOUTH, blockSouth instanceof ITubeConnection || (blockSouth != null && blockSouth.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.NORTH).isPresent()))
                .setValue(WEST, blockWest instanceof ITubeConnection || (blockWest != null && blockWest.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.EAST).isPresent()));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = ((TileTube)builder.getParameter(LootContextParams.BLOCK_ENTITY)).getDrops();
        drops.add(new ItemStack(this));
        return drops;
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        pLevel.setBlock(pPos, this.makeConnections(pLevel, pPos), 2);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return TileTube::tickTube;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        return Shapes.box(4, 4, 4, 12, 12, 12);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileTube(pos, state);
    }
}
