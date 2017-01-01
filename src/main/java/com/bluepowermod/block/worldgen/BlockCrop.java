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

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

;

public class BlockCrop extends BlockCrops implements IGrowable {

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite[] iconArray;

    public BlockCrop() {

        this.setTickRandomly(true);
        this.setCreativeTab(null);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();
        this.setRegistryName(Refs.MODID + ":" + Refs.FLAXCROP_NAME);
    }

    @Override
    public String getUnlocalizedName() {

        return String.format("tile.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int l = getMetaFromState(source.getBlockState(pos));
        if (l <= 2) {
           return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
        } else if (l <= 4) {
           return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        } else if (l <= 6) {
           return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        } else {
           return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (world.getBlockState(pos).getBlock() instanceof BlockCrop) {
            if ( getMetaFromState(world.getBlockState(pos)) == 8) {
                world.setBlockState(pos.down(), getStateFromMeta(5), 2);
            }
        }
    }

    /**
     * is the block grass, dirt or farmland
     */
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getBlock()  == Blocks.FARMLAND;
    }


    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.getLight(pos.up()) >= 9) {
            int meta = getMetaFromState(state);

            if ((meta < 7) && (world.getBlockState(pos.down()).getBlock() instanceof BlockFarmland)) {
                if (random.nextInt(30) == 0) {
                    world.setBlockState(pos, getStateFromMeta(meta + 1), 2);
                }
            }
            if ((meta == 7) && (world.getBlockState(pos.down()).getBlock() instanceof BlockFarmland) && (world.getBlockState(pos.up()).getBlock() instanceof BlockAir)) {
                world.setBlockState(pos.up(), BPBlocks.flax_crop.getStateFromMeta(8), 2);
            }
            // If the bottom somehow becomes fully grown, correct it
            if ((meta > 7) && (world.getBlockState(pos.down()).getBlock() instanceof BlockFarmland)) {
                world.setBlockState(pos, this.getStateFromMeta(7), 2);
                world.setBlockState(pos.up(), BPBlocks.flax_crop.getStateFromMeta(8), 2);
            }

        }
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public TextureAtlasSprite[] getIconArray() {
        return this.iconArray;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    /**
     * getSeed
     * @return
     */
    @Override
    protected Item getSeed() {
        return BPItems.flax_seeds;
    }

    /**
     * getCrop
     * @return
     */
    @Override
    protected Item getCrop() {
        return Items.STRING;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return getMetaFromState(state) == 8 ? this.getCrop() : this.getSeed();
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
     */
    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
        return getMetaFromState(world.getBlockState(pos)) < 7;
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getSeed());
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(TextureMap iconRegister) {

        this.iconArray = new TextureAtlasSprite[9];

        for (int i = 0; i < this.iconArray.length; ++i) {
            int tex = 0;
            if (i == 0 || i == 1) { tex = 0; }
            else if (i == 2) { tex = 1; }
            else if ((i == 3) || (i == 4)) { tex = 2; }
            else if ((i == 5) || (i == 6)) { tex = 3; }
            else if (i == 7) { tex = 4; }
            else if (i == 8) { tex = 5; }

            this.iconArray[i] = iconRegister.registerSprite(new ResourceLocation(this.getRegistryName().toString() + "_stage_" + tex));
        }
    }

    /**
     * fertilize
     * @param world
     * @param random
     */
    @Override
    public void grow(World world, Random random, BlockPos pos, IBlockState state) {
        int meta = getMetaFromState(world.getBlockState(pos));
        if (world.getBlockState(pos.up()).getBlock() instanceof BlockAir && (meta < 7) && !(world.getBlockState(pos.down()).getBlock() instanceof BlockCrop)) {
            meta = meta + MathHelper.getInt(world.rand, 2, 5);
            if (meta > 6) {
                world.setBlockState(pos, getStateFromMeta(7), 2);
                world.setBlockState(pos.up(), BPBlocks.flax_crop.getStateFromMeta(8), 2);
            } else {
                world.setBlockState(pos, getStateFromMeta(meta), 2);
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        if (getMetaFromState(state) >= 8) {
            for (int i = 0; i < 3 + fortune; ++i) {
                if (RANDOM.nextInt(15) <= getMetaFromState(state)) {
                    ret.add(new ItemStack(this.getCrop(), 1, 0));
                }
            }
            if (RANDOM.nextBoolean()) {
                ret.add(new ItemStack(this.getSeed(), 1, 0));
            }
        } else if (getMetaFromState(state) == 7) {
            ret.add(new ItemStack(this.getSeed(), 1 + RANDOM.nextInt(2), 0));
        } else {
            ret.add(new ItemStack(this.getSeed(), 1, 0));
        }
        return ret;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return super.canPlaceBlockAt((World)world, pos) && world.isAirBlock(pos.up());
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        if ((getMetaFromState(world.getBlockState(pos)) == 7) && (world.getBlockState(pos.up()).getBlock() == Blocks.AIR)) {
            ((World)world).setBlockState(pos, getStateFromMeta(5), 2);
        }
        if ((getMetaFromState(world.getBlockState(pos)) == 8) && world.getBlockState(pos.down()).getBlock().getMetaFromState(world.getBlockState(pos.down())) != 7) {
            ((World)world).setBlockToAir(pos);
        }
        if ((getMetaFromState(world.getBlockState(pos)) < 8) && (world.getBlockState(pos.down()).getBlock() instanceof BlockCrop)) {
            ((World)world).setBlockToAir(pos);
        }
        this.checkAndDropBlock((World)world, pos, world.getBlockState(pos));
    }

    /**
     * checks if the block can stay, if not drop as item
     */
    @Override
    protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(world, pos, state)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
        }
    }

    /**
     * Can this block stay at this position. Similar to canPlaceBlockAt except gets checked often with plants.
     */
    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        if (world.getBlockState(pos) != this) return super.canBlockStay(world, pos, state);
        if (world.getBlockState(pos.down()).getBlock().canSustainPlant(world.getBlockState(pos.down()), world, pos.down(), EnumFacing.UP, this) && world.getBlockState(pos.down()).getBlock().isFertile(world, pos.down())) {
            return true;
        }
        return (world.getBlockState(pos.down()).getBlock() instanceof com.bluepowermod.block.worldgen.BlockCrop) && (world.getBlockState(pos.down()).getBlock().getMetaFromState(world.getBlockState(pos.down())) == 7);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

}
