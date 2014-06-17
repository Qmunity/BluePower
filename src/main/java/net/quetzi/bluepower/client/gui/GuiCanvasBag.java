package net.quetzi.bluepower.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.quetzi.bluepower.containers.ContainerCanvasBag;
import net.quetzi.bluepower.references.Refs;

public class GuiCanvasBag extends GuiBase {
    
    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/GUI/canvas_bag.png");
    
    public GuiCanvasBag(IInventory playerInventory, IInventory canvasBagInventory) {
    
        super(new ContainerCanvasBag(playerInventory, canvasBagInventory), resLoc);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        
        this.fontRendererObj.drawString(I18n.format("item.canvas_bag.name", new Object[] {}), 8, 6, 4210752);
    }
}
