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
import com.bluepowermod.container.inventory.InventoryItem;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class ItemSeedBag extends ItemBase {

    public ItemSeedBag(String name) {
        super(new Properties().maxStackSize(1));
        this.setRegistryName(Refs.MODID + ":" + name);
    }

    public static ItemStack getSeedType(ItemStack seedBag) {
        ItemStack seed = ItemStack.EMPTY;

        IInventory seedBagInventory = InventoryItem.getItemInventory(seedBag, "Seed Bag", 9);
        for (int i = 0; i < seedBagInventory.getSizeInventory(); i++) {
            ItemStack is = seedBagInventory.getStackInSlot(i);
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
        IInventory seedBagInventory = InventoryItem.getItemInventory(stack, "Seed Bag", 9);
        for (int i = 0; i < seedBagInventory.getSizeInventory(); i++) {
            ItemStack is = seedBagInventory.getStackInSlot(i);
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
    public ActionResult<ItemStack> onItemRightClick(World worldObj, PlayerEntity playerEntity, Hand handIn) {
        if (!worldObj.isRemote && playerEntity.isSneaking()) {
            //TODO: Open Gui
            //playerEntity.openGui(BluePower.instance, GuiIDs.SEEDBAG.ordinal(), worldObj, (int) playerEntity.posX, (int) playerEntity.posY,
            //(int) playerEntity.posZ);
        }
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerEntity.getHeldItem(handIn));
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

        IInventory seedBagInventory = InventoryItem.getItemInventory(player, player.getHeldItem(hand), "Seed Bag", 9);
        seedBagInventory.openInventory(player);

        ItemStack seed = getSeedType(player.getHeldItem(hand));
        if (!seed.isEmpty() && seed.getItem() instanceof IPlantable) {
            IPlantable plant = (IPlantable) seed.getItem();
            for (int modX = -2; modX < 3; modX++) {
                for (int modZ = -2; modZ < 3; modZ++) {
                    BlockState b = worldIn.getBlockState(pos.add(modX, 0, modZ));
                    if (b.getBlock().canSustainPlant(b, worldIn, pos, Direction.UP, plant)
                            && worldIn.isAirBlock(pos.add(modX, 1, modZ))) {
                        for (int i = 0; i < seedBagInventory.getSizeInventory(); i++) {
                            ItemStack is = seedBagInventory.getStackInSlot(i);
                            if (!is.isEmpty()) {

                                Item item = is.getItem();
                                item.onItemUse(new ItemUseContext(player, hand, new BlockRayTraceResult(
                                        new Vec3d( context.getHitVec().x + modX, context.getHitVec().y, context.getHitVec().z + modZ),
                                        context.getFace(), pos.add(modX, 0, modZ), false)));
                                seedBagInventory.decrStackSize(i, 0);
                                break;
                            }
                        }
                    }
                }
            }
            return ActionResultType.SUCCESS;

        }

        seedBagInventory.closeInventory(player);

        return ActionResultType.PASS;
    }
}
