package net.quetzi.bluepower.network.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.quetzi.bluepower.network.LocationIntPacket;
import net.quetzi.bluepower.tileentities.tier1.TileAlloyFurnace;

public class PacketUpdateAlloyFurnaceGUI extends LocationIntPacket
{

    int maxBurnTicks = 0;
    int burnTicks    = 0;

    public PacketUpdateAlloyFurnaceGUI() {}

    public PacketUpdateAlloyFurnaceGUI(int x, int y, int z, int _maxBurnTicks, int _burnTicks)
    {
        super(x, y, z);
        maxBurnTicks = _maxBurnTicks;
        burnTicks = _burnTicks;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        super.encodeInto(ctx, buffer);
        buffer.writeInt(maxBurnTicks);
        buffer.writeInt(burnTicks);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        super.decodeInto(ctx, buffer);
        maxBurnTicks = buffer.readInt();
        burnTicks = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        TileAlloyFurnace alloyFurnace = (TileAlloyFurnace) player.worldObj.getTileEntity(x, y, z);
        if (alloyFurnace != null) {
            alloyFurnace.setBurnTicks(maxBurnTicks, burnTicks);
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        // TODO Auto-generated method stub

    }

}
