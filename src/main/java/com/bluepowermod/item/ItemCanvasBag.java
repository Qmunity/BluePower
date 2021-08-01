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
 *     
 *     @author Lumien
 */

package com.bluepowermod.item;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.container.ContainerCanvasBag;
import com.bluepowermod.reference.Refs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.inventory.container.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.InteractionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

import net.minecraft.world.item.Item.Properties;

import ActionResult;

public class ItemCanvasBag extends ItemColorableOverlay implements MenuProvider{
    
    public ItemCanvasBag(MinecraftColor color) {
        super(color, Refs.CANVASBAG_NAME, new Properties().stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(Level world, Player player, InteractionHand handIn) {
        if (!world.isClientSide) {
            NetworkHooks.openGui((ServerPlayer) player, this);
        }
        return new ActionResult<ItemStack>(InteractionResult.SUCCESS, player.getItemInHand(handIn));
    }

    @Override
    public Component getDisplayName() {
        return new StringTextComponent(Refs.CANVASBAG_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, PlayerInventory inventory, Player player) {
        return new ContainerCanvasBag(id, inventory);
    }
}
