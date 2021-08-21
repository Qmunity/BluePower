/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.bluepowermod.container;

import com.bluepowermod.client.gui.BPMenuType;
import com.bluepowermod.client.gui.IGuiButtonSensitive;
import com.bluepowermod.container.inventory.InventoryProjectTableCrafting;
import com.bluepowermod.container.slot.SlotProjectTableCrafting;
import com.bluepowermod.tile.tier1.TileProjectTable;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;


import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @author MineMaarten
 */
//@ChestContainer
public class ContainerProjectTable extends AbstractContainerMenu implements IGuiButtonSensitive {

    private final Player player;
    private final CraftingContainer craftingGrid;
    private final ResultContainer craftResult;

    private final Container projectTable;

    public ContainerProjectTable(int windowId, Inventory invPlayer, Container inventory) {
        super(BPMenuType.PROJECT_TABLE, windowId);
        this.projectTable = inventory;
        craftResult =  new ResultContainer();
        craftingGrid = new InventoryProjectTableCrafting(this, projectTable, 3, 3);
        player = invPlayer.player;

        //Output
        addSlot(new SlotProjectTableCrafting(projectTable, player, craftingGrid, craftResult, 0, 127, 34));

        //Crafting Grid
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                //When changing the 34 and 16, this will break the NEI shift clicking the question mark. See NEIPluginInitConfig
                addSlot(new Slot(craftingGrid, j + i * 3, 34 + j * 18, 16 + i * 18));
            }
        }

        //Chest
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(projectTable, j + i * 9, 8 + j * 18, 79 + i * 18));
            }
        }

        bindPlayerInventory(invPlayer);
        this.slotsChanged(this.craftingGrid);
    }

    public ContainerProjectTable( int id, Inventory player )    {
        this( id, player, new SimpleContainer( TileProjectTable.SLOTS ));
    }

    protected void bindPlayerInventory(Inventory invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 126 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlot(new Slot(invPlayer, j, 8 + j * 18, 184));
        }
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {

        boolean clickTypeCrafting = slotId == 0 && slots.get(slotId).hasItem() &&
                (clickTypeIn.equals(ClickType.PICKUP) || clickTypeIn.equals(ClickType.QUICK_MOVE));

        //Save the Matrix State before Crafting
        NonNullList<ItemStack> beforeAction = NonNullList.withSize(9, ItemStack.EMPTY);
        if(clickTypeCrafting){
            for (int i = 1; i < 10; ++i) {
                Slot matrixSlot = slots.get(i);
                ItemStack matrixStack = matrixSlot.getItem();
                beforeAction.set(i - 1, matrixStack);
            }
        }

        //Try to pull from the Project Table Inventory if the last of an item for a recipe.
        if(clickTypeCrafting){
            for (int i = 1; i < 10; ++i) {
                ItemStack beforeStack = beforeAction.get(i - 1);
                Slot matrixSlot = slots.get(i);
                ItemStack matrixStack = matrixSlot.getItem();

                if (matrixStack.getCount() == 0 && beforeStack.getCount() != 0) {
                    for (int ptSlot = 10; ptSlot < 28; ++ptSlot) {
                        Slot inventorySlot = slots.get(ptSlot);
                        ItemStack ptStack = inventorySlot.getItem();
                        if (ptStack.getItem() == beforeStack.getItem() && ptStack.getTag() == beforeStack.getTag()) {
                            ptStack.setCount(ptStack.getCount() - 1);
                            inventorySlot.set(ptStack);
                            beforeStack.setCount(1);
                            matrixSlot.set(beforeStack);
                            break;
                        }
                    }
                }
            }
        }
    }

    protected static void updateCrafting(int id, Level world, Player playerEntity, CraftingContainer craftingInventory, ResultContainer craftResultInventory) {
        if (!world.isClientSide) {
            ServerPlayer serverplayerentity = (ServerPlayer)playerEntity;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInventory, world);
            if (optional.isPresent()) {
                CraftingRecipe icraftingrecipe = optional.get();
                if (craftResultInventory.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.assemble(craftingInventory);
                }
            }

            craftResultInventory.setItem(0, itemstack);
            serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(id, 0, 0, itemstack));
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void slotsChanged(Container inventoryIn) {
            updateCrafting(this.containerId, this.player.getCommandSenderWorld(), this.player, this.craftingGrid, this.craftResult);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public void clearCraftingGrid() {
        for (int i = 1; i < 10; i++) {
            Slot slot = (Slot) slots.get(i);
            if (slot.hasItem()) {
                moveItemStackTo(slot.getItem(), 10, 28, false);
                if (slot.getItem().getCount() <= 0)
                    slot.set(ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.container != this.craftResult && super.canTakeItemForPickAll(stack, slotIn);
    }

    public CraftingContainer getCraftingGrid() {
        return craftingGrid;
    }

    /*
     * 0 result, 1-9 matrix,  10 - 27 inventory, 28 - 63 player inv.
     */
    @Override
    public ItemStack quickMoveStack(Player player, int par2) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(par2);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (0 < par2 && par2 < 10) {
                if (!moveItemStackTo(itemstack1, 10, 28, false))
                    return ItemStack.EMPTY;
            } else if (par2 < 28) {
                if (!moveItemStackTo(itemstack1, 28, 64, false))
                    return ItemStack.EMPTY;
            } else {
                if (!moveItemStackTo(itemstack1, 10, 28, false))
                    return ItemStack.EMPTY;
            }
            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() != itemstack.getCount()) {
                slot.onQuickCraft(itemstack, itemstack1);
            } else {

                this.slotsChanged(this.craftingGrid);
                return ItemStack.EMPTY;
            }
            ItemStack itemstack2 = this.getCarried();
            slot.onTake(player, itemstack1);

            if (par2 == 0)
            {
                player.drop(itemstack2, false);
            }
        }

        this.slotsChanged(this.craftingGrid);
        return itemstack;
    }

    @Override
    public void onButtonPress(Player player, int messageId, int value) {
        this.clearCraftingGrid();
    }
/*
    @Optional.Method(modid = Dependencies.INVTWEAKS)
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getSections() {

        Map<ContainerSection, List<Slot>> sections = new HashMap<ContainerSection, List<Slot>>();
        List<Slot> slotsCraftingIn = new ArrayList<Slot>();
        List<Slot> slotsCraftingOut = new ArrayList<Slot>();
        List<Slot> slotsChest = new ArrayList<Slot>();
        List<Slot> slotsInventory = new ArrayList<Slot>();
        List<Slot> slotsInventoryHotbar = new ArrayList<Slot>();
        for (int i = 0; i < 9; i++) {
            slotsCraftingIn.add(i, (Slot) inventorySlots.get(i));
        }
        slotsCraftingOut.add(0, (Slot) inventorySlots.get(9));
        for (int i = 0; i < 18; i++) {
            slotsChest.add(i, (Slot) inventorySlots.get(i + 9));
        }
        for (int i = 0; i < 27; i++) {
            slotsInventory.add(0, (Slot) inventorySlots.get(i + 27));
        }
        for (int i = 0; i < 9; i++) {
            slotsInventoryHotbar.add(0, (Slot) inventorySlots.get(i + 54));
        }
        sections.put(ContainerSection.CRAFTING_IN, slotsCraftingIn);
        sections.put(ContainerSection.CRAFTING_OUT, slotsCraftingOut);
        sections.put(ContainerSection.CHEST, slotsChest);
        sections.put(ContainerSection.INVENTORY, slotsInventory);
        sections.put(ContainerSection.INVENTORY_HOTBAR, slotsInventoryHotbar);
        return sections;
    }*/
}
