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

import com.bluepowermod.api.misc.IFace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartCustomPlacement;
import uk.co.qmunity.lib.part.IPartFace;
import uk.co.qmunity.lib.part.IPartPlacement;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class BPPartFace extends BPPart implements IPartFace, IFace, IPartCustomPlacement {

    private EnumFacing face = null;

    @Override
    public EnumFacing getFace() {

        return face;
    }

    public boolean canStay() {

        return getWorld().isSideSolid(getPos().offset(getFace()),
                getFace().getOpposite());
    }

    @Override
    public void setFace(EnumFacing face) {

        this.face = face;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        tag.setInteger("face", face.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        face = EnumFacing.getFront(tag.getInteger("face"));
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);

        buffer.writeInt(face.ordinal());
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);

        face = EnumFacing.getFront(buffer.readInt());
    }

    @Override
    public IPartPlacement getPlacement(IPart part, World world, BlockPos location, EnumFacing face, RayTraceResult mop,
                                       EntityPlayer player) {

        return new PartPlacementFace(face.getOpposite());
    }

    @Override
    public void onNeighborBlockChange() {

        if (getParent() == null || getWorld() == null || getWorld().isRemote)
            return;

        if (!canStay()) {
            if (breakAndDrop(null, null)) {
                getParent().removePart(this);
            }
            return;
        }

        super.onNeighborBlockChange();
    }

}
