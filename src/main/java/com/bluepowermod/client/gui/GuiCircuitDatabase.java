package com.bluepowermod.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.client.gui.widget.BaseWidget;
import com.bluepowermod.client.gui.widget.IGuiWidget;
import com.bluepowermod.client.gui.widget.WidgetMode;
import com.bluepowermod.client.gui.widget.WidgetSidewaysTab;
import com.bluepowermod.client.gui.widget.WidgetTab;
import com.bluepowermod.containers.ContainerCircuitDatabaseMain;
import com.bluepowermod.containers.ContainerCircuitDatabaseSharing;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageCircuitDatabaseTemplate;
import com.bluepowermod.network.messages.MessageGuiUpdate;
import com.bluepowermod.network.messages.MessageUpdateTextfield;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.tier3.TileCircuitDatabase;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCircuitDatabase extends GuiCircuitTable {
    
    private static final ResourceLocation copyTabTexture = new ResourceLocation(Refs.MODID, "textures/gui/circuit_database.png");
    private final TileCircuitDatabase     circuitDatabase;
    
    private GuiTextField                  nameField;
    
    public GuiCircuitDatabase(InventoryPlayer invPlayer, TileCircuitDatabase circuitDatabase, GuiIDs guiId) {
    
        super(circuitDatabase, guiId == GuiIDs.CIRCUITDATABASE_MAIN_ID ? new ContainerCircuitDatabaseMain(invPlayer, circuitDatabase) : new ContainerCircuitDatabaseSharing(invPlayer, circuitDatabase), guiId == GuiIDs.CIRCUITDATABASE_MAIN_ID ? copyTabTexture : guiTexture);
        this.circuitDatabase = circuitDatabase;
    }
    
    @Override
    public void initGui() {
    
        super.initGui();
        
        BaseWidget widget = new WidgetTab(1, guiLeft - 32, guiTop + 10, 33, 35, 198, 3, Refs.MODID + ":textures/gui/circuit_database.png") {
            
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
                        break;
                }
            }
        };
        widget.value = circuitDatabase.clientCurrentTab;
        addWidget(widget);
        
        if (circuitDatabase.clientCurrentTab == 0) {
            widget = new WidgetSidewaysTab(2, guiLeft + 44, guiTop + 18, 14, 14, 234, 3, Refs.MODID + ":textures/gui/circuit_database.png") {
                
                @Override
                protected void addTooltip(int curHoveredTab, List<String> curTip, boolean shiftPressed) {
                
                    switch (curHoveredTab) {
                        case 0:
                            curTip.add("gui.circuitDatabase.action.cancel");
                            break;
                        case 1:
                            curTip.add("gui.circuitDatabase.action.savePrivate");
                            break;
                        case 2:
                            curTip.add("gui.circuitDatabase.action.saveServer");
                            break;
                    }
                }
            };
            widget.value = circuitDatabase.selectedShareOption;
            addWidget(widget);
            
            widget = new WidgetMode(3, guiLeft + 80, guiTop + 48, 176, 37, 1, Refs.MODID + ":textures/gui/circuit_database.png") {
                
                @Override
                public void addTooltip(int x, int y, List<String> curTip, boolean shiftPressed) {
                
                    curTip.add("gui.circuitDatabase.action.copy");
                }
            };
            addWidget(widget);
            
            nameField = new GuiTextField(fontRendererObj, guiLeft + 95, guiTop + 35, 70, fontRendererObj.FONT_HEIGHT);
            nameField.setEnableBackgroundDrawing(true);
            nameField.setVisible(true);
            nameField.setTextColor(16777215);
            nameField.setText(circuitDatabase.getText(1));
        }
        
    }
    
    @Override
    protected void mouseClicked(int x, int y, int button) {
    
        super.mouseClicked(x, y, button);
        if (nameField != null) nameField.mouseClicked(x, y, button);
    }
    
    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char par1, int par2) {
    
        if (par2 == 1 || nameField == null)//esc
        {
            super.keyTyped(par1, par2);
        } else {
            if (nameField.textboxKeyTyped(par1, par2)) {
                circuitDatabase.setText(1, nameField.getText());
                NetworkHandler.sendToServer(new MessageUpdateTextfield(circuitDatabase, 1));
            } else {
                super.keyTyped(par1, par2);
            }
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        
        if (nameField != null) {
            this.drawString(guiLeft + 95, guiTop + 25, I18n.format("gui.circuitDatabase.name"), false);
            nameField.drawTextBox();
            
            if (circuitDatabase.copyInventory.getStackInSlot(0) != null) {
                if (shouldDisplayRed(circuitDatabase.copyInventory.getStackInSlot(0))) drawRect(guiLeft + 57, guiTop + 64, guiLeft + 73, guiTop + 80, 0x55FF0000);
            }
        }
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(copyTabTexture);
        
        int processPercentage = circuitDatabase.curCopyProgress * 22 / TileCircuitDatabase.UPLOAD_AND_COPY_TIME;
        if (processPercentage > 0) drawTexturedModalRect(guiLeft + 77, guiTop + 64, 176, 0, processPercentage, 15);
        
        processPercentage = circuitDatabase.curUploadProgress * 22 / TileCircuitDatabase.UPLOAD_AND_COPY_TIME;
        if (processPercentage > 0) drawTexturedModalRect(guiLeft + 57, guiTop + 57 - processPercentage, 176, 37 - processPercentage, 15, processPercentage);
        
    }
    
    @Override
    protected boolean isTextfieldEnabled() {
    
        return circuitDatabase.clientCurrentTab == 1 || circuitDatabase.clientCurrentTab == 2;
    }
    
    @Override
    protected void handleMouseClick(Slot slot, int p_146984_2_, int p_146984_3_, int p_146984_4_) {
    
        if (circuitDatabase.clientCurrentTab == 1 && slot != null && slot.getHasStack() && slot.inventory == circuitDatabase.circuitInventory) {//when in the private database
            circuitDatabase.clientCurrentTab = 0;//Navigate to the copy & share tab.
            NetworkHandler.sendToServer(new MessageCircuitDatabaseTemplate(circuitDatabase, slot.getStack()));
        } else {
            super.handleMouseClick(slot, p_146984_2_, p_146984_3_, p_146984_4_);
        }
    }
    
    @Override
    protected boolean shouldDisplayRed(ItemStack stack) {
    
        if ((circuitDatabase.clientCurrentTab == 1 || circuitDatabase.clientCurrentTab == 2) && circuitDatabase.copyInventory.getStackInSlot(1) != null) {
            return !circuitDatabase.copy(Minecraft.getMinecraft().thePlayer, stack, circuitDatabase.copyInventory.getStackInSlot(1), true);
        } else {
            return false;
        }
    }
    
    @Override
    public void actionPerformed(IGuiWidget widget) {
    
        if (widget.getID() == 1) circuitDatabase.clientCurrentTab = ((BaseWidget) widget).value;
        NetworkHandler.sendToServer(new MessageGuiUpdate(circuitDatabase, widget.getID(), ((BaseWidget) widget).value));
    }
    
    @Override
    public void updateScreen() {
    
        super.updateScreen();
        if (nameField != null) {
            if (!nameField.isFocused()) {
                nameField.setText(circuitDatabase.nameTextField);
            }
            boolean nameDuplicate = false;
            for (ItemStack stack : circuitDatabase.privateDatabase.loadItemStacks()) {
                if (stack.getDisplayName().equals(circuitDatabase.copyInventory.getStackInSlot(0).getDisplayName())) {
                    nameDuplicate = true;
                    break;
                }
            }
            nameField.setTextColor(nameDuplicate ? 0xFFFF0000 : 14737632);
        }
    }
}
