package com.bluepowermod.recipe;

import com.bluepowermod.block.BlockBPMicroblock;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPRecipeSerializer;
import com.bluepowermod.item.ItemSaw;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.security.Provider;

public class MicroblockRecipe extends CustomRecipe {

    public MicroblockRecipe() {
        super(CraftingBookCategory.BUILDING);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        int blockCount = 0;
        int saw = 0;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BlockItem && (Block.byItem(stack.getItem()) instanceof BlockBPMicroblock || !(Block.byItem(stack.getItem()) instanceof EntityBlock))){
                    VoxelShape shape = null;
                    try{
                       shape = Block.byItem(stack.getItem()).defaultBlockState().getShape(null, null);
                    }catch (NullPointerException ignored){
                        //Shulker Boxes try to query the Tile Entity
                    }
                    if (shape == Shapes.block() || Block.byItem(stack.getItem()) == BPBlocks.half_block.get() || Block.byItem(stack.getItem()) == BPBlocks.panel.get()) {
                        blockCount++;
                    }
                } else if (stack.getItem() instanceof ItemSaw) {
                    saw++;
                } else {
                    return false;
                }
            }
        }
        return blockCount == 1 && saw == 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, HolderLookup.Provider provider) {
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                if (Block.byItem(stack.getItem()).defaultBlockState().getShape(null, null) == Shapes.block()){
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("block", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
                    ItemStack outStack = new ItemStack(BPBlocks.half_block.get(), 2);
                    outStack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
                    outStack.set(DataComponents.ITEM_NAME, Component.translatable(stack.getItem().getDescriptionId())
                            .append(" ")
                            .append(Component.translatable(BPBlocks.half_block.get().getDescriptionId())));
                    return outStack;
                }else if (Block.byItem(stack.getItem()) == BPBlocks.half_block.get()){
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("block", stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("block"));
                    ItemStack outStack = new ItemStack(BPBlocks.panel.get(), 2);
                    outStack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
                    Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(nbt.getString("block")));
                    outStack.set(DataComponents.ITEM_NAME, Component.translatable(block.getDescriptionId())
                            .append(" ")
                            .append(Component.translatable(BPBlocks.panel.get().getDescriptionId())));
                    return outStack;
                }else if (Block.byItem(stack.getItem()) == BPBlocks.panel.get()){
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("block", stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("block"));
                    ItemStack outStack = new ItemStack(BPBlocks.cover.get(), 2);
                    outStack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
                    Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(nbt.getString("block")));
                    outStack.set(DataComponents.ITEM_NAME, Component.translatable(block.getDescriptionId())
                            .append(" ")
                            .append(Component.translatable(BPBlocks.cover.get().getDescriptionId())));
                    return outStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BPRecipeSerializer.MICROBLOCK.get();
    }

    public static class Serializer implements RecipeSerializer<MicroblockRecipe> {

        @Override
        public MapCodec<MicroblockRecipe> codec() {
            return RecordCodecBuilder.mapCodec((instance) -> instance.group(
                    Codec.STRING.fieldOf("type").forGetter((type) -> "bluepower:micro_block")
            ).apply(instance, (type) -> new MicroblockRecipe()));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MicroblockRecipe> streamCodec() {
            return new StreamCodec<RegistryFriendlyByteBuf, MicroblockRecipe>() {
                @Override
                public MicroblockRecipe decode(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
                    return new MicroblockRecipe();
                }

                @Override
                public void encode(RegistryFriendlyByteBuf o, MicroblockRecipe microblockRecipe) {

                }
            };
        }
    }
}
