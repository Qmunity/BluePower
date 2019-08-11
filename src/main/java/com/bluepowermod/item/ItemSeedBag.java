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

import com.bluepowermod.BluePower;
import com.bluepowermod.container.ContainerCanvasBag;
import com.bluepowermod.container.ContainerSeedBag;
import com.bluepowermod.container.inventory.InventoryItem;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class ItemSeedBag extends ItemBase implements INamedContainerProvider {

    public ItemSeedBag(String name) {
        super(new Properties().maxStackSize(1));
        this.setRegistryName(Refs.MODID + ":" + name);
    }

    public static ItemStack getSeedType(ItemStack seedBag) {
        ItemStack seed = ItemStack.EMPTY;

        ItemStackHandler seedBagInvHandler = new ItemStackHandler(9);

        //Get Items from the NBT Handler
        if (seedBag.hasTag()) seedBagInvHandler.deserializeNBT(seedBag.getTag().getCompound("inv"));

        for (int i = 0; i < 9; i++) {
            ItemStack is = seedBagInvHandler.getStackInSlot(i);
            if (!is.isEmpty()) {
                seed = is;
            }
        }

        return seed;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {

        return 1D - (double) getItemDamageForDisplay(stack) / (double) 576;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {

        return stack.getTag() != null;
    }

    public int getItemDamageForDisplay(ItemStack stack) {

        int items = 0;
        ItemStackHandler seedBagInvHandler = new ItemStackHandler(9);

        //Get Items from the NBT Handler
        if (stack.hasTag()) seedBagInvHandler.deserializeNBT(stack.getTag().getCompound("inv"));

        for (int i = 0; i < 8; i++) {
            ItemStack is = seedBagInvHandler.getStackInSlot(i);
            if (!is.isEmpty()) {
                items += is.getCount();
            }
        }
        return items;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {

        return 576;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
        if (!world.isRemote && player.isSneaking()) {
            NetworkHooks.openGui((ServerPlayerEntity) player, this);
        }
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, player.getHeldItem(handIn));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World worldIn = context.getWorld();
        Hand hand = context.getHand();
        BlockPos pos = context.getPos();

        if (player.isSneaking()) {
            return ActionResultType.PASS;
        }

        ItemStackHandler seedBagInvHandler = new ItemStackHandler(9);

        //Get Active hand
        Hand activeHand = Hand.MAIN_HAND;
        ItemStack seedBag = player.getHeldItem(activeHand);
        if(!(seedBag.getItem() instanceof ItemSeedBag)){
            seedBag = player.getHeldItemOffhand();
            activeHand = Hand.OFF_HAND;
        }

        //Get Items from the NBT Handler
        if (seedBag.hasTag()) seedBagInvHandler.deserializeNBT(seedBag.getTag().getCompound("inv"));

        ItemStack seed = getSeedType(player.getHeldItem(hand));
        Block block = Block.getBlockFromItem(seed.getItem());
        if (!seed.isEmpty() && block instanceof IPlantable) {
            IPlantable plant = (IPlantable) block;
            BlockState b = worldIn.getBlockState(pos);
            if (b.getBlock().canSustainPlant(b, worldIn, pos, Direction.UP, plant)
                    && worldIn.isAirBlock(pos.offset(Direction.UP))) {
                for (int i = 0; i < 9; i++) {
                    ItemStack is = seedBagInvHandler.getStackInSlot(i);
                    if (!is.isEmpty()) {
                        worldIn.setBlockState(pos.offset(Direction.UP), block.getDefaultState(), 0);
                        seedBagInvHandler.extractItem(i, 1, false);
                        break;
                    }
                }

                //Update items in the NBT
                if (!seedBag.hasTag())
                    seedBag.setTag(new CompoundNBT());
                if (seedBag.getTag() != null) {
                    seedBag.getTag().put("inv", seedBagInvHandler.serializeNBT());
                }

                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Refs.SEEDBAG_NAME);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new ContainerSeedBag(id, inventory);
    }

}
