package net.quetzi.bluepower.init;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.quetzi.bluepower.init.BPBlocks;
import net.minecraft.item.crafting.CraftingManager;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {
    public static void init(CraftingManager craftManager) {
        GameRegistry.addSmelting(BPBlocks.basalt_cobble, new ItemStack(BPBlocks.basalt), 0);
        GameRegistry.addSmelting(BPBlocks.copper_ore, new ItemStack(BPItems.copper_ingot), 0.5F);
        GameRegistry.addSmelting(BPBlocks.tin_ore, new ItemStack(BPItems.tin_ingot), 0.5F);
        GameRegistry.addSmelting(BPBlocks.silver_ore, new ItemStack(BPItems.silver_ingot), 0.7F);
        
        craftManager.addRecipe(new ItemStack(BPBlocks.basalt_brick, 4), new Object[] {"##", "##", '#', BPBlocks.basalt});
        craftManager.addRecipe(new ItemStack(BPBlocks.marble_brick, 4), new Object[] {"##", "##", '#', BPBlocks.marble});
        craftManager.addRecipe(new ItemStack(BPBlocks.copper_block, 1), new Object[] {"###", "###", "###", '#', BPItems.copper_ingot});
        craftManager.addRecipe(new ItemStack(BPBlocks.silver_block, 1), new Object[] {"###", "###", "###", '#', BPItems.silver_ingot});
        craftManager.addRecipe(new ItemStack(BPBlocks.tin_block, 1), new Object[] {"###", "###", "###", '#', BPItems.tin_ingot});
        
        craftManager.addRecipe(new ItemStack(BPItems.ruby_axe, 1), new Object[] {"GG ", "GS ", " S ", 'G', BPItems.ruby, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.ruby_axe, 1), new Object[] {" GG", " SG", " S ", 'G', BPItems.ruby, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.ruby_pickaxe, 1), new Object[] {"GGG", " S ", " S ", 'G', BPItems.ruby, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.ruby_sword, 1), new Object[] {"G", "G", "S", 'G', BPItems.ruby, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.ruby_spade, 1), new Object[] {"G", "S", "S", 'G', BPItems.ruby, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.ruby_hoe, 1), new Object[] {"GG ", " S ", " S ", 'G', BPItems.ruby, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.ruby_hoe, 1), new Object[] {" GG", " S ", " S ", 'G', BPItems.ruby, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.ruby_sickle, 1), new Object[] {" G ", "  G", "SG ", 'G', BPItems.ruby, 'S', Items.stick});
        
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_axe, 1), new Object[] {"GG ", "GS ", " S ", 'G', BPItems.sapphire, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_axe, 1), new Object[] {" GG", " SG", " S ", 'G', BPItems.sapphire, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_pickaxe, 1), new Object[] {"GGG", " S ", " S ", 'G', BPItems.sapphire, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_sword, 1), new Object[] {"G", "G", "S", 'G', BPItems.sapphire, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_spade, 1), new Object[] {"G", "S", "S", 'G', BPItems.sapphire, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_hoe, 1), new Object[] {"GG ", " S ", " S ", 'G', BPItems.sapphire, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_hoe, 1), new Object[] {" GG", " S ", " S ", 'G', BPItems.sapphire, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_sickle, 1), new Object[] {" G ", "  G", "SG ", 'G', BPItems.sapphire, 'S', Items.stick});
        
        craftManager.addRecipe(new ItemStack(BPItems.malachite_axe, 1), new Object[] {"GG ", "GS ", " S ", 'G', BPItems.malachite, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.malachite_axe, 1), new Object[] {" GG", " SG", " S ", 'G', BPItems.malachite, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.malachite_pickaxe, 1), new Object[] {"GGG", " S ", " S ", 'G', BPItems.malachite, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.malachite_sword, 1), new Object[] {"G", "G", "S", 'G', BPItems.malachite, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.malachite_spade, 1), new Object[] {"G", "S", "S", 'G', BPItems.malachite, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.malachite_hoe, 1), new Object[] {"GG ", " S ", " S ", 'G', BPItems.malachite, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.malachite_hoe, 1), new Object[] {" GG", " S ", " S ", 'G', BPItems.malachite, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.malachite_sickle, 1), new Object[] {" G ", "  G", "SG ", 'G', BPItems.malachite, 'S', Items.stick});
        
        craftManager.addRecipe(new ItemStack(BPItems.wood_sickle, 1), new Object[] {" G ", "  G", "SG ", 'G', Blocks.planks, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.stone_sickle, 1), new Object[] {" G ", "  G", "SG ", 'G', Blocks.cobblestone, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.iron_sickle, 1), new Object[] {" G ", "  G", "SG ", 'G', Items.iron_ingot, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.gold_sickle, 1), new Object[] {" G ", "  G", "SG ", 'G', Items.gold_ingot, 'S', Items.stick});
        craftManager.addRecipe(new ItemStack(BPItems.diamond_sickle, 1), new Object[] {" G ", "  G", "SG ", 'G', Items.diamond, 'S', Items.stick});
        
        craftManager.addRecipe(new ItemStack(BPItems.indigo_dye,1), new Object[] {"#", '#', BPBlocks.indigo_flower});
    }
}
