package net.quetzi.bluepower.client.renderers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.part.PartRegistry;

public class RenderItemBPPart implements IItemRenderer {
    
    private List<BPPart> parts = new ArrayList<BPPart>();
    
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
    
        return true;
    }
    
    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
            ItemRendererHelper helper) {
    
        return true;
    }
    
    @SuppressWarnings("incomplete-switch")
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        
        BPPart part = null;
        for(BPPart p : parts)
            if(p.getType().equals(PartRegistry.getPartIdFromItem(item))){
                part = p;
                break;
            }
        if(part == null){
            part = PartRegistry.createPartFromItem(item);
            parts.add(part);
        }
        
        GL11.glPushMatrix();
        {
            switch(type){
                case ENTITY:
                    GL11.glTranslated(-0.5, -0.5, -0.5);
                    break;
                case EQUIPPED:
                    break;
                case EQUIPPED_FIRST_PERSON:
                    break;
                case INVENTORY:
                    GL11.glTranslated(0, -0.15, 0);
                    break;
            }
            part.renderItem(type, item, data);
        }
        GL11.glPopMatrix();
    }
    
}
