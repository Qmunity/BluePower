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

import com.bluepowermod.client.render.RenderDebugScreen;
import com.bluepowermod.client.render.Renderers;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void setup(FMLCommonSetupEvent event) {

    }

    @Override
    public void preInitRenderers() {
        MinecraftForge.EVENT_BUS.register(new RenderDebugScreen());
        CompatibilityUtils.registerRenders();
        OBJLoader.INSTANCE.addDomain(Refs.MODID);
    }


    @Override
    public void initRenderers() {
        Renderers.init();
    }

    @Override
    public PlayerEntity getPlayer() {

        return Minecraft.getInstance().player;
    }

    @Override
    public boolean isSneakingInGui() {


        return Minecraft.getInstance().gameSettings.keyBindSneak.isKeyDown();
    }

    public static Screen getOpenedGui() {
        return Minecraft.getInstance().currentScreen;
    }

    @Override
    public String getSavePath() {

        return Minecraft.getInstance().gameDir.getAbsolutePath();
    }
}
