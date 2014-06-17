package net.quetzi.bluepower.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.quetzi.bluepower.containers.ContainerSeedBag;
import net.quetzi.bluepower.containers.inventorys.InventoryItem;
import net.quetzi.bluepower.items.ItemSeedBag;
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
                        InventoryItem inventory = (InventoryItem) InventoryItem.getItemInventory(is, "Seed Bag", 9);
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
}
