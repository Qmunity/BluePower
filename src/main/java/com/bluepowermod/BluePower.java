/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

import org.apache.logging.log4j.Logger;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.client.gui.GUIHandler;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.convert.WorldConversionEventHandler;
import com.bluepowermod.event.BPEventHandler;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPEnchantments;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.Config;
import com.bluepowermod.init.OreDictionarySetup;
import com.bluepowermod.init.Recipes;
import com.bluepowermod.init.TileEntities;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.wire.redstone.RedstoneApi;
import com.bluepowermod.part.wire.redstone.RedstoneProviderQmunityLib;
import com.bluepowermod.part.wire.redstone.RedstoneProviderVanilla;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.util.Achievements;
import com.bluepowermod.util.Refs;
import com.bluepowermod.world.WorldGenerationHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;

@Mod(modid = Refs.MODID, name = Refs.NAME, dependencies = "required-after:qmunitylib", guiFactory = Refs.GUIFACTORY)
public class BluePower {

    @Instance(Refs.MODID)
    public static BluePower instance;

    @SidedProxy(clientSide = Refs.PROXY_LOCATION + ".ClientProxy", serverSide = Refs.PROXY_LOCATION + ".CommonProxy")
    public static CommonProxy proxy;
    public static Logger log;
    public static Configuration config;
    public static Item.ToolMaterial gemMaterial = EnumHelper.addToolMaterial("GEM", 2, 750, 6.0F, 2.0F, 18);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        event.getModMetadata().version = Refs.fullVersionString();

        log = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());

        BPApi.init(new BluePowerAPI());
        // Load configs
        Config.syncConfig(config);

        BPEnchantments.init();

        CompatibilityUtils.preInit(event);

        FMLCommonHandler.instance().bus().register(new Config());
        BPEventHandler eventHandler = new BPEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        FMLCommonHandler.instance().bus().register(eventHandler);

        MinecraftForge.EVENT_BUS.register(new WorldConversionEventHandler());

        RedstoneApi.getInstance().registerRedstoneProvider(new RedstoneProviderQmunityLib());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        PartManager.registerParts();

        BPBlocks.init();
        BPItems.init();
        PartManager.registerItems();

        TileEntities.init();
        OreDictionarySetup.init();
        GameRegistry.registerWorldGenerator(new WorldGenerationHandler(), 0);

        proxy.init();
        NetworkHandler.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
        CompatibilityUtils.init(event);

        Achievements.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        CompatibilityUtils.postInit(event);
        proxy.initRenderers();

        Recipes.init(CraftingManager.getInstance());
        AlloyFurnaceRegistry.getInstance().generateRecyclingRecipes();

        RedstoneApi.getInstance().registerRedstoneProvider(new RedstoneProviderVanilla());
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

        // register commands
    }

    @EventHandler
    public void event(FMLMissingMappingsEvent event) {

        for (MissingMapping mapping : event.get()) {
            String name = mapping.name;
            if (mapping.name.startsWith("bluepower:lamp")) {
                name = name.replace("silver", "light_gray");
                if (mapping.type == Type.BLOCK) {
                    mapping.remap(GameData.getBlockRegistry().getObject(name));
                } else {
                    mapping.remap(GameData.getItemRegistry().getObject(name));
                }
                continue;
            }
            if (mapping.name.equals("bluepower:bluepower_multipart")) {
                mapping.ignore();
                continue;
            }

            name = name.replace("silver", "light_gray");

            name = name.replace("bluestoneWire", "wire.bluestone");

            name = name.replace("cagelamp", "cagelamp.").replace("fixture", "fixture.");
            if (name.contains("inverted"))
                name = "bluepower:part." + name.substring("bluepower:part.inverted".length()) + ".inverted";

            if (name.equals("bluepower:stone_wire"))
                name = "bluepower:bluestone_wire_tile";
            if (name.equals("bluepower:stone_cathode"))
                name = "bluepower:bluestone_cathode_tile";
            if (name.equals("bluepower:stone_anode"))
                name = "bluepower:bluestone_anode_tile";
            if (name.equals("bluepower:stone_pointer"))
                name = "bluepower:bluestone_pointer_tile";

            if (name.equals("bluepower:silicon_chip"))
                name = "bluepower:silicon_chip_tile";
            if (name.equals("bluepower:taintedsilicon_chip"))
                name = "bluepower:tainted_silicon_chip_tile";
            if (name.equals("bluepower:quartz_resonator"))
                name = "bluepower:quartz_resonator_tile";

            Item item = GameData.getItemRegistry().getObject(name);
            if (item == null)
                continue;
            mapping.remap(item);
        }
    }
}
