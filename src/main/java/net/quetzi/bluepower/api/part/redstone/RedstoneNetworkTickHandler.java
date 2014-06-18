package net.quetzi.bluepower.api.part.redstone;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;

public class RedstoneNetworkTickHandler {
    
    private List<RedstoneNetwork> despawn = new ArrayList<RedstoneNetwork>();
    
    @SubscribeEvent
    public void onTick(TickEvent event) {
    
        if (event.phase == Phase.START && event.type == Type.WORLD) {
            for (RedstoneNetwork net : RedstoneNetwork.networks) {
                if (net.shouldDespawn) {
                    despawn.add(net);
                    continue;
                }
                net.tick();
            }
            for (RedstoneNetwork net : despawn) {
                net.despawn();
            }
            despawn.clear();
        }
    }
    
}
