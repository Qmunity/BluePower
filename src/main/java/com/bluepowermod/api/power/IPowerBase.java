package com.bluepowermod.api.power;

import com.bluepowermod.api.part.BPPart;
import net.minecraft.tileentity.TileEntity;

/**
 * @author Koen Beckers (K4Unl)
 */
public interface IPowerBase {

    public void init(TileEntity _target);

    public void init(BPPart _target);

    public int getAmpStored();

    public void removeEnergy(int amp);

    public void addEnergy(int amp);
}
