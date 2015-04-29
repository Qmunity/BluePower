/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.vec.Vec3i;

public abstract class BPPartFaceRotate extends BPPartFace {

    private int rotation = 0;

    public int getRotation() {

        return rotation % 4;
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
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);
        buffer.writeInt(rotation);
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);
        rotation = buffer.readInt();
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
