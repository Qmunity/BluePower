/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier3;

import com.bluepowermod.api.item.IDatabaseSaveable;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.ItemStackDatabase;
import com.bluepowermod.init.BPConfig;
import com.bluepowermod.tile.tier2.TileCircuitTable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class TileCircuitDatabase extends TileCircuitTable {

    public IInventory copyInventory = new Inventory(2) {

        @Override
        public void setInventorySlotContents(int slot, ItemStack itemStack) {

            super.setInventorySlotContents(slot, itemStack);
            if (slot == 0 && !itemStack.isEmpty()) {
                nameTextField = itemStack.getDisplayName().getString();
            }
        }
    };
    public int clientCurrentTab;
    public int curUploadProgress;
    public int curCopyProgress;
    public int selectedShareOption;
    public static final int UPLOAD_AND_COPY_TIME = 20;
    public final ItemStackDatabase stackDatabase = new ItemStackDatabase();
    public static List<ItemStack> serverDatabaseStacks = new ArrayList<ItemStack>(); // client side used only, sent from the server database.
    private PlayerEntity triggeringPlayer;
    public String nameTextField = "";

    public static boolean hasPermissions(PlayerEntity player) {

        if (BPConfig.CONFIG.serverCircuitSavingOpOnly.get()) {
            //if (!player.can(2, "saveTemplate")) {
                //player.sendMessage(new StringTextComponent("gui.circuitDatabase.info.opsOnly"));
                //return false;
            //}
        }
        return true;
    }

    @Override
    public void setText(int textFieldID, String text) {

        if (textFieldID == 1) {
            nameTextField = text;
            if (!copyInventory.getStackInSlot(0).isEmpty()) {
                copyInventory.getStackInSlot(0).setDisplayName(new StringTextComponent(nameTextField));
            }
        } else {
            super.setText(textFieldID, text);
        }
    }

    @Override
    public String getText(int textFieldID) {

        return textFieldID == 1 ? nameTextField : super.getText(textFieldID);
    }

    /**
     * Returns true of copy succeeded.
     *
     * @param player
     * @param simulate
     * @return
     */
    public boolean copy(PlayerEntity player, ItemStack template, ItemStack target, boolean simulate) {

        if (!template.isEmpty() && !target.isEmpty()) {
            if (template.isItemEqual(target)) {
                IDatabaseSaveable saveable = (IDatabaseSaveable) template.getItem();
                if (saveable.canCopy(template, target)) {
                    if (!player.isCreative()) {
                        List<ItemStack> stacksInTemplate = saveable.getItemsOnStack(template);
                        List<ItemStack> stacksInOutput = saveable.getItemsOnStack(target);

                        if (stacksInTemplate.isEmpty())
                            stacksInTemplate = new ArrayList<ItemStack>();
                        if (stacksInOutput.isEmpty())
                            stacksInOutput = new ArrayList<ItemStack>();

                        List<ItemStack> traversedItems = new ArrayList<ItemStack>();

                        List<ItemStack> allApplicableItems = new ArrayList<ItemStack>();
                        allApplicableItems.addAll(stacksInTemplate);
                        allApplicableItems.addAll(stacksInOutput);
                        for (ItemStack templateStack : allApplicableItems) {
                            boolean alreadyTraversed = false;
                            for (ItemStack traversedItem : traversedItems) {
                                if (traversedItem.isItemEqual(templateStack)
                                        && ItemStack.areItemStackTagsEqual(traversedItem, templateStack)) {
                                    alreadyTraversed = true;
                                    break;
                                }
                            }
                            if (alreadyTraversed)
                                continue;
                            traversedItems.add(templateStack);

                            int count = 0;
                            for (ItemStack stack : stacksInTemplate) {
                                if (stack.isItemEqual(templateStack) && ItemStack.areItemStackTagsEqual(stack, templateStack)) {
                                    count += stack.getCount();
                                }
                            }

                            for (ItemStack stack : stacksInOutput) {
                                if (stack.isItemEqual(templateStack) && ItemStack.areItemStackTagsEqual(stack, templateStack)) {
                                    count -= stack.getCount();
                                }
                            }

                            count *= target.getCount();// if 5 items are inserted to be copied, the required items are x5.

                            if (count > 0) {// At this point we need assist from the inventory.
                                ItemStack retrievedStack = templateStack.copy();
                                retrievedStack.setCount(count);
                                retrievedStack = IOHelper.extract(this, null, retrievedStack, true, simulate, 2);
                                if (retrievedStack.isEmpty() || retrievedStack.getCount() < count)
                                    return false;
                            } else if (count < 0) {
                                ItemStack returnedStack = templateStack.copy();
                                returnedStack.setCount(-count);
                                returnedStack = IOHelper.insert(this, returnedStack, null, simulate);
                                if (!returnedStack.isEmpty() && !simulate) {
                                    IOHelper.spawnItemInWorld(world, returnedStack, pos.getX()+ 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                                }
                            }
                        }
                    }
                    if (!simulate) {
                        ItemStack copyStack = template.copy();
                        copyStack.setCount(target.getCount());
                        copyInventory.setInventorySlotContents(1, copyStack);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void tick() {

        super.tick();
        if (!world.isRemote) {
            if (!copyInventory.getStackInSlot(0).isEmpty()) {
                if (curCopyProgress >= 0) {
                    if (++curCopyProgress > UPLOAD_AND_COPY_TIME) {
                        curCopyProgress = -1;
                        if (copy(triggeringPlayer, copyInventory.getStackInSlot(0), copyInventory.getStackInSlot(1), true)) {
                            copy(triggeringPlayer, copyInventory.getStackInSlot(0), copyInventory.getStackInSlot(1), false);
                        }
                    }
                }

                if (curUploadProgress >= 0) {
                    if (++curUploadProgress > UPLOAD_AND_COPY_TIME) {
                        curUploadProgress = -1;
                        if (selectedShareOption == 1 && triggeringPlayer != null)
                            //BPNetworkHandler.INSTANCE.sendTo(new MessageCircuitDatabaseTemplate(this, copyInventory.getStackInSlot(0)),
                                    //(ServerPlayerEntity) triggeringPlayer);
                        if (selectedShareOption == 2) {
                            stackDatabase.saveItemStack(copyInventory.getStackInSlot(0));
                            //BPNetworkHandler.INSTANCE.sendToAll(new MessageSendClientServerTemplates(stackDatabase.loadItemStacks()));
                        }
                        selectedShareOption = 0;
                    }
                }
            } else {
                curCopyProgress = -1;
                curUploadProgress = -1;
                selectedShareOption = 0;
            }
        }
    }

    public void saveToPrivateLibrary(ItemStack stack) {

        stackDatabase.saveItemStack(stack);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        super.write(tag);

        if (!copyInventory.getStackInSlot(0).isEmpty()) {
            CompoundNBT stackTag = new CompoundNBT();
            copyInventory.getStackInSlot(0).write(stackTag);
            tag.put("copyTemplateStack", stackTag);
        }
        if (!copyInventory.getStackInSlot(1).isEmpty()) {
            CompoundNBT stackTag = new CompoundNBT();
            copyInventory.getStackInSlot(1).write(stackTag);
            tag.put("copyOutputStack", stackTag);
        }

        tag.putInt("curUploadProgress", curUploadProgress);
        tag.putInt("curCopyProgress", curCopyProgress);
        tag.putByte("selectedShareOption", (byte) selectedShareOption);
        return tag;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {

        super.read(state, tag);

        if (tag.contains("copyTemplateStack")) {
            copyInventory.setInventorySlotContents(0, ItemStack.read(tag.getCompound("copyTemplateStack")));
        } else {
            copyInventory.setInventorySlotContents(0, ItemStack.EMPTY);
        }

        if (tag.contains("copyOutputStack")) {
            copyInventory.setInventorySlotContents(1, ItemStack.read(tag.getCompound("copyOutputStack")));
        } else {
            copyInventory.setInventorySlotContents(1, ItemStack.EMPTY);
        }

        curUploadProgress = tag.getInt("curUploadProgress");
        curCopyProgress = tag.getInt("curCopyProgress");
        selectedShareOption = tag.getByte("selectedShareOption");
    }

}
