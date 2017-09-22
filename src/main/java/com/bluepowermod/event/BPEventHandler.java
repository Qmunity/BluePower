/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.event;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.client.gui.GuiCircuitDatabaseSharing;
import com.bluepowermod.container.ContainerSeedBag;
import com.bluepowermod.container.inventory.InventoryItem;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPEnchantments;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.item.ItemSeedBag;
import com.bluepowermod.item.ItemSickle;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageServerTickTime;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.co.qmunity.lib.helper.MathHelper;

public class BPEventHandler {

    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.world.getWorldTime() % 200 == 0) {
                double tickTime = MathHelper.mean(event.world.getMinecraftServer().tickTimeArray) * 1.0E-6D;//In case world are going to get their own thread: MinecraftServer.getServer().worldTickTimes.get(event.world.provider.dimensionId)
                BPNetworkHandler.INSTANCE.sendToDimension(new MessageServerTickTime(tickTime), event.world.provider.getDimension());
            }
        }
    }

    @SubscribeEvent
    public void onAnvilEvent(AnvilUpdateEvent event) {

        if (!event.getLeft().isEmpty() && event.getLeft().getItem() == BPItems.screwdriver) {
            if (!event.getRight().isEmpty() && event.getRight().getItem() == Items.ENCHANTED_BOOK) {
                if (EnchantmentHelper.getEnchantments(event.getRight()).get(Enchantments.SILK_TOUCH) != null) {
                    event.setOutput(new ItemStack(BPItems.silky_screwdriver, 1, event.getLeft().getItemDamage()));
                    event.setCost(20);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {

        if (event.getEntityPlayer().capabilities.isCreativeMode) {
            ItemStack heldItem = event.getEntityPlayer().getHeldItem(event.getHand());
            if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemSickle) {
                heldItem.getItem().onBlockDestroyed(heldItem, event.getWorld(), event.getWorld().getBlockState(event.getPos()), event.getPos(), event.getEntityPlayer());
            }
        }
    }

    @SubscribeEvent
    public void itemPickUp(EntityItemPickupEvent event) {

        EntityPlayer player = event.getEntityPlayer();
        ItemStack pickUp = event.getItem().getItem();
        if (!(player.openContainer instanceof ContainerSeedBag)) {
            for (ItemStack is : player.inventory.mainInventory) {
                if (!is.isEmpty() && is.getItem() instanceof ItemSeedBag) {
                    ItemStack seedType = ItemSeedBag.getSeedType(is);
                    if (!seedType.isEmpty() && seedType.isItemEqual(pickUp)) {
                        InventoryItem inventory = InventoryItem.getItemInventory(is, "Seed Bag", 9);
                        inventory.openInventory(event.getEntityPlayer());
                        ItemStack pickedUp = TileEntityHopper.putStackInInventoryAllSlots(null, inventory, pickUp, null);
                        inventory.closeInventory(is);

                        if (pickedUp.isEmpty()) {
                            event.setResult(Event.Result.ALLOW);
                            event.getItem().setDead();
                            return;
                        } else {
                            event.getItem().setEntityItemStack(pickedUp);
                        }
                    }
                }
            }
        }
    }

    private boolean isAttacking = false;

    @SubscribeEvent
    public void onEntityAttack(LivingAttackEvent event) {

        if (!isAttacking && event.getSource() instanceof EntityDamageSource) {// this event will be trigger recursively by EntityLiving#attackEntityFrom,
            // so we need to stop the loop.
            EntityDamageSource entitySource = (EntityDamageSource) event.getSource();

            if (entitySource.getEntity() instanceof EntityPlayer) {
                EntityPlayer killer = (EntityPlayer) entitySource.getEntity();

                if (!killer.inventory.getCurrentItem().isEmpty()) {
                    if (EnchantmentHelper.getEnchantments(killer.inventory.getCurrentItem()).containsKey(BPEnchantments.disjunction)) {
                        if (event.getEntityLiving() instanceof EntityEnderman || event.getEntityLiving() instanceof EntityDragon) {
                            int level = EnchantmentHelper.getEnchantmentLevel(BPEnchantments.disjunction, killer.inventory.getCurrentItem());
                            isAttacking = true;
                            event.getEntityLiving().attackEntityFrom(event.getSource(), event.getAmount() * (level * 0.5F + 1));
                            isAttacking = false;
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {

        if (event.getSource() instanceof EntityDamageSource) {
            EntityDamageSource entitySource = (EntityDamageSource) event.getSource();

            if (entitySource.getEntity() instanceof EntityPlayer) {
                EntityPlayer killer = (EntityPlayer) entitySource.getEntity();

                if (!killer.inventory.getCurrentItem().isEmpty()) {
                    if (EnchantmentHelper.getEnchantments(killer.inventory.getCurrentItem()).containsKey(BPEnchantments.vorpal)) {
                        int level = EnchantmentHelper.getEnchantmentLevel(BPEnchantments.vorpal, killer.inventory.getCurrentItem());

                        if (level == 1) {
                            if (killer.world.rand.nextInt(6) == 1) {
                                dropHeads(event);
                            }
                        } else if (level == 2) {
                            if (killer.world.rand.nextInt(3) == 1) {
                                dropHeads(event);
                            }
                        }
                    }
                }
            }
        }
    }

    private void dropHeads(LivingDeathEvent event) {

        if (event.getEntityLiving() instanceof EntityCreeper) {
            event.getEntityLiving().entityDropItem(new ItemStack(Items.SKULL, 1, 4), 0.0F);
        }

        if (event.getEntityLiving() instanceof EntityPlayer) {
            ItemStack drop = new ItemStack(Items.SKULL, 1, 3);
            drop.setTagCompound(new NBTTagCompound());
            drop.getTagCompound().setString("SkullOwner", ((EntityPlayer) event.getEntityLiving()).getDisplayName().getFormattedText());
            event.getEntityLiving().entityDropItem(drop, 0.0F);
        }

        if (event.getEntityLiving() instanceof AbstractSkeleton) {
            AbstractSkeleton sk = (AbstractSkeleton) event.getEntityLiving();

            if (sk instanceof EntitySkeleton) {
                event.getEntityLiving().entityDropItem(new ItemStack(Items.SKULL, 1, 0), 0.0F);
            } else {
                event.getEntityLiving().entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0F);
            }
        }

        if (event.getEntityLiving() instanceof EntityZombie) {
            event.getEntityLiving().entityDropItem(new ItemStack(Items.SKULL, 1, 2), 0.0F);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onItemTooltip(ItemTooltipEvent event) {

        if (event.getItemStack().hasTagCompound() && event.getItemStack().getTagCompound().hasKey("tileData")
                && !event.getItemStack().getTagCompound().getBoolean("hideSilkyTooltip")) {
            event.getToolTip().add(I18n.format("gui.tooltip.hasSilkyData"));
        }

        if (ClientProxy.getOpenedGui() instanceof GuiCircuitDatabaseSharing) {
            ItemStack deletingStack = ((GuiCircuitDatabaseSharing) ClientProxy.getOpenedGui()).getCurrentDeletingTemplate();
            if (!deletingStack.isEmpty() && deletingStack == event.getItemStack()) {
                event.getToolTip().add(I18n.format("gui.circuitDatabase.info.sneakClickToConfirmDeleting"));
            } else {
                event.getToolTip().add(I18n.format("gui.circuitDatabase.info.sneakClickToDelete"));
            }
        }
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {

        Item item = event.crafting.getItem();
        if (item == Item.getItemFromBlock(Blocks.AIR))
            return;
    }

    @SubscribeEvent
    public void onBonemealEvent(BonemealEvent event) {

        if (!event.getWorld().isRemote) {
            if (event.getBlock() instanceof BlockGrass) {
                for (int x = event.getPos().getX() - 2; x < event.getPos().getX() + 3; x++) {
                    for (int z = event.getPos().getZ() - 2; z < event.getPos().getZ() + 3; z++) {
                        if (event.getWorld().isAirBlock(new BlockPos(x, event.getPos().getY() + 1, z))) {
                            if (event.getWorld().rand.nextInt(50) == 1) {
                                if (BPBlocks.indigo_flower.canBlockStay(event.getWorld(), event.getPos().add(0,1,0), event.getWorld().getBlockState(event.getPos().add(0,1,0)))) {
                                    event.getWorld().setBlockState(event.getPos().add(0,1,0), BPBlocks.indigo_flower.getDefaultState());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
