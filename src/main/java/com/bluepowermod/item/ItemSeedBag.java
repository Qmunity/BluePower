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
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

;

public class ItemSeedBag extends ItemBase {

    public ItemSeedBag(String name) {

        this.setCreativeTab(BPCreativeTabs.items);
        this.setUnlocalizedName(name);
        this.setRegistryName(Refs.MODID + ":" + name);
        this.maxStackSize = 1;
    }

    public static ItemStack getSeedType(ItemStack seedBag) {
        ItemStack seed = null;

        IInventory seedBagInventory = InventoryItem.getItemInventory(seedBag, "Seed Bag", 9);
        for (int i = 0; i < seedBagInventory.getSizeInventory(); i++) {
            ItemStack is = seedBagInventory.getStackInSlot(i);
            if (is != null) {
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

        return stack.getTagCompound() != null;
    }

    public int getItemDamageForDisplay(ItemStack stack) {

        int items = 0;
        IInventory seedBagInventory = InventoryItem.getItemInventory(stack, "Seed Bag", 9);
        for (int i = 0; i < seedBagInventory.getSizeInventory(); i++) {
            ItemStack is = seedBagInventory.getStackInSlot(i);
            if (is != null) {
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
    public ActionResult<ItemStack> onItemRightClick(World worldObj, EntityPlayer playerEntity, EnumHand handIn) {
        if (!worldObj.isRemote && playerEntity.isSneaking()) {
            playerEntity.openGui(BluePower.instance, GuiIDs.SEEDBAG.ordinal(), worldObj, (int) playerEntity.posX, (int) playerEntity.posY,
                    (int) playerEntity.posZ);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerEntity.getHeldItem(handIn));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            return EnumActionResult.PASS;
        }

        IInventory seedBagInventory = InventoryItem.getItemInventory(player, player.getHeldItem(hand), "Seed Bag", 9);
        seedBagInventory.openInventory(player);

        ItemStack seed = getSeedType(player.getHeldItem(hand));
        if (seed != null && seed.getItem() instanceof IPlantable) {
            IPlantable plant = (IPlantable) seed.getItem();
            for (int modX = -2; modX < 3; modX++) {
                for (int modZ = -2; modZ < 3; modZ++) {
                    IBlockState b = worldIn.getBlockState(pos.add(modX, 0, modZ));
                    if (b.getBlock().canSustainPlant(b, worldIn, pos, EnumFacing.UP, plant)
                            && worldIn.isAirBlock(pos.add(modX, 1, modZ))) {
                        for (int i = 0; i < seedBagInventory.getSizeInventory(); i++) {
                            ItemStack is = seedBagInventory.getStackInSlot(i);
                            if (is != null) {

                                Item item = is.getItem();
                                item.onItemUse(player, worldIn, pos.add(modX, 0, modZ), hand, facing, hitX + modX, hitY, hitZ + modZ);
                                seedBagInventory.decrStackSize(i, 0);
                                break;
                            }
                        }
                    }
                }
            }
            return EnumActionResult.SUCCESS;

        }

        seedBagInventory.closeInventory(player);

        return EnumActionResult.PASS;
    }
}
