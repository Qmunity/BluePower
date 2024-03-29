/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.message;

import com.bluepowermod.tile.IGUITextFieldSensitive;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MessageUpdateTextfield{

    private int textFieldID;
    private String text;

    public MessageUpdateTextfield() {

    }

    public MessageUpdateTextfield(BlockEntity te, int textfieldID) {

        //super(te.getBlockPos());
        textFieldID = textfieldID;
        text = ((IGUITextFieldSensitive) te).getText(textfieldID);
    }

/*
    @Override
    public void toBytes(ByteBuf buffer) {

        super.toBytes(buffer);
        buffer.writeInt(textFieldID);
        //ByteBufUtils.writeUTF8String(buffer, text);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {

        super.fromBytes(buffer);
        textFieldID = buffer.readInt();
        //text = ByteBufUtils.readUTF8String(buffer);
    }
*/

    public void handleClientSide(Player player) {

    }

    public void handleServerSide(Player player) {

        //BlockEntity te = player.world.getBlockEntity(pos);
        //if (te instanceof IGUITextFieldSensitive) {
            //((IGUITextFieldSensitive) te).setText(textFieldID, text);
        //}

    }

}
