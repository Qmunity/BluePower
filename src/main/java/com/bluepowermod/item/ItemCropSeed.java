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

package com.bluepowermod.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraft.util.EnumFacing;;

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;

public class ItemCropSeed extends ItemSeeds implements IPlantable {

    public static Block field_150925_a;

    public ItemCropSeed(Block blockCrop, Block blockSoil) {

        super(blockCrop, blockSoil);
        field_150925_a = blockCrop;
        this.setCreativeTab(BPCreativeTabs.items);
        this.setRegistryName(Refs.MODID + ":" + Refs.FLAXSEED_NAME);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (facing.ordinal() != 1) {
            return EnumActionResult.PASS;
        } else if (player.canPlayerEdit(pos, facing, itemStack) && player.canPlayerEdit(pos.add(0,1,0), facing, itemStack)) {
            if (world.getBlockState(pos).getBlock().canSustainPlant(world.getBlockState(pos),  world, pos, EnumFacing.UP, this) && world.isAirBlock(pos.add(0,1,0))
                    && (world.getBlockState(pos).getBlock().isFertile(world, pos))) {
                world.setBlockState(pos.add(0,1,0), field_150925_a.getDefaultState(), 2);
                itemStack.setCount(itemStack.getCount() - 1);
                player.setHeldItem(hand, itemStack);
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.PASS;
            }
        } else {
            return EnumActionResult.PASS;
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return field_150925_a.getDefaultState();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return String.format("item.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }
}
