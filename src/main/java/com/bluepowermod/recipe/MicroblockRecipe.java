package com.bluepowermod.recipe;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPRecipeSerializer;
import com.bluepowermod.item.ItemSaw;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketBuffer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.Shapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

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
                if (stack.getItem() instanceof BlockItem && !Block.byItem(stack.getItem()).hasBlockEntity(Block.byItem(stack.getItem()).defaultBlockState())){
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
    public ItemStack assemble(CraftingInventory inv) {
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                if (Block.byItem(stack.getItem()).defaultBlockState().getShape(null, null) == Shapes.block()){
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("block", stack.getItem().getRegistryName().toString());
                    ItemStack outStack = new ItemStack(BPBlocks.half_block, 2);
                    outStack.setTag(nbt);
                    outStack.setHoverName(new TranslationTextComponent(stack.getItem().getDescriptionId())
                            .append(" ")
                            .append(new TranslationTextComponent(BPBlocks.half_block.getDescriptionId())));
                    return outStack;
                }else if (Block.byItem(stack.getItem()) == BPBlocks.half_block){
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("block", stack.getTag().getString("block"));
                    ItemStack outStack = new ItemStack(BPBlocks.panel, 2);
                    outStack.setTag(nbt);
                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
                    outStack.setHoverName(new TranslationTextComponent(block.getDescriptionId())
                            .append(" ")
                            .append(new TranslationTextComponent(BPBlocks.panel.getDescriptionId())));
                    return outStack;
                }else if (Block.byItem(stack.getItem()) == BPBlocks.panel){
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("block", stack.getTag().getString("block"));
                    ItemStack outStack = new ItemStack(BPBlocks.cover, 2);
                    outStack.setTag(nbt);
                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
                    outStack.setHoverName(new TranslationTextComponent(block.getDescriptionId())
                            .append(" ")
                            .append(new TranslationTextComponent(BPBlocks.cover.getDescriptionId())));
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
    public IRecipeSerializer<?> getSerializer() {
        return BPRecipeSerializer.MICROBLOCK;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MicroblockRecipe> {
        @Override
        public MicroblockRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new MicroblockRecipe(recipeId);
        }

        @Override
        public MicroblockRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            return new MicroblockRecipe(recipeId);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, MicroblockRecipe recipe) {
        }
    }
}
