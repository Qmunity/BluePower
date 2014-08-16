package com.bluepowermod.client.gui;

import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.bluepowermod.client.gui.widget.WidgetMode;
import com.bluepowermod.containers.ContainerRetriever;
import com.bluepowermod.tileentities.tier2.TileRetriever;
import com.bluepowermod.util.Refs;

/**
 * @author MineMaarten
 */
public class GuiRetriever extends GuiFilter {
    
    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/retriever.png");
    
    public GuiRetriever(InventoryPlayer invPlayer, TileRetriever retriever) {
    
        super(new ContainerRetriever(invPlayer, retriever), retriever, resLoc);
    }
    
    @Override
    public void initGui() {
    
        super.initGui();
        WidgetMode colorWidget = new WidgetMode(1, guiLeft + 117, guiTop + 20, 202, 2, Refs.MODID + ":textures/gui/retriever.png") {
            
            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {
            
                curTip.add("gui.mode");
                curTip.add("gui.retriever.mode." + (value == 0 ? "sequential" : "any"));
                if (shiftPressed) {
                    curTip.add("gui.retriever.mode." + (value == 0 ? "sequential" : "any") + ".info");
                } else {
                    curTip.add("gui.sneakForInfo");
                }
            }
        };
        colorWidget.value = ((TileRetriever) filter).mode;
        addWidget(colorWidget);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
        if (((TileRetriever) filter).mode == 0) {
            int curSlot = ((TileRetriever) filter).slotIndex;
            Gui.func_146110_a(guiLeft + 60 + curSlot % 3 * 18, guiTop + 15 + 18 * (curSlot / 3), 182, 0, 20, 20, 256, 256);
        }
    }
    
}
