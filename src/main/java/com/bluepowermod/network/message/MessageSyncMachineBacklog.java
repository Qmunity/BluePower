package com.bluepowermod.network.message;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.client.gui.GuiContainerBase;
import com.bluepowermod.container.stack.TubeStack;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tileentity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class MessageSyncMachineBacklog{

    private List<TubeStack> stacks = new ArrayList<TubeStack>();

    public MessageSyncMachineBacklog() {

    }

    public MessageSyncMachineBacklog(TileMachineBase tile, List<TubeStack> stacks) {

        //super(tile.getPos());
        this.stacks = stacks;
   }

/*
    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);
        buf.writeInt(stacks.size());
        for (TubeStack stack : stacks) {
            CompoundTag tag = new CompoundTag();
            stack.writeToNBT(tag);
            //ByteBufUtils.writeTag(buf, tag);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);
        int amount = buf.readInt();
        for (int i = 0; i < amount; i++) {
            //stacks.add(TubeStack.loadFromNBT(ByteBufUtils.readTag(buf)));
        }
    }
*/

    public void handleClientSide(Player player) {

        //BlockEntity te = player.world.getBlockEntity(pos);
        //if (te instanceof TileMachineBase) {
            //((TileMachineBase) te).setBacklog(stacks);
            // GuiContainerBase gui = (GuiContainerBase) ClientProxy.getOpenedGui();
            // if (gui != null)
            //    gui.redraw();
        //}
    }

    public void handleServerSide(Player player) {

    }

}
