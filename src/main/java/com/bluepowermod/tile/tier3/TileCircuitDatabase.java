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
import com.bluepowermod.tile.TileBase;
import com.bluepowermod.tile.tier2.TileCircuitTable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class TileCircuitDatabase extends TileCircuitTable {

    public Container copyInventory = new SimpleContainer(2) {

        @Override
        public void setItem(int slot, ItemStack itemStack) {

            super.setItem(slot, itemStack);
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
    private Player triggeringPlayer;
    public String nameTextField = "";

    public TileCircuitDatabase(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static boolean hasPermissions(Player player) {

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
            if (!copyInventory.getItem(0).isEmpty()) {
                copyInventory.getItem(0).set(DataComponents.ITEM_NAME, Component.literal(nameTextField));
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
    public boolean copy(Player player, ItemStack template, ItemStack target, boolean simulate) {

        if (!template.isEmpty() && !target.isEmpty()) {
            if (ItemStack.isSameItem(template, target)) {
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
                                if (ItemStack.isSameItem(traversedItem, templateStack)
                                        && ItemStack.isSameItemSameComponents(traversedItem, templateStack)) {
                                    alreadyTraversed = true;
                                    break;
                                }
                            }
                            if (alreadyTraversed)
                                continue;
                            traversedItems.add(templateStack);

                            int count = 0;
                            for (ItemStack stack : stacksInTemplate) {
                                if (ItemStack.isSameItem(stack, templateStack) && ItemStack.isSameItemSameComponents(stack, templateStack)) {
                                    count += stack.getCount();
                                }
                            }

                            for (ItemStack stack : stacksInOutput) {
                                if (ItemStack.isSameItem(stack, templateStack) && ItemStack.isSameItemSameComponents(stack, templateStack)) {
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
                                    IOHelper.spawnItemInWorld(level, returnedStack, worldPosition.getX()+ 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
                                }
                            }
                        }
                    }
                    if (!simulate) {
                        ItemStack copyStack = template.copy();
                        copyStack.setCount(target.getCount());
                        copyInventory.setItem(1, copyStack);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static void tickCircuitDatabase(Level level, BlockPos pos, BlockState state, TileCircuitDatabase tileCircuitDatabase) {

        TileBase.tickTileBase(level, pos, state, tileCircuitDatabase);
        if (!level.isClientSide) {
            if (!tileCircuitDatabase.copyInventory.getItem(0).isEmpty()) {
                if (tileCircuitDatabase.curCopyProgress >= 0) {
                    if (++tileCircuitDatabase.curCopyProgress > UPLOAD_AND_COPY_TIME) {
                        tileCircuitDatabase.curCopyProgress = -1;
                        if (tileCircuitDatabase.copy(tileCircuitDatabase.triggeringPlayer, tileCircuitDatabase.copyInventory.getItem(0), tileCircuitDatabase.copyInventory.getItem(1), true)) {
                            tileCircuitDatabase.copy(tileCircuitDatabase.triggeringPlayer, tileCircuitDatabase.copyInventory.getItem(0), tileCircuitDatabase.copyInventory.getItem(1), false);
                        }
                    }
                }

                if (tileCircuitDatabase.curUploadProgress >= 0) {
                    if (++tileCircuitDatabase.curUploadProgress > UPLOAD_AND_COPY_TIME) {
                        tileCircuitDatabase.curUploadProgress = -1;
                        if (tileCircuitDatabase.selectedShareOption == 1 && tileCircuitDatabase.triggeringPlayer != null)
                            //BPNetworkHandler.INSTANCE.sendTo(new MessageCircuitDatabaseTemplate(this, copyInventory.getItem(0)),
                                    //(ServerPlayer) triggeringPlayer);
                        if (tileCircuitDatabase.selectedShareOption == 2) {
                            tileCircuitDatabase.stackDatabase.saveItemStack(level.registryAccess(), tileCircuitDatabase.copyInventory.getItem(0));
                            //BPNetworkHandler.INSTANCE.sendToAll(new MessageSendClientServerTemplates(stackDatabase.loadItemStacks()));
                        }
                        tileCircuitDatabase.selectedShareOption = 0;
                    }
                }
            } else {
                tileCircuitDatabase.curCopyProgress = -1;
                tileCircuitDatabase.curUploadProgress = -1;
                tileCircuitDatabase.selectedShareOption = 0;
            }
        }
    }

    public void saveToPrivateLibrary(HolderLookup.Provider provider, ItemStack stack) {

        stackDatabase.saveItemStack(provider, stack);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);

        if (!copyInventory.getItem(0).isEmpty()) {
            CompoundTag stackTag = new CompoundTag();
            copyInventory.getItem(0).save(provider, stackTag);
            tag.put("copyTemplateStack", stackTag);
        }
        if (!copyInventory.getItem(1).isEmpty()) {
            CompoundTag stackTag = new CompoundTag();
            copyInventory.getItem(1).save(provider, stackTag);
            tag.put("copyOutputStack", stackTag);
        }

        tag.putInt("curUploadProgress", curUploadProgress);
        tag.putInt("curCopyProgress", curCopyProgress);
        tag.putByte("selectedShareOption", (byte) selectedShareOption);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);

        if (tag.contains("copyTemplateStack")) {
            copyInventory.setItem(0, ItemStack.parseOptional(provider, tag.getCompound("copyTemplateStack")));
        } else {
            copyInventory.setItem(0, ItemStack.EMPTY);
        }

        if (tag.contains("copyOutputStack")) {
            copyInventory.setItem(1, ItemStack.parseOptional(provider, tag.getCompound("copyOutputStack")));
        } else {
            copyInventory.setItem(1, ItemStack.EMPTY);
        }

        curUploadProgress = tag.getInt("curUploadProgress");
        curCopyProgress = tag.getInt("curCopyProgress");
        selectedShareOption = tag.getByte("selectedShareOption");
    }

}
