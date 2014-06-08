package net.quetzi.bluepower.part.gate;

import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.part.IBPFacePart;

public class GateBase extends BPPart implements IBPFacePart {
    
    @Override
    public String getType() {
    
        return "gatebase";
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "gate.gatebase";
    }
    
    @Override
    public int getFace() {
    
        return 0;
    }
    
    @Override
    public void onNeigbourUpdate() {
        System.out.println("Neighbour update!");
        super.onNeigbourUpdate();
    }
    
}
