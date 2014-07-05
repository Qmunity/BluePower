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

package net.quetzi.bluepower.client.gui;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.references.Refs;

public class BPGuiConfig extends GuiConfig {
    public BPGuiConfig(GuiScreen parent) {
        super(parent, new ConfigElement(BluePower.config.getCategory("World Gen")).getChildElements(), Refs.MODID, false, false, GuiConfig.getAbridgedConfigPath(BluePower.config.toString()));
    }
}
