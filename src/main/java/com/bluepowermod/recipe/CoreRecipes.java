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
import com.bluepowermod.util.Refs;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
        GameRegistry.addSmelting(Blocks.stone, new ItemStack(BPItems.stone_tile, 2), 0);
        GameRegistry.addSmelting(BPItems.zinc_ore_crushed, new ItemStack(BPItems.zinc_ingot), 0.5F);
        GameRegistry.addSmelting(BPItems.zinc_dust, new ItemStack(BPItems.zinc_ingot), 0.5F);
        GameRegistry.addSmelting(BPItems.zinc_ore_purified, new ItemStack(BPItems.zinc_ingot), 0.5F);
        GameRegistry.addRecipe(new ItemStack(BPItems.zinc_dust, 1), "###", "###", "###", '#', BPItems.zinc_tiny_dust);

        // Blocks
        GameRegistry.addRecipe(new ItemStack(BPBlocks.alloyfurnace, 1), "###", "# #", "###", '#', Blocks.brick_block);
        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(BPBlocks.project_table), "SSS", "WCW", "WIW", 'S', "stone", 'W', "plankWood", 'C', Blocks.crafting_table, 'I', Blocks.chest));

        // Decoration
        GameRegistry.addSmelting(BPBlocks.basalt_brick, new ItemStack(BPBlocks.basaltbrick_cracked, 1), 0);
        GameRegistry.addSmelting(BPBlocks.basalt, new ItemStack(BPBlocks.basalt_tile), 0);
        GameRegistry.addSmelting(BPBlocks.marble, new ItemStack(BPBlocks.marble_tile), 0);
        GameRegistry.addRecipe(new ItemStack(BPBlocks.basalt_brick_small, 4), "##", "##", '#', BPBlocks.basalt_brick);
        GameRegistry.addRecipe(new ItemStack(BPBlocks.marble_brick_small, 4), "##", "##", '#', BPBlocks.marble_brick);
        GameRegistry.addRecipe(new ItemStack(BPBlocks.basalt_brick, 1), "#", '#', BPBlocks.basalt_brick_small);
        GameRegistry.addRecipe(new ItemStack(BPBlocks.marble_brick, 1), "#", '#', BPBlocks.marble_brick_small);
        GameRegistry.addShapelessRecipe(new ItemStack(BPBlocks.basalt_paver), BPBlocks.basalt_tile);
        GameRegistry.addShapelessRecipe(new ItemStack(BPBlocks.basalt_tile), BPBlocks.basalt_paver);
        GameRegistry.addShapelessRecipe(new ItemStack(BPBlocks.marble_paver), BPBlocks.marble_tile);
        GameRegistry.addShapelessRecipe(new ItemStack(BPBlocks.marble_tile), BPBlocks.marble_paver);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.basalt_brick, 4), "##", "##", '#', BPBlocks.basalt));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.marble_brick, 4), "##", "##", '#', BPBlocks.marble));
        GameRegistry.addRecipe(new ItemStack(BPBlocks.fancy_basalt, 1), "#", '#', BPBlocks.basalt_brick);
        GameRegistry.addRecipe(new ItemStack(BPBlocks.fancy_marble, 1), "#", '#', BPBlocks.marble_brick);
        GameRegistry.addRecipe(new ItemStack(BPItems.indigo_dye, 1), "#", '#', BPBlocks.indigo_flower);

        // Storage
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.copper_block, 1), "###", "###", "###", '#', "ingotCopper"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.silver_block, 1), "###", "###", "###", '#', "ingotSilver"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.zinc_block, 1), "###", "###", "###", '#', "ingotZinc"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.tungsten_block, 1), "###", "###", "###", '#', "ingotTungsten"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.tungsten_ingot, 1), "###", "###", "###", '#', "nuggetTungsten"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.amethyst_block, 1), "###", "###", "###", '#', "gemAmethyst"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.ruby_block, 1), "###", "###", "###", '#', "gemRuby"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.sapphire_block, 1), "###", "###", "###", '#', "gemSapphire"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.teslatite_block, 1), "###", "###", "###", '#', "dustTeslatite"));
        GameRegistry.addRecipe(new ItemStack(BPItems.amethyst_gem, 9), "#", '#', BPBlocks.amethyst_block);
        GameRegistry.addRecipe(new ItemStack(BPItems.sapphire_gem, 9), "#", '#', BPBlocks.sapphire_block);
        GameRegistry.addRecipe(new ItemStack(BPItems.ruby_gem, 9), "#", '#', BPBlocks.ruby_block);
        GameRegistry.addRecipe(new ItemStack(BPItems.silver_ingot, 9), "#", '#', BPBlocks.silver_block);
        GameRegistry.addRecipe(new ItemStack(BPItems.copper_ingot, 9), "#", '#', BPBlocks.copper_block);
        GameRegistry.addRecipe(new ItemStack(BPItems.zinc_ingot, 9), "#", '#', BPBlocks.zinc_block);
        GameRegistry.addRecipe(new ItemStack(BPItems.tungsten_ingot, 9), "#", '#', BPBlocks.tungsten_block);
        GameRegistry.addRecipe(new ItemStack(BPItems.tungsten_nugget, 9), "#", '#', BPItems.tungsten_ingot);
        GameRegistry.addRecipe(new ItemStack(BPItems.teslatite_dust, 9), "#", '#', BPBlocks.teslatite_block);

        // Tools
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.screwdriver_handle,1), "stickWood", BPItems.indigo_dye));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.screwdriver, 1), "# ", " S", '#', "ingotIron", 'S', BPItems.screwdriver_handle));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.canvas, 1), "SSS", "SiS", "SSS", 'S', Items.string, 'i', "stickWood"));
        GameRegistry.addRecipe(new ItemStack(BPItems.seed_bag, 1), " S ", "C C", "CCC", 'S', Items.string, 'C', BPItems.canvas);
        GameRegistry.addRecipe(new ItemStack(BPItems.canvas_bag, 1, 15), "SSS", "S S", "SSS", 'S', BPItems.canvas);
        registerCanvasBagRecipes(new ItemStack(BPItems.canvas_bag));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.string, 4, 0), new ItemStack(BPItems.wool_card, 1,
                OreDictionary.WILDCARD_VALUE), new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.wool_card, 1, 0), "f", "p", "s", 'f', BPItems.iron_wire, 'p', "plankWood",
                's', "stickWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_axe, 1), "GG ", "GS ", " S ", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_axe, 1), " GG", " SG", " S ", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_pickaxe, 1), "GGG", " S ", " S ", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_sword, 1), "G", "G", "S", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_shovel, 1), "G", "S", "S", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_hoe, 1), "GG ", " S ", " S ", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_hoe, 1), " GG", " S ", " S ", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_sickle, 1), " G ", "  G", "SG ", 'G', "gemRuby", 'S', "stickWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_axe, 1), "GG ", "GS ", " S ", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_axe, 1), " GG", " SG", " S ", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_pickaxe, 1), "GGG", " S ", " S ", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_sword, 1), "G", "G", "S", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_shovel, 1), "G", "S", "S", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_hoe, 1), "GG ", " S ", " S ", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_hoe, 1), " GG", " S ", " S ", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_sickle, 1), " G ", "  G", "SG ", 'G', "gemSapphire", 'S', "stickWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_axe, 1), "GG ", "GS ", " S ", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_axe, 1), " GG", " SG", " S ", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_pickaxe, 1), "GGG", " S ", " S ", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_sword, 1), "G", "G", "S", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_shovel, 1), "G", "S", "S", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_hoe, 1), "GG ", " S ", " S ", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_hoe, 1), " GG", " S ", " S ", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_sickle, 1), " G ", "  G", "SG ", 'G', "gemAmethyst", 'S', "stickWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.wood_sickle, 1), " G ", "  G", "SG ", 'G', "plankWood", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.stone_sickle, 1), " G ", "  G", "SG ", 'G', "cobblestone", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.iron_sickle, 1), " G ", "  G", "SG ", 'G', "ingotIron", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.gold_sickle, 1), " G ", "  G", "SG ", 'G', "ingotGold", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.diamond_sickle, 1), " G ", "  G", "SG ", 'G', "gemDiamond", 'S', "stickWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.athame, 1), "# ", " S", '#', "ingotSilver", 'S', "stickWood"));

        if (Loader.isModLoaded("ForgeMicroblock")) {
            ItemStack diamondPanel = new ItemStack(GameData.getItemRegistry().getObject("ForgeMicroblock:microblock"), 1, 2);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("mat", "tile.blockDiamond");
            diamondPanel.setTagCompound(tag);

            ItemStack ironStrip = new ItemStack(GameData.getItemRegistry().getObject("ForgeMicroblock:microblock"), 1, 769);
            tag = new NBTTagCompound();
            tag.setString("mat", "tile.blockIron");
            ironStrip.setTagCompound(tag);

            GameRegistry.addRecipe(new ItemStack(BPItems.diamond_drawplate), " i ", "idi", " i ", 'i', ironStrip, 'd', diamondPanel);

            ItemStack diamondSawFMP = new ItemStack(GameData.getItemRegistry().getObject("ForgeMicroblock:sawDiamond"), 1, OreDictionary.WILDCARD_VALUE);
            GameRegistry.addShapelessRecipe(new ItemStack(BPItems.silicon_wafer, 16), diamondSawFMP, new ItemStack(BPItems.silicon_boule));
        } else {
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    new ItemStack(BPItems.diamond_drawplate), "IDI", "IDI", "IDI", 'I', "ingotIron", 'D', "gemDiamond"));
            GameRegistry.addShapelessRecipe(new ItemStack(BPItems.silicon_wafer, 16), new ItemStack(BPItems.diamond_saw, 1,
                    OreDictionary.WILDCARD_VALUE), new ItemStack(BPItems.silicon_boule));
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    new ItemStack(BPItems.iron_saw, 1), "SSS", " II", " II", 'S', "stickWood", 'I', "ingotIron"));
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    new ItemStack(BPItems.ruby_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I', "ingotIron", '#', "gemRuby"));
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    new ItemStack(BPItems.amethyst_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I', "ingotIron", '#', "gemAmethyst"));
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    new ItemStack(BPItems.sapphire_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I', "ingotIron", '#', "gemSapphire"));
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    new ItemStack(BPItems.diamond_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I', "ingotIron", '#', "gemDiamond"));
        }
    }

    private static void registerCanvasBagRecipes(ItemStack is) {

        RecipeSorter.register(Refs.MODID + ":canvasBag", CanvasBagRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        for (int damage = Refs.oreDictDyes.length - 1; damage >= 0; damage--) {
            ItemStack input = is.copy();
            input.setItemDamage(damage);

            for (int dye = 0; dye < Refs.oreDictDyes.length; dye++) {
                if (15 - dye != input.getItemDamage()) {
                    ItemStack output = is.copy();
                    output.setItemDamage(dye);
                    GameRegistry.addRecipe(new CanvasBagRecipe(output, input, Refs.oreDictDyes[dye]));
                }
            }
        }
    }
}
