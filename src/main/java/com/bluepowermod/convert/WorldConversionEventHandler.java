package com.bluepowermod.convert;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;

public class WorldConversionEventHandler {

    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public void onWorldPreLoad(WorldEvent.Load event) {

        new WorldConverter(DimensionManager.getCurrentSaveRootDirectory()).convertIfNeeded();
    }

}
