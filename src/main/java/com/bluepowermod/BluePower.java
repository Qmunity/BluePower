/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.client.gui.GUIHandler;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.events.BPEventHandler;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPEnchantments;
import com.bluepowermod.init.BPFluids;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.Config;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.init.OreDictionarySetup;
import com.bluepowermod.init.PartRegister;
import com.bluepowermod.init.Recipes;
import com.bluepowermod.init.TileEntities;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.util.Refs;
import com.bluepowermod.world.WorldGenerationHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Refs.MODID, name = Refs.NAME, guiFactory = Refs.GUIFACTORY)
public class BluePower {

    @Instance(Refs.MODID)
    public static BluePower instance;

    @SidedProxy(clientSide = Refs.PROXY_LOCATION + ".ClientProxy", serverSide = Refs.PROXY_LOCATION + ".CommonProxy")
    public static CommonProxy proxy;
    public static Logger log;
    public static Configuration config;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        event.getModMetadata().version = Refs.fullVersionString();

        log = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());

        BPApi.init(new BluePowerAPI());
        CustomTabs.init();
        // Load configs
        Config.syncConfig(config);
        BPBlocks.init();
        BPItems.init();

        TileEntities.init();
        OreDictionarySetup.init();
        GameRegistry.registerWorldGenerator(new WorldGenerationHandler(), 0);

        BPEnchantments.init();

        CompatibilityUtils.preInit(event);

        FMLCommonHandler.instance().bus().register(new Config());
        BPEventHandler eventHandler = new BPEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        FMLCommonHandler.instance().bus().register(eventHandler);

        PartRegister.registerParts();

        BPApi.getInstance().getBluestoneApi();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        BPFluids.init();

        Recipes.init(CraftingManager.getInstance());
        proxy.init();
        NetworkHandler.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
        CompatibilityUtils.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        CompatibilityUtils.postInit(event);
        AlloyFurnaceRegistry.getInstance().generateRecyclingRecipes();
        proxy.initRenderers();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

        // register commands
    }
}
