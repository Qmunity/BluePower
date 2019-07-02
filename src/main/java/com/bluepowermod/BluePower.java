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
import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.event.BPEventHandler;
import com.bluepowermod.helper.BPItemTier;
import com.bluepowermod.init.*;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.world.WorldGenOres;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Refs.MODID)
public class BluePower {


    public static BluePower instance;
    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new,() -> CommonProxy::new);

    public BluePower(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BPConfig.spec);
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        BPApi.init(new BluePowerAPI());
        BPEnchantments.init();
        MinecraftForge.EVENT_BUS.register(BPEnchantments.class);
        //TODO: Blutricity Capability
        //CapabilityBlutricity.register();
        BPEventHandler eventHandler = new BPEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        BPBlocks.init();
        proxy.preInitRenderers();

    }

    public static Logger log = LogManager.getLogger(Refs.MODID);


    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {

        OreDictionarySetup.init();
        //TODO: World Gen
        WorldGenOres.setupOres();
        //GameRegistry.registerWorldGenerator(new BPWorldGen(), 0);

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
    public void serverStarted(FMLServerStartedEvent event) {
        AlloyFurnaceRegistry.getInstance().generateRecyclingRecipes();
    }

}
