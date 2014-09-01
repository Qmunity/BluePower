/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

/**
 * 
 * @author MineMaarten
 */

public abstract class LocationDoublePacket<REQ extends IMessage> extends AbstractPacket<REQ> {
    
    protected double x, y, z;
    
    public LocationDoublePacket() {
    
    }
    
    public LocationDoublePacket(double x, double y, double z) {
    
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
    
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
    
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }
    
    public NetworkRegistry.TargetPoint getTargetPoint(World world) {
    
        return getTargetPoint(world, 64);
    }
    
    public NetworkRegistry.TargetPoint getTargetPoint(World world, double updateDistance) {
    
        return new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, updateDistance);
    }
}
