package com.bluepowermod.network.message;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import uk.co.qmunity.lib.client.gui.GuiContainerBase;
import uk.co.qmunity.lib.network.LocatedPacket;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.part.tube.TubeStack;
import com.bluepowermod.tile.TileMachineBase;

import cpw.mods.fml.common.network.ByteBufUtils;

public class MessageSyncMachineBacklog extends LocatedPacket<MessageSyncMachineBacklog> {

    private List<TubeStack> stacks = new ArrayList<TubeStack>();

    public MessageSyncMachineBacklog() {

    }

    public MessageSyncMachineBacklog(TileMachineBase tile, List<TubeStack> stacks) {

        super(tile.xCoord, tile.yCoord, tile.zCoord);
        this.stacks = stacks;
    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);
        buf.writeInt(stacks.size());
        for (TubeStack stack : stacks) {
            NBTTagCompound tag = new NBTTagCompound();
            stack.writeToNBT(tag);
            ByteBufUtils.writeTag(buf, tag);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);
        int amount = buf.readInt();
        for (int i = 0; i < amount; i++) {
            stacks.add(TubeStack.loadFromNBT(ByteBufUtils.readTag(buf)));
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

        TileEntity te = player.worldObj.getTileEntity(x, y, z);
        if (te instanceof TileMachineBase) {
            ((TileMachineBase) te).setBacklog(stacks);
            GuiContainerBase gui = (GuiContainerBase) ClientProxy.getOpenedGui();
            if (gui != null)
                gui.redraw();
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

}
