package com.bluepowermod.part;

import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.vec.Vec3i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BPPartFaceRotate extends BPPartFace {

    private int rotation = 0;

    public int getRotation() {

        return rotation;
    }

    public void setRotation(int rotation) {

        int old = this.rotation;
        this.rotation = rotation;
        if (rotation != old)
            sendUpdatePacket();
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

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop,
            EntityPlayer player) {

        int rot = PartRotationHelper.getPlacementRotation(mop);

        if (face == ForgeDirection.UP || face == ForgeDirection.NORTH || face == ForgeDirection.WEST)
            rot += 2;
        if (face == ForgeDirection.DOWN || face == ForgeDirection.EAST)
            if (rot == 0 || rot == 2)
                rot += 2;
        if (face == ForgeDirection.SOUTH)
            if (rot == 1 || rot == 3)
                rot += 2;

        return new PartPlacementFaceRotate(face.getOpposite(), rot % 4);
    }

}
