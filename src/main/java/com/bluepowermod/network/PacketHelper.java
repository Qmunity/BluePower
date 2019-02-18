package com.bluepowermod.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PacketHelper {

    public static void writeBytes(DataOutput out, byte[] bytes) throws IOException {

        out.writeInt(bytes.length);
        out.write(bytes);
    }

    public static byte[] readBytes(DataInput in) throws IOException {

        int amt = in.readInt();
        byte[] bytes = new byte[amt];
        in.readFully(bytes, 0, amt);
        return bytes;
    }

    public static void writeNBT(DataOutput out, NBTTagCompound tag) throws IOException {

        ByteBuf buf = Unpooled.buffer();
        ByteBufUtils.writeTag(buf, tag);
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        writeBytes(out, bytes);
    }

    public static NBTTagCompound readNBT(DataInput in) throws IOException {

        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(readBytes(in));
        NBTTagCompound t = ByteBufUtils.readTag(buf);
        return t;
    }
}
