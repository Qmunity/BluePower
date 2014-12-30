package com.bluepowermod.convert;

import net.minecraftforge.event.world.WorldEvent;
import uk.co.qmunity.lib.world.SaveHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class WorldConversionEventHandler {

    @SubscribeEvent
    public void onWorldPreLoad(WorldEvent.Load event) {

        new WorldConverter(SaveHelper.getSaveLocation(event.world)).convertIfNeeded();
    }

}
