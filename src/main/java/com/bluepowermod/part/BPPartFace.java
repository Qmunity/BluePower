package com.bluepowermod.part;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.part.IPartFace;

public abstract class BPPartFace extends BPPart implements IPartFace {

    private ForgeDirection face = null;

    @Override
    public ForgeDirection getFace() {

        return face;
    }

    @Override
    public boolean canStay() {

        return getWorld().isSideSolid(getX() + getFace().offsetX, getY() + getFace().offsetY, getZ() + getFace().offsetZ, getFace().getOpposite());
    }

    public void setFace(ForgeDirection face) {

        this.face = face;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        tag.setInteger("face", face.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        face = ForgeDirection.getOrientation(tag.getInteger("face"));
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        tag.setInteger("face", face.ordinal());
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        face = ForgeDirection.getOrientation(tag.getInteger("face"));
    }

}
