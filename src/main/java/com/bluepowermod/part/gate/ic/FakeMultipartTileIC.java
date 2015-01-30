package com.bluepowermod.part.gate.ic;

import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.tile.TileMultipart;

public class FakeMultipartTileIC extends TileMultipart {

    private GateIntegratedCircuit ic;

    public FakeMultipartTileIC(GateIntegratedCircuit ic) {

        this.ic = ic;
    }

    public GateIntegratedCircuit getIC() {

        return ic;
    }

    @Override
    public void sendUpdatePacket(IPart part, int channel) {

        ic.sendUpdatePacket(part, channel);
    }

}
