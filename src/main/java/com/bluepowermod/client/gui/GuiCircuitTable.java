/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import com.bluepowermod.client.gui.widget.WidgetVerticalScrollbar;
import com.bluepowermod.container.ContainerCircuitTable;
import com.bluepowermod.container.slot.SlotCircuitTableCrafting;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.message.MessageGuiUpdate;
import com.bluepowermod.network.message.MessageUpdateTextfield;
import com.bluepowermod.tile.tier2.TileCircuitTable;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCircuitTable extends GuiBase {

    protected static final ResourceLocation guiTexture = new ResourceLocation(Refs.MODID, "textures/gui/circuit_table.png");
    private static final ResourceLocation scrollTexture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private final TileCircuitTable circuitTable;
    protected WidgetVerticalScrollbar scrollbar;

    private GuiTextField searchField;
    private int oldScroll;

    /**
     * Used to back up the ContainerSearcher's inventory slots before filling it with the player's inventory slots for
     * the inventory tab.
     */
    //private List backupContainerSlots;
    private boolean field_74234_w;

    private final boolean[] displayRed = new boolean[24];

    public GuiCircuitTable(InventoryPlayer invPlayer, TileCircuitTable circuitTable) {

        this(circuitTable, new ContainerCircuitTable(invPlayer, circuitTable), guiTexture);
    }

    public GuiCircuitTable(TileCircuitTable circuitTable, Container container, ResourceLocation resLoc) {

        super(circuitTable, container, resLoc);
        allowUserInput = true;
        ySize = 224;
        this.circuitTable = circuitTable;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {

        super.initGui();
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        searchField = new GuiTextField(fontRendererObj, guiLeft + 8, guiTop + 20, 89, fontRendererObj.FONT_HEIGHT);
        searchField.setMaxStringLength(15);
        searchField.setEnableBackgroundDrawing(true);
        searchField.setVisible(true);
        searchField.setTextColor(16777215);
        searchField.setText(circuitTable.getText(0));
        scrollbar = new WidgetVerticalScrollbar(guiLeft + 155, guiTop + 32, 54);
        scrollbar.setListening(true);
        //  scrollbar.setCurrentState(circuitTable.slotsScrolled);
        addWidget(scrollbar);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        scrollbar.handleMouseInput();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
    public void onGuiClosed() {

        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {

        super.mouseClicked(x, y, button);
        if (isTextfieldEnabled()) {
            searchField.mouseClicked(x, y, button);
            if (searchField.isFocused() && button == 1) {
                searchField.setText("");
                circuitTable.setText(0, searchField.getText());
                NetworkHandler.sendToServer(new MessageUpdateTextfield(circuitTable, 0));
            }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char par1, int par2) {

        if (field_74234_w) {
            field_74234_w = false;
            //  searchField.setText("");
        }

        if (par2 == 1)//esc
        {
            super.keyTyped(par1, par2);
        } else {
            if (isTextfieldEnabled()) {
                if (searchField.textboxKeyTyped(par1, par2)) {
                    circuitTable.setText(0, searchField.getText());
                    NetworkHandler.sendToServer(new MessageUpdateTextfield(circuitTable, 0));
                } else {
                    super.keyTyped(par1, par2);
                }
            }
        }
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {

        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        if (isTextfieldEnabled())
            searchField.drawTextBox();

        int i1 = guiLeft + 156;
        int k = guiTop + 48;
        int l = k + 112;
        mc.getTextureManager().bindTexture(scrollTexture);
        //drawTexturedModalRect(i1, k + (int) ((l - k - 17) * currentScroll), 232 + (needsScrollBars() ? 0 : 12), 0, 12, 15);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if (displayRed[i * 8 + j]) {
                    drawRect(guiLeft + 8 + j * 18, guiTop + 33 + i * 18, guiLeft + 24 + j * 18, guiTop + 49 + i * 18, 0x55FF0000);
                }
            }
        }
    }

    @Override
    public void updateScreen() {

        super.updateScreen();
        for (int i = 0; i < 24; i++) {
            displayRed[i] = inventorySlots.getSlot(i).getHasStack() ? shouldDisplayRed(inventorySlots.getSlot(i).getStack()) : false;
        }
        int scrollbarStates = circuitTable.getRequiredScrollStates();
        scrollbar.setStates(scrollbarStates);
        scrollbar.enabled = scrollbarStates > 0;

        /*      if (oldTileScroll != circuitTable.slotsScrolled) {
                  scrollbar.setCurrentState(circuitTable.slotsScrolled);
                  oldTileScroll = circuitTable.slotsScrolled;
              }*/
        if (oldScroll != scrollbar.getState()) {
            oldScroll = scrollbar.getState();
            NetworkHandler.sendToServer(new MessageGuiUpdate(circuitTable, 0, scrollbar.getState()));
        }
    }

    public void updateScrollbar(int state) {
        int scrollbarStates = circuitTable.getRequiredScrollStates();
        scrollbar.setStates(scrollbarStates);
        scrollbar.enabled = scrollbarStates > 0;
        if (state < scrollbarStates)
            scrollbar.setCurrentState(state);
        oldScroll = state;
    }

    protected boolean shouldDisplayRed(ItemStack stack) {

        return !SlotCircuitTableCrafting.canCraft(stack, circuitTable);
    }

    protected boolean isTextfieldEnabled() {

        return true;
    }

}
