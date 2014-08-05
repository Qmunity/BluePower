package com.bluepowermod.client.gui.gate;

import com.bluepowermod.client.gui.GuiScreenBase;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageGuiUpdate;
import com.bluepowermod.part.gate.GateBase;

/**
 * 
 * @author MineMaarten
 */

public abstract class GuiGate extends GuiScreenBase {
    
    private final GateBase gate;
    
    public GuiGate(GateBase gate, int xSize, int ySize) {
    
        super(xSize, ySize);
        this.gate = gate;
    }
    
    protected void sendToServer(int id, int value) {
    
        NetworkHandler.sendToServer(new MessageGuiUpdate(gate, id, value));
    }
    
    @Override
    public boolean doesGuiPauseGame() {
    
        return false;
    }
}
