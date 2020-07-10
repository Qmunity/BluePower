/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.api.wire.redstone.CapabilityRedstoneDevice;
import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.event.BPEventHandler;
import com.bluepowermod.event.BPRecyclingReloadListener;
import com.bluepowermod.init.*;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.world.BPWorldGen;
import com.bluepowermod.world.WorldGenFlowers;
import com.bluepowermod.world.WorldGenOres;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


@Mod(Refs.MODID)
public class BluePower {


    public static BluePower instance;
    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new,() -> CommonProxy::new);

    public BluePower(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BPConfig.spec);
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(BPEnchantments.class);

        BPEventHandler eventHandler = new BPEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);

        BPApi.init(new BluePowerAPI());
        BPEnchantments.init();
        BPBlocks.init();
        proxy.preInitRenderers();

    }

    public static Logger log = LogManager.getLogger(Refs.MODID);

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(BPNetworkHandler::init);
        OreDictionarySetup.init();
        WorldGenOres.setupOres();
        WorldGenFlowers.setupFlowers();
        BPWorldGen.setupGeneralWorldGen();
        CapabilityBlutricity.register();
        CapabilityRedstoneDevice.register();
        proxy.setup(event);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> BPContainerType::registerScreenFactories);
        CompatibilityUtils.init(event);

    }

    @SubscribeEvent
    public void complete(FMLLoadCompleteEvent event) {
        proxy.initRenderers();
        CompatibilityUtils.postInit(event);
        Recipes.init();
    }

    @SubscribeEvent
    public void onResourceReload(AddReloadListenerEvent event) {
        //Add Reload Listener for the Alloy Furnace Recipe Generator
        event.addListener(new BPRecyclingReloadListener(event.getDataPackRegistries()));
    }

    @SubscribeEvent
    public void onLootLoad(LootTableLoadEvent event)
    {
        ResourceLocation grass = new ResourceLocation("minecraft", "blocks/tall_grass");
        if (event.getName().equals(grass)){
                event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation("bluepower", "blocks/tall_grass"))).name("bluepower:tall_grass").build());
        }
    }

    @SubscribeEvent
    public void onServerStarted(FMLServerAboutToStartEvent event){
        BPRecyclingReloadListener.server = event.getServer();
        //Reload to make sure Recycling Recipes are available
        if(BPConfig.CONFIG.alloyFurnaceDatapackGenerator.get()){
            ResourcePackList<?> resourcepacklist = event.getServer().getResourcePacks();
            resourcepacklist.reloadPacksFromFinders();
            ArrayList<String> packs = new ArrayList<>();
            packs.add("file/bluepower");
            packs.add("mod:bluepower");
            event.getServer().reloadPacks(packs);
        }
    }

}
