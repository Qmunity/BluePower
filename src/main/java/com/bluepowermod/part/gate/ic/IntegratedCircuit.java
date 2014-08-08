package com.bluepowermod.part.gate.ic;

import java.util.ArrayList;
import java.util.List;

import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.part.gate.GateBase;

public class IntegratedCircuit extends GateBase {
    
    private final List<GateBase> gates = new ArrayList<GateBase>();
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
    }
    
    @Override
    public String getGateID() {
    
        return "integratedCircuit";
    }
    
    @Override
    protected void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
        // TODO Auto-generated method stub
        
    }
    
}
