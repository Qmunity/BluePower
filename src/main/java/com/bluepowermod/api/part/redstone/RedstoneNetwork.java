package com.bluepowermod.api.part.redstone;

import java.util.ArrayList;
import java.util.List;

public final class RedstoneNetwork {
    
    public static final List<RedstoneNetwork> networks = new ArrayList<RedstoneNetwork>();
    
    public static RedstoneNetwork getNetworkForPart(IBPRedstoneConductor c) {
    
        if (c == null) return null;
        
        return null;
    }
    
    private List<IBPRedstoneConductor> parts         = new ArrayList<IBPRedstoneConductor>();
    
    protected boolean                  shouldDespawn = false;
    
    private RedstoneNetwork(IBPRedstoneConductor c) {
    
        parts.add(c);
    }
    
    public void tick() {
    
    }
    
    public void markForDespawn() {
    
        shouldDespawn = true;
    }
    
    public void despawn() {
    
        RedstoneNetwork.networks.remove(this);
        
        for (IBPRedstoneConductor c : parts)
            if (c.getNetwork() == this) c = null;
        
        for (IBPRedstoneConductor c : parts)
            if (c.getNetwork() == null) c.setNetwork(getNetworkForPart(c));
    }
    
    public void merge(RedstoneNetwork net) {
    
        for (IBPRedstoneConductor p : net.parts)
            parts.add(p);
        
        net.markForDespawn();
    }
    
    public void split(IBPRedstonePart part) {
    
    }
    
}
