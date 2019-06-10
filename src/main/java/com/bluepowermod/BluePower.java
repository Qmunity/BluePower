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
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.event.BPEventHandler;
import com.bluepowermod.init.*;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.reference.Refs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Refs.MODID)
public class BluePower {


    public static BluePower instance;
    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new,() -> CommonProxy::new);

    public BluePower(){
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().register(this);

        BPApi.init(new BluePowerAPI());
        //Load configs
        //TODO: Configs
        //Config.syncConfig(config);
        BPEnchantments.init();
        MinecraftForge.EVENT_BUS.register(BPEnchantments.class);
        CapabilityBlutricity.register();

        MinecraftForge.EVENT_BUS.register(new Config());
        BPEventHandler eventHandler = new BPEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        BPBlocks.init();
        BPItems.init();
        proxy.preInitRenderers();

    }

    public static Logger log = LogManager.getLogger(Refs.MODID);


    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {

        proxy.initRenderers();
        TileEntities.init();
        OreDictionarySetup.init();
        //TODO: World Gen
        //GameRegistry.registerWorldGenerator(new WorldGenerationHandler(), 0);

        proxy.setup(event);
        BPNetworkHandler.initBP();
        //TODO: GUI
        //NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
        CompatibilityUtils.init(event);

    }

    @SubscribeEvent
    public void complete(FMLLoadCompleteEvent event) {
        CompatibilityUtils.postInit(event);
        Recipes.init();
        AlloyFurnaceRegistry.getInstance().generateRecyclingRecipes();
    }

}
