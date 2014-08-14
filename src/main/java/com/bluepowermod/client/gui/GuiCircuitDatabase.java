package com.bluepowermod.client.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.client.gui.widget.BaseWidget;
import com.bluepowermod.client.gui.widget.IGuiWidget;
import com.bluepowermod.client.gui.widget.WidgetSidewaysTab;
import com.bluepowermod.client.gui.widget.WidgetTab;
import com.bluepowermod.containers.ContainerCircuitDatabaseMain;
import com.bluepowermod.containers.ContainerCircuitDatabaseSharing;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageGuiUpdate;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.tier3.TileCircuitDatabase;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCircuitDatabase extends GuiCircuitTable {
    
    private static final ResourceLocation copyTabTexture = new ResourceLocation(Refs.MODID, "textures/gui/circuit_database.png");
    private final TileCircuitDatabase     circuitDatabase;
    
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
                            curTip.add("gui.circuitDatabase.tab.cancel");
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
            widget.value = circuitDatabase.selectedShareOption;
            addWidget(widget);
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        
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
    public void actionPerformed(IGuiWidget widget) {
    
        if (widget.getID() == 1) circuitDatabase.clientCurrentTab = ((BaseWidget) widget).value;
        NetworkHandler.sendToServer(new MessageGuiUpdate(circuitDatabase, widget.getID(), ((BaseWidget) widget).value));
    }
}
