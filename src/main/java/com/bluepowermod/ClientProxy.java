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

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.client.render.RenderDebugScreen;
import com.bluepowermod.client.render.Renderers;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.item.ItemMonocle;
import com.bluepowermod.part.PartManager;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init() {

        ClientRegistry.registerKeyBinding(ItemMonocle.keybind);
    }

    @Override
    public void initRenderers() {

        MinecraftForge.EVENT_BUS.register(new IconSupplier());
        MinecraftForge.EVENT_BUS.register(new RenderDebugScreen());
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

    @Override
    public void setFOVMultiplier(EntityPlayer player, float fovmult) {

        Field movementfactor = getMovementFactorField();
        try {
            movementfactor.set(player, fovmult);
        } catch (IllegalAccessException e) {
        }
    }

    protected static Field movementfactorfieldinstance;

    public static Field getMovementFactorField() {

        if (movementfactorfieldinstance == null) {
            try {
                movementfactorfieldinstance = EntityPlayer.class.getDeclaredField("speedOnGround");
                movementfactorfieldinstance.setAccessible(true);
            } catch (NoSuchFieldException e) {
                try {
                    movementfactorfieldinstance = EntityPlayer.class.getDeclaredField("field_71108_cd");
                    movementfactorfieldinstance.setAccessible(true);
                } catch (NoSuchFieldException e1) {
                    try {
                        movementfactorfieldinstance = EntityPlayer.class.getDeclaredField("ci");
                        movementfactorfieldinstance.setAccessible(true);
                    } catch (NoSuchFieldException e2) {
                    }
                }
            }
        }
        return movementfactorfieldinstance;
    }
}
