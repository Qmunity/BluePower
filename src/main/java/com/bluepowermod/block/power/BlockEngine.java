/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileEngine;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

/**
 * @author TheFjong, MoreThanHidden
 */
public class BlockEngine extends BlockContainerBase implements SimpleWaterloggedBlock {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty GEAR = BooleanProperty.create("gear");
    public static final BooleanProperty GLIDER = BooleanProperty.create("glider");
    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockEngine() {

        super(Material.METAL, TileEngine.class);
        registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVE, false)
                .setValue(GEAR, false)
                .setValue(GLIDER, false)
                .setValue(FACING, Direction.DOWN)
                .setValue(WATERLOGGED, false));
        setRegistryName(Refs.MODID, Refs.ENGINE_NAME);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(ACTIVE, GEAR, GLIDER, FACING, WATERLOGGED);
    }


    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, Level worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return super.getStateForPlacement(context).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }


    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return AABBUtils.rotate(Block.box(0.01,0,0.01,15.99F,10,15.99F), blockState.getValue(FACING).getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("cast")
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity player, ItemStack iStack) {
        if (world.getBlockEntity(pos) instanceof TileEngine) {
            Direction facing;

            if (player.xRot > 45) {

                facing = Direction.DOWN;
            } else if (player.xRot < -45) {

                facing = Direction.UP;
            } else {

                facing = player.getDirection().getOpposite();
            }

            TileEngine tile = (TileEngine) world.getBlockEntity(pos);
            if (tile != null) {
                tile.setOrientation(facing);
            }
            world.setBlock(pos, state.setValue(FACING, facing), 2);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, World world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (!player.getInventory().getSelected().isEmpty()) {
            Item item = player.getInventory().getSelected().getItem();
            if (item == BPItems.screwdriver) {
                return InteractionResult.SUCCESS;
            }
        }

        return super.use(blockState, world, pos, player, hand, rayTraceResult);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

}
