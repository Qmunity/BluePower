/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.quetzi.bluepower.api.BPRegistry;
import net.quetzi.bluepower.api.part.PartRegistry;
import net.quetzi.bluepower.api.part.redstone.RedstoneNetworkTickHandler;
import net.quetzi.bluepower.client.gui.GUIHandler;
import net.quetzi.bluepower.compat.CompatibilityUtils;
import net.quetzi.bluepower.events.BPEventHandler;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.init.Config;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.init.OreDictionarySetup;
import net.quetzi.bluepower.init.Recipes;
import net.quetzi.bluepower.network.NetworkHandler;
import net.quetzi.bluepower.recipe.AlloyFurnaceRegistry;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.TileEntities;
import net.quetzi.bluepower.world.WorldGenerationHandler;

import org.apache.logging.log4j.Logger;

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

@Mod(modid = Refs.MODID, name = Refs.NAME)
public class BluePower {
    
    @Instance(Refs.MODID)
    public static BluePower   instance;
    
    @SidedProxy(clientSide = Refs.PROXY_LOCATION + ".ClientProxy", serverSide = Refs.PROXY_LOCATION + ".CommonProxy")
    public static CommonProxy proxy;
    public static Logger      log;
    
    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {
    
        event.getModMetadata().version = Refs.fullVersionString();
        
        log = event.getModLog();
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        
        CustomTabs.init();
        // Load configs
        config.load();
        Config.setUp(config);
        config.save();
        BPBlocks.init();
        BPItems.init();
        BPRegistry.alloyFurnaceRegistry = AlloyFurnaceRegistry.getInstance();
        
        TileEntities.init();
        OreDictionarySetup.init();
        GameRegistry.registerWorldGenerator(new WorldGenerationHandler(), 0);
        
        PartRegistry.init();
        
        CompatibilityUtils.preInit(event);
        
        FMLCommonHandler.instance().bus().register(new RedstoneNetworkTickHandler());
        
        BPEventHandler eventHandler =new BPEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        FMLCommonHandler.instance().bus().register(eventHandler);
    }
    
    @EventHandler
    public void Init(FMLInitializationEvent event) {
    
        Recipes.init(CraftingManager.getInstance());
        proxy.init();
        NetworkHandler.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
        CompatibilityUtils.init(event);
    }
    
    @EventHandler
    public void PostInit(FMLPostInitializationEvent event) {
    
        CompatibilityUtils.postInit(event);
        AlloyFurnaceRegistry.getInstance().generateRecyclingRecipes();
        proxy.initRenderers();
    }
    
    @EventHandler
    public void ServerStarting(FMLServerStartingEvent event) {
    
        // register commands
    }
}
