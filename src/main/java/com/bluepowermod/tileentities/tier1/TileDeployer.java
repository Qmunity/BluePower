package com.bluepowermod.tileentities.tier1;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tileentities.IEjectAnimator;
import com.bluepowermod.tileentities.TileBase;

/**
 * @author MineMaarten
 */
public class TileDeployer extends TileBase implements ISidedInventory, IEjectAnimator {
    
    private int                     animationTimer   = -1;
    private final ItemStack[]       inventory        = new ItemStack[9];
    private static final List<Item> blacklistedItems = new ArrayList<Item>();
    
    static {
        blacklistedItems.add(Items.ender_pearl);
    }
    
    @Override
    public void updateEntity() {
    
        if (animationTimer >= 0) {
            if (++animationTimer > 10) {
                animationTimer = -1;
            }
        }
    }
    
    private boolean canDeployItem(ItemStack stack) {
    
        return stack != null && !blacklistedItems.contains(stack.getItem());
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
                player.inventory.setInventorySlotContents(i, stack);
            }
            
            rightClick(player, 9);
            
            for (int i = 0; i < inventory.length; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack == null || stack.stackSize <= 0) {
                    inventory[i] = null;
                } else {
                    inventory[i] = stack;
                }
                player.inventory.setInventorySlotContents(i, null);
            }
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack != null && stack.stackSize > 0) {
                    ItemStack remainder = IOHelper.insert(this, stack, getFacingDirection().getOpposite(), false);
                    if (remainder != null) {
                        worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, remainder));
                    }
                    player.inventory.setInventorySlotContents(i, null);
                }
            }
        }
    }
    
    /**
     * Be sure to set up the fake player's hotbar with the right clicked items. starting with hotbar slot 0.
     * @param player
     * @param useItems this method will set the current selected slot of the fake player to 0, and move on to the next slot useItems - 1 times.
     *          So to use the first slot only, pass 1, to use the full hotbar, 9.
     * @return
     */
    protected boolean rightClick(FakePlayer player, int useItems) {
    
        if (useItems > 9) throw new IllegalArgumentException("Hotbar is 9 items in width! You're trying " + useItems + "!");
        
        ForgeDirection faceDir = getFacingDirection();
        int dx = faceDir.offsetX;
        int dy = faceDir.offsetY;
        int dz = faceDir.offsetZ;
        int x = xCoord + dx;
        int y = yCoord + dy;
        int z = zCoord + dz;
        
        player.setPosition(x, y, z);
        player.rotationPitch = faceDir.offsetY * -90;
        switch (faceDir) {
            case NORTH:
                player.rotationYaw = 180;
                break;
            case SOUTH:
                player.rotationYaw = 0;
                break;
            case WEST:
                player.rotationYaw = 90;
                break;
            case EAST:
                player.rotationYaw = -90;
        }
        
        try {
            PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(player, Action.RIGHT_CLICK_AIR, x, y, z, faceDir.ordinal(), worldObj);
            if (event.isCanceled()) return false;
            
            Block block = worldObj.getBlock(x, y, z);
            List<EntityLivingBase> detectedEntities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1));
            
            Entity entity = detectedEntities.isEmpty() ? null : detectedEntities.get(worldObj.rand.nextInt(detectedEntities.size()));
            
            if (entity != null) {
                for (int i = 0; i < useItems; i++) {
                    player.inventory.currentItem = i;
                    ItemStack stack = player.getCurrentEquippedItem();
                    if (canDeployItem(stack) && stack.getItem().itemInteractionForEntity(stack, player, (EntityLivingBase) entity)) return true;
                    if (entity instanceof EntityAnimal && ((EntityAnimal) entity).interact(player)) return true;
                }
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                ItemStack stack = player.getCurrentEquippedItem();
                if (canDeployItem(stack) && stack.getItem().onItemUseFirst(stack, player, worldObj, xCoord, yCoord, zCoord, faceDir.ordinal(), dx, dy, dz)) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                if (!worldObj.isAirBlock(x, y, x) && block.onBlockActivated(worldObj, x, y, z, player, faceDir.ordinal(), dx, dy, dz)) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                ItemStack stack = player.getCurrentEquippedItem();
                if (canDeployItem(stack) && stack.getItem().onItemUse(stack, player, worldObj, xCoord, yCoord, zCoord, faceDir.ordinal(), dx, dy, dz)) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                ItemStack stack = player.getCurrentEquippedItem();
                if (canDeployItem(stack)) {
                    ItemStack copy = stack.copy();
                    stack.getItem().onItemRightClick(stack, worldObj, player);
                    if (!copy.isItemEqual(stack)) return true;
                }
            }
            return false;
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
    
        return BPBlocks.deployer.getUnlocalizedName();
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
