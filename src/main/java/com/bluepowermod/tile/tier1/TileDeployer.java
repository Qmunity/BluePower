/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.BluePower;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.IEjectAnimator;
import com.bluepowermod.tile.TileBase;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

;

/**
 * @author MineMaarten
 */
public class TileDeployer extends TileBase implements ISidedInventory, IEjectAnimator {
    
    private int                      animationTimer      = -1;
    private final ItemStack[]        inventory           = new ItemStack[9];
    private static final List<Item>  blacklistedItems    = new ArrayList<Item>();
    private static final GameProfile FAKE_PLAYER_PROFILE = new GameProfile(UUID.randomUUID(), "[BP Deployer]");
    
    static {
        blacklistedItems.add(Items.ENDER_PEARL);
    }
    
    @Override
    public void update() {
    
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
        if (!world.isRemote && newValue) {
            animationTimer = 0;
            sendUpdatePacket();
            
            FakePlayer player = FakePlayerFactory.get((WorldServer) world, FAKE_PLAYER_PROFILE);
            for (int i = 0; i < inventory.length; i++) {
                ItemStack stack = inventory[i];
                player.inventory.setInventorySlotContents(i, stack);
            }
            
            rightClick(player, 9);
            
            for (int i = 0; i < inventory.length; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack == null || stack.getCount() <= 0) {
                    inventory[i] = null;
                } else {
                    inventory[i] = stack;
                }
                player.inventory.setInventorySlotContents(i, null);
            }
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack != null && stack.getCount() > 0) {
                    ItemStack remainder = IOHelper.insert(this, stack, getFacingDirection().getOpposite(), false);
                    if (remainder != null) {
                        world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, remainder));
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
        
        EnumFacing faceDir = getFacingDirection();
        int dx = faceDir.getFrontOffsetX();
        int dy = faceDir.getFrontOffsetY();
        int dz = faceDir.getFrontOffsetZ();
        int x = pos.getX() + dx;
        int y = pos.getY() + dy;
        int z = pos.getZ() + dz;
        
        player.setPosition(x + 0.5, y + 0.5 - player.eyeHeight, z + 0.5);
        player.rotationPitch = faceDir.getFrontOffsetY() * -90;
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
            PlayerInteractEvent event =  new PlayerInteractEvent.RightClickEmpty(player, EnumHand.MAIN_HAND);
            if (event.isCanceled()) return false;
            
            Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
            List<EntityLivingBase> detectedEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1));
            
            Entity entity = detectedEntities.isEmpty() ? null : detectedEntities.get(world.rand.nextInt(detectedEntities.size()));
            
            if (entity != null) {
                for (int i = 0; i < useItems; i++) {
                    player.inventory.currentItem = i;
                    ItemStack stack = player.getHeldItemMainhand();
                    if (canDeployItem(stack) && stack.getItem().itemInteractionForEntity(stack, player, (EntityLivingBase) entity, EnumHand.MAIN_HAND)) return true;
                    if (entity instanceof EntityAnimal && ((EntityAnimal) entity).processInteract(player, EnumHand.MAIN_HAND)) return true;
                }
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                ItemStack stack = player.getHeldItemMainhand();
                if (canDeployItem(stack) && stack.getItem().onItemUseFirst(player, world, new BlockPos(x, y, z), faceDir, dx, dy, dz, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                if (!world.isAirBlock(new BlockPos(x, y, z)) && block.onBlockActivated(world, new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)), player, EnumHand.MAIN_HAND, faceDir, dx, dy, dz)) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                ItemStack stack = player.getHeldItemMainhand();
                boolean isGoingToShift = false;              
                if(stack != null){
                	if(stack.getItem() == Items.REEDS || stack.getItem() instanceof ItemRedstone){
                		isGoingToShift = true;
                	}
                }
                int useX = isGoingToShift ? pos.getX() : x;
                int useY = isGoingToShift ? pos.getY() : y;
                int useZ = isGoingToShift ? pos.getZ() : z;
                if (canDeployItem(stack) && stack.getItem().onItemUse(player, world, new BlockPos(useX, useY, useZ), EnumHand.MAIN_HAND, faceDir, dx, dy, dz) == EnumActionResult.SUCCESS) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                ItemStack stack = player.getHeldItemMainhand();
                if (canDeployItem(stack)) {
                    ItemStack copy = stack.copy();
                    //TODO Check this
                    player.setHeldItem(EnumHand.MAIN_HAND, stack.getItem().onItemRightClick(world, player, EnumHand.MAIN_HAND).getResult());
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
            inventory[i] = new ItemStack(tc);
        }
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tCompound) {
    
        super.writeToNBT(tCompound);
        
        for (int i = 0; i < 9; i++) {
            if (inventory[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                inventory[i].writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
            }
        }
        return tCompound;
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
            if (itemStack.getCount() <= amount) {
                setInventorySlotContents(slot, null);
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.getCount() == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        
        return itemStack;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        return getStackInSlot(i);
    }

    
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
    
        inventory[i] = itemStack;
    }
    
    @Override
    public String getName() {
    
        return BPBlocks.deployer.getUnlocalizedName();
    }
    
    @Override
    public boolean hasCustomName() {
    
        return true;
    }
    
    @Override
    public int getInventoryStackLimit() {
    
        return 64;
    }
    
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
    
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

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
    public int[] getSlotsForFace(EnumFacing side) {
        EnumFacing direction = getFacingDirection();

        if (side == direction) { return new int[] {}; }
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        for (int i : getSlotsForFace(direction)) {
            if (index == i) { return true; }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        for (int i : getSlotsForFace(direction)) {
            if (index == i) { return true; }
        }
        return false;
    }
    
    @Override
    public boolean isEjecting() {
    
        return animationTimer >= 0;
    }
 
    @Override
    public boolean canConnectRedstone() {
    	
    	return true;
    }

    //Todo Fields
    @Override
    public boolean isEmpty() {
        return inventory.length == 0;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

}
