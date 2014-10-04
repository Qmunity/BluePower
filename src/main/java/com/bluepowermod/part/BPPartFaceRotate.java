package com.bluepowermod.part;

import net.minecraft.nbt.NBTTagCompound;

public abstract class BPPartFaceRotate extends BPPartFace {

    private int rotation = 0;

    public int getRotation() {

        return rotation;
    }

    public void setRotation(int rotation) {

        this.rotation = rotation;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setInteger("rotation", rotation);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        rotation = tag.getInteger("rotation");
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setInteger("rotation", rotation);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        rotation = tag.getInteger("rotation");
    }

}
