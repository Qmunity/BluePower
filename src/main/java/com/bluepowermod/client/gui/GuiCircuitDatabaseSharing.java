/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.BluePower;
import com.bluepowermod.client.gui.widget.WidgetTab;
import com.bluepowermod.containers.ContainerCircuitDatabaseSharing;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageCircuitDatabaseTemplate;
import com.bluepowermod.network.messages.MessageGuiUpdate;
import com.bluepowermod.tileentities.tier3.TileCircuitDatabase;
import com.bluepowermod.util.Refs;
import com.qmunity.lib.client.gui.widget.BaseWidget;
import com.qmunity.lib.client.gui.widget.IGuiWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCircuitDatabaseSharing extends GuiCircuitTable {

    private final TileCircuitDatabase circuitDatabase;
    private int curDeletingTemplate = -1;

    public GuiCircuitDatabaseSharing(InventoryPlayer invPlayer, TileCircuitDatabase circuitDatabase) {

        super(circuitDatabase, new ContainerCircuitDatabaseSharing(invPlayer, circuitDatabase), guiTexture);
        this.circuitDatabase = circuitDatabase;
        allowUserInput = true;
    }

    @Override
    public void initGui() {

        super.initGui();

        WidgetTab widget = new WidgetTab(1, guiLeft - 32, guiTop + 10, 33, 35, 198, 3, Refs.MODID + ":textures/gui/circuit_database.png") {

            @Override
            protected void addTooltip(int curHoveredTab, List<String> curTip, boolean shiftPressed) {

                switch (curHoveredTab) {
                case 0:
                    curTip.add("gui.circuitDatabase.tab.copyAndShare");
                    break;
                case 1:
                    curTip.add("gui.circuitDatabase.tab.private");
                    break;
                case 2:
                    curTip.add("gui.circuitDatabase.tab.server");
                    if (Minecraft.getMinecraft().isSingleplayer())
                        curTip.add("gui.circuitDatabase.info.serverOnly");
                    break;
                }
            }
        };
        widget.value = circuitDatabase.clientCurrentTab;
        widget.enabledTabs[2] = !Minecraft.getMinecraft().isSingleplayer();
        addWidget(widget);

    }

    @Override
    protected boolean isInfoStatLeftSided() {
        return false;
    }

    @Override
    protected void handleMouseClick(Slot slot, int p_146984_2_, int p_146984_3_, int p_146984_4_) {

        if (slot != null && slot.getHasStack() && slot.inventory == circuitDatabase.circuitInventory) {
            if (BluePower.proxy.isSneakingInGui()) {
                if (slot.getSlotIndex() == curDeletingTemplate) {
                    if (circuitDatabase.clientCurrentTab == 1) {
                        circuitDatabase.stackDatabase.deleteStack(slot.getStack());
                        circuitDatabase.updateGateInventory();
                    } else {
                        NetworkHandler.sendToServer(new MessageCircuitDatabaseTemplate(circuitDatabase, slot.getStack(), true));
                    }
                } else {
                    curDeletingTemplate = slot.getSlotIndex();
                    return;
                }
            } else {
                circuitDatabase.clientCurrentTab = 0;// Navigate to the copy & share tab.
                NetworkHandler.sendToServer(new MessageCircuitDatabaseTemplate(circuitDatabase, slot.getStack()));
            }
        } else {
            super.handleMouseClick(slot, p_146984_2_, p_146984_3_, p_146984_4_);
        }
        curDeletingTemplate = -1;
    }

    public ItemStack getCurrentDeletingTemplate() {

        return curDeletingTemplate == -1 ? null : inventorySlots.getSlot(curDeletingTemplate).getStack();
    }

    @Override
    protected boolean shouldDisplayRed(ItemStack stack) {

        if ((circuitDatabase.clientCurrentTab == 1 || circuitDatabase.clientCurrentTab == 2)
                && circuitDatabase.copyInventory.getStackInSlot(1) != null) {
            return !circuitDatabase.copy(Minecraft.getMinecraft().thePlayer, stack, circuitDatabase.copyInventory.getStackInSlot(1), true);
        } else {
            return false;
        }
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        if (widget.getID() == 1) {
            circuitDatabase.clientCurrentTab = ((BaseWidget) widget).value;
        }
        if (widget instanceof BaseWidget)
            NetworkHandler.sendToServer(new MessageGuiUpdate(circuitDatabase, widget.getID(), ((BaseWidget) widget).value));
    }
}
