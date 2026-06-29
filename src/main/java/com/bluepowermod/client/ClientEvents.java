package com.bluepowermod.client;

import com.bluepowermod.BluePower;
import com.bluepowermod.client.render.placement_preview.PlacementPreviewReloadListener;
import com.bluepowermod.reference.Refs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = Dist.CLIENT, modid = Refs.MODID, bus = Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void onClientReload(RegisterClientReloadListenersEvent event){
        event.registerReloadListener(PlacementPreviewReloadListener.INSTANCE);
    }
}
