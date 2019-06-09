/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.worldgen;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockStoneOre extends Block {

    private String name;

    private boolean transparent = false;
    private boolean witherproof = false;
    private String[] tooltip = new String[] {};

    public BlockStoneOre(String name) {

        super(Material.ROCK);

        this.name = name;
        setResistance(5.0F);
        setHardness(4.0F);
        this.setHarvestLevel("pickaxe", 1);
        setTranslationKey(name);
        setCreativeTab(BPCreativeTabs.blocks);
        setSoundType(SoundType.STONE);
        setRegistryName(Refs.MODID, name);
        BPBlocks.blockList.add(this);
    }

    public Block setToolLevel(int level) {

        super.setHarvestLevel("pickaxe", level);
        return this;
    }

    @Override
    public String getTranslationKey() {

        return String.format("tile." + Refs.MODID + ":" + name);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Block.getBlockFromName(Refs.MODID + ":" + name));
    }


    // Allow storage blocks to be used as a beacon base
    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return this == BPBlocks.amethyst_block || this == BPBlocks.ruby_block || this == BPBlocks.malachite_block || this == BPBlocks.sapphire_block
                || this == BPBlocks.copper_block || this == BPBlocks.zinc_block || this == BPBlocks.silver_block
                || this == BPBlocks.tungsten_block;
    }

    public BlockStoneOre setTransparent(boolean transparent) {

        this.transparent = transparent;

        return this;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return !transparent;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return !transparent;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return !transparent;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return transparent ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.SOLID;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return transparent ? 0 : super.getLightOpacity(state, world, pos);
    }

    public BlockStoneOre setWitherproof(boolean witherproof) {

        this.witherproof = witherproof;
        return this;
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        if(witherproof){
            tooltip.add(MinecraftColor.RED.getChatColor() + "Witherproof");
        }
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        if (witherproof)
            return !(entity instanceof EntityWither) && super.canEntityDestroy(state, world, pos, entity);

        return super.canEntityDestroy(state, world, pos, entity);
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        if (!witherproof)
            super.onBlockExploded(world, pos, explosion);
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion) {

        return witherproof ? false : super.canDropFromExplosion(explosion);
    }

}
