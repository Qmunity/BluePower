package com.bluepowermod.part;

import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartCustomPlacement;
import uk.co.qmunity.lib.part.IPartFace;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.PartPlacementFace;
import uk.co.qmunity.lib.vec.Vec3i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.misc.IFace;

public abstract class BPPartFace extends BPPart implements IPartFace, IFace, IPartCustomPlacement {

    private ForgeDirection face = ForgeDirection.UNKNOWN;

    @Override
    public ForgeDirection getFace() {

        return face;
    }

    @Override
    public boolean canStay() {

        return getWorld().isSideSolid(getX() + getFace().offsetX, getY() + getFace().offsetY, getZ() + getFace().offsetZ,
                getFace().getOpposite());
    }

    @Override
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

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop,
            EntityPlayer player) {

        return new PartPlacementFace(face.getOpposite());
    }

}
