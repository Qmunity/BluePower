package net.quetzi.bluepower.init;

import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.blocks.BlockBasalt;
import net.quetzi.bluepower.blocks.BlockBasaltCobble;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {
    public static void init() {
        GameRegistry.addSmelting(new ItemStack(new BlockBasaltCobble()), new ItemStack(new BlockBasalt()), 0);
    }
}
