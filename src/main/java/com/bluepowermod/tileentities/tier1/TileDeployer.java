package com.bluepowermod.tileentities.tier1;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import com.bluepowermod.BluePower;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.tileentities.IEjectAnimator;
import com.bluepowermod.tileentities.TileBase;

public class TileDeployer extends TileBase implements ISidedInventory, IEjectAnimator {
    
    private int               animationTimer = -1;
    private final ItemStack[] inventory      = new ItemStack[9];
    
    @Override
    public void updateEntity() {
    
        if (animationTimer >= 0) {
            if (++animationTimer > 10) {
                animationTimer = -1;
            }
        }
    }
    
    @Override
    protected void redstoneChanged(boolean newValue) {
    
        super.redstoneChanged(newValue);
        if (!worldObj.isRemote && newValue) {
            animationTimer = 0;
            sendUpdatePacket();
            
            FakePlayer player = FakePlayerFactory.getMinecraft((WorldServer) worldObj);
            for (int i = 0; i < inventory.length; i++) {
                ItemStack stack = inventory[i];
                if (stack != null) {
                    player.setCurrentItemOrArmor(0, stack);
                    if (rightClick(player, stack)) {
                        stack = player.getCurrentEquippedItem();
                        if (stack == null || stack.stackSize <= 0) {
                            inventory[i] = null;
                        } else {
                            inventory[i] = stack;
                        }
                        player.setCurrentItemOrArmor(0, null);
                        markDirty();
                        break;
                    }
                }
            }
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack != null && stack.stackSize > 0) {
                    ItemStack remainder = IOHelper.insert(this, stack, getFacingDirection().getOpposite(), false);
                    if (remainder != null) {
                        worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5, yCoord + 3, zCoord + 0.5, remainder));
                    }
                    player.inventory.setInventorySlotContents(i, null);
                }
            }
        }
    }
    
    private boolean rightClick(FakePlayer player, ItemStack stack) {
    
        ForgeDirection faceDir = getFacingDirection();
        int dx = faceDir.offsetX;
        int dy = faceDir.offsetY;
        int dz = faceDir.offsetZ;
        int x = xCoord + dx;
        int y = yCoord + dy;
        int z = zCoord + dz;
        
        try {
            player.setPosition(x, y, z);
            
            PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(player, Action.RIGHT_CLICK_AIR, x, y, z, faceDir.ordinal(), worldObj);
            if (event.isCanceled()) return false;
            
            Block block = worldObj.getBlock(x, y, z);
            List<EntityLivingBase> detectedEntities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1));
            Item item = stack.getItem();
            
            Entity entity = detectedEntities.isEmpty() ? null : detectedEntities.get(worldObj.rand.nextInt(detectedEntities.size()));
            
            if (entity != null && (item.itemInteractionForEntity(stack, player, (EntityLivingBase) entity) || !(entity instanceof EntityAnimal) || ((EntityAnimal) entity).interact(player))) { return true; }
            
            if (item.onItemUseFirst(stack, player, worldObj, x, y, z, faceDir.ordinal(), dx, dy, dz)) return true;
            if (!worldObj.isAirBlock(x, y, x) && block.onBlockActivated(worldObj, x, y, z, player, faceDir.ordinal(), dx, dy, dz)) return true;
            if (item.onItemUse(stack, player, worldObj, x, y, z, faceDir.ordinal(), dx, dy, dz)) return true;
            ItemStack copy = stack.copy();
            item.onItemRightClick(stack, worldObj, player);
            return !stack.equals(copy);
        } catch (Throwable e) {
            BluePower.log.error("Deployer crashed! Stacktrace: ");
            e.printStackTrace();
            return true;
        }
    }
    
    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound) {
    
        super.readFromNBT(tCompound);
        
        for (int i = 0; i < 9; i++) {
            NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
            inventory[i] = ItemStack.loadItemStackFromNBT(tc);
        }
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public void writeToNBT(NBTTagCompound tCompound) {
    
        super.writeToNBT(tCompound);
        
        for (int i = 0; i < 9; i++) {
            if (inventory[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                inventory[i].writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
            }
        }
    }
    
    @Override
    public void writeToPacketNBT(NBTTagCompound tag) {
    
        super.writeToPacketNBT(tag);
        tag.setBoolean("animation", animationTimer >= 0);
    }
    
    @Override
    public void readFromPacketNBT(NBTTagCompound tag) {
    
        super.readFromPacketNBT(tag);
        if (tag.getBoolean("animation")) {
            animationTimer = 0;
        }
    }
    
    @Override
    public int getSizeInventory() {
    
        return inventory.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int i) {
    
        return inventory[i];
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int amount) {
    
        // this needs to be side aware as well
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null) {
            if (itemStack.stackSize <= amount) {
                setInventorySlotContents(slot, null);
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        
        return itemStack;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
    
        return getStackInSlot(i);
    }
    
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
    
        inventory[i] = itemStack;
    }
    
    @Override
    public String getInventoryName() {
    
        return "tile.buffer.name";
    }
    
    @Override
    public boolean hasCustomInventoryName() {
    
        return true;
    }
    
    @Override
    public int getInventoryStackLimit() {
    
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
    
        return true;
    }
    
    @Override
    public void openInventory() {
    
    }
    
    @Override
    public void closeInventory() {
    
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
    
        return true;
    }
    
    @Override
    public List<ItemStack> getDrops() {
    
        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (stack != null) drops.add(stack);
        return drops;
    }
    
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
    
        ForgeDirection direction = getFacingDirection();
        
        if (var1 == direction.ordinal()) { return new int[] {}; }
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    }
    
    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
    
        for (int i : getAccessibleSlotsFromSide(side)) {
            if (slot == i) { return true; }
        }
        return false;
    }
    
    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
    
        for (int i : getAccessibleSlotsFromSide(side)) {
            if (slot == i) { return true; }
        }
        return false;
    }
    
    @Override
    public boolean isEjecting() {
    
        return animationTimer >= 0;
    }
    
}
