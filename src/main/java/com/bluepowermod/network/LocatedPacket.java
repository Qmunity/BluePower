package com.bluepowermod.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class LocatedPacket<T extends LocatedPacket<T>> extends Packet<T>{

    protected BlockPos pos;

    public LocatedPacket(int x, int y, int z){

        this.pos = new BlockPos(x,y,z);
    }

    public LocatedPacket(BlockPos pos) {

        this.pos = pos;
    }

    public LocatedPacket(){

    }

    @Override
    public void read(DataInput buffer) throws IOException{

        pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void write(DataOutput buffer) throws IOException{

        buffer.writeInt(pos.getX());
        buffer.writeInt(pos.getY());
        buffer.writeInt(pos.getZ());
    }

    protected TileEntity getTileEntity(World world){
        return world.getTileEntity(new BlockPos(pos));
    }

}
