/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileBPMultipart;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MoreThanHidden
 */
public class BlockBPMultipart extends ContainerBlock {


    public BlockBPMultipart() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F));
        setRegistryName(Refs.MODID + ":multipart");
        BPBlocks.blockList.add(this);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity tileentity = builder.get(LootParameters.BLOCK_ENTITY);
        List<ItemStack> itemStacks = new ArrayList<>();
        if (tileentity instanceof TileBPMultipart) {
            TileBPMultipart te = (TileBPMultipart)tileentity;
            LootContext.Builder finalBuilder = builder;

            te.getStates().stream().map(s -> s.getDrops(finalBuilder)).forEach(itemStacks::addAll);
        }
        return itemStacks;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileBPMultipart){
            ((TileBPMultipart) te).getStates().forEach(s -> s.onReplaced(worldIn, pos, s, isMoving));
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        Block block = Block.getBlockFromItem(player.getHeldItem(handIn).getItem());
        if(block != Blocks.AIR) {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof TileBPMultipart){
                BlockState partState = block.getStateForPlacement(new BlockItemUseContext(new ItemUseContext(player, handIn, hit)));
                if (partState != null) {
                    ((TileBPMultipart)te).addState(partState);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileBPMultipart){
            List<VoxelShape> shapeList = new ArrayList<>();
            ((TileBPMultipart) te).getStates().forEach(s -> shapeList.add(s.getShape(worldIn, pos)));
            if(shapeList.size() > 0)
                return shapeList.stream().reduce(shapeList.get(0), VoxelShapes::or);
        }
        return VoxelShapes.empty();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileBPMultipart){
            for(BlockState s : ((TileBPMultipart) te).getStates()){
                return new ItemStack(s.getBlock());
            }
        }
        return null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addHitEffects(BlockState state, World worldObj, RayTraceResult target, ParticleManager manager) {
        return true;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileBPMultipart) {
            ((TileBPMultipart) te).getStates().forEach(s -> s.neighborChanged(world, pos, blockIn, fromPos, bool));
        }
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos) {
        return state;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileBPMultipart();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
