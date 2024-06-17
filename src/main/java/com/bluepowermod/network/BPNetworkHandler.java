/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.network;

import com.bluepowermod.network.message.*;
import net.neoforged.bus.api.IEventBus;

public class BPNetworkHandler extends BPMessageHandler {

    public BPNetworkHandler(IEventBus modEventBus) {
        super(modEventBus);
    }

    @Override
    protected void registerClientToServer(PacketRegistrar registrar) {
        registrar.play(MessageGuiUpdate.TYPE, MessageGuiUpdate.STREAM_CODEC);
        registrar.play(MessageCraftingSync.TYPE, MessageCraftingSync.STREAM_CODEC);
    }

    @Override
    protected void registerServerToClient(PacketRegistrar registrar) {
    }
}
