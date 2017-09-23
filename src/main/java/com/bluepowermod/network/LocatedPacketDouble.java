package com.bluepowermod.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class LocatedPacketDouble<T extends LocatedPacket<T>> extends Packet<T> {

    protected BlockPos pos;

    public LocatedPacketDouble(BlockPos location) {
        this.pos = location;
    }

    public LocatedPacketDouble(double x, double y, double z) {

        this.pos = new BlockPos(x,y,z);
    }

    public LocatedPacketDouble() {

    }

    @Override
    public void read(DataInput buffer) throws IOException {

        pos = new BlockPos(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        buffer.writeDouble(pos.getX());
        buffer.writeDouble(pos.getY());
        buffer.writeDouble(pos.getZ());
    }

    public NetworkRegistry.TargetPoint getTargetPoint(World world) {

        return getTargetPoint(world, 64);
    }

    public NetworkRegistry.TargetPoint getTargetPoint(World world, double updateDistance) {

        return new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), updateDistance);
    }

}
