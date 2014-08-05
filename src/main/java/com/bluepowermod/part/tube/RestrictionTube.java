package com.bluepowermod.part.tube;

import net.minecraft.util.IIcon;

import com.bluepowermod.client.renderers.IconSupplier;

public class RestrictionTube extends PneumaticTube {
    
    @Override
    public String getType() {
    
        return "restrictionTube";
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "restrictionTube";
    }
    
    @Override
    public int getWeigth() {
    
        return 5000;
    }
    
    @Override
    protected IIcon getSideIcon() {
    
        return IconSupplier.restrictionTubeSide;
    }
    
    @Override
    protected IIcon getNodeIcon() {
    
        return IconSupplier.restrictionTubeNode;
    }
}
