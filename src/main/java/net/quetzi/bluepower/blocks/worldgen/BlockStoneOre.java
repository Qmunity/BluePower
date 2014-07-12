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

package net.quetzi.bluepower.blocks.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

import java.util.Random;

public class BlockStoneOre extends Block {

    private String name;

    public BlockStoneOre(String name) {

        super(Material.rock);

        this.name = name;

        this.setBlockName(name);
        this.setHardness(3.0F);
        if (name == Refs.BASALT_NAME) {
            this.setResistance(25.0F);
            this.setHarvestLevel("pickaxe", 1);
        } else if (name == Refs.MARBLE_NAME) {
            this.setResistance(1.0F);
            this.setHarvestLevel("pickaxe", 1);
            this.setHardness(1.5F);
        } else if (name == Refs.TUNGSTENORE_NAME) {
        	this.setResistance(6.0F);
        	this.setHarvestLevel("pickaxe", 3);
        }else if (name == Refs.TUNGSTENBLOCK_NAME) {
        	this.setResistance(25.0F);
        	this.setHarvestLevel("pickaxe", 3);
        }else {
            this.setResistance(5.0F);
            this.setHarvestLevel("pickaxe", 2);
        }
        // this.textureName = Refs.MODID + ":" + name;
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
        this.setStepSound(soundTypeStone);
    }


    @Override
    public String getUnlocalizedName() {

        return String.format("tile.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    @Override
    public Item getItemDropped(int par1, Random par2, int par3) {

        if (this.name.matches(Refs.BASALT_NAME)) {
            return Item.getItemFromBlock(Block.getBlockFromName(Refs.MODID + ":" + Refs.BASALTCOBBLE_NAME));
        }
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
                || this == BPBlocks.tin_block || this == BPBlocks.silver_block || this == BPBlocks.tungsten_block;
    }
}
