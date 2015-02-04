package com.bluepowermod.container;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ICrafting;

import com.bluepowermod.container.ContainerGhosts;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageSyncMachineBacklog;
import com.bluepowermod.tile.TileMachineBase;

public abstract class ContainerMachineBase extends ContainerGhosts {

    private int backlogSize = -1;
    private final TileMachineBase machine;

    public ContainerMachineBase(TileMachineBase machine) {
        this.machine = machine;
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (Object crafter : crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (backlogSize != machine.getBacklog().size() && icrafting instanceof EntityPlayerMP) {
                NetworkHandler.sendTo(new MessageSyncMachineBacklog(machine, machine.getBacklog()), (EntityPlayerMP) icrafting);
            }
        }
        backlogSize = machine.getBacklog().size();
    }
}
