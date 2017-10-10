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

package com.bluepowermod.block.machine;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAlloyFurnace extends BlockContainerBase {

    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockAlloyFurnace() {

        super(Material.ROCK, TileAlloyFurnace.class);
        setUnlocalizedName(Refs.ALLOYFURNACE_NAME);
        setRegistryName(Refs.MODID, Refs.ALLOYFURNACE_NAME);
        setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE);
    }


    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState()
                .withProperty(FACING, EnumFacing.HORIZONTALS[meta & 3])
                .withProperty(ACTIVE, (meta & 4) != 0);
    }

    public static void setState(boolean active, World worldIn, BlockPos pos){
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        worldIn.setBlockState(pos, iblockstate.withProperty(ACTIVE, active), 3);
        if (tileentity != null){
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack iStack) {
        super.onBlockPlacedBy(world, pos, state, placer, iStack);
        world.setBlockState(pos, state.withProperty(FACING, placer.getAdjustedHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() + (state.getValue(ACTIVE) ? 4 : 0);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }


    @Override
    public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rnd) {
        if (stateIn.getValue(ACTIVE)) {
            int l = stateIn.getValue(FACING).ordinal();
            float f = pos.getX() + 0.5F;
            float f1 = pos.getY() + 0.0F + rnd.nextFloat() * 6.0F / 16.0F;
            float f2 = pos.getZ() + 0.5F;
            float f3 = 0.52F;
            float f4 = rnd.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BPBlocks.alloyfurnace);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(ACTIVE) ? 13 : 0;
    }

    @Override
    public GuiIDs getGuiID() {

        return GuiIDs.ALLOY_FURNACE;
    }

    @Override
    protected boolean canRotateVertical() {

        return false;
    }

}
