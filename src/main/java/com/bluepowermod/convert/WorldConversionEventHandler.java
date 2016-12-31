package com.bluepowermod.convert;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldConversionEventHandler {

    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public void onWorldPreLoad(WorldEvent.Load event) {

        new WorldConverter(DimensionManager.getCurrentSaveRootDirectory()).convertIfNeeded();
    }

}
