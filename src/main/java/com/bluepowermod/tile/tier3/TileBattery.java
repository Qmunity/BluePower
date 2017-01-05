package com.bluepowermod.tile.tier3;

import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author MoreThanHidden
 */

public class TileBattery extends TileMachineBase {
    public int energy;

    public TileBattery(){
        this.energy = 0;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("energy", energy);
        return compound;

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        energy = compound.getInteger("energy");
    }

}
