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
import com.bluepowermod.client.render.Renderers;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.event.BPEventHandler;
import com.bluepowermod.init.*;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.util.DatapackUtils;
import com.bluepowermod.world.BPWorldGen;
import com.bluepowermod.world.WorldGenFlowers;
import com.bluepowermod.world.WorldGenOres;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
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
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        //Add Reload Listener for the Alloy Furnace Recipe Generator
        event.getServer().getResourceManager().addReloadListener((IResourceManagerReloadListener) resourceManager -> {
            if(BPConfig.CONFIG.alloyFurnaceDatapackGenerator.get()) {
                RecipeManager recipeManager = event.getServer().getRecipeManager();
                AlloyFurnaceRegistry.getInstance().generateRecyclingRecipes(recipeManager);
                AlloyFurnaceRegistry.getInstance().generateRecipeDatapack(event);
            }else{
                //If disabled remove any generated recipes
                String path = event.getServer().getDataDirectory().getPath() + "/saves/" + event.getServer().getFolderName() + "/datapacks";
                if (event.getServer().isDedicatedServer()) {
                    path = event.getServer().getDataDirectory().getPath() + "/" + event.getServer().getFolderName() + "/datapacks";
                }
                DatapackUtils.clearBPAlloyFurnaceDatapack(path);
            }
        });
    }

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event){
        //Reload to make sure Recycling Recipes are available
        if(BPConfig.CONFIG.alloyFurnaceDatapackGenerator.get()){
            event.getServer().reload();
        }
    }

}
