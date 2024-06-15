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

package com.bluepowermod;

import com.bluepowermod.client.gui.BPMenuType;
import com.bluepowermod.client.render.RenderDebugScreen;
import com.bluepowermod.client.render.Renderers;
import com.bluepowermod.compat.CompatibilityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInitRenderers(IEventBus eventBus) {
        CompatibilityUtils.registerRenders();

        eventBus.register(new Renderers());
    }


    @Override
    public void initRenderers() {
        Renderers.init();
    }

    @Override
    public Player getPlayer() {

        return Minecraft.getInstance().player;
    }

    @Override
    public boolean isSneakingInGui() {
        return Minecraft.getInstance().options.keyShift.isDown();
    }

    public static Screen getOpenedGui() {
        return Minecraft.getInstance().screen;
    }

    @Override
    public String getSavePath() {

        return Minecraft.getInstance().gameDirectory.getAbsolutePath();
    }
}
