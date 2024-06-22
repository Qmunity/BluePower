/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import com.bluepowermod.BluePower;
import com.bluepowermod.client.gui.widget.IGuiWidget;
import com.bluepowermod.client.gui.widget.WidgetTab;
import com.bluepowermod.container.ContainerCircuitDatabaseSharing;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiCircuitDatabaseSharing extends GuiContainerBaseBP<ContainerCircuitDatabaseSharing> implements MenuAccess<ContainerCircuitDatabaseSharing> {

    private final ContainerCircuitDatabaseSharing circuitDatabase;
    private int curDeletingTemplate = -1;
    private static final ResourceLocation copyTabTexture = ResourceLocation.fromNamespaceAndPath(Refs.MODID, "textures/gui/circuit_database.png");

    public GuiCircuitDatabaseSharing(ContainerCircuitDatabaseSharing container, Inventory playerInventory, Component title){
        super(container, playerInventory, title, copyTabTexture);
        this.circuitDatabase = container;
        //allowUserInput = true;
    }

    @Override
    public void init() {

        super.init();

        WidgetTab widget = new WidgetTab(1, leftPos - 32, topPos + 10, 33, 35, 198, 3, Refs.MODID + ":textures/gui/circuit_database.png") {

            @Override
            protected void addTooltip(int curHoveredTab, List<String> curTip, boolean shiftPressed) {

                switch (curHoveredTab) {
                case 0:
                    curTip.add("gui.bluepower:circuitDatabase.tab.copyAndShare");
                    break;
                case 1:
                    curTip.add("gui.bluepower:circuitDatabase.tab.private");
                    break;
                case 2:
                    curTip.add("gui.bluepower:circuitDatabase.tab.server");
                    if (Minecraft.getInstance().hasSingleplayerServer())
                        curTip.add("gui.bluepower:circuitDatabase.info.serverOnly");
                    break;
                }
            }
        };
        //widget.value = circuitDatabase.clientCurrentTab;
        widget.enabledTabs[2] = !Minecraft.getInstance().hasSingleplayerServer();
        addWidget(widget);

    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {

        if (slot != null && slot.hasItem() && slot.container == inventory) {
            if (BluePower.proxy.get().isSneakingInGui()) {
                if (slot.getSlotIndex() == curDeletingTemplate) {
                    //if (circuitDatabase.clientCurrentTab == 1) {
                        //circuitDatabase.stackDatabase.deleteStack(slot.getItem());
                       // circuitDatabase.updateGateInventory();
                    //} else {
                        //BPNetworkHandler.INSTANCE.sendToServer(new MessageCircuitDatabaseTemplate(circuitDatabase, slot.getItem(), true));
                    //}
                } else {
                    curDeletingTemplate = slot.getSlotIndex();
                    return;
                }
            } else {
                //circuitDatabase.clientCurrentTab = 0;// Navigate to the copy & share tab.
                //BPNetworkHandler.INSTANCE.sendToServer(new MessageCircuitDatabaseTemplate(circuitDatabase, slot.getItem()));
            }
        } else {
            super.slotClicked(slot, slotId, mouseButton, type);
        }
        curDeletingTemplate = -1;
    }

    public ItemStack getCurrentDeletingTemplate() {

        return curDeletingTemplate == -1 ? ItemStack.EMPTY : inventory.getItem(curDeletingTemplate);
    }
/*

    @Override
    protected boolean shouldDisplayRed(ItemStack stack) {

        if ((circuitDatabase.clientCurrentTab == 1 || circuitDatabase.clientCurrentTab == 2)
                && !circuitDatabase.copyInventory.getItem(1).isEmpty()) {
            return !circuitDatabase.copy(Minecraft.getInstance().player, stack, circuitDatabase.copyInventory.getItem(1), true);
        } else {
            return false;
        }
    }
*/

    @Override
    public void actionPerformed(IGuiWidget widget) {

        if (widget.getID() == 1) {
            //circuitDatabase.clientCurrentTab = ((BaseWidget) widget).value;
        }
        //BPNetworkHandler.INSTANCE.sendToServer(new MessageGuiUpdate(circuitDatabase, widget.getID(), ((BaseWidget) widget).value));
    }

    @Override
    protected boolean isInfoStatLeftSided() {

        return false;
    }
}
