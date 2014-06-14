package net.quetzi.bluepower.client.renderers;

import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class IconSupplier {
    
    public static IIcon pneumaticTubeSideIcon;
    public static IIcon pneumaticTubeNodeIcon;
    
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
    
        if (event.map.getTextureType() == 0) {
            pneumaticTubeSideIcon = event.map.registerIcon(Refs.MODID + ":Tubes/pneumatic_tube_side");
            pneumaticTubeNodeIcon = event.map.registerIcon(Refs.MODID + ":Tubes/tube_end");
        }
    }
}
