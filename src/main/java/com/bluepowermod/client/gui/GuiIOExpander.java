package com.bluepowermod.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.bluepowermod.containers.ContainerIOExpander;
import com.bluepowermod.tileentities.tier3.TileIOExpander;
import com.bluepowermod.util.Refs;

public class GuiIOExpander extends GuiBase {
    
    private final TileIOExpander          ioExpander;
    
    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID + ":textures/gui/ioexpandergui.png");
    
    public GuiIOExpander(InventoryPlayer invPlayer, TileIOExpander ioExpander) {
    
        super(new ContainerIOExpander(invPlayer, ioExpander), resLoc);
        this.ioExpander = ioExpander;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    
    }
}
