package com.bluepowermod.convert;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class WorldConversionEventHandler {

    @SubscribeEvent
    public void onWorldPreLoad(WorldEvent.Load event) {

        new WorldConverter(DimensionManager.getCurrentSaveRootDirectory()).convertIfNeeded();
    }

}
