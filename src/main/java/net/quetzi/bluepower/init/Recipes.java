package net.quetzi.bluepower.init;

import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.blocks.BlockBasalt;
import net.quetzi.bluepower.blocks.BlockBasaltBrick;
import net.quetzi.bluepower.blocks.BlockBasaltCobble;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {
    public static void init() {
        GameRegistry.addSmelting(new ItemStack(Blocks.basalt), new ItemStack(Blocks.basalt_cobble), 0);
        GameRegistry.addRecipe(new ItemStack(Blocks.basalt_brick,4), new Object[]{"XX","XX",'X', Blocks.basalt});
        GameRegistry.addRecipe(new ItemStack(Blocks.marble_brick,4), new Object[]{"XX","XX",'X', Blocks.marble});
    }
}
