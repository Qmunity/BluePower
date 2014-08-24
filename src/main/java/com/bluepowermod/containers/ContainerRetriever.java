package com.bluepowermod.containers;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.client.gui.GuiBase;
import com.bluepowermod.tileentities.tier2.TileRetriever;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
public class ContainerRetriever extends ContainerFilter {

    private int slotIndex, mode;
    private final TileRetriever retriever;

    public ContainerRetriever(InventoryPlayer invPlayer, TileRetriever retriever) {

        super(invPlayer, retriever);
        this.retriever = retriever;
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {

        super.detectAndSendChanges();

        for (Object crafter : crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (slotIndex != retriever.slotIndex) {
                icrafting.sendProgressBarUpdate(this, 2, retriever.slotIndex);
            }
            if (mode != retriever.mode) {
                icrafting.sendProgressBarUpdate(this, 3, retriever.mode);
            }
        }
        slotIndex = retriever.slotIndex;
        mode = retriever.mode;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {

        super.updateProgressBar(id, value);

        if (id == 2) {
            retriever.slotIndex = value;
        }
        if (id == 3) {
            retriever.mode = value;
            ((GuiBase) ClientProxy.getOpenedGui()).redraw();
        }
    }
}
