package net.quetzi.bluepower.init;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.quetzi.bluepower.init.BPBlocks;
import net.minecraft.item.crafting.CraftingManager;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {
    public static void init(CraftingManager craftManager) {
        GameRegistry.addSmelting(BPBlocks.basalt_cobble, new ItemStack(BPBlocks.basalt), 0);
        GameRegistry.addSmelting(BPBlocks.copper_ore, new ItemStack(BPItems.copper_ingot), 0.5F);
        GameRegistry.addSmelting(BPBlocks.tin_ore, new ItemStack(BPItems.tin_ingot), 0.5F);
        GameRegistry.addSmelting(BPBlocks.silver_ore, new ItemStack(BPItems.silver_ingot), 0.7F);
        
        craftManager.addRecipe(new ItemStack(BPBlocks.basalt_brick,4), new Object[] {"##", "##", '#', BPBlocks.basalt});
        craftManager.addRecipe(new ItemStack(BPBlocks.marble_brick,4), new Object[] {"##", "##", '#', BPBlocks.marble});
        craftManager.addRecipe(new ItemStack(BPBlocks.copper_block,1), new Object[] {"###", "###", "###", '#', BPItems.copper_ingot});
        craftManager.addRecipe(new ItemStack(BPBlocks.silver_block,1), new Object[] {"###", "###", "###", '#', BPItems.silver_ingot});
        craftManager.addRecipe(new ItemStack(BPBlocks.tin_block,1), new Object[] {"###", "###", "###", '#', BPItems.tin_ingot});
        
        craftManager.addRecipe(new ItemStack(BPBlocks.alloy_furnace,1), new Object[] {"###", "# #", "###", '#', Blocks.brick_block});
    }
}
