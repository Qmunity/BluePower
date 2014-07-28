package com.bluepowermod.events;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import com.bluepowermod.containers.ContainerSeedBag;
import com.bluepowermod.containers.inventorys.InventoryItem;
import com.bluepowermod.init.BPEnchantments;
import com.bluepowermod.items.ItemSeedBag;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BPEventHandler {
  
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
    
    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
    	if(event.source instanceof EntityDamageSource) {
    		EntityDamageSource entitySource = (EntityDamageSource) event.source;
    		
    		if(entitySource.getEntity() instanceof EntityPlayer) {
    			EntityPlayer killer = (EntityPlayer) entitySource.getEntity();
    			
    			if(killer.inventory.getCurrentItem() != null) {	
    				if(EnchantmentHelper.getEnchantments(killer.inventory.getCurrentItem()).containsKey(BPEnchantments.vorpal.effectId)) {
    					int level = EnchantmentHelper.getEnchantmentLevel(BPEnchantments.vorpal.effectId, killer.inventory.getCurrentItem());
    					
    					if(level == 1) {
    						if(killer.worldObj.rand.nextInt(6) == 1) {
    							this.dropHeads(event);
    						}
    					} else if(level == 2) {
    						if(killer.worldObj.rand.nextInt(3) == 1) {
	    						this.dropHeads(event);
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
    private void dropHeads(LivingDeathEvent event) {
    	if(event.entityLiving instanceof EntityCreeper) {
			event.entityLiving.entityDropItem(new ItemStack(Items.skull, 1, 4), 0.0F);
			return;
		}
		
		if(event.entityLiving instanceof EntityPlayer) {
			ItemStack drop = new ItemStack(Items.skull, 1, 3);
			drop.stackTagCompound = new NBTTagCompound();
			drop.stackTagCompound.setString("SkullOwner", ((EntityPlayer) event.entityLiving).getDisplayName());
			event.entityLiving.entityDropItem(drop, 0.0F);
			return;
		}
		
		if(event.entityLiving instanceof EntitySkeleton) {
			EntitySkeleton sk = (EntitySkeleton) event.entityLiving;
			
			if(sk.getSkeletonType() == 0) {
				event.entityLiving.entityDropItem(new ItemStack(Items.skull, 1, 0), 0.0F);
				return;
			} else {
				event.entityLiving.entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
				return;
			}
		}
		
		if(event.entityLiving instanceof EntityZombie) {
			event.entityLiving.entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0F);
			return;
		}
    }
}
