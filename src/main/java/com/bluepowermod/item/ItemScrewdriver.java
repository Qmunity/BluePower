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
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.Random;


public class ItemScrewdriver extends ItemBase implements IScrewdriver {

    public ItemScrewdriver() {
        super(new Properties().maxDamage(250));
        setRegistryName(Refs.MODID + ":" + Refs.SCREWDRIVER_NAME);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        Block block = context.getWorld().getBlockState(context.getPos()).getBlock();
        if (context.getPlayer() != null && context.getPlayer().isCrouching()){
            block.rotate(context.getWorld().getBlockState(context.getPos()), context.getWorld(), context.getPos(), Rotation.CLOCKWISE_180);
        } else{
            block.rotate(context.getWorld().getBlockState(context.getPos()), context.getWorld(), context.getPos(), Rotation.CLOCKWISE_90);
        }
        damage(context.getPlayer().getHeldItem(context.getHand()), 1, context.getPlayer(), false);
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean damage(ItemStack stack, int damage, PlayerEntity player, boolean simulated) {

        if (player != null && player.isCreative())
            return true;
        if ((stack.getDamage() % stack.getMaxDamage()) + damage > stack.getMaxDamage())
            return false;

        if (!simulated) {
            if (player instanceof ServerPlayerEntity && stack.attemptDamageItem(damage, new Random(), (ServerPlayerEntity) player)) {
                player.sendBreakAnimation(Hand.MAIN_HAND);
                stack.setCount(stack.getCount() - 1);
                player.addStat(Stats.ITEM_BROKEN.get(stack.getItem()), 1);

                if (stack.getCount() < 0)
                    stack.setCount(0);

                stack.setDamage(0);
            }
        }

        return true;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
        return true;
    }

}
