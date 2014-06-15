package net.quetzi.bluepower.client.renderers;

import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class IconSupplier {
    
    public static IIcon pneumaticTubeSide;
    public static IIcon pneumaticTubeNode;
    public static IIcon pneumaticTubeColorNode;
    public static IIcon pneumaticTubeColorSide;
    public static IIcon cagedLampFootSide;
    public static IIcon cagedLampFootTop;
    public static IIcon cagedLampCageSide;
    public static IIcon cagedLampCageTop;
    public static IIcon cagedLampLampActive;
    public static IIcon cagedLampLampInactive;
    
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
    
        if (event.map.getTextureType() == 0) {
            pneumaticTubeSide = event.map.registerIcon(Refs.MODID + ":tubes/pneumatic_tube_side");
            pneumaticTubeNode = event.map.registerIcon(Refs.MODID + ":tubes/tube_end");
            pneumaticTubeColorSide = event.map.registerIcon(Refs.MODID + ":tubes/tube_color_side");
            pneumaticTubeColorNode = event.map.registerIcon(Refs.MODID + ":/tubes/tube_color_end");
            
            cagedLampFootSide = event.map.registerIcon(Refs.MODID + ":lamps/cage_foot_side");
            cagedLampFootTop = event.map.registerIcon(Refs.MODID + ":lamps/cage_foot_top");
            cagedLampCageSide = event.map.registerIcon(Refs.MODID + ":lamps/cage_side");
            cagedLampCageTop = event.map.registerIcon(Refs.MODID + ":lamps/cage_top");
            
            cagedLampLampActive = event.map.registerIcon(Refs.MODID + ":lamps/cage_lamp_white_on");
            cagedLampLampInactive = event.map.registerIcon(Refs.MODID + ":lamps/cage_lamp_white_off");
        }
    }
}
