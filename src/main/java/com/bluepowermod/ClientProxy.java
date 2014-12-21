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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.client.renderers.Renderers;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.part.PartManager;

import cpw.mods.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {

    }

    @Override
    public void initRenderers() {

        MinecraftForge.EVENT_BUS.register(new IconSupplier());
        PartManager.registerRenderers();
        Renderers.init();

        CompatibilityUtils.registerRenders();
    }

    @Override
    public EntityPlayer getPlayer() {

        return FMLClientHandler.instance().getClientPlayerEntity();
    }

    @Override
    public boolean isSneakingInGui() {

        return Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());
    }

    public static GuiScreen getOpenedGui() {

        return FMLClientHandler.instance().getClient().currentScreen;
    }

    @Override
    public String getSavePath() {

        return Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
    }
}
