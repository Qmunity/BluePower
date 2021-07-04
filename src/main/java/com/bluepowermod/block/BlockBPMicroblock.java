package com.bluepowermod.block;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.TileBPMicroblock;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockBPMicroblock extends ContainerBlock implements IBPPartBlock, IWaterLoggable {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final VoxelShape size;

    public BlockBPMicroblock(VoxelShape size) {
        super(Properties.of(Material.STONE).noOcclusion().strength(2));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
        this.size = size;
        BPBlocks.blockList.add(this);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity tileentity = builder.getParameter(LootParameters.BLOCK_ENTITY);
        List<ItemStack> itemStacks = new ArrayList<>();
        if(tileentity instanceof TileBPMultipart){
            tileentity = ((TileBPMultipart) tileentity).getTileForState(state);
        }
        if (tileentity instanceof TileBPMicroblock) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("block", ((TileBPMicroblock)tileentity).getBlock().getRegistryName().toString());
            ItemStack stack = new ItemStack(this);
            stack.setTag(nbt);
            stack.setHoverName(new TranslationTextComponent(((TileBPMicroblock)tileentity).getBlock().getDescriptionId())
                    .append(new StringTextComponent(" "))
                    .append(new TranslationTextComponent(this.getDescriptionId())));
            itemStacks.add(stack);
        }
        return itemStacks;
    }

    @Override
    public ItemStack getCloneItemStack(IBlockReader world, BlockPos pos, BlockState state) {
        TileEntity tileentity = world.getBlockEntity(pos);
        ItemStack stack = ItemStack.EMPTY;
        if(tileentity instanceof TileBPMultipart){
            tileentity = ((TileBPMultipart) tileentity).getTileForState(state);
        }
        if (tileentity instanceof TileBPMicroblock) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("block", ((TileBPMicroblock) tileentity).getBlock().getRegistryName().toString());
            stack = new ItemStack(this);
            stack.setTag(nbt);
            stack.setHoverName(new TranslationTextComponent(((TileBPMicroblock) tileentity).getBlock().getDescriptionId())
                    .append(new StringTextComponent(" "))
                    .append(new TranslationTextComponent(this.getDescriptionId())));
        }
        return stack;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABBUtils.rotate(size, state.getValue(FACING));
    }

    public VoxelShape getSize() {
        return size;
    }

    @Override
    public Boolean blockCapability(BlockState state, Capability capability, @Nullable Direction side) {
        return side == state.getValue(FACING).getOpposite();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        AxisAlignedBB aabb = size.bounds();
        return AABBUtils.rotate(Block.box(3, aabb.minY * 16, 3, 13, aabb.maxY, 13), state.getValue(FACING));
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        if(player != null && !player.isCrouching()) {
            return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
        }
        Vector3d vec = context.getPlayer().getLookAngle();
        return this.defaultBlockState().setValue(FACING, Direction.getNearest(vec.x, vec.y, vec.z)).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new TileBPMicroblock();
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof TileBPMicroblock && stack.hasTag() && stack.getTag().contains("block")) {
            //Update Microblock Type based on Stack
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stack.getTag().getString("block")));
            ((TileBPMicroblock) tileentity).setBlock(block);
        }else if(tileentity instanceof TileBPMultipart && stack.hasTag() && stack.getTag().contains("block")){
            //Update Multipart Microblock Type based on Stack
            TileBPMicroblock tile = (TileBPMicroblock)((TileBPMultipart)tileentity).getTileForState(state);
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stack.getTag().getString("block")));
            tile.setBlock(block);
        }
    }

}
