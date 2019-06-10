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

import com.bluepowermod.BluePower;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class BPGuiConfig extends GuiConfig {

    public BPGuiConfig(Screen parent) {

        super(parent, BPGuiConfig.getConfigElements(), Refs.MODID, false, false, GuiConfig.getAbridgedConfigPath(BluePower.config
                .toString()));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<IConfigElement> getConfigElements() {

        List<IConfigElement> list = new ArrayList<IConfigElement>();
        List<IConfigElement> listZinc = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_ZINC)).getChildElements();
        List<IConfigElement> listCopper = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_COPPER)).getChildElements();
        List<IConfigElement> listSilver = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_SILVER)).getChildElements();
        List<IConfigElement> listTungsten = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_TUNGSTEN)).getChildElements();
        List<IConfigElement> listRuby = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_RUBY)).getChildElements();
        List<IConfigElement> listAmethyst = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_AMETHYST)).getChildElements();
        List<IConfigElement> listSapphire = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_SAPPHIRE)).getChildElements();
        List<IConfigElement> listTeslatite = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_TESLATITE)).getChildElements();
        List<IConfigElement> listWorldGen = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_WORLDGEN)).getChildElements();
        List<IConfigElement> listSettings = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_SETTINGS)).getChildElements();
        List<IConfigElement> listEnchants = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_ENCHANTS)).getChildElements();
        List<IConfigElement> listPneumaticTube = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_TUBES)).getChildElements();
        List<IConfigElement> listCircuitDatabase = new ConfigElement(BluePower.config.getCategory(Refs.CONFIG_CIRCUIT_DATABASE))
        .getChildElements();

        list.add(new DummyConfigElement.DummyCategoryElement("World Gen", "config.bluepower:worldgen", listWorldGen));
        list.add(new DummyConfigElement.DummyCategoryElement("Settings", "config.bluepower:settings", listSettings));
        list.add(new DummyConfigElement.DummyCategoryElement("Enchantments", "config.bluepower:enchantments", listEnchants));
        list.add(new DummyConfigElement.DummyCategoryElement("Pneumatic Tubes", "config.bluepower:tubes", listPneumaticTube));
        list.add(new DummyConfigElement.DummyCategoryElement("Circuit Database", "config.bluepower:circuitDatabase", listCircuitDatabase));
        list.add(new DummyConfigElement.DummyCategoryElement("Copper", "config.bluepower:copper", listCopper));
        list.add(new DummyConfigElement.DummyCategoryElement("Zinc", "config.bluepower:zinc", listZinc));
        list.add(new DummyConfigElement.DummyCategoryElement("Silver", "config.bluepower:silver", listSilver));
        list.add(new DummyConfigElement.DummyCategoryElement("Tungsten", "config.bluepower:tungsten", listTungsten));
        list.add(new DummyConfigElement.DummyCategoryElement("Ruby", "config.bluepower:ruby", listRuby));
        list.add(new DummyConfigElement.DummyCategoryElement("Amethyst", "config.bluepower:amethyst", listAmethyst));
        list.add(new DummyConfigElement.DummyCategoryElement("Sapphire", "config.bluepower:sapphire", listSapphire));
        list.add(new DummyConfigElement.DummyCategoryElement("Teslatite", "config.bluepower:teslatite", listTeslatite));
        return list;
    }
}
