package net.quetzi.bluepower.api.part.redstone;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;

public class RedstoneNetworkTickHandler {
    
    @SubscribeEvent
    public void onTick(TickEvent event) {
    
        if (event.phase == Phase.START && event.type == Type.WORLD) {
            for(RedstoneNetwork net : RedstoneNetwork.networks){
                net.tick();
            }
        }
    }
    
}
