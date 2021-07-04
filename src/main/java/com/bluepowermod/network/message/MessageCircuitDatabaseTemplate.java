/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.message;

import com.bluepowermod.helper.ItemStackDatabase;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Used from client to server to select a template from the private library of the client. Used from server to client to message the client to save
 * the current template to the private library of the client.
 *
 * @author MineMaarten, amadornes
 */
public class MessageCircuitDatabaseTemplate{

    private ItemStack stack;
    private boolean deleting; // server side only used

    public MessageCircuitDatabaseTemplate() {

    }

    public MessageCircuitDatabaseTemplate(TileCircuitDatabase circuitDatabase, ItemStack stack) {

        //super(circuitDatabase.getPos().getX(), circuitDatabase.getPos().getY(), circuitDatabase.getPos().getZ());
        this.stack = stack;
    }

    public MessageCircuitDatabaseTemplate(TileCircuitDatabase circuitDatabase, ItemStack stack, boolean deleting) {

        //super(circuitDatabase.getPos().getX(), circuitDatabase.getPos().getY(), circuitDatabase.getPos().getZ());
        this.stack = stack;
        this.deleting = deleting;
    }

    public void handleClientSide(PlayerEntity player) {
        //TileEntity te = player.world.getBlockEntity(pos);
        //if (te instanceof TileCircuitDatabase) {
            //((TileCircuitDatabase) te).saveToPrivateLibrary(stack);
        //}
    }

    public void handleServerSide(PlayerEntity player) {

        if (deleting) {
            if (TileCircuitDatabase.hasPermissions(player)) {
                ItemStackDatabase stackDatabase = new ItemStackDatabase();
                stackDatabase.deleteStack(stack);
                //BPNetworkHandler.INSTANCE.sendToAll(new MessageSendClientServerTemplates(stackDatabase.loadItemStacks()));
            }
        } else {
            //TileEntity te = player.world.getBlockEntity(pos);
            //if (te instanceof TileCircuitDatabase) {
                //((TileCircuitDatabase) te).copyInventory.setItem(0, stack);
                //TODO: Open GUI
                //player.openGui(BluePower.instance, GuiIDs.CIRCUITDATABASE_MAIN_ID.ordinal(), player.world, pos.getX(), pos.getY(), pos.getZ());
            //}
        }
    }

}
