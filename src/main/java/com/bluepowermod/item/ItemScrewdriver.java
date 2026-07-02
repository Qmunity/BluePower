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
import com.bluepowermod.block.gates.BlockGateBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;

import java.util.Random;

import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;

import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ItemScrewdriver extends ItemBase implements IScrewdriver {

    public ItemScrewdriver() {
        super(new Properties().durability(250));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        BlockEntity te = context.getLevel().getBlockEntity(context.getClickedPos());
        BlockState state;
        if (te instanceof TileBPMultipart multipart){
            state = MultipartUtils.getClosestState(context.getPlayer(), context.getClickedPos());
        } else {
            state = context.getLevel().getBlockState(context.getClickedPos());
        }
        if (context.getPlayer() != null && context.getPlayer().isCrouching()){
            if (state.getBlock() instanceof BlockGateBase gateBase){
                if (!gateBase.cycleDisabledStates(state, context.getLevel(), context.getClickedPos())){
                    state.getBlock().rotate(state, context.getLevel(), context.getClickedPos(), Rotation.CLOCKWISE_180);
                }
            } else {
                state.getBlock().rotate(state, context.getLevel(), context.getClickedPos(), Rotation.CLOCKWISE_180);
            }
        } else{
            state.getBlock().rotate(state, context.getLevel(), context.getClickedPos(), Rotation.CLOCKWISE_90);
        }
        damage(context.getPlayer().getItemInHand(context.getHand()), 1, context.getPlayer(), false);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean damage(ItemStack stack, int damage, Player player, boolean simulated) {

        if (player != null && player.isCreative())
            return true;
        if ((stack.getDamageValue() % stack.getMaxDamage()) + damage > stack.getMaxDamage())
            return false;

        if (!simulated) {
            if (player instanceof ServerPlayer && stack.hurt(damage, RandomSource.create(), (ServerPlayer) player)) {
                player.broadcastBreakEvent(InteractionHand.MAIN_HAND);
                stack.setCount(stack.getCount() - 1);
                player.awardStat(Stats.ITEM_BROKEN.get(stack.getItem()), 1);

                if (stack.getCount() < 0)
                    stack.setCount(0);

                stack.setDamageValue(0);
            }
        }

        return true;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader world, BlockPos pos, Player player) {
        return true;
    }

}
