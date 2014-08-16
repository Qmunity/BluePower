package com.bluepowermod.events;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

import com.bluepowermod.ClientProxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BPEventHandlerClient {

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {

        ClientProxy.mouseX = event.mouseX;
        ClientProxy.mouseY = event.mouseY;
    }

}
