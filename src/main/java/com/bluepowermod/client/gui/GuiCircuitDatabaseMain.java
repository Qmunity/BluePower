/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import com.bluepowermod.client.gui.widget.*;
import com.bluepowermod.container.ContainerCircuitDatabaseMain;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class GuiCircuitDatabaseMain extends GuiContainerBaseBP<ContainerCircuitDatabaseMain> implements IHasContainer<ContainerCircuitDatabaseMain> {

    private final ContainerCircuitDatabaseMain circuitDatabase;
    private static final ResourceLocation copyTabTexture = new ResourceLocation(Refs.MODID, "textures/gui/circuit_database.png");
    private TextFieldWidget nameField;
    private WidgetSidewaysTab shareOptionTab;
    private WidgetMode copyButton;

    public GuiCircuitDatabaseMain(ContainerCircuitDatabaseMain container, PlayerInventory playerInventory, ITextComponent title){
        super(container, playerInventory, title, copyTabTexture);
        this.circuitDatabase = container;
        ySize = 224;
    }

    @Override
    public void init() {

        super.init();

        WidgetTab widget = new WidgetTab(1, guiLeft - 32, guiTop + 10, 33, 35, 198, 3, Refs.MODID + ":textures/gui/circuit_database.png") {

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
                    if (Minecraft.getInstance().isSingleplayer())
                        curTip.add("gui.bluepower:circuitDatabase.info.serverOnly");
                    break;
                }
            }
        };
        widget.value = circuitDatabase.selectedShareOption;
        widget.enabledTabs[2] = !Minecraft.getInstance().isSingleplayer();
        addWidget(widget);

        shareOptionTab = new WidgetSidewaysTab(2, guiLeft + 44, guiTop + 18, 14, 14, 234, 3, Refs.MODID
                + ":textures/gui/circuit_database.png") {

            @Override
            protected void addTooltip(int curHoveredTab, List<String> curTip, boolean shiftPressed) {

                switch (curHoveredTab) {
                case 0:
                    curTip.add("gui.bluepower:circuitDatabase.action.cancel");
                    break;
                case 1:
                    curTip.add("gui.bluepower:circuitDatabase.action.savePrivate");
                    if (!enabledTabs[curHoveredTab]) {
                        curTip.add("gui.bluepower:circuitDatabase.info.nameTaken");
                    }
                    break;
                case 2:
                    curTip.add("gui.bluepower:circuitDatabase.action.saveServer");
                    if (Minecraft.getInstance().isSingleplayer()) {
                        curTip.add("gui.bluepower:circuitDatabase.info.serverOnly");
                    } else if (!enabledTabs[curHoveredTab]) {
                        curTip.add("gui.bluepower:circuitDatabase.info.nameTaken");
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

                curTip.add("gui.bluepower:circuitDatabase.action.copy");
            }
        };
        addWidget(copyButton);

        //Keyboard.enableRepeatEvents(true);
        //nameField = new TextFieldWidget(0, font, guiLeft + 95, guiTop + 35, 70, font.FONT_HEIGHT);
        nameField.setEnableBackgroundDrawing(true);
        nameField.setVisible(true);
        nameField.setTextColor(16777215);
        //nameField.setText(circuitDatabase.getText(1));

    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        super.mouseClicked(x, y, button);
        nameField.mouseClicked(x, y, button);
        if (nameField.isFocused() && button == 1) {
            nameField.setText("");
            //circuitDatabase.setText(1, nameField.getText());
            //BPNetworkHandler.INSTANCE.sendToServer(new MessageUpdateTextfield(circuitDatabase, 0));
        }
        return false;
    }

    @Override
    public boolean keyPressed(int par1, int par2, int par3) {
        if (par2 == 1)// esc
        {
            super.keyPressed(par1, par2, par3);
        } else {
            if (nameField.keyPressed(par1, par2, par3)) {
                //circuitDatabase.setText(1, nameField.getText());
                //BPNetworkHandler.INSTANCE.sendToServer(new MessageUpdateTextfield(circuitDatabase, 1));
            } else {
                super.keyPressed(par1, par2, par3);
            }
        }
        return true;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float par1, int par2, int par3) {

        super.drawGuiContainerBackgroundLayer(matrixStack, par1, par2, par3);

        this.drawString(matrixStack, guiLeft + 95, guiTop + 25, I18n.format("gui.bluepower:circuitDatabase.name"), false);
        //nameField.drawTextBox();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(copyTabTexture);

        int processPercentage = circuitDatabase.curCopyProgress * 22 / TileCircuitDatabase.UPLOAD_AND_COPY_TIME;
        if (processPercentage > 0)
            blit(matrixStack, guiLeft + 77, guiTop + 64, 176, 0, processPercentage, 15);

        processPercentage = circuitDatabase.curUploadProgress * 22 / TileCircuitDatabase.UPLOAD_AND_COPY_TIME;
        if (processPercentage > 0)
            blit(matrixStack, guiLeft + 57, guiTop + 57 - processPercentage, 176, 37 - processPercentage, 15, processPercentage);

    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        if (widget.getID() == 1) {
            //circuitDatabase.clientCurrentTab = ((BaseWidget) widget).value;
        }
        //BPNetworkHandler.INSTANCE.sendToServer(new MessageGuiUpdate(circuitDatabase, widget.getID(), ((BaseWidget) widget).value));
    }

    @Override
    public void tick() {

        super.tick();
        /*if (!nameField.isFocused()) {
            nameField.setText(circuitDatabase.nameTextField);
        }
        boolean nameDuplicatePrivate = false;
        boolean nameDuplicateServer = false;
        if (!circuitDatabase.copyInventory.getStackInSlot(0).isEmpty()) {
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
        shareOptionTab.enabledTabs[2] = !nameDuplicateServer && !Minecraft.getInstance().isSingleplayer();

        copyButton.enabled = !circuitDatabase.copyInventory.getStackInSlot(0).isEmpty()
                && !circuitDatabase.copyInventory.getStackInSlot(1).isEmpty()
                && circuitDatabase.copy(Minecraft.getInstance().player, circuitDatabase.copyInventory.getStackInSlot(0),
                        circuitDatabase.copyInventory.getStackInSlot(1), true);*/
    }

    @Override
    protected boolean isInfoStatLeftSided() {

        return false;
    }
}
