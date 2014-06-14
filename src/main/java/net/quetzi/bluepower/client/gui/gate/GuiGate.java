package net.quetzi.bluepower.client.gui.gate;

import net.quetzi.bluepower.client.gui.GuiScreenBase;
import net.quetzi.bluepower.network.NetworkHandler;
import net.quetzi.bluepower.network.messages.MessageGuiUpdate;
import net.quetzi.bluepower.part.gate.GateBase;

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
