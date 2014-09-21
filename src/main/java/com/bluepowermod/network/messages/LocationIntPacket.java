/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

/**
 * 
 * @author MineMaarten
 */

public abstract class LocationIntPacket<REQ extends IMessage> extends AbstractPacket<REQ> {

    protected int x, y, z;

    public LocationIntPacket() {

    }

    public LocationIntPacket(int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    public NetworkRegistry.TargetPoint getTargetPoint(World world) {

        return getTargetPoint(world, 64);
    }

    public NetworkRegistry.TargetPoint getTargetPoint(World world, double updateDistance) {

        return new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, updateDistance);
    }

    protected Block getBlock(World world) {

        return world.getBlock(x, y, z);
    }

    protected TileEntity getTileEntity(World world) {
        return world.getTileEntity(x, y, z);
    }
}
