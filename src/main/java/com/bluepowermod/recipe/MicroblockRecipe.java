package com.bluepowermod.recipe;

import com.bluepowermod.block.BlockBPMicroblock;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPRecipeSerializer;
import com.bluepowermod.item.ItemSaw;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class MicroblockRecipe extends CustomRecipe {

    public MicroblockRecipe(ResourceLocation idIn) {
        super(idIn);
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
                    if (shape == Shapes.block() || Block.byItem(stack.getItem()) == BPBlocks.half_block || Block.byItem(stack.getItem()) == BPBlocks.panel) {
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
    public ItemStack assemble(CraftingContainer inv) {
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                if (Block.byItem(stack.getItem()).defaultBlockState().getShape(null, null) == Shapes.block()){
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("block", stack.getItem().getRegistryName().toString());
                    ItemStack outStack = new ItemStack(BPBlocks.half_block, 2);
                    outStack.setTag(nbt);
                    outStack.setHoverName(new TranslatableComponent(stack.getItem().getDescriptionId())
                            .append(" ")
                            .append(new TranslatableComponent(BPBlocks.half_block.getDescriptionId())));
                    return outStack;
                }else if (Block.byItem(stack.getItem()) == BPBlocks.half_block){
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("block", stack.getTag().getString("block"));
                    ItemStack outStack = new ItemStack(BPBlocks.panel, 2);
                    outStack.setTag(nbt);
                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
                    outStack.setHoverName(new TranslatableComponent(block.getDescriptionId())
                            .append(" ")
                            .append(new TranslatableComponent(BPBlocks.panel.getDescriptionId())));
                    return outStack;
                }else if (Block.byItem(stack.getItem()) == BPBlocks.panel){
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("block", stack.getTag().getString("block"));
                    ItemStack outStack = new ItemStack(BPBlocks.cover, 2);
                    outStack.setTag(nbt);
                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
                    outStack.setHoverName(new TranslatableComponent(block.getDescriptionId())
                            .append(" ")
                            .append(new TranslatableComponent(BPBlocks.cover.getDescriptionId())));
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
        return BPRecipeSerializer.MICROBLOCK;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<MicroblockRecipe> {
        @Override
        public MicroblockRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new MicroblockRecipe(recipeId);
        }

        @Override
        public MicroblockRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return new MicroblockRecipe(recipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, MicroblockRecipe recipe) {
        }
    }
}
