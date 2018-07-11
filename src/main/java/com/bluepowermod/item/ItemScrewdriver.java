/*
 * This file is part of Blue Power.
 *
 *      Blue Power is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      Blue Power is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.item;

import com.bluepowermod.api.misc.IScrewdriver;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.block.machine.BlockEngine;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

;

public class ItemScrewdriver extends ItemBase implements IScrewdriver {

    public ItemScrewdriver() {

        setUnlocalizedName(Refs.SCREWDRIVER_NAME);
        setCreativeTab(BPCreativeTabs.tools);
        setMaxDamage(250);
        setMaxStackSize(1);
        setRegistryName(Refs.MODID + ":" + Refs.SCREWDRIVER_NAME);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        Block block = world.getBlockState(pos).getBlock();

            if (player.isSneaking() && block.rotateBlock(world, pos, side.getOpposite())) {
                damage(player.getHeldItem(hand), 1, player, false);
                return EnumActionResult.SUCCESS;
            } else if (block.rotateBlock(world, pos, side)) {
                damage(player.getHeldItem(hand), 1, player, false);
                return EnumActionResult.SUCCESS;
            }

        return EnumActionResult.PASS;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {

        return true;
    }

    @Override
    public boolean damage(ItemStack stack, int damage, EntityPlayer player, boolean simulated) {

        if (player != null && player.capabilities.isCreativeMode)
            return true;
        if ((stack.getItemDamage() % stack.getMaxDamage()) + damage > stack.getMaxDamage())
            return false;

        if (!simulated) {
            if (stack.attemptDamageItem(damage, new Random(), (EntityPlayerMP) player)) {
                if (player != null)
                    player.renderBrokenItemStack(stack);
                stack.setCount(stack.getCount() - 1);

                if (player != null && player instanceof EntityPlayer)
                    player.addStat(StatList.getObjectBreakStats(stack.getItem()), 1);

                if (stack.getCount() < 0)
                    stack.setCount(0);

                stack.setItemDamage(0);
            }
        }

        return true;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

}
