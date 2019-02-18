/*
 * This file is part of Blue Power.
 *
 *      Blue Power is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      Blue Power is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

        package com.bluepowermod.recipe;

        import com.bluepowermod.init.BPBlocks;
        import com.bluepowermod.init.BPItems;
        import com.bluepowermod.reference.Refs;
        import net.minecraft.init.Blocks;
        import net.minecraft.item.ItemStack;
        import net.minecraftforge.fml.common.registry.GameRegistry;
        import net.minecraftforge.oredict.RecipeSorter;

/**
 * Created by Quetzi on 27/10/14.
 */
public class CoreRecipes {

    public static void init() {

        // Smelting
        GameRegistry.addSmelting(BPBlocks.basalt_cobble, new ItemStack(BPBlocks.basalt), 0);
        GameRegistry.addSmelting(BPBlocks.copper_ore, new ItemStack(BPItems.copper_ingot), 0.5F);
        GameRegistry.addSmelting(BPBlocks.zinc_ore, new ItemStack(BPItems.zinc_ingot), 0.5F);
        GameRegistry.addSmelting(BPBlocks.silver_ore, new ItemStack(BPItems.silver_ingot), 0.7F);
        GameRegistry.addSmelting(BPBlocks.tungsten_ore, new ItemStack(BPItems.tungsten_nugget), 0.8F);
        GameRegistry.addSmelting(Blocks.STONE, new ItemStack(BPItems.stone_tile, 2), 0);
        GameRegistry.addSmelting(BPItems.zinc_ore_crushed, new ItemStack(BPItems.zinc_ingot), 0.5F);
        GameRegistry.addSmelting(BPItems.zinc_dust, new ItemStack(BPItems.zinc_ingot), 0.5F);
        GameRegistry.addSmelting(BPItems.zinc_ore_purified, new ItemStack(BPItems.zinc_ingot), 0.5F);

        // Decoration
        GameRegistry.addSmelting(BPBlocks.basalt_brick, new ItemStack(BPBlocks.basaltbrick_cracked, 1), 0);
        GameRegistry.addSmelting(BPBlocks.basalt, new ItemStack(BPBlocks.basalt_tile), 0);
        GameRegistry.addSmelting(BPBlocks.marble, new ItemStack(BPBlocks.marble_tile), 0);

/*        if (Loader.isModLoaded("ForgeMicroblock")) {
            ItemStack diamondPanel = new ItemStack(GameData.getItemRegistry().getObject(new ResourceLocation("ForgeMicroblock:microblock")), 1, 2);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("mat", "tile.blockDiamond");
            diamondPanel.setTagCompound(tag);

            ItemStack ironStrip = new ItemStack(GameData.getItemRegistry().getObject(new ResourceLocation("ForgeMicroblock:microblock")), 1, 769);
            tag = new NBTTagCompound();
            tag.setString("mat", "tile.blockIron");
            ironStrip.setTagCompound(tag);

            foo.addShapedRecipe(new ItemStack(BPItems.diamond_drawplate), " i ", "idi", " i ", 'i', ironStrip, 'd', diamondPanel);

            ItemStack diamondSawFMP = new ItemStack(GameData.getItemRegistry().getObject(new ResourceLocation("ForgeMicroblock:sawDiamond")), 1,
                    OreDictionary.WILDCARD_VALUE);
            foo.addShapelessRecipe(new ItemStack(BPItems.silicon_wafer, 16), diamondSawFMP, new ItemStack(BPItems.silicon_boule));

            foo.addShapedRecipe(BPItems.amethyst_saw, "WSS", "WGS", 'W', "stickWood", 'S', "rodStone", 'G',
                    BPItems.amethyst_gem);
            foo.addShapedRecipe(BPItems.ruby_saw, "WSS", "WGS", 'W', "stickWood", 'S', "rodStone", 'G', BPItems.ruby_gem);
            foo.addShapedRecipe(BPItems.sapphire_saw, "WSS", "WGS", 'W', "stickWood", 'S', "rodStone", 'G',
                    BPItems.sapphire_gem);
        } else {
            foo.addShapedRecipe(new ItemStack(BPItems.diamond_drawplate), "IDI", "IDI", "IDI", 'I', "ingotIron", 'D',
                    "gemDiamond");
            GameRegistry.addShapelessRecipe(new ItemStack(BPItems.silicon_wafer, 16), new ItemStack(BPItems.diamond_saw, 1,
                    OreDictionary.WILDCARD_VALUE), new ItemStack(BPItems.silicon_boule));
            foo.addShapedRecipe(new ItemStack(BPItems.iron_saw, 1), "SSS", " II", " II", 'S', "stickWood", 'I', "ingotIron");
            foo.addShapedRecipe(new ItemStack(BPItems.ruby_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I', "ingotIron",
                    '#', "gemRuby");
            foo.addShapedRecipe(new ItemStack(BPItems.amethyst_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I',
                    "ingotIron", '#', "gemAmethyst");
            foo.addShapedRecipe(new ItemStack(BPItems.sapphire_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I',
                    "ingotIron", '#', "gemSapphire");
            foo.addShapedRecipe(new ItemStack(BPItems.diamond_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I',
                    "ingotIron", '#', "gemDiamond");
        }*/
    }

}