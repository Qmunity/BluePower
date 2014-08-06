/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

/*
 * @author Quetzi
 */

package com.bluepowermod.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import com.bluepowermod.BluePower;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class BPGuiConfig extends GuiConfig {

    public BPGuiConfig(GuiScreen parent) {

        super(parent,
                BPGuiConfig.getConfigElements(),
                Refs.MODID,
                false,
                false,
                GuiConfig.getAbridgedConfigPath(BluePower.config.toString()));
    }

    private static List<IConfigElement> getConfigElements() {

        List<IConfigElement> list = new ArrayList<IConfigElement>();
        List<IConfigElement> listZinc = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_ZINC)).getChildElements();
        List<IConfigElement> listCopper = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_COPPER)).getChildElements();
        List<IConfigElement> listSilver = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_SILVER)).getChildElements();
        List<IConfigElement> listTungsten = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_TUNGSTEN)).getChildElements();
        List<IConfigElement> listRuby = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_RUBY)).getChildElements();
        List<IConfigElement> listAmethyst = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_AMETHYST)).getChildElements();
        List<IConfigElement> listSapphire = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_SAPPHIRE)).getChildElements();
        List<IConfigElement> listNikolite = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_TESLATITE)).getChildElements();
        List<IConfigElement> listWorldGen = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_WORLDGEN)).getChildElements();
        List<IConfigElement> listSettings = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_SETTINGS)).getChildElements();
        List<IConfigElement> listRecipes = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_RECIPES)).getChildElements();
        List<IConfigElement> listEnchants = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_ENCHANTS)).getChildElements();

        list.add(new DummyConfigElement.DummyCategoryElement("World Gen", "bluepower.config.worldgen", listWorldGen));
        list.add(new DummyConfigElement.DummyCategoryElement("Settings", "bluepower.config.settings", listSettings));
        list.add(new DummyConfigElement.DummyCategoryElement("Recipes", "bluepower.config.recipes", listRecipes));
        list.add(new DummyConfigElement.DummyCategoryElement("Enchantments", "bluepower.config.enchantments", listEnchants));
        list.add(new DummyConfigElement.DummyCategoryElement("Copper", "bluepower.config.copper", listCopper));
        list.add(new DummyConfigElement.DummyCategoryElement("Zinc", "bluepower.config.zinc", listZinc));
        list.add(new DummyConfigElement.DummyCategoryElement("Silver", "bluepower.config.silver", listSilver));
        list.add(new DummyConfigElement.DummyCategoryElement("Tungsten", "bluepower.config.tungsten", listTungsten));
        list.add(new DummyConfigElement.DummyCategoryElement("Ruby", "bluepower.config.ruby", listRuby));
        list.add(new DummyConfigElement.DummyCategoryElement("Amethyst", "bluepower.config.amethyst", listAmethyst));
        list.add(new DummyConfigElement.DummyCategoryElement("Sapphire", "bluepower.config.sapphire", listSapphire));
        list.add(new DummyConfigElement.DummyCategoryElement("Nikolite", "bluepower.config.teslatite", listNikolite));

        return list;
    }
}
