package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class BlockRubberLeaves extends LeavesBlock {

    public BlockRubberLeaves(){
        this.setRegistryName(Refs.MODID + ":" + Refs.RUBBERLEAVES_NAME);
        this.setTranslationKey(Refs.MODID + ":" + Refs.RUBBERLEAVES_NAME);
        this.setCreativeTab(BPCreativeTabs.blocks);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, true));
        BPBlocks.blockList.add(this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return Blocks.LEAVES.getRenderLayer();
    }

    @Override
    public boolean isOpaqueCube(BlockState state){
        return Blocks.LEAVES.isOpaqueCube(state);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldSideBeRendered(BlockState state, IBlockAccess world, BlockPos pos, Direction side){
        return Blocks.LEAVES.shouldSideBeRendered(state, world, pos, side);
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return NonNullList.withSize(1, new ItemStack(this));
    }

    @Override
    protected void dropApple(World worldIn, BlockPos pos, BlockState state, int chance) {
        Item sticky_resin = Item.getByNameOrId("ic2:misc_resource");
        if (sticky_resin != null && worldIn.rand.nextInt(chance) == 0){
            spawnAsEntity(worldIn, pos, new ItemStack(sticky_resin, 1, 4));
        }
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    @Nonnull
    @Override
    public BlockState getStateFromMeta(int meta){
        return this.getDefaultState()
                .withProperty(DECAYABLE, (meta & 4) == 0)
                .withProperty(CHECK_DECAY, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(BlockState state){
        int meta = 0;

        if (!state.getValue(DECAYABLE)){
            meta |= 4;
        }

        if (state.getValue(CHECK_DECAY)){
            meta |= 8;
        }

        return meta;
    }


    @Override
    public Item getItemDropped(BlockState state, Random rand, int fortune){
        return Item.getItemFromBlock(Blocks.SAPLING);
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.OAK;
    }
}
