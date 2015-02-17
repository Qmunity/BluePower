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
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.gui.widget.BaseWidget;
import uk.co.qmunity.lib.client.gui.widget.IGuiWidget;
import uk.co.qmunity.lib.client.gui.widget.WidgetMode;
import uk.co.qmunity.lib.client.gui.widget.WidgetSidewaysTab;
import uk.co.qmunity.lib.client.gui.widget.WidgetTab;

import com.bluepowermod.container.ContainerCircuitDatabaseMain;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageGuiUpdate;
import com.bluepowermod.network.message.MessageUpdateTextfield;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;
import com.bluepowermod.util.Refs;

public class GuiCircuitDatabaseMain extends GuiContainerBaseBP {

    private final TileCircuitDatabase circuitDatabase;
    private static final ResourceLocation copyTabTexture = new ResourceLocation(Refs.MODID, "textures/gui/circuit_database.png");
    private GuiTextField nameField;
    private WidgetSidewaysTab shareOptionTab;
    private WidgetMode copyButton;

    public GuiCircuitDatabaseMain(InventoryPlayer invPlayer, TileCircuitDatabase circuitDatabase) {

        super(circuitDatabase, new ContainerCircuitDatabaseMain(invPlayer, circuitDatabase), copyTabTexture);
        this.circuitDatabase = circuitDatabase;
        ySize = 224;
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

        shareOptionTab = new WidgetSidewaysTab(2, guiLeft + 44, guiTop + 18, 14, 14, 234, 3, Refs.MODID
                + ":textures/gui/circuit_database.png") {

            @Override
            protected void addTooltip(int curHoveredTab, List<String> curTip, boolean shiftPressed) {

                switch (curHoveredTab) {
                case 0:
                    curTip.add("gui.circuitDatabase.action.cancel");
                    break;
                case 1:
                    curTip.add("gui.circuitDatabase.action.savePrivate");
                    if (!enabledTabs[curHoveredTab]) {
                        curTip.add("gui.circuitDatabase.info.nameTaken");
                    }
                    break;
                case 2:
                    curTip.add("gui.circuitDatabase.action.saveServer");
                    if (Minecraft.getMinecraft().isSingleplayer()) {
                        curTip.add("gui.circuitDatabase.info.serverOnly");
                    } else if (!enabledTabs[curHoveredTab]) {
                        curTip.add("gui.circuitDatabase.info.nameTaken");
                    }
                    break;
                }
            }
        };
        shareOptionTab.value = circuitDatabase.selectedShareOption;
        addWidget(shareOptionTab);

        copyButton = new WidgetMode(3, guiLeft + 80, guiTop + 48, 176, 37, 1, Refs.MODID + ":textures/gui/circuit_database.png") {

            @Override
            public void addTooltip(int x, int y, List<String> curTip, boolean shiftPressed) {

                curTip.add("gui.circuitDatabase.action.copy");
            }
        };
        addWidget(copyButton);

        Keyboard.enableRepeatEvents(true);
        nameField = new GuiTextField(fontRendererObj, guiLeft + 95, guiTop + 35, 70, fontRendererObj.FONT_HEIGHT);
        nameField.setEnableBackgroundDrawing(true);
        nameField.setVisible(true);
        nameField.setTextColor(16777215);
        nameField.setText(circuitDatabase.getText(1));

    }

    @Override
    protected void mouseClicked(int x, int y, int button) {

        super.mouseClicked(x, y, button);
        nameField.mouseClicked(x, y, button);
        if (nameField.isFocused() && button == 1) {
            nameField.setText("");
            circuitDatabase.setText(1, nameField.getText());
            BPNetworkHandler.INSTANCE.sendToServer(new MessageUpdateTextfield(circuitDatabase, 0));
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char par1, int par2) {

        if (par2 == 1)// esc
        {
            super.keyTyped(par1, par2);
        } else {
            if (nameField.textboxKeyTyped(par1, par2)) {
                circuitDatabase.setText(1, nameField.getText());
                BPNetworkHandler.INSTANCE.sendToServer(new MessageUpdateTextfield(circuitDatabase, 1));
            } else {
                super.keyTyped(par1, par2);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {

        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        this.drawString(guiLeft + 95, guiTop + 25, I18n.format("gui.circuitDatabase.name"), false);
        nameField.drawTextBox();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(copyTabTexture);

        int processPercentage = circuitDatabase.curCopyProgress * 22 / TileCircuitDatabase.UPLOAD_AND_COPY_TIME;
        if (processPercentage > 0)
            drawTexturedModalRect(guiLeft + 77, guiTop + 64, 176, 0, processPercentage, 15);

        processPercentage = circuitDatabase.curUploadProgress * 22 / TileCircuitDatabase.UPLOAD_AND_COPY_TIME;
        if (processPercentage > 0)
            drawTexturedModalRect(guiLeft + 57, guiTop + 57 - processPercentage, 176, 37 - processPercentage, 15, processPercentage);

    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        if (widget.getID() == 1) {
            circuitDatabase.clientCurrentTab = ((BaseWidget) widget).value;
        }
        BPNetworkHandler.INSTANCE.sendToServer(new MessageGuiUpdate(circuitDatabase, widget.getID(), ((BaseWidget) widget).value));
    }

    @Override
    public void updateScreen() {

        super.updateScreen();
        if (!nameField.isFocused()) {
            nameField.setText(circuitDatabase.nameTextField);
        }
        boolean nameDuplicatePrivate = false;
        boolean nameDuplicateServer = false;
        if (circuitDatabase.copyInventory.getStackInSlot(0) != null) {
            for (ItemStack stack : circuitDatabase.stackDatabase.loadItemStacks()) {
                if (stack.getDisplayName().equals(circuitDatabase.copyInventory.getStackInSlot(0).getDisplayName())) {
                    nameDuplicatePrivate = true;
                    break;
                }
            }
            for (ItemStack stack : TileCircuitDatabase.serverDatabaseStacks) {
                if (stack.getDisplayName().equals(circuitDatabase.copyInventory.getStackInSlot(0).getDisplayName())) {
                    nameDuplicateServer = true;
                    break;
                }
            }
        }
        shareOptionTab.enabledTabs[1] = !nameDuplicatePrivate;
        shareOptionTab.enabledTabs[2] = !nameDuplicateServer && !Minecraft.getMinecraft().isSingleplayer();

        copyButton.enabled = circuitDatabase.copyInventory.getStackInSlot(0) != null
                && circuitDatabase.copyInventory.getStackInSlot(1) != null
                && circuitDatabase.copy(Minecraft.getMinecraft().thePlayer, circuitDatabase.copyInventory.getStackInSlot(0),
                        circuitDatabase.copyInventory.getStackInSlot(1), true);
    }

    @Override
    protected boolean isInfoStatLeftSided() {

        return false;
    }
}
