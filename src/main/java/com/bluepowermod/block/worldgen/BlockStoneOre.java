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

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.item.Item;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStoneOre extends Block {

    private String name;

    private boolean transparent = false;
    private boolean witherproof = false;
    private String[] tooltip = new String[] {};

    public BlockStoneOre(String name) {

        super(Material.rock);

        this.name = name;
        setResistance(5.0F);
        setHardness(4.0F);
        this.setHarvestLevel("pickaxe", 1);
        setBlockName(name);
        setCreativeTab(BPCreativeTabs.blocks);
        setStepSound(soundTypeStone);
    }

    public Block setToolLevel(int level) {

        super.setHarvestLevel("pickaxe", level);
        return this;
    }

    @Override
    public String getUnlocalizedName() {

        return String.format("tile." + Refs.MODID + ":" + name);
    }

    @Override
    public Item getItemDropped(int par1, Random par2, int par3) {

        return Item.getItemFromBlock(Block.getBlockFromName(Refs.MODID + ":" + name));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        blockIcon = iconRegister.registerIcon(Refs.MODID + ":" + name);
    }

    // Allow storage blocks to be used as a beacon base
    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {

        return this == BPBlocks.amethyst_block || this == BPBlocks.ruby_block || this == BPBlocks.sapphire_block
                || this == BPBlocks.copper_block || this == BPBlocks.zinc_block || this == BPBlocks.silver_block
                || this == BPBlocks.tungsten_block;
    }

    public BlockStoneOre setTransparent(boolean transparent) {

        this.transparent = transparent;

        return this;
    }

    @Override
    public boolean isNormalCube() {

        return !transparent;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isBlockNormalCube() {

        return !transparent;
    }

    @Override
    public boolean isOpaqueCube() {

        return !transparent;
    }

    @Override
    public boolean renderAsNormalBlock() {

        return !transparent;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderInPass(int pass) {

        return transparent ? true : super.canRenderInPass(pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {

        return transparent ? 1 : super.getRenderBlockPass();
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess w, int x, int y, int z, int p_149646_5_) {

        if (transparent)
            return w.getBlock(x, y, z) != this;
        return super.shouldSideBeRendered(w, x, y, z, p_149646_5_);
    }

    @Override
    public int getLightOpacity() {

        return transparent ? 0 : super.getLightOpacity();
    }

    public BlockStoneOre setWitherproof(boolean witherproof) {

        this.witherproof = witherproof;

        return this;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {

        if (witherproof)
            return !(entity instanceof EntityWither) && super.canEntityDestroy(world, x, y, z, entity);

        return super.canEntityDestroy(world, x, y, z, entity);
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {

        if (!witherproof)
            super.onBlockExploded(world, x, y, z, explosion);
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion) {

        return witherproof ? false : super.canDropFromExplosion(explosion);
    }

    public BlockStoneOre setTooltip(String... tooltip) {

        this.tooltip = tooltip;

        return this;
    }

    public String[] getTooltip() {

        return tooltip;
    }

}
