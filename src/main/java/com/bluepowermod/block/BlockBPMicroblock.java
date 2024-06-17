package com.bluepowermod.block;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.TileBPMicroblock;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.util.AABBUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.neoforged.neoforge.capabilities.BlockCapability;

public class BlockBPMicroblock extends BaseEntityBlock implements IBPPartBlock, SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final VoxelShape size;

    //Codec
    public static final MapCodec<BlockBPMicroblock> CODEC = simpleCodec((props) -> new BlockBPMicroblock(Shapes.block()));


    public BlockBPMicroblock(VoxelShape size) {
        super(Properties.of().noOcclusion().strength(2));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
        this.size = size;
        BPBlocks.blockList.add(this);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity tileentity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
        List<ItemStack> itemStacks = new ArrayList<>();
        if(tileentity instanceof TileBPMultipart){
            tileentity = ((TileBPMultipart) tileentity).getTileForState(state);
        }
        if (tileentity instanceof TileBPMicroblock) {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("block", BuiltInRegistries.BLOCK.getKey(((TileBPMicroblock)tileentity).getBlock()).toString());
            ItemStack stack = new ItemStack(this);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
            stack.set(DataComponents.ITEM_NAME, Component.translatable(((TileBPMicroblock)tileentity).getBlock().getDescriptionId())
                    .append(Component.literal(" "))
                    .append(Component.translatable(this.getDescriptionId())));
            itemStacks.add(stack);
        }
        return itemStacks;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        BlockEntity tileentity = level.getBlockEntity(pos);
        ItemStack stack = ItemStack.EMPTY;
        if(tileentity instanceof TileBPMultipart){
            tileentity = ((TileBPMultipart) tileentity).getTileForState(state);
        }
        if (tileentity instanceof TileBPMicroblock) {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("block", BuiltInRegistries.BLOCK.getKey(((TileBPMicroblock) tileentity).getBlock()).toString());
            stack = new ItemStack(this);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
            stack.set(DataComponents.ITEM_NAME, Component.translatable(((TileBPMicroblock) tileentity).getBlock().getDescriptionId())
                    .append(Component.literal(" "))
                    .append(Component.translatable(this.getDescriptionId())));
        }
        return stack;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AABBUtils.rotate(size, state.getValue(FACING));
    }

    public VoxelShape getSize() {
        return size;
    }

    @Override
    public Boolean blockCapability(BlockState state, BlockCapability capability, @Nullable Direction side) {
        return side == state.getValue(FACING).getOpposite();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        AABB aabb = size.bounds();
        return AABBUtils.rotate(Block.box(3, aabb.minY * 16, 3, 13, aabb.maxY, 13), state.getValue(FACING));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }


    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        if(player != null && !player.isCrouching()) {
            return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
        }
        Vec3 vec = context.getPlayer().getLookAngle();
        return this.defaultBlockState().setValue(FACING, Direction.getNearest(vec.x, vec.y, vec.z)).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileBPMicroblock(pos, state);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof TileBPMicroblock && stack.has(DataComponents.CUSTOM_DATA) && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains("block")) {
            //Update Microblock Type based on Stack
            Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("block")));
            ((TileBPMicroblock) tileentity).setBlock(block);
        }else if(tileentity instanceof TileBPMultipart && stack.has(DataComponents.CUSTOM_DATA) && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains("block")){
            //Update Multipart Microblock Type based on Stack
            TileBPMicroblock tile = (TileBPMicroblock)((TileBPMultipart)tileentity).getTileForState(state);
            Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("block")));
            tile.setBlock(block);
        }
    }

}
