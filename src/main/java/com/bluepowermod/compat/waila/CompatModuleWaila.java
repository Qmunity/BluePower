/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.waila;

import com.bluepowermod.compat.CompatModule;
import com.bluepowermod.tile.TileMachineBase;
import com.bluepowermod.util.Dependencies;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author amadornes
 *
 */
public class CompatModuleWaila extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

    }

    @Override
    public void init(FMLInitializationEvent ev) {

        FMLInterModComms.sendMessage(Dependencies.WAILA, "register", getClass().getName() + ".callbackRegister");

    }

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

    }

    @Override
    public void registerBlocks() {

    }

    @Override
    public void registerItems() {

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerRenders() {

    }

    public static void callbackRegister(IWailaRegistrar registrar) {

        registrar.registerBodyProvider(new WailaProviderMachines(), TileMachineBase.class);
        registrar.registerBodyProvider(new WailaProviderParts(), TileEntity.class);
    }
}
