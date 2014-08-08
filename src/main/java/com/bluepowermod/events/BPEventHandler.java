package com.bluepowermod.events;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.bluepowermod.containers.ContainerSeedBag;
import com.bluepowermod.containers.inventorys.InventoryItem;
import com.bluepowermod.init.BPEnchantments;
import com.bluepowermod.items.ItemSeedBag;
import com.bluepowermod.items.ItemSickle;
import com.bluepowermod.util.Dependencies;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class BPEventHandler {
    
    private boolean warned = false;
    
    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
    
        if (event.phase == TickEvent.Phase.END && event.player.worldObj.isRemote && !warned && !Loader.isModLoaded(Dependencies.FMP)) {
            event.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "WARNING: You're running BluePower without having " + EnumChatFormatting.RED + "ForgeMultipart installed. Even though it will work, " + EnumChatFormatting.RED + "ForgeMultipart is "
                    + EnumChatFormatting.BOLD + "highly" + EnumChatFormatting.RESET + EnumChatFormatting.RED + " recommended. Continue at own risk, " + EnumChatFormatting.RED + "expect bugs."));
            warned = true;
        }
    }
    
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
    
        if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && event.entityPlayer.capabilities.isCreativeMode) {
            ItemStack heldItem = event.entityPlayer.getCurrentEquippedItem();
            if (heldItem != null && heldItem.getItem() instanceof ItemSickle) {
                heldItem.getItem().onBlockDestroyed(heldItem, event.world, event.world.getBlock(event.x, event.y, event.z), event.x, event.y, event.z, event.entityPlayer);
            }
        }
    }
    
    @SubscribeEvent
    public void itemPickUp(EntityItemPickupEvent event) {
    
        EntityPlayer player = event.entityPlayer;
        ItemStack pickUp = event.item.getEntityItem();
        if (!(player.openContainer instanceof ContainerSeedBag)) {
            for (ItemStack is : player.inventory.mainInventory) {
                if (is != null && is.getItem() instanceof ItemSeedBag) {
                    ItemStack seedType = ItemSeedBag.getSeedType(is);
                    if (seedType != null && seedType.isItemEqual(pickUp)) {
                        InventoryItem inventory = InventoryItem.getItemInventory(is, "Seed Bag", 9);
                        inventory.openInventory();
                        ItemStack pickedUp = TileEntityHopper.func_145889_a(inventory, pickUp, -1);
                        inventory.closeInventory(is);
                        
                        if (pickedUp == null) {
                            event.setResult(Result.ALLOW);
                            event.item.setDead();
                            return;
                        } else {
                            event.item.setEntityItemStack(pickedUp);
                        }
                    }
                }
            }
        }
    }
    
    private boolean isAttacking = false;
    
    @SubscribeEvent
    public void onEntityAttack(LivingAttackEvent event) {
    
        if (!isAttacking && event.source instanceof EntityDamageSource) {// this event will be trigger recursively by EntityLiving#attackEntityFrom,
                                                                         // so we need to stop the loop.
            EntityDamageSource entitySource = (EntityDamageSource) event.source;
            
            if (entitySource.getEntity() instanceof EntityPlayer) {
                EntityPlayer killer = (EntityPlayer) entitySource.getEntity();
                
                if (killer.inventory.getCurrentItem() != null) {
                    if (EnchantmentHelper.getEnchantments(killer.inventory.getCurrentItem()).containsKey(BPEnchantments.disjunction.effectId)) {
                        if (event.entityLiving instanceof EntityEnderman || event.entityLiving instanceof EntityDragon) {
                            int level = EnchantmentHelper.getEnchantmentLevel(BPEnchantments.disjunction.effectId, killer.inventory.getCurrentItem());
                            isAttacking = true;
                            event.entityLiving.attackEntityFrom(event.source, event.ammount * (level * 0.5F + 1));
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
    
        if (event.source instanceof EntityDamageSource) {
            EntityDamageSource entitySource = (EntityDamageSource) event.source;
            
            if (entitySource.getEntity() instanceof EntityPlayer) {
                EntityPlayer killer = (EntityPlayer) entitySource.getEntity();
                
                if (killer.inventory.getCurrentItem() != null) {
                    if (EnchantmentHelper.getEnchantments(killer.inventory.getCurrentItem()).containsKey(BPEnchantments.vorpal.effectId)) {
                        int level = EnchantmentHelper.getEnchantmentLevel(BPEnchantments.vorpal.effectId, killer.inventory.getCurrentItem());
                        
                        if (level == 1) {
                            if (killer.worldObj.rand.nextInt(6) == 1) {
                                dropHeads(event);
                            }
                        } else if (level == 2) {
                            if (killer.worldObj.rand.nextInt(3) == 1) {
                                dropHeads(event);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void dropHeads(LivingDeathEvent event) {
    
        if (event.entityLiving instanceof EntityCreeper) {
            event.entityLiving.entityDropItem(new ItemStack(Items.skull, 1, 4), 0.0F);
            return;
        }
        
        if (event.entityLiving instanceof EntityPlayer) {
            ItemStack drop = new ItemStack(Items.skull, 1, 3);
            drop.stackTagCompound = new NBTTagCompound();
            drop.stackTagCompound.setString("SkullOwner", ((EntityPlayer) event.entityLiving).getDisplayName());
            event.entityLiving.entityDropItem(drop, 0.0F);
            return;
        }
        
        if (event.entityLiving instanceof EntitySkeleton) {
            EntitySkeleton sk = (EntitySkeleton) event.entityLiving;
            
            if (sk.getSkeletonType() == 0) {
                event.entityLiving.entityDropItem(new ItemStack(Items.skull, 1, 0), 0.0F);
                return;
            } else {
                event.entityLiving.entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
                return;
            }
        }
        
        if (event.entityLiving instanceof EntityZombie) {
            event.entityLiving.entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0F);
            return;
        }
    }
}
