package com.bluepowermod.tileentities.tier2;

import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.compat.fmp.IMultipartCompat;
import com.bluepowermod.part.tube.PneumaticTube;
import com.bluepowermod.references.Dependencies;
import com.bluepowermod.tileentities.tier1.TileFilter;

public class TileRetriever extends TileFilter {
    
    @Override
    protected void pullItem() {
    
        if (isBufferEmpty()) {
            TileEntity extractingInventory = getTileCache()[getFacingDirection().ordinal()].getTileEntity();
            IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
            PneumaticTube tube = compat.getBPPart(extractingInventory, PneumaticTube.class);
            if (tube != null) {
                tube.getLogic().retrieveStack(this, null);
            } else {
                super.pullItem();
            }
        }
    }
}
