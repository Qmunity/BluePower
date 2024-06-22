/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import com.bluepowermod.container.ContainerCircuitTable;
import com.bluepowermod.reference.Refs;

@OnlyIn(Dist.CLIENT)
public class GuiCircuitTable extends GuiContainerBaseBP<ContainerCircuitTable> implements MenuAccess<ContainerCircuitTable> {

    protected static final ResourceLocation guiTexture = ResourceLocation.fromNamespaceAndPath(Refs.MODID, "textures/gui/circuit_table.png");
    private static final ResourceLocation scrollTexture = ResourceLocation.parse("textures/gui/container/creative_inventory/tabs.png");
    private final ContainerCircuitTable circuitTable;

    /** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
    private float currentScroll;

    /** True if the scrollbar is being dragged */
    private boolean isScrolling;

    /**
     * True if the left mouse button was held down last time drawScreen was called.
     */
    private boolean wasClicking;
    private EditBox searchField;
    private final boolean firstRun = true;
    private int ticksExisted;

    /**
     * Used to back up the ContainerSearcher's inventory slots before filling it with the player's inventory slots for the inventory tab.
     */
    // private List backupContainerSlots;
    private boolean field_74234_w;

    private final boolean[] displayRed = new boolean[24];

    public GuiCircuitTable(ContainerCircuitTable container, Inventory playerInventory, Component title){
        super(container, playerInventory, title, guiTexture);
        imageHeight = 224;
        this.circuitTable = container;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void init() {

        super.init();
        //Todo 1.17
        //buttons.clear();
        //Keyboard.enableRepeatEvents(true);
        //searchField = new TextFieldWidget(0, font, leftPos + 8, topPos + 20, 89, font.FONT_HEIGHT);
        //searchField.setMaxStringLength(15);
        //searchField.setBordered(true);
        //searchField.setVisible(true);
        //searchField.setTextColor(16777215);
        //searchField.setText(circuitTable.getText(0));

    }

    @Override
    public void onClose() {
        super.onClose();
        //Keyboard.enableRepeatEvents(false);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        super.mouseClicked(x, y, button);
        if (isTextfieldEnabled()) {
            searchField.mouseClicked(x, y, button);
            if (searchField.isFocused() && button == 1) {
                searchField.setValue("");
                //circuitTable.setText(0, searchField.getText());
                //BPNetworkHandler.INSTANCE.sendToServer(new MessageUpdateTextfield(circuitTable, 0));
            }
        }
        return true;
    }

    @Override
    public boolean keyPressed(int par1, int par2, int par3) {


        if (field_74234_w) {
            field_74234_w = false;
            // searchField.setText("");
        }

        if (par2 == 1)// esc
        {
            super.keyPressed(par1, par2, par3);
        } else {
            if (isTextfieldEnabled()) {
                if (searchField.keyPressed(par1, par2, par3)) {
                    //circuitTable.putText(0, searchField.getText());
                    //BPNetworkHandler.INSTANCE.sendToServer(new MessageUpdateTextfield(circuitTable, 0));
                } else {
                    super.keyPressed(par1, par2, par3);
                }
            }
        }
        return true;
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float par1, int par2, int par3) {

        super.renderBg(guiGraphics, par1, par2, par3);
        //if (isTextfieldEnabled())
            //searchField.drawTextBox();

        int i1 = leftPos + 156;
        int k = topPos + 48;
        int l = k + 112;
        this.minecraft.getTextureManager().bindForSetup(scrollTexture);
         //blit(i1, k + (int) ((l - k - 17) * currentScroll), 232 + (needsScrollBars() ? 0 : 12), 0, 12, 15);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if (displayRed[i * 8 + j]) {
                    guiGraphics.fill(leftPos + 8 + j * 18, topPos + 33 + i * 18, leftPos + 24 + j * 18, topPos + 49 + i * 18, 0x55FF0000);
                }
            }
        }
    }

    //@Override
    //public void containerTick() {
        //for (int i = 0; i < 24; i++) {
            //displayRed[i] = inventory.getItem(i).isEmpty() && shouldDisplayRed(inventory.getItem(i));
        //}
    //}

    //protected boolean shouldDisplayRed(ItemStack stack) {

        //return !SlotCircuitTableCrafting.canCraft(stack, circuitTable);
    //}

    protected boolean isTextfieldEnabled() {

        return true;
    }

    public void updateScrollbar(int slotsScrolled) {

        currentScroll = slotsScrolled;
    }

}
