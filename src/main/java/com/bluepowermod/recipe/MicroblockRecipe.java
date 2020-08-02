package com.bluepowermod.recipe;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPRecipeSerializer;
import com.bluepowermod.item.ItemSaw;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class MicroblockRecipe extends SpecialRecipe {

    public MicroblockRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        int blockCount = 0;
        int saw = 0;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BlockItem){
                    VoxelShape shape = null;
                    try{
                       shape = Block.getBlockFromItem(stack.getItem()).getDefaultState().getShape(null, null);
                    }catch (NullPointerException ignored){
                        //Shulker Boxes try to query the Tile Entity
                    }
                    if (shape == VoxelShapes.fullCube() || Block.getBlockFromItem(stack.getItem()) == BPBlocks.half_block || Block.getBlockFromItem(stack.getItem()) == BPBlocks.panel) {
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
    public ItemStack getCraftingResult(CraftingInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                if (Block.getBlockFromItem(stack.getItem()).getDefaultState().getShape(null, null) == VoxelShapes.fullCube()){
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("block", stack.getItem().getRegistryName().toString());
                    ItemStack outStack = new ItemStack(BPBlocks.half_block, 2);
                    outStack.setTag(nbt);
                    outStack.setDisplayName(new TranslationTextComponent(stack.getItem().getTranslationKey())
                            .appendString(" ")
                            .append(new TranslationTextComponent(BPBlocks.half_block.getTranslationKey())));
                    return outStack;
                }else if (Block.getBlockFromItem(stack.getItem()) == BPBlocks.half_block){
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("block", stack.getTag().getString("block"));
                    ItemStack outStack = new ItemStack(BPBlocks.panel, 2);
                    outStack.setTag(nbt);
                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
                    outStack.setDisplayName(new TranslationTextComponent(block.getTranslationKey())
                            .appendString(" ")
                            .append(new TranslationTextComponent(BPBlocks.panel.getTranslationKey())));
                    return outStack;
                }else if (Block.getBlockFromItem(stack.getItem()) == BPBlocks.panel){
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("block", stack.getTag().getString("block"));
                    ItemStack outStack = new ItemStack(BPBlocks.cover, 2);
                    outStack.setTag(nbt);
                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
                    outStack.setDisplayName(new TranslationTextComponent(block.getTranslationKey())
                            .appendString(" ")
                            .append(new TranslationTextComponent(BPBlocks.cover.getTranslationKey())));
                    return outStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BPRecipeSerializer.MICROBLOCK;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MicroblockRecipe> {
        @Override
        public MicroblockRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new MicroblockRecipe(recipeId);
        }

        @Override
        public MicroblockRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new MicroblockRecipe(recipeId);
        }

        @Override
        public void write(PacketBuffer buffer, MicroblockRecipe recipe) {
        }
    }
}
