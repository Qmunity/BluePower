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
import uk.co.qmunity.lib.part.IPartCustomPlacement;
import uk.co.qmunity.lib.part.IPartFace;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.IFace;

public abstract class BPPartFace extends BPPart implements IPartFace, IFace, IPartCustomPlacement {

    private ForgeDirection face = ForgeDirection.UNKNOWN;

    @Override
    public ForgeDirection getFace() {

        return face;
    }

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
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);

        buffer.writeInt(face.ordinal());
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);

        face = ForgeDirection.getOrientation(buffer.readInt());
    }

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop,
            EntityPlayer player) {

        return new PartPlacementFace(face.getOpposite());
    }

    @Override
    public void onNeighborBlockChange() {

        if (getParent() == null || getWorld() == null || getWorld().isRemote)
            return;

        super.onNeighborBlockChange();

        if (getParent() == null || getWorld() == null || getWorld().isRemote)
            return;

        if (!canStay())
            breakAndDrop(false);
    }

}
