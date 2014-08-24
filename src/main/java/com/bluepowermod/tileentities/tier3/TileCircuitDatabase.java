package com.bluepowermod.tileentities.tier3;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.item.IDatabaseSaveable;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.ItemStackDatabase;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.Config;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageCircuitDatabaseTemplate;
import com.bluepowermod.network.messages.MessageSendClientServerTemplates;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.tier2.TileCircuitTable;

public class TileCircuitDatabase extends TileCircuitTable {

    public IInventory copyInventory = new InventoryBasic("copy inventory", false, 2) {

        @Override
        public void setInventorySlotContents(int slot, ItemStack itemStack) {

            super.setInventorySlotContents(slot, itemStack);
            if (slot == 0 && itemStack != null) {
                nameTextField = itemStack.getDisplayName();
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
    private EntityPlayer triggeringPlayer;
    public String nameTextField = "";

    @Override
    protected List<ItemStack> getApplicableItems() {

        List<ItemStack> items = new ArrayList<ItemStack>();
        if (worldObj == null || !worldObj.isRemote) {
            return items;
        } else {
            items.addAll(clientCurrentTab == 1 ? stackDatabase.loadItemStacks() : serverDatabaseStacks);
            return items;
        }
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        switch (messageId) {
        case 1:
            player.openGui(BluePower.instance, value == 0 ? GuiIDs.CIRCUITDATABASE_MAIN_ID.ordinal() : GuiIDs.CIRCUITDATABASE_SHARING_ID.ordinal(),
                    worldObj, xCoord, yCoord, zCoord);
            break;
        case 2:
            if (value == 2 && !hasPermissions(player))
                return;
            selectedShareOption = value;
            if (selectedShareOption > 0) {
                triggeringPlayer = player;
                curUploadProgress = 0;
            } else {
                curUploadProgress = -1;
            }
            break;
        case 3:
            triggeringPlayer = player;
            curCopyProgress = curCopyProgress >= 0 || !copy(player, copyInventory.getStackInSlot(0), copyInventory.getStackInSlot(1), true) ? -1 : 0;
            break;

        }
        super.onButtonPress(player, messageId, value);
    }

    public static boolean hasPermissions(EntityPlayer player) {

        if (Config.serverCircuitSavingOpOnly) {
            if (!player.canCommandSenderUseCommand(2, "saveTemplate")) {
                player.addChatMessage(new ChatComponentTranslation("gui.circuitDatabase.info.opsOnly"));
                return false;
            }
        }
        return true;
    }

    @Override
    public void setText(int textFieldID, String text) {

        if (textFieldID == 1) {
            nameTextField = text;
            if (copyInventory.getStackInSlot(0) != null) {
                copyInventory.getStackInSlot(0).setStackDisplayName(nameTextField);
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
    public boolean copy(EntityPlayer player, ItemStack template, ItemStack target, boolean simulate) {

        if (template != null && target != null) {
            if (template.isItemEqual(target)) {
                IDatabaseSaveable saveable = (IDatabaseSaveable) template.getItem();
                if (saveable.canCopy(template, target)) {
                    if (!player.capabilities.isCreativeMode) {
                        List<ItemStack> stacksInTemplate = saveable.getItemsOnStack(template);
                        List<ItemStack> stacksInOutput = saveable.getItemsOnStack(target);

                        if (stacksInTemplate == null)
                            stacksInTemplate = new ArrayList<ItemStack>();
                        if (stacksInOutput == null)
                            stacksInOutput = new ArrayList<ItemStack>();

                        List<ItemStack> traversedItems = new ArrayList<ItemStack>();

                        List<ItemStack> allApplicableItems = new ArrayList<ItemStack>();
                        allApplicableItems.addAll(stacksInTemplate);
                        allApplicableItems.addAll(stacksInOutput);
                        for (ItemStack templateStack : allApplicableItems) {
                            boolean alreadyTraversed = false;
                            for (ItemStack traversedItem : traversedItems) {
                                if (traversedItem.isItemEqual(templateStack) && ItemStack.areItemStackTagsEqual(traversedItem, templateStack)) {
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
                                    count += stack.stackSize;
                                }
                            }

                            for (ItemStack stack : stacksInOutput) {
                                if (stack.isItemEqual(templateStack) && ItemStack.areItemStackTagsEqual(stack, templateStack)) {
                                    count -= stack.stackSize;
                                }
                            }

                            count *= target.stackSize;// if 5 items are inserted to be copied, the required items are x5.

                            if (count > 0) {// At this point we need assist from the inventory.
                                ItemStack retrievedStack = templateStack.copy();
                                retrievedStack.stackSize = count;
                                retrievedStack = IOHelper.extract(this, ForgeDirection.UNKNOWN, retrievedStack, true, simulate, 2);
                                if (retrievedStack == null || retrievedStack.stackSize < count)
                                    return false;
                            } else if (count < 0) {
                                ItemStack returnedStack = templateStack.copy();
                                returnedStack.stackSize = -count;
                                returnedStack = IOHelper.insert(this, returnedStack, ForgeDirection.UNKNOWN, simulate);
                                if (returnedStack != null && !simulate) {
                                    IOHelper.spawnItemInWorld(worldObj, returnedStack, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                                }
                            }
                        }
                    }
                    if (!simulate) {
                        ItemStack copyStack = template.copy();
                        copyStack.stackSize = target.stackSize;
                        copyInventory.setInventorySlotContents(1, copyStack);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateEntity() {

        super.updateEntity();
        if (!worldObj.isRemote) {
            if (copyInventory.getStackInSlot(0) != null) {
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
                            NetworkHandler.sendTo(new MessageCircuitDatabaseTemplate(this, copyInventory.getStackInSlot(0)),
                                    (EntityPlayerMP) triggeringPlayer);
                        if (selectedShareOption == 2) {
                            stackDatabase.saveItemStack(copyInventory.getStackInSlot(0));
                            NetworkHandler.sendToAll(new MessageSendClientServerTemplates(stackDatabase.loadItemStacks()));
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
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        if (copyInventory.getStackInSlot(0) != null) {
            NBTTagCompound stackTag = new NBTTagCompound();
            copyInventory.getStackInSlot(0).writeToNBT(stackTag);
            tag.setTag("copyTemplateStack", stackTag);
        }
        if (copyInventory.getStackInSlot(1) != null) {
            NBTTagCompound stackTag = new NBTTagCompound();
            copyInventory.getStackInSlot(1).writeToNBT(stackTag);
            tag.setTag("copyOutputStack", stackTag);
        }

        tag.setInteger("curUploadProgress", curUploadProgress);
        tag.setInteger("curCopyProgress", curCopyProgress);
        tag.setByte("selectedShareOption", (byte) selectedShareOption);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        if (tag.hasKey("copyTemplateStack")) {
            copyInventory.setInventorySlotContents(0, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("copyTemplateStack")));
        } else {
            copyInventory.setInventorySlotContents(0, null);
        }

        if (tag.hasKey("copyOutputStack")) {
            copyInventory.setInventorySlotContents(1, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("copyOutputStack")));
        } else {
            copyInventory.setInventorySlotContents(1, null);
        }

        curUploadProgress = tag.getInteger("curUploadProgress");
        curCopyProgress = tag.getInteger("curCopyProgress");
        selectedShareOption = tag.getByte("selectedShareOption");
    }

    @Override
    public String getInventoryName() {

        return BPBlocks.circuit_database.getUnlocalizedName();
    }
}
