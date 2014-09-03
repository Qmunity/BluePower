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

package com.bluepowermod.blocks.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.util.Refs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockStoneOre extends Block {

    private String name;

    public BlockStoneOre(String name) {

        super(Material.rock);

        this.name = name;
        this.setResistance(5.0F);
        this.setHardness(4.0F);
        this.setHarvestLevel("pickaxe", 1);
        this.setBlockName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
        this.setStepSound(soundTypeStone);
    }


    public Block setToolLevel(int level) {
        super.setHarvestLevel("pickaxe", level);
        return this;
    }

    @Override
    public String getUnlocalizedName() {

        return String.format("tile." + Refs.MODID + ":" + this.name);
    }

    @Override
    public Item getItemDropped(int par1, Random par2, int par3) {

        return Item.getItemFromBlock(Block.getBlockFromName(Refs.MODID + ":" + this.name));
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

        blockIcon = iconRegister.registerIcon(Refs.MODID + ":" + this.name);
    }

    // Allow storage blocks to be used as a beacon base
    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {

        return this == BPBlocks.amethyst_block || this == BPBlocks.ruby_block || this == BPBlocks.sapphire_block || this == BPBlocks.copper_block
                || this == BPBlocks.zinc_block || this == BPBlocks.silver_block || this == BPBlocks.tungsten_block;
    }
}
