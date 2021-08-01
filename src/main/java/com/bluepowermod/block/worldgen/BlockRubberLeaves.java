package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import RenderShape;

public class BlockRubberLeaves extends LeavesBlock {

    public BlockRubberLeaves(Properties properties){
        super(properties);
        this.setRegistryName(Refs.MODID + ":" + Refs.RUBBERLEAVES_NAME);
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, 7).setValue(PERSISTENT, true));
        BPBlocks.blockList.add(this);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return Blocks.OAK_LEAVES.getRenderShape(state);
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nullable Player player, @Nonnull ItemStack item, World world, BlockPos pos, int fortune) {
        return NonNullList.withSize(1, new ItemStack(this));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(DISTANCE, PERSISTENT);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        drops.add(new ItemStack(Item.byBlock(BPBlocks.rubber_sapling)));
        return drops;
    }

}
