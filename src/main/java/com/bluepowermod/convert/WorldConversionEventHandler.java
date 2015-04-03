package com.bluepowermod.convert;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WorldConversionEventHandler {

    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public void onWorldPreLoad(WorldEvent.Load event) {

        new WorldConverter(DimensionManager.getCurrentSaveRootDirectory()).convertIfNeeded();
    }

}
