/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.BluePower;
import com.bluepowermod.block.BlockContainerFacingBase;
import com.bluepowermod.container.ContainerDeployer;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.IEjectAnimator;
import com.bluepowermod.tile.TileBase;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @author MineMaarten
 */
public class TileDeployer extends TileBase implements ISidedInventory, IEjectAnimator, INamedContainerProvider {

    private static final List<Item>  blacklistedItems    = new ArrayList<Item>();
    private static final GameProfile FAKE_PLAYER_PROFILE = new GameProfile(UUID.randomUUID(), "[BP Deployer]");
    public static final int SLOTS = 10;
    private final NonNullList<ItemStack> inventory       = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    static {
        blacklistedItems.add(Items.ENDER_PEARL);
    }

    public TileDeployer() {
        super(BPTileEntityType.DEPLOYER);
    }

    private boolean canDeployItem(ItemStack stack) {
    
        return !stack.isEmpty() && !blacklistedItems.contains(stack.getItem());
    }
    
    @Override
    protected void redstoneChanged(boolean newValue) {
    
        super.redstoneChanged(newValue);
        if (!world.isRemote && newValue) {
            sendUpdatePacket();
            
            FakePlayer player = FakePlayerFactory.get((ServerWorld) world, FAKE_PLAYER_PROFILE);
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.get(i);
                player.inventory.setInventorySlotContents(i, stack);
            }
            
            rightClick(player, 9);
            
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack.isEmpty() || stack.getCount() <= 0) {
                    inventory.set(i, ItemStack.EMPTY);
                } else {
                    inventory.set(i, stack);
                }
                player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
            }
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getCount() > 0) {
                    ItemStack remainder = IOHelper.insert(this, stack, getFacingDirection().getOpposite(), false);
                    if (!remainder.isEmpty()) {
                        world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, remainder));
                    }
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
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
        
        Direction faceDir = getFacingDirection();
        int dx = faceDir.getXOffset();
        int dy = faceDir.getYOffset();
        int dz = faceDir.getZOffset();
        int x = pos.getX() + dx;
        int y = pos.getY() + dy;
        int z = pos.getZ() + dz;
        
        player.setPosition(x + 0.5, y + 0.5 - player.getEyeHeight(), z + 0.5);
        player.rotationPitch = faceDir.getYOffset() * -90;
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
            PlayerInteractEvent event =  new PlayerInteractEvent.RightClickEmpty(player, Hand.MAIN_HAND);
            if (event.isCanceled()) return false;
            
            Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
            List<LivingEntity> detectedEntities = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1));
            
            Entity entity = detectedEntities.isEmpty() ? null : detectedEntities.get(world.rand.nextInt(detectedEntities.size()));
            
            if (entity != null) {
                for (int i = 0; i < useItems; i++) {
                    player.inventory.currentItem = i;
                    ItemStack stack = player.getHeldItemMainhand();
                    if (canDeployItem(stack) && stack.getItem().itemInteractionForEntity(stack, player, (LivingEntity) entity, Hand.MAIN_HAND)) return true;
                    if (entity instanceof AnimalEntity && ((AnimalEntity) entity).processInteract(player, Hand.MAIN_HAND)) return true;
                }
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                ItemStack stack = player.getHeldItemMainhand();
                if (canDeployItem(stack) && stack.getItem().onItemUseFirst(stack, new ItemUseContext(player, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(dx, dy, dz), faceDir, new BlockPos(x, y, z),false))) == ActionResultType.SUCCESS) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                if (!world.isAirBlock(new BlockPos(x, y, z)) && block.onBlockActivated(world.getBlockState(new BlockPos(x, y, z)), world, new BlockPos(x, y, z), player, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(dx, dy, dz), faceDir, new BlockPos(x, y, z),false))) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                ItemStack stack = player.getHeldItemMainhand();
                boolean isGoingToShift = false;              
                if(!stack.isEmpty()){
                	if(stack.getItem() == Items.SUGAR_CANE || stack.getItem() == Items.REDSTONE){
                		isGoingToShift = true;
                	}
                }
                int useX = isGoingToShift ? pos.getX() : x;
                int useY = isGoingToShift ? pos.getY() : y;
                int useZ = isGoingToShift ? pos.getZ() : z;
                if (canDeployItem(stack) && stack.getItem().onItemUse(new ItemUseContext(player, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(dx, dy, dz), faceDir, new BlockPos(x, y, z),false))) == ActionResultType.SUCCESS) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.inventory.currentItem = i;
                ItemStack stack = player.getHeldItemMainhand();
                if (canDeployItem(stack)) {
                    ItemStack copy = stack.copy();
                    //TODO Check this
                    player.setHeldItem(Hand.MAIN_HAND, stack.getItem().onItemRightClick(world, player, Hand.MAIN_HAND).getResult());
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
    public void read(CompoundNBT tCompound) {
    
        super.read(tCompound);
        
        for (int i = 0; i < 9; i++) {
            CompoundNBT tc = tCompound.getCompound("inventory" + i);
            inventory.set(i, new ItemStack((IItemProvider) tc));
        }
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public CompoundNBT write(CompoundNBT tCompound) {
    
        super.write(tCompound);
        
        for (int i = 0; i < 9; i++) {
                CompoundNBT tc = new CompoundNBT();
                inventory.get(i).write(tc);
                tCompound.put("inventory" + i, tc);
        }
        return tCompound;
    }

    @Override
    public int getSizeInventory() {
    
        return inventory.size();
    }
    
    @Override
    public ItemStack getStackInSlot(int i) {
    
        return inventory.get(i);
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int amount) {
    
        // this needs to be side aware as well
        ItemStack itemStack = getStackInSlot(slot);
        if (!itemStack.isEmpty()) {
            if (itemStack.getCount() <= amount) {
                setInventorySlotContents(slot, ItemStack.EMPTY);
            } else {
                itemStack = itemStack.split(amount);
                if (itemStack.getCount() == 0) {
                    setInventorySlotContents(slot,ItemStack.EMPTY);
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
    
        inventory.set(i, itemStack);
    }
    
    @Override
    public int getInventoryStackLimit() {
    
        return 64;
    }
    
    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return player.getPosition().withinDistance(pos, 64.0D);
    }

    @Override
    public void openInventory(PlayerEntity player) {

    }

    @Override
    public void closeInventory(PlayerEntity player) {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
    
        return true;
    }
    
    @Override
    public NonNullList<ItemStack> getDrops() {
    
        NonNullList<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (!stack.isEmpty()) drops.add(stack);
        return drops;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        Direction direction = getFacingDirection();

        if (side == direction) { return new int[] {}; }
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        for (int i : getSlotsForFace(direction)) {
            if (index == i) { return true; }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        for (int i : getSlotsForFace(direction)) {
            if (index == i) { return true; }
        }
        return false;
    }
    
    @Override
    public boolean isEjecting() {
    
        return (world.getBlockState(pos)).get(BlockContainerFacingBase.ACTIVE);
    }
 
    @Override
    public boolean canConnectRedstone() {
    	
    	return true;
    }

    //Todo Fields
    @Override
    public boolean isEmpty() {
        return inventory.size() == 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Refs.BLOCKDEPLOYER_NAME);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity playerEntity) {
        return new ContainerDeployer(id, inventory, this);
    }
}
