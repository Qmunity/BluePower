package com.bluepowermod.client.render.placement_preview;

import com.bluepowermod.BluePower;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.Variant.Deserializer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelBakery.ModelBakerImpl;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.IForgeModelBaker;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class PlacementPreviewReloadListener extends SimpleJsonResourceReloadListener {
    private final Map<BlockState, Variant> models = new Object2ObjectOpenHashMap<>();
    private final Cache<BlockState, BakedModel> previewCache = CacheBuilder.newBuilder().expireAfterAccess(15, TimeUnit.MINUTES).build();
    private final Set<Item> relevantItems = new HashSet<>();

    public static final Variant.Deserializer VARIANT_DESERIALIZER = new Deserializer();
    public static ModelBakery BAKERY = null;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final PlacementPreviewReloadListener INSTANCE = new PlacementPreviewReloadListener();
    public PlacementPreviewReloadListener() {
        super(GSON, "bluepower/placement_preview");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<BlockState, Variant> results = new HashMap<>();
        Set<Item> relevantItems = new HashSet<>();
        loopOverItems:
        for (var entry : resourceLocationJsonElementMap.entrySet()){
            Map<BlockState,Variant> stateVariants = new HashMap<>();
            ResourceLocation itemId = entry.getKey();
            Item item = ForgeRegistries.ITEMS.getValue(itemId);

            if (item == Items.AIR) {
                BluePower.log.error("Found placement preview file for invalid item id: {}", itemId);
                continue;
            }
            if (!(item instanceof BlockItem)) {
                BluePower.log.error("Found placement preview file for unsupported item: {} (only BlockItems are currently supported)", itemId);
                continue;
            }
            loopOverBlocks:
            for (var blockVariants : entry.getValue().getAsJsonObject().entrySet()){
                if (!(blockVariants.getValue() instanceof JsonObject object)) continue;
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockVariants.getKey()));
                if (block == Blocks.AIR){
                    BluePower.log.error("Found placement preview file for invalid block id: {}", blockVariants.getKey());
                    continue;
                }
                var stateDefinition = block.getStateDefinition();
                for (var stateEntry : object.entrySet()){
                    String selector = stateEntry.getKey();
                    JsonObject variantObject = stateEntry.getValue().getAsJsonObject();
                    Variant variant = VARIANT_DESERIALIZER.deserialize(variantObject, null, null);
                    try {
                        var predicate = ModelBakery.predicate(stateDefinition, selector);
                        for (BlockState state : stateDefinition.getPossibleStates()) {
                            if (predicate.test(state)) {
                                @Nullable Variant existingVariant = stateVariants.put(state, variant);
                                if (existingVariant != null) {
                                    BluePower.log.warn("Overlapping placement preview definition for state {}", state);
                                    continue loopOverItems;
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        BluePower.log.error("Exception loading placement preview for item {}, block {}, variant {}: {}", item, block, selector, e.getMessage());
                        continue loopOverBlocks;
                    }

                }
            }
            // if no errors, keep all declared states for this item
            results.putAll(stateVariants);
            relevantItems.add(item);
        }
        models.clear();
        models.putAll(results);
        this.previewCache.invalidateAll();
        this.relevantItems.clear();
        this.relevantItems.addAll(relevantItems);
    }

    @Nullable
    public BakedModel getModelFromState(BlockState state){
        if (!models.containsKey(state)) return null;
        if (BAKERY == null) return null;
        Variant variant = models.get(state);
        try {
            return previewCache.get(state, () -> {
                ModelBaker baker = BAKERY.new ModelBakerImpl((r, m) -> getDefaultTextureGetter().apply(m), variant.getModelLocation());
                return baker.bake(variant.getModelLocation(), variant, getDefaultTextureGetter());
            });
        } catch (ExecutionException e) {
            BluePower.log.error(e);
            return null;
        }
    }

    public boolean hasItem(Item item){
        return relevantItems.contains(item);
    }

    public static Function<Material, TextureAtlasSprite> getDefaultTextureGetter(){
        return Material::sprite;
    }

   /* @Nullable
    public UnbakedModel getModelFromState(BlockState state){
        return models.get(state);
    }*/
}
