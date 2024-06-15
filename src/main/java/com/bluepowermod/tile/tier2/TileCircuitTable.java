/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier2;

import com.bluepowermod.client.gui.IGuiButtonSensitive;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.IGUITextFieldSensitive;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author MineMaarten
 */
public class TileCircuitTable extends TileBase implements Container, IGUITextFieldSensitive, IGuiButtonSensitive {

    public static final int SLOTS = 24;
    protected NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    public int slotsScrolled;
    private String textboxString = "";

    public TileCircuitTable(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.CIRCUIT_TABLE.get(), pos, state);
    }

    @Override
    public void setText(int textFieldID, String text) {

        textboxString = text;
    }

    @Override
    public String getText(int textFieldID) {

        return textboxString;
    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (!stack.isEmpty())
                drops.add(stack);
        return drops;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);

        ListTag tagList = new ListTag();
        for (int currentIndex = 0; currentIndex < inventory.size(); ++currentIndex) {
                CompoundTag tagCompound = new CompoundTag();
                tagCompound.putByte("Slot", (byte) currentIndex);
                inventory.get(currentIndex).save(provider, tagCompound);
                tagList.add(tagCompound);
        }
        tag.put("Items", tagList);

        tag.putString("textboxString", textboxString);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);

        ListTag tagList = tag.getList("Items", 10);
        inventory = NonNullList.withSize(24, ItemStack.EMPTY);
        for (int i = 0; i < tagList.size(); ++i) {
            CompoundTag tagCompound = tagList.getCompound(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.size()) {
                inventory.set(slot, ItemStack.parseOptional(provider, tagCompound));
            }
        }

        textboxString = tag.getString("textboxString");
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

        ItemStack itemStack = getItem(slot);
        if (!itemStack.isEmpty()) {
            if (itemStack.getCount() <= amount) {
                setItem(slot, ItemStack.EMPTY);
            } else {
                itemStack = itemStack.split(amount);
                if (itemStack.getCount() == 0) {
                    setItem(slot, ItemStack.EMPTY);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack itemStack = getItem(i);
        if (!itemStack.isEmpty()) {
            setItem(i, ItemStack.EMPTY);
        }
        return itemStack;
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
        return player.blockPosition().closerThan(worldPosition,64.0D);
    }


    @Override
    public void startOpen(Player player) {

    }

    @Override
    public void stopOpen(Player player) {

    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void clearContent() {

    }

    @Override
    public boolean canPlaceItem(int p_94041_1_, ItemStack p_94041_2_) {

        return true;
    }

    @Override
    public void onButtonPress(Player player, int messageId, int value) {
        if (messageId == 0) {
            slotsScrolled = value;
        }
        //updateGateInventory();
        //((ICrafting) player).sendContainerAndContentsToPlayer(player.openContainer, player.openContainer.getInventory());
    }
}
