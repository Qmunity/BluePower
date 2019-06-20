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

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public class ItemSilkyScrewdriver extends ItemBase {

    public ItemSilkyScrewdriver() {

        setTranslationKey(Refs.SILKYSCREWDRIVER_NAME);
        setCreativeTab(BPCreativeTabs.tools);
        setMaxDamage(250);
        setMaxStackSize(1);
        setRegistryName(Refs.MODID + ":" + Refs.SILKYSCREWDRIVER_NAME);
    }


    @Override
    public ActionResultType onItemUseFirst(PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, Hand hand) {
            return ActionResultType.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isFull3D() {

        return true;
    }

    @Override
    public UseAction getItemUseAction(ItemStack par1ItemStack) {

        return UseAction.BLOCK;
    }
}
