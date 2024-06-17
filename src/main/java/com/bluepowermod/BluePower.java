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
import com.bluepowermod.client.gui.BPMenuType;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.event.BPEventHandler;
import com.bluepowermod.event.BPRecyclingReloadListener;
import com.bluepowermod.init.*;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.world.BPWorldGen;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;


@Mod(Refs.MODID)
public class BluePower {


    public static BluePower instance;
    public static Supplier<CommonProxy>  proxy = FMLEnvironment.dist == Dist.CLIENT ? ClientProxy::new : CommonProxy::new;

    public BluePower(IEventBus modEventBus){
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BPConfig.spec);
        instance = this;
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::complete);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.register(new BPCreativeTabs());

        BPBlocks.BLOCKS.register(modEventBus);
        BPItems.ITEMS.register(modEventBus);
        BPCreativeTabs.CREATIVE_TABS.register(modEventBus);
        BPBlockEntityType.BLOCK_ENTITY_TYPE.register(modEventBus);
        BPEnchantments.ENCHANTMENT.register(modEventBus);
        BPRecipeTypes.RECIPE_TYPE.register(modEventBus);
        BPRecipeSerializer.RECIPE_SERIALIZERS.register(modEventBus);
        BPWorldGen.FEATURES.register(modEventBus);
        BPWorldGen.PLACEMENTS.register(modEventBus);
        BPMenuType.MENU_TYPES.register(modEventBus);
        new BPNetworkHandler(modEventBus);

        BPEventHandler eventHandler = new BPEventHandler();
        NeoForge.EVENT_BUS.register(eventHandler);
        NeoForge.EVENT_BUS.register(this);

        BPApi.init(new BluePowerAPI());
        proxy.get().preInitRenderers(modEventBus);
    }

    public static Logger log = LogManager.getLogger(Refs.MODID);

    public void setup(FMLCommonSetupEvent event) {
        proxy.get().setup(event);
        CompatibilityUtils.init(event);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event){
        CapabilityBlutricity.register(event);
        CapabilityRedstoneDevice.register(event);

        //Register Energy Storage Capabilities
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, BPBlockEntityType.ENGINE.get(), (engine, side) -> engine.storage);

        //Register Item Handler Capabilities
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BPBlockEntityType.BLULECTRIC_FURNACE.get(), SidedInvWrapper::new);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BPBlockEntityType.ALLOY_FURNACE.get(), SidedInvWrapper::new);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BPBlockEntityType.BLULECTRIC_ALLOY_FURNACE.get(), SidedInvWrapper::new);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BPBlockEntityType.PROJECT_TABLE.get(), SidedInvWrapper::new);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BPBlockEntityType.AUTO_PROJECT_TABLE.get(), SidedInvWrapper::new);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BPBlockEntityType.DEPLOYER.get(), SidedInvWrapper::new);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BPBlockEntityType.BUFFER.get(), SidedInvWrapper::new);
    }

    public void complete(FMLLoadCompleteEvent event) {
        event.enqueueWork(proxy.get()::initRenderers);
        CompatibilityUtils.postInit(event);
        Recipes.init();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onResourceReload(AddReloadListenerEvent event) {
        //Add Reload Listener for the Alloy Furnace Recipe Generator
        event.addListener(new BPRecyclingReloadListener(event.getServerResources()));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onServerStart(ServerStartedEvent event) {
        //Check Alloy furnace recipes again after tags are populated
        BPRecyclingReloadListener.onResourceManagerReload(event.getServer().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING));
    }

}
