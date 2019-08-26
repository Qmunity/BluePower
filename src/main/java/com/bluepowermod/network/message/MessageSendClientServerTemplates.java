/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.message;

import com.bluepowermod.tile.tier3.TileCircuitDatabase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

public class MessageSendClientServerTemplates{

    private List<ItemStack> stacks;

    public MessageSendClientServerTemplates() {

    }

    public MessageSendClientServerTemplates(List<ItemStack> stacks) {

        this.stacks = stacks;
    }

/*
    @Override
    public void fromBytes(ByteBuf buf) {

        int amount = buf.readInt();
        stacks = new ArrayList<ItemStack>();
        for (int i = 0; i < amount; i++) {
            //stacks.add(ByteBufUtils.readItemStack(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(stacks.size());
        //for (ItemStack stack : stacks)
            //ByteBufUtils.writeItemStack(buf, stack);
    }
*/

    public void write(DataOutput buffer) throws IOException {

    }

    public void read(DataInput buffer) throws IOException {

    }

    public void handleClientSide(PlayerEntity player) {

        TileCircuitDatabase.serverDatabaseStacks = stacks;
    }

    public void handleServerSide(PlayerEntity player) {

    }

}
