package net.quetzi.bluepower.init;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {
    public static void init(CraftingManager craftManager) {
        GameRegistry.addSmelting(new ItemStack(Blocks.basalt), new ItemStack(Blocks.basalt_cobble), 0);
        craftManager.addRecipe(new ItemStack(Blocks.basalt_brick,4), new Object[] {"##", "##", '#', Blocks.basalt});
        craftManager.addRecipe(new ItemStack(Blocks.marble_brick,4), new Object[] {"##", "##", '#', Blocks.marble});
    }
}
