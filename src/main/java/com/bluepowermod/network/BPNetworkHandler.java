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
import com.bluepowermod.reference.Refs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.network.registration.NetworkRegistry;

public class BPNetworkHandler {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(Refs.MODID);
        registrar.play(new ResourceLocation(Refs.MODID, "message_gui_update"), MessageGuiUpdate::new, handler -> handler.server(MessageGuiUpdate::handle));
        registrar.play(new ResourceLocation(Refs.MODID, "message_crafting_sync"), MessageCraftingSync::new, handler -> handler.server(MessageCraftingSync::handle));
        //wrapper.registerMessage(2, MessageUpdateTextfield.class, MessageUpdateTextfield.class, Dist.DEDICATED_SERVER);
        //wrapper.registerMessage(3, MessageCircuitDatabaseTemplate.class, MessageCircuitDatabaseTemplate.class, Dist.DEDICATED_SERVER);
        //wrapper.registerMessage(4, MessageCircuitDatabaseTemplate.class, MessageCircuitDatabaseTemplate.class, Dist.CLIENT);
        //wrapper.registerMessage(5, MessageDebugBlock.class, MessageDebugBlock.class, Dist.CLIENT);
        //wrapper.registerMessage(6, MessageSendClientServerTemplates.class, MessageSendClientServerTemplates.class, Dist.CLIENT);
        //wrapper.registerMessage(7, MessageRedirectTubeStack.class, MessageRedirectTubeStack.class, Dist.CLIENT);
        //wrapper.registerMessage(8, MessageServerTickTime.class, MessageServerTickTime::encode, MessageServerTickTime::decode, MessageServerTickTime::handle);

        //wrapper.registerMessage(9, MessageWirelessNewFreq.class, MessageWirelessNewFreq.class, Dist.DEDICATED_SERVER);
        //wrapper.registerMessage(10, MessageWirelessSaveFreq.class, MessageWirelessSaveFreq.class, Dist.DEDICATED_SERVER);
        //wrapper.registerMessage(11, MessageWirelessFrequencySync.class, MessageWirelessFrequencySync.class, Dist.CLIENT);
        //wrapper.registerMessage(12, MessageWirelessRemoveFreq.class, MessageWirelessRemoveFreq.class, Dist.DEDICATED_SERVER);

        //wrapper.registerMessage(13, MessageSyncMachineBacklog.class, MessageSyncMachineBacklog.class, Dist.CLIENT);
    }

}
