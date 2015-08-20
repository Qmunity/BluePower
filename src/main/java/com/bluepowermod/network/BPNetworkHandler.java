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

import uk.co.qmunity.lib.network.NetworkHandler;

import com.bluepowermod.network.message.MessageCircuitDatabaseTemplate;
import com.bluepowermod.network.message.MessageDebugBlock;
import com.bluepowermod.network.message.MessageGuiUpdate;
import com.bluepowermod.network.message.MessageICInteract;
import com.bluepowermod.network.message.MessageICPlace;
import com.bluepowermod.network.message.MessageRedirectTubeStack;
import com.bluepowermod.network.message.MessageSendClientServerTemplates;
import com.bluepowermod.network.message.MessageServerTickTime;
import com.bluepowermod.network.message.MessageSyncMachineBacklog;
import com.bluepowermod.network.message.MessageUpdateTextfield;
import com.bluepowermod.network.message.MessageWirelessFrequencySync;
import com.bluepowermod.network.message.MessageWirelessNewFreq;
import com.bluepowermod.network.message.MessageWirelessRemoveFreq;
import com.bluepowermod.network.message.MessageWirelessSaveFreq;
import com.bluepowermod.reference.Refs;

import cpw.mods.fml.relauncher.Side;

public class BPNetworkHandler {

    public static final NetworkHandler INSTANCE = new NetworkHandler(Refs.MODID);

    public static void initBP() {

        INSTANCE.registerPacket(MessageGuiUpdate.class, MessageGuiUpdate.class, Side.SERVER);
        INSTANCE.registerPacket(MessageUpdateTextfield.class, MessageUpdateTextfield.class, Side.SERVER);
        INSTANCE.registerPacket(MessageCircuitDatabaseTemplate.class, MessageCircuitDatabaseTemplate.class, Side.SERVER);
        INSTANCE.registerPacket(MessageCircuitDatabaseTemplate.class, MessageCircuitDatabaseTemplate.class, Side.CLIENT);
        INSTANCE.registerPacket(MessageDebugBlock.class, MessageDebugBlock.class, Side.CLIENT);
        INSTANCE.registerPacket(MessageSendClientServerTemplates.class, MessageSendClientServerTemplates.class, Side.CLIENT);
        INSTANCE.registerPacket(MessageRedirectTubeStack.class, MessageRedirectTubeStack.class, Side.CLIENT);
        INSTANCE.registerPacket(MessageServerTickTime.class, Side.CLIENT);

        INSTANCE.registerPacket(MessageWirelessNewFreq.class, MessageWirelessNewFreq.class, Side.SERVER);
        INSTANCE.registerPacket(MessageWirelessSaveFreq.class, MessageWirelessSaveFreq.class, Side.SERVER);
        INSTANCE.registerPacket(MessageWirelessFrequencySync.class, MessageWirelessFrequencySync.class, Side.CLIENT);
        INSTANCE.registerPacket(MessageWirelessRemoveFreq.class, MessageWirelessRemoveFreq.class, Side.SERVER);

        INSTANCE.registerPacket(MessageSyncMachineBacklog.class, MessageSyncMachineBacklog.class, Side.CLIENT);

        INSTANCE.registerPacket(MessageICInteract.class, Side.SERVER);
        INSTANCE.registerPacket(MessageICPlace.class, Side.SERVER);
    }

}
