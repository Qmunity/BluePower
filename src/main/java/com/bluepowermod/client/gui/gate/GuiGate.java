/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui.gate;

import com.bluepowermod.client.gui.GuiScreenBase;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.message.MessageGuiUpdate;
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
