package net.quetzi.bluepower.client.renderers;

import net.minecraftforge.client.MinecraftForgeClient;
import net.quetzi.bluepower.init.BPItems;

public class Renderers {
    
    public static void init() {
    
        MinecraftForgeClient.registerItemRenderer(BPItems.multipart,
                new RenderItemBPPart());
    }
}
