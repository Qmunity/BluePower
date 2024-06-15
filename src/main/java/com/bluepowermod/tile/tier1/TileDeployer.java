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
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.IEjectAnimator;
import com.bluepowermod.tile.TileBase;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import net.minecraft.core.NonNullList;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * @author MineMaarten
 */
public class TileDeployer extends TileBase implements WorldlyContainer, IEjectAnimator, MenuProvider {

    private static final List<Item>  blacklistedItems    = new ArrayList<Item>();
    private static final GameProfile FAKE_PLAYER_PROFILE = new GameProfile(UUID.randomUUID(), "[BP Deployer]");
    public static final int SLOTS = 10;
    private final NonNullList<ItemStack> inventory       = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    static {
        blacklistedItems.add(Items.ENDER_PEARL);
    }

    public TileDeployer(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.DEPLOYER.get(), pos, state);
    }

    private boolean canDeployItem(ItemStack stack) {
    
        return !stack.isEmpty() && !blacklistedItems.contains(stack.getItem());
    }
    
    @Override
    protected void redstoneChanged(boolean newValue) {
    
        super.redstoneChanged(newValue);
        if (!level.isClientSide && newValue) {
            sendUpdatePacket();
            
            FakePlayer player = FakePlayerFactory.get((ServerLevel) level, FAKE_PLAYER_PROFILE);
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.get(i);
                player.getInventory().setItem(i, stack);
            }
            
            rightClick(player, 9);
            
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.isEmpty() || stack.getCount() <= 0) {
                    inventory.set(i, ItemStack.EMPTY);
                } else {
                    inventory.set(i, stack);
                }
                player.getInventory().setItem(i, ItemStack.EMPTY);
            }
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!stack.isEmpty() && stack.getCount() > 0) {
                    ItemStack remainder = IOHelper.insert(this, stack, getFacingDirection().getOpposite(), false);
                    if (!remainder.isEmpty()) {
                        level.addFreshEntity(new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, remainder));
                    }
                    player.getInventory().setItem(i, ItemStack.EMPTY);
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
        int dx = faceDir.getStepX();
        int dy = faceDir.getStepY();
        int dz = faceDir.getStepZ();
        int x = worldPosition.getX() + dx;
        int y = worldPosition.getY() + dy;
        int z = worldPosition.getZ() + dz;
        
        player.setPos(x + 0.5, y + 0.5 - player.getEyeHeight(), z + 0.5);
        player.setXRot(faceDir.getStepY() * -90);
        switch (faceDir) {
            case NORTH:
                player.setYRot(180);
                break;
            case SOUTH:
                player.setYRot(0);
                break;
            case WEST:
                player.setYRot(90);
                break;
            case EAST:
                player.setYRot(-90);
        }
        
        try {
            PlayerInteractEvent event =  new PlayerInteractEvent.RightClickEmpty(player, InteractionHand.MAIN_HAND);
            if (((ICancellableEvent)event).isCanceled()) return false;
            
            Block block = level.getBlockState(new BlockPos(x, y, z)).getBlock();
            List<LivingEntity> detectedEntities = level.getEntitiesOfClass(LivingEntity.class, new AABB(x, y, z, x + 1, y + 1, z + 1));
            
            Entity entity = detectedEntities.isEmpty() ? null : detectedEntities.get(level.random.nextInt(detectedEntities.size()));
            
            if (entity != null) {
                for (int i = 0; i < useItems; i++) {
                    player.getInventory().selected = i;
                    ItemStack stack = player.getMainHandItem();
                    if (canDeployItem(stack) && stack.getItem().interactLivingEntity(stack, player, (LivingEntity) entity, InteractionHand.MAIN_HAND).shouldSwing()) return true;
                    if (entity instanceof Animal && ((Animal) entity).mobInteract(player, InteractionHand.MAIN_HAND).shouldSwing()) return true;
                }
            }
            
            for (int i = 0; i < useItems; i++) {
                player.getInventory().selected = i;
                ItemStack stack = player.getMainHandItem();
                if (canDeployItem(stack) && stack.getItem().onItemUseFirst(stack, new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(dx, dy, dz), faceDir, new BlockPos(x, y, z),false))) == InteractionResult.SUCCESS) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.getInventory().selected = i;
                if (!level.isEmptyBlock(new BlockPos(x, y, z)) && level.getBlockState(new BlockPos(x, y, z)).useItemOn(player.getMainHandItem() ,level, player, InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(dx, dy, dz), faceDir, new BlockPos(x, y, z),false)) == ItemInteractionResult.SUCCESS) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.getInventory().selected = i;
                ItemStack stack = player.getMainHandItem();
                boolean isGoingToShift = false;              
                if(!stack.isEmpty()){
                	if(stack.getItem() == Items.SUGAR_CANE || stack.getItem() == Items.REDSTONE){
                		isGoingToShift = true;
                	}
                }
                int useX = isGoingToShift ? worldPosition.getX() : x;
                int useY = isGoingToShift ? worldPosition.getY() : y;
                int useZ = isGoingToShift ? worldPosition.getZ() : z;
                if (canDeployItem(stack) && stack.getItem().useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(dx, dy, dz), faceDir, new BlockPos(x, y, z),false))) == InteractionResult.SUCCESS) return true;
            }
            
            for (int i = 0; i < useItems; i++) {
                player.getInventory().selected = i;
                ItemStack stack = player.getMainHandItem();
                if (canDeployItem(stack)) {
                    ItemStack copy = stack.copy();
                    //TODO Check this
                    player.setItemInHand(InteractionHand.MAIN_HAND, stack.getItem().use(level, player, InteractionHand.MAIN_HAND).getObject());
                    if (!ItemStack.isSameItem(copy, stack)) return true;
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
    public void loadAdditional(CompoundTag tCompound, HolderLookup.Provider provider) {
    
        super.loadAdditional(tCompound, provider);
        
        for (int i = 0; i < 9; i++) {
            CompoundTag tc = tCompound.getCompound("inventory" + i);
            inventory.set(i, ItemStack.parseOptional(provider, tc));
        }
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    protected void saveAdditional(CompoundTag tCompound, HolderLookup.Provider provider) {
    
        super.saveAdditional(tCompound, provider);
        
        for (int i = 0; i < 9; i++) {
                CompoundTag tc = new CompoundTag();
                inventory.get(i).save(provider, tc);
                tCompound.put("inventory" + i, tc);
        }
    }

    @Override
    public int getContainerSize() {
    
        return inventory.size();
    }
    
    @Override
    public ItemStack getItem(int i) {
    
        return inventory.get(i);
    }
    
    @Override
    public ItemStack removeItem(int slot, int amount) {
    
        // this needs to be side aware as well
        ItemStack itemStack = getItem(slot);
        if (!itemStack.isEmpty()) {
            if (itemStack.getCount() <= amount) {
                setItem(slot, ItemStack.EMPTY);
            } else {
                itemStack = itemStack.split(amount);
                if (itemStack.getCount() == 0) {
                    setItem(slot,ItemStack.EMPTY);
                }
            }
        }
        
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return getItem(i);
    }

    
    @Override
    public void setItem(int i, ItemStack itemStack) {
    
        inventory.set(i, itemStack);
    }
    
    @Override
    public int getMaxStackSize() {
    
        return 64;
    }
    
    @Override
    public boolean stillValid(Player player) {
        return player.blockPosition().closerThan(worldPosition, 64.0D);
    }

    @Override
    public void startOpen(Player player) {

    }

    @Override
    public void stopOpen(Player player) {

    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
    
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
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        for (int i : getSlotsForFace(direction)) {
            if (index == i) { return true; }
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        for (int i : getSlotsForFace(direction)) {
            if (index == i) { return true; }
        }
        return false;
    }
    
    @Override
    public boolean isEjecting() {
    
        return (getBlockState()).getValue(BlockContainerFacingBase.ACTIVE);
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
    public void clearContent() {

    }

    @Override
    public Component getDisplayName() {
        return Component.literal(Refs.BLOCKDEPLOYER_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player playerEntity) {
        return new ContainerDeployer(id, inventory, this);
    }
}
