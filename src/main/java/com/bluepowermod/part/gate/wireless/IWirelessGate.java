package com.bluepowermod.part.gate.wireless;

import uk.co.qmunity.lib.vec.IWorldLocation;

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wireless.IWirelessDevice;

public interface IWirelessGate extends IWirelessDevice, IFace, IWorldLocation {

    public WirelessMode getMode();

    public void setMode(WirelessMode mode);

    public boolean isBundled();

    @Override
    public Frequency getFrequency();

}
