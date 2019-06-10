package com.bluepowermod.network.annotation;

import com.bluepowermod.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import com.bluepowermod.network.annotation.SyncedField.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author MineMaarten
 */

public class PacketCUpdateGui extends Packet<PacketCUpdateGui> {
    private int syncId;
    private Object value;
    private byte type;

    public PacketCUpdateGui(){}

    public PacketCUpdateGui(int syncId, SyncedField syncField){
        this.syncId = syncId;
        value = syncField.getValue();
        type = getType(syncField);
    }

    public static byte getType(SyncedField syncedField){
        if(syncedField instanceof SyncedInt) return 0;
        else if(syncedField instanceof SyncedFloat) return 1;
        else if(syncedField instanceof SyncedDouble) return 2;
        else if(syncedField instanceof SyncedBoolean) return 3;
        else if(syncedField instanceof SyncedString) return 4;
        else if(syncedField instanceof SyncedEnum) return 5;
        else if(syncedField instanceof SyncedItemStack) return 6;
        else if(syncedField instanceof SyncedFluidTank) return 7;
        else {
            throw new IllegalArgumentException("Invalid sync type! " + syncedField);
        }
    }

    public static Object readField(ByteBuf buf, int type){
        switch(type){
            case 0:
                return buf.readInt();
            case 1:
                return buf.readFloat();
            case 2:
                return buf.readDouble();
            case 3:
                return buf.readBoolean();
            case 4:
                return ByteBufUtils.readUTF8String(buf);
            case 5:
                return buf.readByte();
            case 6:
                return ByteBufUtils.readItemStack(buf);
            case 7:
                if(!buf.readBoolean()) return null;
                return FluidStack.loadFluidStackFromNBT(ByteBufUtils.readTag(buf));
        }
        throw new IllegalArgumentException("Invalid sync type! " + type);
    }

    public static void writeField(ByteBuf buf, Object value, int type){
        switch(type){
            case 0:
                buf.writeInt((Integer)value);
                break;
            case 1:
                buf.writeFloat((Float)value);
                break;
            case 2:
                buf.writeDouble((Double)value);
                break;
            case 3:
                buf.writeBoolean((Boolean)value);
                break;
            case 4:
                ByteBufUtils.writeUTF8String(buf, (String)value);
                break;
            case 5:
                buf.writeByte((Byte)value);
                break;
            case 6:
                ByteBufUtils.writeItemStack(buf, (ItemStack)value);
                break;
            case 7:
                buf.writeBoolean(value != null);
                if(value != null) {
                    FluidStack stack = (FluidStack)value;
                    stack.writeToNBT(ByteBufUtils.readTag(buf));
                }
                break;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf){
        syncId = buf.readInt();
        type = buf.readByte();
        value = readField(buf, type);
    }

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeInt(syncId);
        buf.writeByte(type);
        writeField(buf, value, type);
    }

    @Override
    public void handleClientSide(PlayerEntity player){
        Container container = player.openContainer;
        //if(container instanceof ContainerBase) {
            //((ContainerBase)container).updateField(syncId, value);
        //}
    }

    @Override
    public void handleServerSide(PlayerEntity player){

    }

    @Override
    public void read(DataInput buffer) throws IOException{}

    @Override
    public void write(DataOutput buffer) throws IOException{}

}
