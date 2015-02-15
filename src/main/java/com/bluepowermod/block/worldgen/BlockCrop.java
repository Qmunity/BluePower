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

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCrop extends BlockCrops implements IGrowable {

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public BlockCrop() {

        this.setTickRandomly(true);
        this.setCreativeTab(null);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.disableStats();
        this.setBlockName(Refs.FLAXCROP_NAME);
        this.setBlockTextureName(Refs.MODID + ":" + Refs.FLAXCROP_NAME);
    }

    @Override
    public String getUnlocalizedName() {

        return String.format("tile.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {

        int l = world.getBlockMetadata(x, y, z);
        if (l <= 2) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
        } else if (l <= 4) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        } else if (l <= 6) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {

        if (world.getBlock(x, y, z) instanceof BlockCrop) {
            if (world.getBlockMetadata(x, y, z) == 8) {
                world.setBlockMetadataWithNotify(x, y - 1, z, 5, 2);
            }
        }
    }

    /**
     * is the block grass, dirt or farmland
     */
    @Override
    protected boolean canPlaceBlockOn(Block block) {

        return block == Blocks.farmland;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {

        if (world.getBlockLightValue(x, y + 1, z) >= 9) {
            int meta = world.getBlockMetadata(x, y, z);

            if ((meta < 7) && (world.getBlock(x, y -1, z) instanceof BlockFarmland)) {
                if (random.nextInt(30) == 0) {
                    world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
                }
            }
            if ((meta == 7) && (world.getBlock(x, y - 1, z) instanceof BlockFarmland) && (world.getBlock(x, y + 1, z) instanceof BlockAir)) {
                world.setBlock(x, y + 1, z, BPBlocks.flax_crop, 8, 2);
            }
            // If the bottom somehow becomes fully grown, correct it
            if ((meta > 7) && (world.getBlock(x, y - 1, z) instanceof BlockFarmland)) {
                world.setBlockMetadataWithNotify(x, y, z, 7, 2);
                world.setBlock(x, y + 1, z, BPBlocks.flax_crop, 8, 2);
            }

        }
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {

        if (meta < 0 || meta > 8) {
            meta = 8;
        }

        return this.iconArray[meta];
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType() {

        return 6;
    }

    /**
     * getSeed
     * @return
     */
    @Override
    protected Item func_149866_i() {

        return BPItems.flax_seeds;
    }

    /**
     * getCrop
     * @return
     */
    @Override
    protected Item func_149865_P() {

        return Items.string;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int p_149690_5_, float p_149690_6_, int p_149690_7_) {

        super.dropBlockAsItemWithChance(world, x, y, z, p_149690_5_, p_149690_6_, 0);
    }

    @Override
    public Item getItemDropped(int meta, Random random, int p_149650_3_) {

        return meta == 8 ? this.func_149865_P() : this.func_149866_i();
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {

        return 1;
    }

    /**
     * boolean canFertilise
     * @param world
     * @param x
     * @param y
     * @param z
     * @param isClient
     * @return
     */
    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isClient) {

        return world.getBlockMetadata(x, y, z) < 7;
    }

    /**
     * shouldFertilize
     * @param world
     * @param random
     * @param x
     * @param y
     * @param z
     * @return
     */
    @Override
    public boolean func_149852_a(World world, Random random, int x, int y, int z) {

        return true;
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    @Override
    public Item getItem(World world, int x, int y, int z) {

        return this.func_149866_i();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

        this.iconArray = new IIcon[9];

        for (int i = 0; i < this.iconArray.length; ++i) {
            int tex = 0;
            if (i == 0 || i == 1) { tex = 0; }
            else if (i == 2) { tex = 1; }
            else if ((i == 3) || (i == 4)) { tex = 2; }
            else if ((i == 5) || (i == 6)) { tex = 3; }
            else if (i == 7) { tex = 4; }
            else if (i == 8) { tex = 5; }

            this.iconArray[i] = iconRegister.registerIcon(this.getTextureName() + "_stage_" + tex);
        }
    }

    /**
     * fertilize
     * @param world
     * @param random
     * @param x
     * @param y
     * @param z
     */
    @Override
    public void func_149853_b(World world, Random random, int x, int y, int z) {

        int meta = world.getBlockMetadata(x, y, z);
        if (world.getBlock(x, y + 1, z) instanceof BlockAir && (meta < 7) && !(world.getBlock(x, y - 1, z) instanceof BlockCrop)) {
            meta = meta + MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
            if (meta > 6) {
                world.setBlockMetadataWithNotify(x, y, z, 7, 2);
                world.setBlock(x, y + 1, z, BPBlocks.flax_crop, 8, 2);
            } else {
                world.setBlockMetadataWithNotify(x, y, z, meta, 2);
            }
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {

        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        if (metadata >= 8) {
            for (int i = 0; i < 3 + fortune; ++i) {
                if (world.rand.nextInt(15) <= metadata) {
                    ret.add(new ItemStack(this.func_149865_P(), 1, 0));
                }
            }
            if (world.rand.nextBoolean()) {
                ret.add(new ItemStack(this.func_149866_i(), 1, 0));
            }
        } else if (metadata == 7) {
            ret.add(new ItemStack(this.func_149866_i(), 1 + world.rand.nextInt(2), 0));
        } else {
            ret.add(new ItemStack(this.func_149866_i(), 1, 0));
        }
        return ret;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {

        return super.canPlaceBlockAt(world, x, y, z) && world.isAirBlock(x, y + 1, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        super.onNeighborBlockChange(world, x, y, z, block);
        if ((world.getBlockMetadata(x, y, z) == 7) && (world.getBlock(x, y + 1, z) == Blocks.air)) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }
        if ((world.getBlockMetadata(x, y , z) == 8) && (world.getBlockMetadata(x, y - 1, z) != 7)) {
            world.setBlockToAir(x, y, z);
        }
        if ((world.getBlockMetadata(x, y, z) < 8) && (world.getBlock(x, y - 1, z) instanceof BlockCrop)) {
            world.setBlockToAir(x, y, z);
        }
        this.checkAndDropBlock(world, x, y, z);
    }

    /**
     * checks if the block can stay, if not drop as item
     */
    @Override
    protected void checkAndDropBlock(World world, int x, int y, int z) {

        if (!this.canBlockStay(world, x, y, z)) {
            int l = world.getBlockMetadata(x, y, z);
            this.dropBlockAsItem(world, x, y, z, l, 0);
            world.setBlock(x, y, z, Blocks.air, 0, 2);
        }
    }

    /**
     * Can this block stay at this position. Similar to canPlaceBlockAt except gets checked often with plants.
     */
    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {

        if (world.getBlock(x, y, z) != this) return super.canBlockStay(world, x, y, z);
        if (world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this) && world.getBlock(x, y - 1, z).isFertile(world, x, y - 1, z)) {
            return true;
        }
        return (world.getBlock(x, y - 1, z) instanceof com.bluepowermod.block.worldgen.BlockCrop) && (world.getBlockMetadata(x, y - 1, z) == 7);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {

    	return EnumPlantType.Crop;
    }
}
