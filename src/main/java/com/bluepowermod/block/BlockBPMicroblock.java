package com.bluepowermod.block;

import com.bluepowermod.api.misc.PlacementType;
import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileBPMicroblock;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockBPMicroblock extends ContainerBlock implements IBPPartBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private final PlacementType placementType;
    private final VoxelShape size;

    public BlockBPMicroblock(VoxelShape size, PlacementType type) {
        super(Properties.create(Material.WOOD));
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
        this.size = size;
        BPBlocks.blockList.add(this);
        this.placementType = type;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity tileentity = builder.get(LootParameters.BLOCK_ENTITY);
        List<ItemStack> itemStacks = new ArrayList<>();
        if(tileentity instanceof TileBPMultipart){
            tileentity = ((TileBPMultipart) tileentity).getTileForState(state);
        }
        if (tileentity instanceof TileBPMicroblock) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("block", ((TileBPMicroblock)tileentity).getBlock().getRegistryName().toString());
            ItemStack stack = new ItemStack(this);
            stack.setTag(nbt);
            stack.setDisplayName(new TranslationTextComponent(((TileBPMicroblock)tileentity).getBlock().getTranslationKey())
                    .appendText(" ")
                    .appendSibling(new TranslationTextComponent(this.getTranslationKey())));
            itemStacks.add(stack);
        }
        return itemStacks;
    }

    @Override
    public ResourceLocation getLootTable() {
        return new ResourceLocation(Refs.MODID + ":blocks/microblock");
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABBUtils.rotate(size, state.get(FACING));
    }

    public VoxelShape getSize() {
        return size;
    }

    @Override
    public Boolean blockCapability(BlockState state, Capability capability, @Nullable Direction side) {
        return side == state.get(FACING).getOpposite();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        AxisAlignedBB aabb = size.getBoundingBox();
        return AABBUtils.rotate(Block.makeCuboidShape(3, aabb.minY * 16, 3, 13, aabb.maxY, 13), state.get(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        if(placementType == PlacementType.NEAR && player != null && !player.isSneaking()) {
            Vec3d vec = context.getPlayer().getLookVec();
            return this.getDefaultState().with(FACING, Direction.getFacingFromVector(vec.x, vec.y, vec.z));
        }
        return this.getDefaultState().with(FACING, context.getFace());
    }

    public PlacementType getPlacementType() {
        return placementType;
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand) {
        return getDefaultState().with(FACING, facing);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileBPMicroblock();
    }


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileentity = worldIn.getTileEntity(pos);
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
